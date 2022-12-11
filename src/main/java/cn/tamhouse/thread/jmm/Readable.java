package cn.tamhouse.thread.jmm;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author th
 * @Descirbe java内存模型之可见性
 * @date 2022/12/11 13:27
 */
@Slf4j
public class Readable {
    volatile static boolean run=true;
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {

            while (run){
                //线程执行
                //log.info("线程运行");
            }
            //log.info("线程停止");
        }).start();

        TimeUnit.SECONDS.sleep(1);

        run=false;//线程并不会停止

    }
}
