package cn.tamhouse.thread.print;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Tamhouse
 * @date 2022年12月10日 21:47
 */
@Slf4j
public class MixPrintByAwait {

    static ReentrantLock lock=new ReentrantLock();
    Condition condition1=lock.newCondition();
    Condition condition2=lock.newCondition();
    Condition condition3=lock.newCondition();
    private int printNum;
    private int loopNums;

    public MixPrintByAwait(int printNum, int loopNums) {
        this.printNum = printNum;
        this.loopNums = loopNums;
    }

    public void printOne(){
        for(int i=0;i<loopNums;i++){
            lock.lock();
            try {
                while (printNum!=1){
                    try {
                        condition1.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                log.info("1");
                printNum=2;
                //唤醒2
                condition2.signalAll();
            } finally {
                lock.unlock();
            }
        }

    }

    public void printTwo(){
        for(int i=0;i<loopNums;i++){
            lock.lock();
            try {
                while (printNum!=2){
                    try {
                        condition2.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                log.info("2");
                //唤醒2
                printNum=3;
                condition3.signalAll();
            } finally {
                lock.unlock();
            }
        }

    }

    public void printThree(){
        for(int i=0;i<loopNums;i++){
            lock.lock();
            try {
                while (printNum!=3){
                    try {
                        condition3.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                log.info("3");
                printNum=1;
                //唤醒2
                condition1.signalAll();
            } finally {
                lock.unlock();
            }
        }

    }

    public static void main(String[] args) {
        MixPrintByAwait mixPrintByAwait=new MixPrintByAwait(1,5);
        new Thread(mixPrintByAwait::printOne,"t1").start();
        new Thread(mixPrintByAwait::printTwo,"t2").start();
        new Thread(mixPrintByAwait::printThree,"t3").start();
    }
}
