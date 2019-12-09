package vip.isass.core.net.request.worker.sequential;

import vip.isass.core.net.request.Request;
import vip.isass.core.net.request.worker.WorkerPool;
import vip.isass.core.net.session.BlueSession;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import vip.isass.core.net.session.BlueSession;

import java.util.concurrent.*;

/**
 * @author hone 2018/5/7
 * 需要使用此线程池时，业务创建此 bean 即可
 */
@Accessors(chain = true)
public class SequentialWorkerPool implements WorkerPool {

    /**
     * 核心线程数（最小线程数）
     */
    @Getter
    @Setter
    @Value("${pool.corePoolSize:5}")
    private int corePoolSize;

    /**
     * 线程池允许最大的线程数
     */
    @Getter
    @Setter
    @Value("${pool.maximumPoolSize:10}")
    private int maximumPoolSize;

    @Getter
    @Setter
    @Value("${pool.taskThreshold:10}")
    private int taskThreshold;

    /**
     * 空闲存活时间，单位：毫秒
     */
    @Getter
    @Setter
    @Value("${pool.keepAliveTime:10000}")
    private int keepAliveTime;

    /**
     * key: 分组的 key
     */
    @Getter
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<Request>> workMap = new ConcurrentHashMap<>();

    /**
     * key: 分组的 key
     */
    @Getter
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<Request>> workingMap = new ConcurrentHashMap<>();

    private ExecutorService workerService;

    private ExecutorService dispatcherService = new ThreadPoolExecutor(
            1,
            1,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadFactoryBuilder().setNameFormat("dispatcher-%d").build());

    private final DispatcherWorker dispatcherWorker = new DispatcherWorker(this);

    /**
     * 将网络事件包添加到队列中
     */
    @Override
    public void putRequestInQueue(Request request) {
        workMap.computeIfAbsent(request.getGroup(), k -> new ConcurrentLinkedQueue<>()).offer(request);
    }


    public void shutdownNow() {
        workerService.shutdownNow();
        dispatcherService.shutdownNow();
    }

    @Override
    public SequentialWorkerPool init() {
        initWorkerExecutorService();
        return this;
    }

    private void initWorkerExecutorService() {
        workerService = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1),
                new ThreadFactoryBuilder()
                        .setNameFormat("worker-%d")
                        .setDaemon(true)
                        .build());
        dispatcherService.execute(dispatcherWorker);
    }

    @Slf4j
    private static class DispatcherWorker implements Runnable {

        private SequentialWorkerPool sequentialWorkerPool;

        public DispatcherWorker(SequentialWorkerPool sequentialWorkerPool) {
            this.sequentialWorkerPool = sequentialWorkerPool;
        }

        private int count;
        private boolean isCore = true;

        @Override
        public void run() {
            while (true) {
                try {
                    doRun();
                } catch (Throwable throwable) {
                    //                    log.error(throwable.getMessage(), throwable);
                }
            }
        }

        private void doRun() {
            if (attainThreshold()) {
                if (isCore) {
                    isCore = count++ < sequentialWorkerPool.getCorePoolSize();
                }
                SequentialWorker sequentialWorker = new SequentialWorker(sequentialWorkerPool, isCore);
                sequentialWorkerPool.workerService.execute(sequentialWorker);
            }
        }

        private boolean attainThreshold() {
            return idleTaskCount() >= sequentialWorkerPool.getTaskThreshold();
        }

        private long idleTaskCount() {
            return sequentialWorkerPool.getWorkMap().values().stream().filter(queue -> !queue.isEmpty()).count() - sequentialWorkerPool.getWorkingMap().size();
        }

    }

    public static void main(String[] args) {

        SequentialWorkerPool pool = new SequentialWorkerPool()
                .setCorePoolSize(5)
                .setMaximumPoolSize(10)
                .setTaskThreshold(10)
                .setKeepAliveTime(10000)
                .init();

        for (int i = 0; i < 100; i++) {
            pool.putRequestInQueue(
                    new Request<>(
                            i + "",
                            // new BlueBinaryPacket(),
                            null,
                            new BlueSession(null),
                            null)
            );
        }

    }

}
