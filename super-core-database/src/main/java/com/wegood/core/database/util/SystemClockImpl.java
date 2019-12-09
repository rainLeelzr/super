package com.wegood.core.database.util;

import com.wegood.core.support.ISystemClock;
import org.springframework.stereotype.Component;

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
