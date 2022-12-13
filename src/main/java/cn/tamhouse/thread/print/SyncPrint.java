package cn.tamhouse.thread.print;

import lombok.extern.slf4j.Slf4j;

/**
 * @author th
 * @Descirbe 通过wait/notify的方式顺序打印,要求先打印2 再打印1
 * @date 2022/12/10 16:38
 */
@Slf4j
public class SyncPrint {
    static boolean isTwoPrint=false;
    public synchronized void printOne()  {
        while (!isTwoPrint){
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
        }
        log.info("1");
    }

    public synchronized void printTwo(){
        log.info("2");
        isTwoPrint=true;
        this.notifyAll();
    }

    public static void main(String[] args) {
        SyncPrint syncPrint=new SyncPrint();
        new Thread(syncPrint::printOne,"t1").start();
        new Thread(syncPrint::printTwo,"t2").start();
    }
}
