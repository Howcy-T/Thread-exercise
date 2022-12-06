package cn.tamhouse.thread.wait;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author th
 * @Descirbe
 * @date 2022/12/6 16:13
 */
@Slf4j
public class TestWait {

    private static final Object lock=new Object();
    public static int a=1;
    public static void main(String[] args) throws InterruptedException {
        new Thread(() ->{
            synchronized (lock){
                if (a!=2){
                    log.info("未满足条件，owner线程调用wait方法");
                    try {
                        lock.wait();
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    log.info("满足条件，继续执行..");
                }
            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
        synchronized (lock){
            log.info("拿到锁，继续执行...");
            a=2;
            lock.notify();
        }
    }
}
