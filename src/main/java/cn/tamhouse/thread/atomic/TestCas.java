package cn.tamhouse.thread.atomic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Tamhouse
 * @Describe
 * @Date 2022/12/22 15:55
 */
@Slf4j
public class TestCas {
    public static void main(String[] args) throws InterruptedException {
        Student student=new Student();
        student.setAge(10);
        student.setName("zhangsan");
        AtomicReference<Student> studentAtomicReference=new AtomicReference<>(student);
        Student prev = studentAtomicReference.get();
        new Thread(()->{
            //student=new Student(10,"zhangsan");
            student.setAge(12);
            studentAtomicReference.set(student);
        }).start();
        TimeUnit.SECONDS.sleep(1);
        Student next = new Student(18, "lisi");
        boolean compareAndSet = studentAtomicReference.compareAndSet(prev, next);
        log.info("cas修改年龄:{}",compareAndSet);
        log.info("修改后的student:{}",studentAtomicReference.get());
    }





}

class Student{
    private int age;
    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Student(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public Student() {
    }

    @Override
    public String toString() {
        return "Student{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
