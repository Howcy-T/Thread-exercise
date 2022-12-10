package cn.tamhouse.thread.print;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Tamhouse
 * @Describe 多个线程顺序交替打印数字 1 2 3 重复5次
 * @date 2022年12月10日 20:37
 */
@Slf4j
public class MixPrintByWait {

    /**
     * 要打印的数字
     */
    private int printNum;

    /**
     * 循环次数
     */
    private int loopNum;


    public void print(int num){

        for (int i=0;i<loopNum;i++){

            synchronized (this){
                while (num != printNum) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                log.info(String.valueOf(num));
                //改变下次打印的数字
                printNum = (printNum+1)%3==0?3:(printNum+1)%3;
                this.notifyAll();
            }
        }
    }




    public MixPrintByWait() {
    }

    public MixPrintByWait(int printNum, int loopNum) {
        this.printNum = printNum;
        this.loopNum = loopNum;
    }

    public static void main(String[] args) {
        MixPrintByWait mixPrintByWait=new MixPrintByWait(1,5);
        for (int i = 1; i < 4; i++) {
            int finalI = i;
            new Thread(() -> mixPrintByWait.print(finalI ),"t"+i).start();
        }
    }

}
