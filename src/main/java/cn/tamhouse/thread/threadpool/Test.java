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
        ThreadPool threadPool=new ThreadPool(2,500, TimeUnit.MILLISECONDS,5,((queue, task) -> {
            //死等
            //queue.put(task);
            //带超时等待
            //queue.offer(task,500,TimeUnit.MILLISECONDS);
            //抛出异常
            //throw new RuntimeException("队列满了，直接放弃任务");
            //调用者自己执行
            task.run();
            //放弃任务
            //log.info("任务:{}已经放弃",task);
        }));
        for (int i = 0; i < 10; i++) {
            int j=i;
            Runnable task=()->{
                log.info("hello:{}",j+1);
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            };
            threadPool.execute(task);
            //log.info("任务:{}被执行",task.toString());
        }
    }
}
