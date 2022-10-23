package cn.tamhouse.thread.practice;

import java.util.concurrent.TimeUnit;

/**
 * @author th
 * @Descirbe 水壶没洗 开水没有 茶壶 茶杯要洗   茶叶和火有  如何泡茶最省时？
 * @date 2022/10/23 16:30
 */
public class MakeTea {

    public static void main(String[] args)  {
        //洗水壶、烧水
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        //洗茶壶 茶杯 放茶叶
        Thread t2 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                //等水烧开
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();


    }
}
