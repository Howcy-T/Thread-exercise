package cn.tamhouse.thread.mutex;

import lombok.extern.slf4j.Slf4j;

/**
 * @author th
 * @Descirbe 同步计算 同时加减操作 最后结果为0
 * @date 2022/10/23 17:11
 */
public class SyncCalculate {


    public static void main(String[] args) throws InterruptedException {
        Room room=new Room();
        Thread add = new Thread(room::increment);
        Thread sub = new Thread(room::decrement);
        add.start();
        sub.start();
        add.join();
        sub.join();
        System.out.println(room.getNum());
    }
}


class Room{
    private int num=0;

    public void increment(){
        for (int i = 0; i < 5000; i++) {
            synchronized (this){
                num++;
            }

        }
    }

    public void decrement(){
        for (int i = 0; i < 5000; i++) {
            synchronized (this){
                num--;
            }

        }
    }

    public int getNum() {
        synchronized (this){
            return num;
        }
    }
}
