package com.wegood.core.web.res;

import com.wegood.core.support.JsonUtil;
import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * @author Rain
 */
@Accessors(chain = true)
public class Resource {

    @Getter
    @Setter
    private TransportProtocol transportProtocol;

    /**
     * 大写的 Http 方法名
     */
    @Getter
    @Setter
    private String httpMethod;

    /**
     * 资源标识
     */
    @Getter
    @Setter
    private String uri;

    public enum TransportProtocol {

        HTTP(1, "http"),
        TCP(2, "tcp"),
        WEBSOCKET(3, "websocket");

        private Integer code;

        @Getter
        private String desc;

        TransportProtocol(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @JsonValue
        public Integer getCode() {
            return code;
        }

        @JsonCreator
        public static TransportProtocol parseFromCode(Integer code) {
            for (TransportProtocol transportProtocol : TransportProtocol.values()) {
                if (transportProtocol.code.equals(code)) {
                    return transportProtocol;
                }
            }
            return null;
        }

        public static TransportProtocol parseFromCodeOrException(Integer code) {
            TransportProtocol transportProtocol = parseFromCode(code);
            if (transportProtocol == null) {
                throw new IllegalArgumentException("不支持的参数：TransportProtocol.code: " + code);
            }
            return transportProtocol;
        }

        public static TransportProtocol random() {
            return values()[RandomUtil.randomInt(TransportProtocol.values().length)];
        }

    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Resource resource = (Resource) object;
        return transportProtocol == resource.transportProtocol &&
            Objects.equals(httpMethod, resource.httpMethod) &&
            Objects.equals(uri, resource.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transportProtocol, httpMethod, uri);
    }

    public static void main(String[] args) throws JsonProcessingException {
        System.out.println(JsonUtil.DEFAULT_INSTANCE.writeValueAsString(TransportProtocol.HTTP));
    }

    @Override
    public String toString() {
        return new StringBuilder("{")
            .append("\"transportProtocol\":")
            .append(transportProtocol)
            .append(",\"httpMethod\":\"")
            .append(httpMethod).append('\"')
            .append(",\"uri\":\"")
            .append(uri).append('\"')
            .append('}')
            .toString();
    }

}
