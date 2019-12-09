package com.wegood.core.net.packet;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 数据包
 *
 * @author Rain
 */
@Getter
@Setter
@Accessors(chain = true)
public class BluePacket implements Packet {

    Integer fullLength;

    Integer type;

    Integer serializeMode;

    Object content;

    @Override
    public String toString() {
        return new StringBuilder("{")
                .append("\"fullLength\":")
                .append(fullLength)
                .append(",\"type\":")
                .append(type)
                .append(",\"serializeMode\":")
                .append(serializeMode)
                .append(",\"content\":")
                .append(content)
                .append('}')
                .toString();
    }
}
