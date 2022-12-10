package cn.tamhouse.thread.print;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Tamhouse
 * @date 2022年12月10日 22:33
 */
@Slf4j
public class MixPrintByPark {

    private int loopNums;

    static Thread t1;
    static Thread t2;
    static Thread t3;

    public MixPrintByPark(int loopNums) {
        this.loopNums = loopNums;
    }

    public void print(String printStr,Thread next){
        for (int i=0;i<loopNums;i++){
            LockSupport.park();
            log.info(printStr);
            LockSupport.unpark(next);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MixPrintByPark mixPrintByPark=new MixPrintByPark(5);
        t1=new Thread(() -> mixPrintByPark.print("1",t2));
        t2=new Thread(() -> mixPrintByPark.print("2",t3));
        t3=new Thread(() -> mixPrintByPark.print("3",t1));
        t1.start();
        t2.start();
        t3.start();
        TimeUnit.SECONDS.sleep(1);
        LockSupport.unpark(t1);
    }

}



