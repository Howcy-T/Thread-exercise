package cn.tamhouse.thread.threadpool;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Tamhouse
 * @Describe
 * @Date 2022/12/28 14:13
 */
public class ThreadPool {
}

class BlockingQueue<T>{

    /**
     * 队列实现
     */
    private Deque<T> queue =new ArrayDeque<>();

    /**
     * 锁
     */
    private ReentrantLock lock=new ReentrantLock();

    /**
     * 生产者条件变量
     */
    private Condition fullWaitSet=lock.newCondition();

    /**
     * 消费者条件变量
     */
    private Condition emptyWaitSet=lock.newCondition();

    /**
     * 队列容量
     */
    private final int capacity;

    public BlockingQueue() {
        this(10);
    }

    public BlockingQueue(int capacity) {
        if (capacity<=0){
            capacity=1;
        }
        this.capacity = capacity;
    }

    /**
     * 限时获取
     * @param timeout
     * @param unit
     * @return
     */
    public T poll(long timeout, TimeUnit unit){
        //超时时间单位统一转换纳秒
        long nanos = unit.toNanos(timeout);
        lock.lock();
        try {
            //如果队列为空，则等待
            while (queue.isEmpty()){
                if (nanos<=0){
                    //时间到了，没获取到直接返回空值
                    return null;
                }
                //返回剩余等待时间
                nanos = emptyWaitSet.awaitNanos(nanos);
            }
            T t = queue.removeFirst();
            //唤醒生产者
            fullWaitSet.signal();
            return t;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 阻塞获取
     * @return
     */
    public T take(){
        lock.lock();
        try {
            //如果队列为空，则等待
            while (queue.isEmpty()){
                emptyWaitSet.await();
            }
            T t = queue.removeFirst();
            //唤醒生产者
            fullWaitSet.signal();
            return t;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 阻塞投入
     */
    public void put(T t){
        lock.lock();
        try{
            //当队列长度等于容量上线时，生产者阻塞
            while (queue.size()==capacity){
                fullWaitSet.await();
            }
            queue.addLast(t);
            //唤醒消费者
            emptyWaitSet.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public int size(){
        lock.lock();
        try {
            return queue.size();
        }finally {
            lock.unlock();
        }
    }
}
