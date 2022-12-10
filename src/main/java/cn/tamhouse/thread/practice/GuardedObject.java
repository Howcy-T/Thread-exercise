package cn.tamhouse.thread.practice;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class GuardedObject {

    private int id=0;

    public GuardedObject() {
    }

    public GuardedObject(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
     * 被访问的变量
     */
    private Object response;

    /**
     * 获取资源
     * @return
     */
    public synchronized Object get(long timeout) throws InterruptedException {
        //如果不满足条件则挂起
        long begin=System.currentTimeMillis();
        long passTime=0;
        while (response==null){
           log.info("变量为空，等待中...");

           if (timeout==0){
               this.wait();
           }else {
               long waitTime=timeout-passTime;
               if (waitTime<=0){
                   log.info("放弃等待");
                   break;
               }
               this.wait(waitTime);
               passTime=System.currentTimeMillis()-begin;
           }
        }

        return response;
    }

    public synchronized Object get() throws InterruptedException {
        return this.get(0);
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