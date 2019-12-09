package vip.isass.core.net.request.worker.normal;

import vip.isass.core.net.request.Request;
import vip.isass.core.net.request.handler.RequestHandler;
import vip.isass.core.net.request.worker.Worker;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Scope;
import vip.isass.core.net.request.handler.RequestHandler;

import javax.annotation.Resource;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Rain
 */
@Slf4j
@ConditionalOnMissingBean(Worker.class)
@Scope("prototype")
public class DefaultWorker extends Worker {

    @Resource
    private RequestHandler requestHandler;

    private static final int MAX_QUEUE_SIZE = Math.min(1000, 1000);

    private BlockingQueue<Request> blockingQueue;

    public DefaultWorker() {
        this.blockingQueue = new ArrayBlockingQueue<>(MAX_QUEUE_SIZE);
        this.setDaemon(true);
    }

    @Override
    @SneakyThrows
    protected Request pick() {
        return this.blockingQueue.take();
    }

    @Override
    protected void doWork(Request request) {
        requestHandler.handle(request);
    }

    /**
     * 接收一个事件请求，放入队列中
     */
    public final void acceptRequest(Request request) {
        boolean ok = this.blockingQueue.offer(request);
        if (!ok) {
            log.error("添加请求到 请求队列 失败！");
        }
    }

}
