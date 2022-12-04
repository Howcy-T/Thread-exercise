## 并发编程学习

### Synchronized底层操作之Monitor

- Monitor为监控者或称为管程，每个java对象都可以关联一个monitor对象，如果使用Synchronized来进行加重量级锁的话，会在Mark word中存放monitor地址，来与monitor对象相关联。

- 在synchronized代码块中操作时，被锁住的对象的对象头中的Mark Word会发生变化

![img](imgs/3f6898572f1746a99a674c545ab7beb1.png)

![img](imgs/d9cacd75c48f4072b8d26b83ff681e52.png)

- mark word主要为64bit,根据对象的不同状态，对象头的内容也不一致，Normal(正常)，Biased(偏离锁)，Lightweight Locked(轻量级锁)，Heavyweight Lock(重量级锁)。

- 在进入synchronized代码块操作之后，对象头中的Mark Word内容与Monitor进行绑定

- **Monitor的内部结构**：

  - owner：即当前拿到锁的线程
  - EntryList：未拿到锁的其他阻塞线程 ，当owner释放锁的时候，entryList中的线程重新竞争锁（非公平）。
  - WaitSet：当线程拿到锁的时候，因为某种原因需要让出cpu资源，也就是调用了wait方法或者join方法，线程进入WAITING或者TIME_WAITING状态时，线程进入WaitSet

  ![img](imgs/1ee657e0a3db4da5b483f001bceea4bf.png)



### 锁膨胀

- 当锁没有竞争关系的时候，使用的是偏量锁
- 当锁开始有竞争的时候，会膨胀为轻量锁 
- 当锁竞争激烈的时候，膨胀为重量级锁

### 轻量级锁加解锁过程

- 轻量级锁尝试加锁的时候，锁记录的对象指针 Object Reference 会指向被加锁的对象，锁记录会和被锁住的对象头中mark word进行CAS交换操作，只有对象头mark word中最后两位为01 且无偏向记录的时候CAS才会成功，也即加锁成功![在这里插入图片描述](imgs/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl81MDI4MDU3Ng==,size_16,color_FFFFFF,t_70.png)

- 加锁成功之后，对象的对象头储存的就是锁记录的地址和状态 00 表示轻量级锁![在这里插入图片描述](imgs/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl81MDI4MDU3Ng==,size_16,color_FFFFFF,t_70-16667560647462.png)
- 如果CAS交换失败，则对应两种情况：
  - 其他线程已经加锁，存在竞争现象，首先会进行自旋锁，自旋一定次数后，如果还是失败就进入锁膨胀阶段。
  - 