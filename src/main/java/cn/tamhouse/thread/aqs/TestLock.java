package cn.tamhouse.thread.threadpool.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author Tamhouse
 * @Describe
 * @Date 2022/12/30 17:43
 */
@Slf4j
public class TestLock {
    public static void main(String[] args) {
        Lock lock=new LockImpl();

        new Thread(()->{
            lock.lock();
            log.info("lock one time...");
            lock.lock();
            try {
                log.info("locking....");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }finally {
                lock.unlock();
                log.info("unlocking...");
            }
        },"t1").start();
        new Thread(()->{
            lock.lock();
            try {
                log.info("locking....");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }finally {
                lock.unlock();
                log.info("unlocking...");
            }
        },"t2").start();
        new Thread(()->{
            lock.lock();
            try {
                log.info("locking....");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }finally {
                lock.unlock();
                log.info("unlocking...");
            }
        },"t3").start();
    }
}
