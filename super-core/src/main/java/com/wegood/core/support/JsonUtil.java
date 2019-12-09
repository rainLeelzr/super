package com.wegood.core.support;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.wegood.core.entity.Json;
import com.wegood.core.support.json.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author Rain
 */
public class JsonUtil {

    public static SimpleModule simpleModule = new SimpleModule()
        .addSerializer(LocalDateTime.class, new StdDelegatingSerializer(new LocalDateTimeToLongConvert()))
        .addDeserializer(LocalDateTime.class, new StdDelegatingDeserializer<>(new LongToLocalDateTimeConvert()))
        .addSerializer(LocalDate.class, new StdDelegatingSerializer(new LocalDateToLongConvert()))
        .addDeserializer(LocalDate.class, new StdDelegatingDeserializer<>(new LongToLocalDateConvert()))
        .addSerializer(LocalTime.class, new StdDelegatingSerializer(new LocalTimeToLongConvert()))
        .addDeserializer(LocalTime.class, new StdDelegatingDeserializer<>(new LongToLocalTimeConvert()))
        .addDeserializer(Json.class, new StdDelegatingDeserializer<>(new ObjectToJsonConvert()))
        .addDeserializer(LocalDateTime.class, new StdDelegatingDeserializer<>(new StringToLocalDateTimeConvert()));

    public static final ObjectMapper DEFAULT_INSTANCE = new ObjectMapper()
        // 当实体类中不含有 json 字符串的某些字段时，不抛出异常
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        // 非 bean 对象不抛异常
        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)

        // BigDecimal 精度
        .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)

        .registerModule(simpleModule);

    public static final ObjectMapper NOT_NULL_INSTANCE = new ObjectMapper()
        // 当实体类中不含有 json 字符串的某些字段时，不抛出异常
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        // 非 bean 对象不抛异常
        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)

        // BigDecimal 精度
        .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)

        // 只输出非 null 字段
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)

        .registerModule(simpleModule);

    public static Json fromObject(Object object) {
        return new DefaultJson().fromObject(object);
    }

}
