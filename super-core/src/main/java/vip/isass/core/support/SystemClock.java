package vip.isass.core.support;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author Rain
 */
@Slf4j
public class SystemClock {

    private static ISystemClock iSystemClock;

    public static long now() {
        loadSystemClockImpl();
        return iSystemClock == null ? System.currentTimeMillis() : iSystemClock.now();
    }

    public static Date nowDate() {
        loadSystemClockImpl();
        return iSystemClock == null ? new Date() : iSystemClock.nowDate();
    }

    private static void loadSystemClockImpl() {
        if (iSystemClock == null) {
            synchronized (SystemClock.class) {
                if (iSystemClock == null) {
                    try {
                        iSystemClock = SpringContextUtil.getBean(ISystemClock.class);
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
        }
    }

}
