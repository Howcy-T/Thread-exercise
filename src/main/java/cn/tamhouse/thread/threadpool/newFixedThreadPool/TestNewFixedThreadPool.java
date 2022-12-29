package cn.tamhouse.thread.threadpool.newFixedThreadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Tamhouse
 * @Describe 测试定长线程池
 * @Date 2022/12/29 14:07
 */
@Slf4j
public class TestNewFixedThreadPool {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(2, new ThreadFactory() {
           private final AtomicInteger poolNumber=new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"pool"+poolNumber.getAndIncrement());
            }
        });

        threadPool.execute(()-> log.info("1"));
        threadPool.execute(()-> log.info("2"));
        threadPool.execute(()-> log.info("3"));
    }
}
