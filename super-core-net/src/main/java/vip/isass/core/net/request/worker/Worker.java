package vip.isass.core.net.request.worker;

/**
 * @author Rain
 */

import vip.isass.core.net.request.Request;
import vip.isass.core.net.request.worker.event.WorkCompletedEvent;
import vip.isass.core.net.request.worker.event.WorkExceptionEvent;
import vip.isass.core.net.request.worker.event.WorkStartEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import vip.isass.core.net.request.worker.event.WorkCompletedEvent;
import vip.isass.core.net.request.worker.event.WorkExceptionEvent;
import vip.isass.core.net.request.worker.event.WorkStartEvent;

import javax.annotation.Resource;

@Slf4j
public abstract class Worker extends Thread {

    /**
     * 不精准的计数器
     */
    private volatile int workCount = 0;

    private boolean isStarted = false;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    protected void controlFlow() {
        while (isStarted) {
            Request request = pick();
            doWorkWrapper(request);
        }
    }

    /**
     * 选择一个网络请求
     */
    protected Request pick() {
        throw new UnsupportedOperationException("请实现此方法");
    }

    /**
     * 执行业务流程
     */
    protected abstract void doWork(Request request) throws Exception;

    protected void doWorkWrapper(Request request) {
        try {
            applicationEventPublisher.publishEvent(new WorkStartEvent().setRequest(request).setWorkCount(++workCount));
            doWork(request);
        } catch (Exception e) {
            applicationEventPublisher.publishEvent(new WorkExceptionEvent().setRequest(request).setException(e));
        } finally {
            applicationEventPublisher.publishEvent(new WorkCompletedEvent().setRequest(request));
        }
    }


    @Override
    public void run() {
        isStarted = true;
        controlFlow();
    }

}
