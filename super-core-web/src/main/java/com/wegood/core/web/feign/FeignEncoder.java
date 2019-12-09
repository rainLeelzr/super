package com.wegood.core.web.feign;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.wegood.core.support.Converter;
import com.wegood.core.support.LocalDateTimeUtil;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 解决GET的Ctrl传对象会自动转为POST的问题
 * 解决对象转换的问题：Date,Collection
 *
 * @author Rain
 */
@Component
public class FeignEncoder implements Encoder {

    private static final String FEIGN_GET = "feign/get";

    public static final String CONTENT_TYPE_LOCALS_GET = HttpHeaders.CONTENT_TYPE + "=" + FEIGN_GET;

    private static final List<String> IGNORE_PROPERTIES = CollUtil.newArrayList("whereConditions");

    private Encoder springEncoder;

    private List<Converter<String>> converters;

    public FeignEncoder() {
        converters = CollUtil.newArrayList(new Converter<String>() {
            @Override
            public boolean supports(Object o) {
                return ClassUtils.isAssignableValue(Collection.class, o);
            }

            @Override
            public String convert(Object collection) {
                String s = collection.toString();
                return s.substring(1, s.length() - 1);
            }
        }, new Converter<String>() {
            @Override
            public boolean supports(Object o) {
                return ClassUtils.isAssignableValue(Date.class, o);
            }

            @Override
            public String convert(Object o) {

                return String.valueOf(((Date) o).getTime());
            }
        }, new Converter<String>() {
            @Override
            public boolean supports(Object o) {
                return ClassUtils.isAssignableValue(LocalDateTime.class, o);
            }

            @Override
            public String convert(Object o) {
                return LocalDateTimeUtil.localDateTimeToEpochMilli((LocalDateTime) o) + "";
            }
        });
    }

    @Autowired
    public void setSpringEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        this.springEncoder = new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    @Override
    public void encode(Object requestBody, Type bodyType, RequestTemplate request) throws EncodeException {
        if (requestBody == null) {
            return;
        }

        Collection<String> contentTypes = request.headers().get(HttpHeaders.CONTENT_TYPE);
        if (contentTypes == null || contentTypes.isEmpty()) {
            springEncoder.encode(requestBody, bodyType, request);
        } else {
            Iterator<String> it = contentTypes.iterator();
            boolean hasLocalsGet = false;
            while (it.hasNext()) {
                String type = it.next();
                if (FEIGN_GET.equalsIgnoreCase(type)) {
                    Map<String, ?> stringObjectMap = BeanUtil.beanToMap(requestBody);

                    Map<String, Collection<String>> queries = new HashMap<>(16);

                    for (Map.Entry<String, ?> stringObjectEntry : stringObjectMap.entrySet()) {
                        String key = stringObjectEntry.getKey();
                        if (IGNORE_PROPERTIES.contains(key)) {
                            continue;
                        }

                        Object value = stringObjectEntry.getValue();
                        if (value == null) {
                            continue;
                        }

                        String convert = null;
                        for (Converter<String> converter : converters) {
                            if (converter.supports(value)) {
                                convert = converter.convert(value);
                                break;
                            }
                        }
                        queries.put(key, Collections.singletonList(StrUtil.nullToDefault(convert, value.toString())));
                    }
                    if (!queries.isEmpty()) {
                        request.queries(queries);
                    }

                    hasLocalsGet = true;
                    break;
                }
            }
            if (!hasLocalsGet) {
                springEncoder.encode(requestBody, bodyType, request);
            }
        }
    }

}
