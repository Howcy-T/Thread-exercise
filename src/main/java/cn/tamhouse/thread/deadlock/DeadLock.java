package cn.tamhouse.thread.deadlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author th
 * @Descirbe 模拟死锁
 * @date 2022/12/10 11:05
 */
@Slf4j
public class DeadLock {
    private static Object a=new Object();
    private static Object b=new Object();

    public static void main(String[] args) {
        new Thread(()->{
            synchronized (a){
                log.info("lock a...");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (b){
                    log.info("lock b...");
                }
            }

        },"t1").start();

        new Thread(()->{
            synchronized (b){
                log.info("lock b...");
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (a){
                    log.info("lock a...");
                }
            }

        },"t2").start();
    }
}
