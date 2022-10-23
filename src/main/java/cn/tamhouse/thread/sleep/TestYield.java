package cn.tamhouse.thread.sleep;

import java.util.concurrent.TimeUnit;

/**
 * @author th
 * @Descirbe yield作用：让出cpu 使当前线程
 * @date 2022/9/4 15:52
 */
public class TestYield {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            Thread.yield();
            System.out.println("t1开始执行");
        }, "t1");
        t1.start();


        Thread t2 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("t2开始执行");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2");
        t2.start();

        TimeUnit.SECONDS.sleep(1);


    }
}
