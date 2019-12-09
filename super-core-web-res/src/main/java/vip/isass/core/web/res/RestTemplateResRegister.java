package vip.isass.core.web.res;

import cn.hutool.core.util.StrUtil;
import vip.isass.core.web.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;

/**
 * @author Rain
 */
@Slf4j
@ConditionalOnMissingBean(ResRegister.class)
public class RestTemplateResRegister implements ResRegister {

    @javax.annotation.Resource
    private RestTemplate restTemplate;

    @Value("${security.auth.url.get-all-res}")
    private String getAllResUrl;

    @Value("${security.auth.url.add-batch-res}")
    private String addBatchRes;

    @Override
    public List<Resource> getAllRegisteredResource() {
        ParameterizedTypeReference<Resp<List<Resource>>> type = new ParameterizedTypeReference<Resp<List<Resource>>>() {

        };

        ResponseEntity<Resp<List<Resource>>> response = null;
        try {
            response = restTemplate.exchange(
                getAllResUrl,
                HttpMethod.GET,
                null,
                type
            );
        } catch (Exception e) {
            log.error("获取已注册resource失败！");
            throw e;
        }
        Resp<List<Resource>> resp = response.getBody();

        return resp.getData();
    }

    @Override
    public List<Resource> getAllRegisteredResourceByPrefixUri(String prefixUri) {
        ParameterizedTypeReference<Resp<List<Resource>>> type = new ParameterizedTypeReference<Resp<List<Resource>>>() {

        };

        ResponseEntity<Resp<List<Resource>>> response = null;
        try {
            response = restTemplate.exchange(
                getAllResUrl + (StrUtil.isBlank(prefixUri) ? "" : "?uriStartWith=" + prefixUri),
                HttpMethod.GET,
                null,
                type
            );
        } catch (Exception e) {
            log.error("获取已注册resource失败！");
            throw e;
        }
        Resp<List<Resource>> resp = response.getBody();

        return resp.getData();
    }

    @Override
    public void register(Collection<Resource> collect) {
        HttpEntity<Collection<Resource>> httpEntity = new HttpEntity<>(collect);
        ParameterizedTypeReference<Resp<Integer>> type = new ParameterizedTypeReference<Resp<Integer>>() {
        };

        ResponseEntity<Resp<Integer>> resp = null;
        try {
            resp = restTemplate.exchange(
                addBatchRes,
                HttpMethod.POST,
                httpEntity,
                type
            );
        } catch (Exception e) {
            log.error("注册resource失败！");
            log.error(e.getMessage(), e);
            return;
        }
        if (resp.getStatusCode() == HttpStatus.OK && resp.getBody() != null && resp.getBody().getSuccess()) {
            log.info("成功注册了{}个resource", resp.getBody().getData());
        } else {
            log.error("保存resource错误:{}", resp.toString());
        }
    }

}
