package cn.tamhouse.thread.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Tamhouse
 * @Describe
 * @Date 2022/12/28 14:13
 */
@Slf4j
public class ThreadPool {

    /**
     * 阻塞队列
     */
    private BlockingQueue<Runnable> taskQueue;

    /**
     * 线程集合
     */
    private Set<Worker> workers = new HashSet<>();

    /**
     * 核心线程数
     */
    private int coreSize;

    /**
     * 超时时间
     */
    private long timeout;

    /**
     * 时间单位
     */
    private TimeUnit unit;

    /**
     * 阻塞队列大小
     */
    private int queueSize;

    private RejectStrategy<Runnable> rejectStrategy;

    public ThreadPool(int coreSize, long timeout, TimeUnit unit, int queueSize,RejectStrategy<Runnable> rejectStrategy) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.unit = unit;
        this.queueSize = queueSize;
        this.taskQueue = new BlockingQueue<>(queueSize);
        this.rejectStrategy=rejectStrategy;
    }

    public void execute(Runnable task) {
        //判断是否超过最大工作线程数量
        synchronized (workers) {
            if (workers.size() < coreSize) {
                //小于核心线程数，正常工作
                Worker worker = new Worker(task);
                workers.add(worker);
                log.info("worker:{}加入线程池",worker);
                worker.start();
            } else {
                //大于核心线程数，尝试加入阻塞队列
//                log.info("task:{}加入阻塞队列",task);
//                taskQueue.offer(task,timeout,unit);
                taskQueue.tryPut(rejectStrategy,task);
            }
        }
    }





    class Worker extends Thread {
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            while (task != null||(task=taskQueue.poll(timeout,unit))!=null) {
                try {
                    task.run();
                    log.info("worker:{}执行任务task:{}",this,task);
                }finally {
                    task=null;
                }
            }
            synchronized (workers){
                workers.remove(this);
            }
        }
    }

}

@Slf4j
class BlockingQueue<T> {

    /**
     * 队列实现
     */
    private Deque<T> queue = new ArrayDeque<>();

    /**
     * 锁
     */
    private ReentrantLock lock = new ReentrantLock();

    /**
     * 生产者条件变量
     */
    private Condition fullWaitSet = lock.newCondition();

    /**
     * 消费者条件变量
     */
    private Condition emptyWaitSet = lock.newCondition();

    /**
     * 队列容量
     */
    private final int capacity;

    public BlockingQueue() {
        this(10);
    }

    public BlockingQueue(int capacity) {
        if (capacity <= 0) {
            capacity = 1;
        }
        this.capacity = capacity;
    }

    /**
     * 限时获取
     *
     * @param timeout
     * @param unit
     * @return
     */
    public T poll(long timeout, TimeUnit unit) {
        //超时时间单位统一转换纳秒
        long nanos = unit.toNanos(timeout);
        lock.lock();
        try {
            //如果队列为空，则等待
            while (queue.isEmpty()) {
                if (nanos <= 0) {
                    //时间到了，没获取到直接返回空值
                    return null;
                }
                //返回剩余等待时间
                try {
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            //唤醒生产者
            fullWaitSet.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 阻塞获取
     *
     * @return
     */
    public T take() {
        lock.lock();
        try {
            //如果队列为空，则等待
            while (queue.isEmpty()) {
                try {
                    log.info("等待任务添加到延迟队列。。。");
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            T t = queue.removeFirst();
            //唤醒生产者
            fullWaitSet.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 阻塞投入
     */
    public void put(T t) {
        lock.lock();
        try {
            //当队列长度等于容量上线时，生产者阻塞
            while (queue.size() == capacity) {
                log.info("任务等待加入阻塞队列。。。");
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

    /**
     * 带超时时间的投放阻塞队列
     * @param t
     * @param timeout
     * @param unit
     * @return 是否入队成功
     */
    public boolean offer(T t,long timeout,TimeUnit unit){
        lock.lock();
        try {
            long nanos = unit.toNanos(timeout);
            //当队列长度等于容量上线时，生产者阻塞
            while (queue.size() == capacity) {
                if (nanos<=0){
                    log.info("入队超时。。。");
                    return false;
                }
                log.info("任务等待加入阻塞队列。。。");
                try {
                    nanos = fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addLast(t);
            //唤醒消费者
            emptyWaitSet.signal();
            return true;
        }  finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 尝试把任务加入阻塞队列，执行相应拒绝策略
     * @param rejectStrategy
     * @param task
     */
    public void tryPut(RejectStrategy<T> rejectStrategy, T task) {
        lock.lock();
        try {
            //判断队列是否已满
            if (queue.size() == capacity) {
                rejectStrategy.reject(this,task);
            }else {
                queue.addLast(task);
                emptyWaitSet.signal();
            }
        }finally {
            lock.unlock();
        }
    }
}
