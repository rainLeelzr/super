package vip.isass.core.log.slf4j;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志工具
 *
 * @author rain
 */
@Slf4j
public class LogUtil {

    public static final Map<String, Level> LEVEL_MAP = new ConcurrentHashMap<>();

    /**
     * 关闭日志
     */
    public static void loggerOff(String loggerName) {
        Assert.notBlank(loggerName, "loggerName 必填");
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = loggerContext.getLogger(loggerName);
        if (logger.getLevel() != null) {
            LEVEL_MAP.put(loggerName, logger.getLevel());
        }
        logger.setLevel(Level.OFF);
    }

    /**
     * 关闭日志
     */
    public static void loggerOff(Class<?> clazz) {
        Assert.notNull(clazz, "clazz 必填");
        loggerOff(clazz.getName());
    }

    /**
     * 恢复日志级别
     */
    public static void loggerRestore(String loggerName) {
        Assert.notBlank(loggerName, "loggerName 必填");
        Level level = LEVEL_MAP.remove(loggerName);
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = loggerContext.getLogger(loggerName);
        logger.setLevel(level);
    }

    /**
     * 恢复日志级别
     */
    public static void loggerRestore(Class<?> clazz) {
        Assert.notNull(clazz, "clazz 必填");
        loggerRestore(clazz.getName());
    }

}
