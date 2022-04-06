package vip.isass.core.net.tag;

import cn.hutool.core.lang.Assert;

/**
 * @author Rain
 */
public class TagUtil {

    public static final int VALUE_NUM_LIMIT = 1000;

    public static Class<?>[] SUPPORT_VALUE_ARR = new Class[]{
        boolean.class,
        Boolean.class,
        byte.class,
        Byte.class,
        short.class,
        Short.class,
        int.class,
        Integer.class,
        long.class,
        Long.class,
        String.class
    };

    public static <T> void checkValueType(T value) {
        Assert.isTrue(supportValueType(value), "不支持该标签值类型[{}]", value == null ? null : value.getClass());
    }

    public static <T> boolean supportValueType(T value) {
        if (value == null) {
            return false;
        }

        Class<?> clazz = value.getClass();
        for (Class<?> aClass : SUPPORT_VALUE_ARR) {
            if (clazz.equals(aClass)) {
                return true;
            }
        }
        return false;
    }

}
