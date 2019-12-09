package vip.isass.core.web.security.authentication.ms;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vip.isass.core.web.header.AdditionalRequestHeaderProvider;

/**
 * @author Rain
 */
@Getter
@Setter
@Accessors(chain = true)
@Component
public class MsAuthenticationHeaderProvider implements AdditionalRequestHeaderProvider {

    public static final String HEADER = "Ms-Authorization";

    @Value("${spring.application.name:unknown}")
    private String appName;

    @Value("${security.ms.secret:wegood123456}")
    private String secret;

    @Value(".${security.ms.secret:wegood123456}")
    private String dotSecret;

    private String fullMsAuthenticationHeaderValue = "";

    @Override
    public String getHeaderName() {
        return HEADER;
    }

    @Override
    public String getValue() {
        if ("".equals(fullMsAuthenticationHeaderValue)) {
            fullMsAuthenticationHeaderValue = appName + dotSecret;
        }
        return fullMsAuthenticationHeaderValue;
    }

    @Override
    public boolean override() {
        return false;
    }

    @Override
    public boolean support(String method, String url) {
        return true;
    }

}
