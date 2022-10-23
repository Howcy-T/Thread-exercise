package cn.tamhouse.thread.interrupt;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author th
 * @Descirbe 打断park   park会让线程暂停，当interrupted 为true的时候park失效
 * @date 2022/10/23 15:44
 */
public class InterruptPark {

    public static void main(String[] args) throws InterruptedException {
        Thread t1=new Thread(() -> {
            System.out.println("线程运行");
            LockSupport.park();
            System.out.println(Thread.interrupted());
            LockSupport.park();
            System.out.println("线程继续运行");
        });
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        t1.interrupt();
    }
}
