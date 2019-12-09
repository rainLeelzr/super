package vip.isass.core.web.security.config;

import cn.hutool.core.collection.CollUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import vip.isass.core.web.uri.UriPrefixProvider;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Rain
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
@Configuration
@ConfigurationProperties("security.auth")
public class PermitUrlConfiguration {

    private static String HEALTH_CHECK_URI = "/actuator/health";

    private static String ERROR_URI = "/error";

    @Resource
    private UriPrefixProvider uriPrefixProvider;

    private List<String> permitUrls;

    public List<String> getPermitUrls() {
        if (permitUrls == null) {
            return CollUtil.newArrayList(
                uriPrefixProvider.getContextPath() + HEALTH_CHECK_URI,
                uriPrefixProvider.getUriPrefix() + ERROR_URI);
        }
        if (!permitUrls.contains(uriPrefixProvider.getContextPath() + HEALTH_CHECK_URI)) {
            permitUrls.add(uriPrefixProvider.getContextPath() + HEALTH_CHECK_URI);
        }
        if (!permitUrls.contains(uriPrefixProvider.getContextPath() + ERROR_URI)) {
            permitUrls.add(uriPrefixProvider.getContextPath() + ERROR_URI);
        }
        return permitUrls;
    }

}
