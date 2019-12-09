package vip.isass.core.database.util;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.stereotype.Component;
import vip.isass.core.sequence.Sequence;

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
