package com.wegood.core.database.util;

import com.wegood.core.sequence.Sequence;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.stereotype.Component;

/**
 * @author Rain
 */
@Component
public class LongSequenceImpl implements Sequence<Long> {

    @Override
    public Long next() {
        return get();
    }

    public static Long get() {
        return IdWorker.getId();
    }

    @Override
    public boolean support(Class clazz) {
        return clazz == Long.class;
    }

}
