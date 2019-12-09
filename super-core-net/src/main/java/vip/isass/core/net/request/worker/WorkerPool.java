package vip.isass.core.net.request.worker;

import vip.isass.core.net.request.Request;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author hone 2018/5/8
 */
public interface WorkerPool extends InitializingBean {

    /**
     * 将一个网络请求，放到请求处理器的队列中，等待空闲业务线程处理
     */
    void putRequestInQueue(Request request);

    /**
     * 初始化
     * 创建此 bean 时，会自动被 spring 调用一次
     */
    WorkerPool init();

    @Override
    default void afterPropertiesSet() {
        init();
    }
}
