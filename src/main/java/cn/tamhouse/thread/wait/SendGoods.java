package cn.tamhouse.thread.wait;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author th
 * @Descirbe 模拟送货场景，小明需要等快递员送货上门才能干活，其他人不用
 * @date 2022/12/6 17:40
 */
@Slf4j
public class SendGoods {

    public static boolean isGoodsExist = false;

    public static boolean isFoodExist = false;

    public static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (lock) {
                if (!isGoodsExist) {
                    log.info("货还没到，直接开摆");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (isGoodsExist) {
                    //synchronized (lock) {
                    log.info("货到了，开搞");
                    // }
                } else {
                    log.info("到不了，不干了");
                }
            }
        }, "小明").start();

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                synchronized (lock) {
                    log.info("干活中...");
                }
            }, "其他人").start();
        }

        new Thread(() -> {
            synchronized (lock){
                if (!isFoodExist) {
                    log.info("外卖没到，等待中...");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (isFoodExist) {

                log.info("外卖到了，开始干活");

            } else {
                log.info("外卖还是没来，干不了活了");
            }
        }, "小红").start();

        //主线程睡眠1s,放弃竞争
        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> {
            synchronized (lock) {
                log.info("送外卖...");
                isFoodExist = true;
                //提醒小明干活了
                lock.notify();
            }
        }, "外卖员").start();

//        new Thread(() -> {
//            synchronized (lock){
//                log.info("送外卖了...");
//                isFoodExist=true;
//                lock.notify();
//            }
//        }).start();

    }
}
