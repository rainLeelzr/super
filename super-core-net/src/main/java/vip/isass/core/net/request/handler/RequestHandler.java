package vip.isass.core.net.request.handler;

import vip.isass.core.net.request.Request;

/**
 * @author Rain
 */
public interface RequestHandler {

    /**
     * 执行处理
     */
    void handle(Request request);
}
