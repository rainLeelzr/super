package vip.isass.core.database.util;

import org.springframework.stereotype.Component;
import vip.isass.core.support.ISystemClock;

import java.util.Date;

/**
 * @author Rain
 */
@Component
public class SystemClockImpl implements ISystemClock {

    public long now() {
        return com.baomidou.mybatisplus.core.toolkit.SystemClock.now();
    }

    public Date nowDate() {
        return new Date(now());
    }

}
