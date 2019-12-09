package com.wegood.core.net.request.worker.event.handler;

import com.wegood.core.net.request.Request;
import com.wegood.core.net.request.worker.event.WorkCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;

/**
 * @author Rain
 */
@Slf4j
public class WorkCompletedHandler implements ApplicationListener<WorkCompletedEvent> {

    /**
     * 网络请求的业务处理完成时，触发此方法。
     * 不论业务过程是否抛出异常，均触发此方法。
     */
    @Override
    public void onApplicationEvent(WorkCompletedEvent event) {
        Request request = event.getRequest();
        log.debug("业务处理已完成。{},", request.toString());
    }

}
