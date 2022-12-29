package cn.tamhouse.thread.threadpool.newSingleThreadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Tamhouse
 * @Describe 只有一个线程运行的线程池 如果线程出现异常，则重新创建一个线程执行任务
 * @Date 2022/12/29 21:03
 */
@Slf4j
public class TestNewSingleThreadPool {

    public static void main(String[] args) {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            private final AtomicInteger poolNumber=new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"pool"+poolNumber.getAndIncrement());
            }
        });
    }
}
