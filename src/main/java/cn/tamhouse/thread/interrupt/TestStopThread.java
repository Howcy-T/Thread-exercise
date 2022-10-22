package cn.tamhouse.thread.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * @author th
 * @Descirbe
 * @date 2022/10/22 20:38
 */
public class TestStopThread {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("线程:" + Thread.currentThread().getName() + "执行中...");
            }
            System.out.println("线程:" + Thread.currentThread().getName() + "已经停止");

        });
        t1.start();
        TimeUnit.SECONDS.sleep(2);
        t1.interrupt();
    }
}
