package vip.isass.core.support;

import cn.hutool.core.util.ObjectUtil;

/**
 * @author Rain
 */
public interface Converter<T> {

    /**
     * 默认返回 false
     * 若需要使用本方法，请实现类复写此方法。
     */
    default boolean supports(Object o) {
        return false;
    }

    /**
     * 转换方法
     * 将类型为 S 的对象转换为 类型为 T 的对象
     */
    T convert(Object source);

    /**
     * 将 S 转换为 T ,若转换过程抛出异常，则返回默认值
     */
    default T defaultIfException(Object source, T defaultValue) {
        try {
            return convert(source);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 将 S 转换为 T。
     * 出现以下任一情况，则返回默认值
     * 1: 转换过程抛出异常
     * 2: 入参 source 为空
     * 3: 转换逻辑结果为空
     */
    default T defaultIfNull(Object source, T defaultValue) {
        if (source == null) {
            return defaultValue;
        }
        return ObjectUtil.defaultIfNull(defaultValue, defaultIfException(source, defaultValue));
        // throw new UnsupportedOperationException(
        //     StrUtil.format("此转换器[{}]不支持参数类型[{}]的转换", this.getClass().getName(), source.getClass().getName()));
    }

}
