package com.wegood.core.net.request.handler;

import com.wegood.core.net.request.Request;

/**
 * @author Rain
 */
public interface RequestHandler {

    /**
     * 执行处理
     */
    void handle(Request request);
}
