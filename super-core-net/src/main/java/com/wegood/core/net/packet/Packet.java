package com.wegood.core.net.packet;

import lombok.Getter;

/**
 * 数据包
 *
 * @author Rain
 */
public interface Packet {

    Integer getFullLength();

    Packet setFullLength(Integer fullLength);

    /**
     * 包类型
     *
     * @see Type
     */
    Integer getType();

    Packet setType(Integer type);

    /**
     * body 的序列化方式
     *
     * @see com.wegood.core.serialization.SerializeMode
     */
    Integer getSerializeMode();

    Packet setSerializeMode(Integer serializeMode);

    Object getContent();

    Packet setContent(Object content);

    enum Type {

        // 心跳
        HEART_BEAT(1),

        // 客户端以http的格式发起请求；服务端响应请求
        HTTP_REQUEST(2),

        // 服务器主动推送http格式的内容给客户端
        PUSH_HTTP_CONTENT(3),

        // 设置channel通道的userId(String类型),tcp通道、websocket二进制通道：userId直接用utf-8字节数组写在content里;
        // websocket文本通道：json字段content即是字符串的userId
        SET_USER_ID(4),

        // 服务器主动推送消息给客户端
        PUSH(5),

        // 请求错误，或者服务器抛出异常，websocket文本通道：异常信息写在content里
        ERROR(6);

        @Getter
        private Integer code;

        Type(Integer code) {
            this.code = code;
        }

        public static Type parseByCode(Integer code) {
            for (Type type : Type.values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("不支持的参数值：code=" + code);
        }
    }
}
