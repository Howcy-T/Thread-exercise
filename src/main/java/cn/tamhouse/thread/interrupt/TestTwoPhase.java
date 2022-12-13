package cn.tamhouse.thread.interrupt;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author th
 * @Descirbe 两阶段终止模式
 * 优雅打断线程，并给被打断的线程处理后事的机会
 * @date 2022/10/22 21:05
 */

@Slf4j
public class TestTwoPhase {

    public static void main(String[] args) throws InterruptedException {

        TwoPhaseTerminationBetter tpt = new TwoPhaseTerminationBetter();
        tpt.start();
        TimeUnit.MILLISECONDS.sleep(3400);
        tpt.stop();

    }


}


@Slf4j
class TwoPhaseTermination {


    /**
     * 监控线程，用于监控系统情况，也可以是其他的业务线程
     */
    private Thread monitor;

    /**
     * 启动监控线程
     */
    public void start() {
        monitor = new Thread(() -> {
            //循环检查
            while (true) {
                //如果当前线程被打断则停止
                if (Thread.currentThread().isInterrupted()) {
                    log.info("线程被打断，处理后事....");
                    break;
                }
                //未打断则睡眠两秒
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    //如果有异常则设置打断标记
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                //无异常执行监控记录，也可以理解为处理业务逻辑
                log.info("执行监控记录");
            }
        });
        monitor.start();
    }

    /**
     * 停止监控
     */
    public void stop() {
        monitor.interrupt();
    }
}


/**
 * 犹豫模式，防止同时调用同一个方法
 */
@Slf4j
class TwoPhaseTerminationBetter {


    /**
     * 监控线程，用于监控系统情况，也可以是其他的业务线程
     */
    private Thread monitor;

    private static volatile boolean isStop = false;
    private static volatile boolean start = false;

    /**
     * 启动监控线程
     */
    public void start() {
        monitor = new Thread(() -> {
            //循环检查
            while (true) {
                synchronized (this) {
                    if (start) {
                        return;
                    }
                    start = true;
                }
                //如果当前线程被打断则停止
                if (isStop) {
                    log.info("线程被打断，处理后事....");
                    break;
                }
                //未打断则睡眠两秒
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                }
                //无异常执行监控记录，也可以理解为处理业务逻辑
                log.info("执行监控记录");
            }
        });
        monitor.start();
    }

    /**
     * 停止监控
     */
    public void stop() {
        isStop = true;
        monitor.interrupt();
    }
}
