package cn.tamhouse.thread.wait;

import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.hash;

/**
 * @author th
 * @Descirbe 保护性暂停示例
 * @date 2022/12/7 11:31
 */
@Slf4j
public class GuardedSuspension {
    public static void main(String[] args) {
        GuardedObject guardedObject=new GuardedObject();
        new Thread(()->{
            try {
                Integer o = (Integer) guardedObject.get();
                log.info("获取变量:{}",o);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1").start();

        new Thread(()-> {
            guardedObject.compact(getNums());
        },"t2").start();
    }

    private static int getNums(){
        int count =0;
        for (int i = 0; i < 1000000000; i++) {
            count=count+i;
        }
        return count;
    }




}


@Slf4j
class GuardedObject{
    /**
     * 被访问的变量
     */
    private Object response;

    /**
     * 获取资源
     * @return
     */
    public synchronized Object get() throws InterruptedException {
        //如果不满足条件则挂起
        while (response==null){
           log.info("变量为空，等待中...");
           this.wait();
        }
        return response;
    }

    /**
     * 存放资源
     * @param response
     */
    public synchronized void compact(Object response){
        log.info("给变量赋值");
        this.response=response;
        this.notifyAll();
    }
}