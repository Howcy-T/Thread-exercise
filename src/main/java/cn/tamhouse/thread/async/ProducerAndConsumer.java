package cn.tamhouse.thread.async;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * @author th
 * @Descirbe 异步模型，生产者和消费者
 * @date 2022/12/9 11:13
 */
@Slf4j
public class ProducerAndConsumer {

    public static void main(String[] args) throws InterruptedException {
        //创建消息队列
        MessageQueue messageQueue=new MessageQueue();
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                int id= (int) Thread.currentThread().getId();
                Message message=new Message(id,Thread.currentThread().getName()+"的消息");
                messageQueue.put(message);
                log.info("生产消息：{}",message);
            },"生产者【"+i+"】").start();
            TimeUnit.SECONDS.sleep(1);
        }


            new Thread(() ->{
                while (true){
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = messageQueue.take();
                    log.info("消费消息：{}", message);
                }
            },"消费者").start();

    }
}

/**
 * 消息队列，模拟线程间的通信
 */
@Slf4j
class MessageQueue{
    //消息队列集合
    private Deque<Message> messageQueue=new ArrayDeque<>();
    //队列容量
    private int capacity;

    MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    public MessageQueue() {
        this(4);
    }

    /**
     * 获取消息
     * @return
     */
    public synchronized Message take(){
        while (messageQueue.isEmpty()){
            try {
                log.warn("无资源，等待中..");
                this.wait();
            } catch (InterruptedException e) {
                log.error("异常",e);
            }
        }
        Message message = messageQueue.remove();
        //唤醒生产者
        this.notifyAll();
        return message;
    }


    public synchronized void put(Message message){
        while (messageQueue.size()==capacity){
            try {
                log.warn("队列已满，等待中...");
                this.wait();
            } catch (InterruptedException e) {
                log.error("异常",e);
            }
        }
        messageQueue.add(message);
        //唤醒消费者
        this.notifyAll();
    }
}

final class Message{
    private int id;
    private String value;

    public Message() {
    }

    public Message(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}
