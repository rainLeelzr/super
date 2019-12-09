package vip.isass.core.entity;

import cn.hutool.core.util.RandomUtil;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 最常用的 entity
 *
 * @author rain
 */
public interface IEntity<E> extends Serializable {

    long serialVersionUID = 1L;

    /**
     * 生成随机的entity
     * 所有字段都随机赋值
     */
    E randomEntity();

    default String randomString() {
        return RandomUtil.randomString(6);
    }

    default Byte randomByte() {
        return (byte) RandomUtil.randomInt(Byte.MAX_VALUE);
    }

    default Boolean randomBoolean() {
        return RandomUtil.randomBoolean();
    }

    default Integer randomInteger() {
        return RandomUtil.randomInt();
    }

    default Long randomLong() {
        return RandomUtil.randomLong();
    }

    default BigDecimal randomBigDecimal() {
        return RandomUtil.randomBigDecimal(BigDecimal.TEN);
    }

}
