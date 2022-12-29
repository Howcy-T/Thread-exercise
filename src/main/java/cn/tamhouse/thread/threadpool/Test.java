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
        ThreadPool threadPool=new ThreadPool(2,500, TimeUnit.MILLISECONDS,5,((queue, task) -> {}));
        for (int i = 0; i < 10; i++) {
            int j=i;
            Runnable task=()->{
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("hello:{}",j+1);
            };
            threadPool.execute(task);
            log.info("任务:{}被执行",task.toString());
        }
    }
}
