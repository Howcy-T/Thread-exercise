package cn.tamhouse.thread.print;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * @author Tamhouse
 * @date 2022年12月10日 20:17
 */
@Slf4j
public class SyncPrintByPark {
    public  void printOne(){
        LockSupport.park();
        log.info("1");
    }

    public  void printTwo(Thread thread){
        log.info("2");
        LockSupport.unpark(thread);
    }

    public static void main(String[] args) {
        SyncPrintByPark syncPrintByPark=new SyncPrintByPark();
        Thread t1 = new Thread(syncPrintByPark::printOne, "t1");
        t1.start();
        new Thread(()->syncPrintByPark.printTwo(t1),"t2").start();
    }

}
