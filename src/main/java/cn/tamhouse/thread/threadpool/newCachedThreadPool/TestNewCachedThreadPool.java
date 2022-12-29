package cn.tamhouse.thread.threadpool.newCachedThreadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Tamhouse
 * @Describe 带缓冲的线程池 只有救急线程没有核心线程 阻塞队列不存放任何任务
 * @Date 2022/12/29 17:06
 */
@Slf4j
public class TestNewCachedThreadPool {

    public static void main(String[] args) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool(new ThreadFactory() {
            private final AtomicInteger poolNumber=new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"pool"+poolNumber.getAndIncrement());
            }
        });

        for (int i = 0; i < 3; i++) {
            int j=i;
            cachedThreadPool.execute(()-> log.info(""+j));
        }
    }
}
