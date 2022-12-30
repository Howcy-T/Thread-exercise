package cn.tamhouse.thread.threadpool.api;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Tamhouse
 * @Describe 测试线程池的api
 * @Date 2022/12/30 15:04
 */
@Slf4j
public class TestApi {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.execute(()-> log.info("1"));
        Future<String> result = executorService.submit(() -> {
            log.info("2");
            return "2";
        });
        log.info("result:{}",result.get());
        List<Callable<String>> taskList=List.of(()-> "3",()->"4",()->"5",()->"6");
        String result2 = executorService.invokeAny(taskList);
        log.info("invokeAny result:{}",result2);
        List<Future<String>> futures = executorService.invokeAll(taskList);
        //log.info("线程池停止，没入队的任务直接放弃");
        //executorService.shutdown();
        log.info("线程池立即停止，所有任务终止");
        executorService.shutdownNow();
        futures.forEach(stringFuture -> {
            try {
                log.info("invokeAll result:{}",stringFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });


    }
}
