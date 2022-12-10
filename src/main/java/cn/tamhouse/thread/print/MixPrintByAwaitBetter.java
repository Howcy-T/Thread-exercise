package cn.tamhouse.thread.print;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Tamhouse
 * @date 2022年12月10日 22:17
 */
@Slf4j
public class MixPrintByAwaitBetter extends ReentrantLock {
    private int loopNums;

    public MixPrintByAwaitBetter(int loopNums){
        super();
        this.loopNums=loopNums;
    }

    public void print(String printNum, Condition current,Condition next){
        for (int i = 0; i < loopNums; i++) {
            lock();
            try{
                current.await();
                log.info(printNum);
                //唤醒下一个
                next.signalAll();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MixPrintByAwaitBetter mixPrintByAwaitBetter=new MixPrintByAwaitBetter(5);
        Condition condition1 = mixPrintByAwaitBetter.newCondition();
        Condition condition2 = mixPrintByAwaitBetter.newCondition();
        Condition condition3 = mixPrintByAwaitBetter.newCondition();
        new Thread(() -> {
           mixPrintByAwaitBetter.print("1",condition1,condition2);
        }).start();
        new Thread(() -> {
            mixPrintByAwaitBetter.print("2",condition2,condition3);
        }).start();
        new Thread(() -> {
            mixPrintByAwaitBetter.print("3",condition3,condition1);
        }).start();
        //主线程为发起者
        TimeUnit.SECONDS.sleep(1);
        mixPrintByAwaitBetter.lock();
        try {
            condition1.signalAll();
        } finally {
            mixPrintByAwaitBetter.unlock();
        }

    }
}
