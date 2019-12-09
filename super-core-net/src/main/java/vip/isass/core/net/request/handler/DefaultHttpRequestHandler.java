package vip.isass.core.net.request.handler;

import vip.isass.core.net.config.NetProperties;
import vip.isass.core.net.packet.Packet;
import vip.isass.core.net.packet.impl.HttpContent;
import vip.isass.core.net.request.Request;
import vip.isass.core.protobuf.Base;
import vip.isass.core.protobuf.im.IM;
import vip.isass.core.serialization.SerializeMode;
import vip.isass.core.serialization.impl.protobuf2.ProtobufMethodCache;
import vip.isass.core.support.UriRequestMapping;
import vip.isass.core.support.JsonUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;
import com.googlecode.protobuf.format.JsonFormat;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author Rain
 */
@Slf4j
@ConditionalOnMissingBean(RequestHandler.class)
public class DefaultHttpRequestHandler implements RequestHandler {

    @Resource
    private NetProperties netProperties;

    @Primary
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(netProperties.getRestTemplateTimeOut());
        requestFactory.setReadTimeout(netProperties.getRestTemplateTimeOut());
        return new RestTemplate(requestFactory);
    }

    @Resource
    private RestTemplate restTemplate;

    @Value("${server.gateway.http.serverName}")
    private String httpGatewayServerName;

    @Override
    public void handle(Request request) {
        Packet packet = request.getPacket();
        Packet.Type type = Packet.Type.parseByCode(packet.getType());
        switch (type) {
            case HTTP_REQUEST:
                handelProtocolExchangeHttp(request);
                break;
            case HEART_BEAT:
            default:
                throw new IllegalArgumentException(StrUtil.format("未支持的包类型：[{}]", type));
        }
    }

    @SneakyThrows
    private void handelProtocolExchangeHttp(Request request) {
        Packet packet = request.getPacket();
        Object content = packet.getContent();

        // 反序列化HttpContent
        HttpContent httpContent;
        String javaProtobufClassC2S = null;
        String javaProtobufClassS2C = null;

        SerializeMode serializeMode = SerializeMode.getByCode(packet.getSerializeMode());
        switch (serializeMode) {
            case JSON:
                if (content instanceof byte[]) {
                    String str = new String((byte[]) content, UTF_8);
                    httpContent = JsonUtil.DEFAULT_INSTANCE.readValue((byte[]) content, HttpContent.class);
                } else {
                    httpContent = JsonUtil.DEFAULT_INSTANCE.convertValue(packet.getContent(), HttpContent.class);
                }
                break;
            case PROTOBUF2:
                Base.HttpFrame httpFrame = Base.HttpFrame.parseFrom((byte[]) content);
                httpContent = new HttpContent()
                    .setUrl(httpFrame.getUrl())
                    .setHttpMethod(httpFrame.getHttpMethod())
                    .setHttpHeaders(httpFrame
                        .getHttpHeadersList()
                        .stream()
                        .collect(Collectors.toMap(Base.StringEntry::getKey, Base.StringEntry::getValue)));

                javaProtobufClassC2S = httpFrame.getJavaProtobufClassC2S();
                javaProtobufClassS2C = httpFrame.getJavaProtobufClassS2C();
                GeneratedMessage bodyPb = (GeneratedMessage) ProtobufMethodCache.PARSE_METHOD_CACHE.get(javaProtobufClassC2S).invoke(null, httpFrame.getBody());
                String bodyStr = JsonFormat.printToString(bodyPb);
                httpContent.setBody(bodyStr);
                break;
            case PROTOBUF3:
            default:
                throw new UnsupportedOperationException("未支持的序列化方式：" + serializeMode);
        }

        packet.setContent(httpContent);

        // 获取请求url
        String url = httpContent.getUrl();

        if (StrUtil.isBlank(url)) {
            log.error("请求的url字符串为空，跳过请求！");
            packet.setType(Packet.Type.ERROR.getCode());
            packet.setSerializeMode(SerializeMode.JSON.getCode());
            packet.setContent("发起的http请求的url不能空");
            request.sendResponse(packet);
            return;
        }

        ResponseEntity<String> resp = null;
        try {
            resp = httpForward(this.restTemplate, httpContent);
            setResponseUrl(resp.getHeaders(), httpContent);
            String body = resp.getBody();
            if (StrUtil.isBlank(body)) {
                body = "";
            } else if (body.length() < 2) {
                body = "\"" + body + "\"";
            } else if (!isJson(body)) {
                body = "\"" + body + "\"";
            }
            log.debug("http网关执行结果：{}", resp);

            if (serializeMode == SerializeMode.JSON) {
                httpContent.setBody(body);
                packet.setContent(httpContent);
            } else if (serializeMode == SerializeMode.PROTOBUF2) {
                Method method = ProtobufMethodCache.BUILDER_METHOD_CACHE.get(javaProtobufClassS2C);
                GeneratedMessage.Builder builder = (GeneratedMessage.Builder) method.invoke(method.getDeclaringClass());
                JsonFormat.merge(body, builder);
                httpContent.setBody(builder.build().toByteString());

                Base.HttpFrame.Builder contentBuilder = Base.HttpFrame.newBuilder()
                    .setUrl(httpContent.getUrl())
                    .setHttpMethod(httpContent.getHttpMethod())
                    .setBody((ByteString) httpContent.getBody());
                if (MapUtil.isNotEmpty(httpContent.getHttpHeaders())) {
                    httpContent.getHttpHeaders().forEach(
                        (k, v) -> contentBuilder.addHttpHeaders(Base.StringEntry.newBuilder().setKey(k).setValue(v))
                    );
                }
                packet.setContent(contentBuilder.build());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            packet.setType(Packet.Type.ERROR.getCode());
            packet.setSerializeMode(SerializeMode.JSON.getCode());
            packet.setContent(e.getMessage());
        }

        // 推送结果给客户端
        request.sendResponse(packet);
    }

    public static void main(String[] args) throws JsonFormat.ParseException {
        String json = "{\"success\":true,\"errorCode\":0,\"errorMsg\":null,\"errorDetail\":null,\"data\":{\"toUserId\":111,\"message\":\"test内容\"}}";
        IM.SendTextToUserS2C.Builder builder = IM.SendTextToUserS2C.newBuilder();
        JsonFormat.merge(json, builder);
    }

    private ResponseEntity<String> httpForward(RestTemplate restTemplate, HttpContent httpContent) {
        ResponseEntity<String> resp;

        HttpHeaders headers = getHttpHeaders(httpContent);

        HttpEntity<Object> formEntity = new HttpEntity<>(httpContent.getBody(), headers);

        // 获取请求类型
        Integer httpMethod = httpContent.getHttpMethod();
        HttpContent.HttpMethodEnum httpMethodEnum = HttpContent.HttpMethodEnum.parseFromId(httpMethod);
        switch (httpMethodEnum) {
            case GET:
                resp = restTemplate.exchange(httpContent.getUrl(), HttpMethod.GET, formEntity, String.class);
                break;
            case PUT:
                resp = restTemplate.exchange(httpContent.getUrl(), HttpMethod.PUT, formEntity, String.class);
                break;
            case POST:
                resp = restTemplate.exchange(httpContent.getUrl(), HttpMethod.POST, formEntity, String.class);
                break;
            case DELETE:
                resp = restTemplate.exchange(httpContent.getUrl(), HttpMethod.DELETE, formEntity, String.class);
                break;
            case HEAD:
            case TRACE:
            case OPTIONS:
            case PATCH:
            default:
                throw new UnsupportedOperationException("不支持的HttpMethodEnum值：[+" + httpMethod + "]");
        }
        return resp;
    }

    private boolean isJson(String str) {
        if (str.startsWith("{") && str.endsWith("}")) {
            return true;
        }
        if (str.startsWith("[") && str.endsWith("]")) {
            return true;
        }
        return false;
    }

    private HttpHeaders getHttpHeaders(HttpContent httpContent) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
        Map<String, String> httpHeaders = httpContent.getHttpHeaders();
        if (MapUtil.isNotEmpty(httpHeaders)) {
            httpHeaders.forEach(headers::add);
        }
        return headers;
    }

    /**
     * 将请求的url，转换成微服务的controller定义的requestMapping
     */
    private void setResponseUrl(HttpHeaders respHeaders, HttpContent httpContent) {
        List<String> mappings = respHeaders.get(UriRequestMapping.MAPPING_KEY);
        if (CollUtil.isEmpty(mappings)) {
            return;
        }
        String mapping = mappings.get(0);
        if (mapping == null || mapping.length() == 0) {
            return;
        }
        httpContent.setUrl(mapping);
    }

}
