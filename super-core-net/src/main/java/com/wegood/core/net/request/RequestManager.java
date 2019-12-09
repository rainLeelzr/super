package com.wegood.core.net.request;

import com.wegood.core.net.request.worker.WorkerPool;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 请求管理器
 * 根据客户端的消息命令，分发到各个指定的方法中去处理请求
 *
 * @author Rain
 */
@Component
public class RequestManager {

    /**
     * 处理客户端请求的线程池
     */
    @Resource
    private WorkerPool workerPool;

    public void addRequest(Request request) {
        this.workerPool.putRequestInQueue(request);
    }

}
