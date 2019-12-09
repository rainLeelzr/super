package com.wegood.core.web.log.model;

import com.wegood.core.web.log.Log;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author Rain
 */
@Getter
@Setter
@Accessors(chain = true)
public class RequestLog implements Log {

    private String time;

    private String loginUser;

    private String uri;

    private String method;

    private Map<String, String> requestHeader;

    private Map<String, String> responseHeader;

    private String param;

    private String responseBody;

    /**
     * 请求耗时，毫秒
     */
    private int cost;

    @Override
    public String toString() {
        return new StringBuilder("{")
            .append("\"time\":\"")
            .append(time).append('\"')
            .append(",\"loginUser\":\"")
            .append(loginUser).append('\"')
            .append(",\"uri\":\"")
            .append(uri).append('\"')
            .append(",\"method\":\"")
            .append(method).append('\"')
            .append(",\"requestHeader\":")
            .append(requestHeader)
            .append(",\"responseHeader\":")
            .append(responseHeader)
            .append(",\"param\":\"")
            .append(param).append('\"')
            .append(",\"responseBody\":\"")
            .append(responseBody).append('\"')
            .append(",\"cost\":")
            .append(cost)
            .append('}')
            .toString();
    }

}
