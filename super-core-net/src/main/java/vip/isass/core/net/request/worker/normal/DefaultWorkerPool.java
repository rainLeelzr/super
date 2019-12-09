package vip.isass.core.net.request.worker.normal;

import vip.isass.core.net.request.Request;
import vip.isass.core.net.request.worker.WorkerPool;
import vip.isass.core.support.SpringContextUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import javax.annotation.Resource;

/**
 * 管理业务逻辑工人线程线程池
 * 分配式
 *
 * @author Rain
 */
@Slf4j
@ConditionalOnMissingBean(WorkerPool.class)
public class DefaultWorkerPool implements WorkerPool {

    @Resource
    private SpringContextUtil ipasSpringContextUtil;

    /**
     * 最少的工人队列
     */
    private int minWorkerCount = 5;

    /**
     * 最大的工人队列
     */
    private int maxWorkerCount = 30;

    private DefaultWorker[] workers;

    /**
     * 初始化工作线程。
     * 创建此 bean 时，会自动被 spring 调用一次
     */
    @Override
    public DefaultWorkerPool init() {
        log.info("DefaultWorkerPool Initializing");
        workers = new DefaultWorker[this.minWorkerCount];

        for (int i = 0; i < workers.length; i++) {
            DefaultWorker worker = SpringContextUtil.getBean(DefaultWorker.class);
            worker.setName("Worker-" + i);
            workers[i] = worker;
            worker.start();
        }
        return this;
    }

    /**
     * 将网络事件包分配到指定的
     */
    @Override
    public void putRequestInQueue(Request request) {
        int index = RandomUtil.randomInt(0, workers.length);
        DefaultWorker worker = workers[index];
        worker.acceptRequest(request);
    }

    public static void main(String[] args) {
        int i = 55 % 5;
        System.out.println(i);
    }
}
