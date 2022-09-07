package cn.tamhouse.thread.sleep;

import java.util.concurrent.TimeUnit;

/**
 * @author th
 * @Descirbe
 * @date 2022/9/4 15:28
 */
public class TestSleep {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
                System.out.println("hello");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");
        System.out.println(t1.getState());
        t1.start();
        System.out.println(t1.getState());
        //t1.interrupt();//打断睡眠
        //System.out.println(t1.getState());
    }
}
