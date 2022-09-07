package cn.tamhouse.thread.join;

import java.util.concurrent.TimeUnit;

/**
 * @author th
 * @Descirbe
 * @date 2022/9/4 16:24
 */
public class TestJoin {

    static int a = 0;

    public static void main(String[] args) throws InterruptedException {
        test();
        TimeUnit.SECONDS.sleep(2);
        System.out.println(a);
    }

    private static void test() throws InterruptedException {
        Thread t1 = new Thread(() -> {

            try {
                TimeUnit.SECONDS.sleep(1);
                a = 10;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



        });
        t1.start();
        t1.join();
        System.out.println("a=" + a);
    }
}
