package vip.isass.core.sequence.impl;

import vip.isass.core.sequence.Sequence;
import vip.isass.core.support.SpringContextUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author rain
 */
@Slf4j
public class LongSequence implements Sequence<Long> {

    private static Sequence<Long> sequence;

    @Override
    public Long next() {
        return get();
    }

    public static Long get() {
        if (sequence == null) {
            synchronized (LongSequence.class) {
                if (sequence == null) {
                    try {
                        sequence = SpringContextUtil.getBeanOfSupport(Sequence.class, Long.class);
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
        }
        return sequence == null ? RandomUtil.randomLong(1000000000, Long.MAX_VALUE) : sequence.next();
    }

    @Override
    public boolean support(Class clazz) {
        return clazz == Long.class;
    }

}
