package com.wegood.core.net.request.worker.event.handler;

import com.wegood.core.net.request.Request;
import com.wegood.core.net.request.worker.event.WorkStartEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;

/**
 * @author Rain
 */
@Slf4j
public class WorkStartHandler implements ApplicationListener<WorkStartEvent> {

    /**
     * 网络请求的业务开始处理前，触发此方法
     */
    @Override
    public void onApplicationEvent(WorkStartEvent event) {
        Request request = event.getRequest();
        log.debug("开始处理第{}个请求[客户端ip：{}]：{}",
            event.getWorkCount(),
            request.getSession().getRemoteIP(),
            request.toString());
    }

}
