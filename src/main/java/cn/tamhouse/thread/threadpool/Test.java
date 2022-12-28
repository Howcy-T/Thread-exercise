package cn.tamhouse.thread.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author Tamhouse
 * @Describe
 * @Date 2022/12/28 17:44
 */
@Slf4j
public class Test {
    public static void main(String[] args) {
        ThreadPool threadPool=new ThreadPool(2,1000, TimeUnit.MILLISECONDS,5);
        for (int i = 0; i < 6; i++) {
            int j=i;
            Runnable task=()->{
                log.info("hello:{}",j+1);
            };
            threadPool.execute(task);
            log.info("任务:{}被执行",task.toString());
        }
    }
}
