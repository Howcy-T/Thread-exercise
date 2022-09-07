package cn.tamhouse.thread.join;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author th
 * @Descirbe
 * @date 2022/9/4 16:15
 */
public class TestField {

    static int a=0;

    public static void main(String[] args) throws InterruptedException {
        test();
        TimeUnit.SECONDS.sleep(2);
        System.out.println(a);
    }

    private static void test() {
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
                a=10;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        System.out.println("a="+a);


    }

}
