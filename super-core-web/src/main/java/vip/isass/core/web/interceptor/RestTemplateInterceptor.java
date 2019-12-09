package vip.isass.core.web.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import vip.isass.core.web.header.AdditionalRequestHeaderProvider;

import java.io.IOException;
import java.util.List;

/**
 * @author Rain
 */
@Component
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Autowired(required = false)
    private List<AdditionalRequestHeaderProvider> additionalHeaderProviders;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        if (additionalHeaderProviders == null) {
            return execution.execute(request, body);
        }

        HttpHeaders headers = request.getHeaders();
        additionalHeaderProviders.forEach(h -> {
            if (!h.support(request.getMethodValue(), request.getURI().getHost())) {
                return;
            }
            if (h.override()) {
                headers.set(h.getHeaderName(), h.getValue());
                return;
            }
            if (headers.getFirst(h.getHeaderName()) == null) {
                headers.set(h.getHeaderName(), h.getValue());
            }
        });
        return execution.execute(request, body);
    }

}
