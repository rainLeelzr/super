package vip.isass.core.web.uri;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Rain
 */
@Slf4j
@Getter
@Component
public class UriPrefixProvider {

    private String appName = "";

    private String contextPath = "";

    private String uriPrefix;

    @Resource
    public void setAppName(@Value("${spring.application.name:}") String applicationName) {
        if (StrUtil.isNotBlank(applicationName)) {
            String shortName = AppNameConvert.getShortNameByApplicationName(applicationName);
            if (StrUtil.isNotBlank(shortName)) {
                this.appName = "/" + shortName;
            }
        }
    }

    @Resource
    public void setContextPath(@Value("${server.servlet.context-path:}") String contextPath) {
        if (StrUtil.isNotBlank(contextPath)) {
            this.contextPath = "/" + contextPath;
        }
    }

    public String getUriPrefix() {
        if (this.uriPrefix == null) {
            this.uriPrefix = appName + contextPath;
        }
        return uriPrefix;
    }

    @Getter
    public enum AppNameConvert {

        /**
         * 活动服务
         */
        ACTIVITY("activity", "wegood-service-activity"),
        APIDOC("apidoc", "wegood-service-apidoc"),
        AUTH("auth", "wegood-service-auth"),
        BASE("base", "wegood-service-base"),
        COUPON("coupon", "wegood-service-coupon"),
        FINANCE("finance", "wegood-service-finance"),
        GATEWAY("gateway", "wegood-service-gateway"),
        GOODS("goods", "wegood-service-goods"),
        JOB_CENTER("job-center", "wegood-service-job-center"),
        JOB_EXECUTOR("job-executor", "wegood-service-job-executor"),
        MESSAGE("message", "wegood-service-message"),
        ORDER("order", "wegood-service-order"),
        PAY("pay", "wegood-service-pay"),
        PDD("pdd", "wegood-service-pdd"),
        POSTER("poster", "wegood-service-poster"),
        PUSH("push", "wegood-service-push"),
        SEARCH("search", "wegood-service-search"),
        TAOBAO("taobao", "wegood-service-taobao"),
        UPLOAD("upload", "wegood-service-upload"),
        ADVERTISING("ad", "wegood-service-advertising"),;

        private String shortName;

        private String applicationName;

        private static final Map<String, String> MAPPING;

        static {
            MAPPING = Arrays.stream(AppNameConvert.values())
                .collect(Collectors.toMap(AppNameConvert::getApplicationName, AppNameConvert::getShortName));
        }

        AppNameConvert(String shortName, String applicationName) {
            this.shortName = shortName;
            this.applicationName = applicationName;
        }

        public static String getShortNameByApplicationName(String applicationName) {
            String shortName = MAPPING.getOrDefault(applicationName, "");
            if (StrUtil.isBlank(shortName)) {
                log.warn("applicationName[{}]未添加AppNameConvert枚举", applicationName);
            }
            return shortName;
        }
    }

}
