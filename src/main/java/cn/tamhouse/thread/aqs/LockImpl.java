package cn.tamhouse.thread.threadpool.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author Tamhouse
 * @Describe 自定义不可重入锁的实现：外部实现Lock接口 内部继承AQS
 * @Date 2022/12/30 16:57
 */
public class LockImpl implements Lock {

    static class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            final Thread current = Thread.currentThread();
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(current);
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getExclusiveOwnerThread()!=null&&Thread.currentThread()==getExclusiveOwnerThread()){
                setExclusiveOwnerThread(null);
                setState(0);
            }
            return true;
        }

        @Override
        protected boolean isHeldExclusively() {
            return Thread.currentThread()==getExclusiveOwnerThread();
        }


        public Condition newCondition() {
            return new ConditionObject();
        }
    }

    private final Sync sync=new Sync();

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
