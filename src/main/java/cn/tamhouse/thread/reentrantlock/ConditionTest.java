package cn.tamhouse.thread.reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author th
 * @Descirbe
 * @date 2022/12/10 15:49
 */
@Slf4j
public class ConditionTest {
    static boolean hasTakeOut=false;
    static boolean hasDeliver=false;
    static ReentrantLock room=new ReentrantLock();
    //休息室
    static Condition waitTakeOut=room.newCondition();
    static Condition waitDeliver=room.newCondition();
    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            room.lock();
            while (!hasTakeOut){
                log.info("外卖没到直接开摆...");
                try {
                    waitTakeOut.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                log.info("外卖到了，开始干活...");
            } finally {
                room.unlock();
            }
        },"小明").start();



        new Thread(()->{
            room.lock();
            while (!hasDeliver){
                log.info("快递还不到，sb顺丰");
                try {
                    waitDeliver.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                log.info("快递总算到了啊");
            } finally {
                room.unlock();
            }
        },"小红").start();


        new Thread(()->{
            room.lock();
            try {
                hasTakeOut=true;
                waitTakeOut.signalAll();
                log.info("外卖已送达");
            } finally {
                room.unlock();
            }
        },"外卖小哥").start();

        TimeUnit.SECONDS.sleep(2);

        new Thread(()->{
            room.lock();
            try {
                hasDeliver=true;
                waitDeliver.signalAll();
                log.info("快递已送达");
            } finally {
                room.unlock();
            }
        },"顺丰").start();

    }
}
