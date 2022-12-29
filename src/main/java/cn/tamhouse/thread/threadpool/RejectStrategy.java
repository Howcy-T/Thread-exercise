package cn.tamhouse.thread.threadpool;

/**
 * @author Tamhouse
 * @Describe 线程池拒绝策略
 * @Date 2022/12/28 22:06
 */
@FunctionalInterface
public interface RejectStrategy<T> {

    /**
     * 阻塞队列的拒绝策略
     * @param queue 阻塞队列
     * @param task 待执行的任务
     */
    void reject(BlockingQueue<T> queue,T task);
}
