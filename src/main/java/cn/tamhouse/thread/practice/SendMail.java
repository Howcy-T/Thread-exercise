package cn.tamhouse.thread.practice;

import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author th
 * @Descirbe 模拟写信和收信
 * @date 2022/12/8 17:35
 */
@Slf4j
public class SendMail {

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            new Thread(()->{
                GuardedObject guardedObject = Box.createGuardedObject();
                log.info("等待收信...");
                try {
                    String mail= (String) guardedObject.get(5000);
                    log.info("收到信件：{}",mail);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },"收信人"+i).start();
        }
        TimeUnit.SECONDS.sleep(1);
        for (Integer id : Box.getIds()) {
            new Thread(() ->{
                GuardedObject guardedObject = Box.getGuardedObject(id);
                String content="内容："+id;
                log.info("写信中...");
                guardedObject.compact(content);
            },"写信人"+id).start();
        }
    }
}


class Box{
    public static final Map<Integer,GuardedObject> boxes = new Hashtable<>();

    private static int id=1;
    /**
     * 生成id
     */
    private static synchronized int generateId(){
        return id++;
    }

    public static GuardedObject createGuardedObject(){
        GuardedObject guardedObject = new GuardedObject(generateId());
        boxes.put(guardedObject.getId(), guardedObject);
        return guardedObject;
    }

    public static GuardedObject getGuardedObject(int id){
        return boxes.remove(id);
    }

    public static Set<Integer> getIds(){
        return boxes.keySet();
    }
}
