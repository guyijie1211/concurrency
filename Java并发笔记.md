## 1.基础知识



###1.1新建线程方法(3种)

1.继承Thread，重写run()方法:
```java
Thread newThread = new Thread() {
    @Override
    public void run() {
        System.out.println("newThread，继承Thread，重写run()");
    }
};
class MyNewThread extends Thread {
    @Override
    public void run() {
        System.out.println("MyNewThread类，继承Thread，重写run()");
        //如果重写了该方法且该方法中没有super.run()，那么是永远不会调用Runnable实现的run()方法；
        super.run();
    }
}
newThread.start();
new MyNewThread().start();
```
2.实现runnable接口:
```java
Thread newRunableThread = new Thread(new Runnable() {
    @Override
    public void run() {
        System.out.println("newRunableThread,实现runnable接口");
    }
});
newRunableThread.start();
```
3.实现callable接口(可以获取结果):
```java
class newCallableThread implements Callable<String> {
    @Override
    public String call() throws Exception {
        return "newCallableThread,实现callable接口";
    }
}
FutureTask<String> futureTask = new FutureTask<>(new newCallableThread());
Thread callableThread = new Thread(futureTask);
callableThread.start();
System.out.println(futureTask.get());
```



###1.2线程状态转换

线程转换流程如图所示，不同方法会是线程进入对应状态   

用一个表格对上面的六种状态进行归纳   
状态名称|说明
:---: | :---: 
NEW|初始状态，线程被构建，但还没有调用`start()`方法
RUNNABLE|运行状态，Java线程将操作系统中的就绪和运行两种状态笼统地成为“运行中”
BLOCKED|阻塞状态，标识线程阻塞与锁
WAITING|等待状态，表示线程进入等待状态，进入该状态表示当前线程需要等待其他线程做出一些特定动作（通知或中断）
TIME_WAITING|超时等待状态，该状态不同于WAITING，他是可以在指定时间内自行返回的
TERMINATED|终止状态，表示当前线程已经执行完毕  



###1.3线程状态的基本操作

####1.interrupt()(中断)： 

其他线程可以用interrupt( )来对本线程进行中断操作，本线程可以用isInterrupted( )来感知其他线程对自己的中断操作。也可以用Thread.interrupted( )来测试当前线程是否被中断。Thread.interrupted( )和InterruptedException异常的抛出，会清除中断标志位。  

方法名|详细解释|备注
:---:|:---:|:---:
`public void interrupt()`|中断该线程对象|如果该线程被调用了`wait()`/`sleep()`/`join()`方法时，就会抛出`InterruptedException`并且**清除中断标志**
`public boolean isInterrupted()`|测试该线程对象是否被中断|中断标志**不会被清除**
`public static boolean interrupted`|测试当前线程是否被中断|中断标志**会被清除**

以下代码演示抛出`InterruptedException`后中断标志的清除

```java
public class InterruptDemo {
    public static void main(String[] args) throws InterruptedException {
        //执行时sleep5秒的线程，sleep状态下是阻塞状态，阻塞时被interrupt()会抛出InterruptedException异常
        Thread sleepThread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        };

        //死循环的线程
        Thread busyThread = new Thread(() -> {
            while(true);
        });

        //启动两个线程
        sleepThread.start();
        busyThread.start();
        //打断两个线程
        sleepThread.interrupt();
        busyThread.interrupt();
        //保证InterruptedException被抛出后才执行isInterrupted()判断
        Thread.sleep(1000);
        //输出线程是否被打断的判断结果
        System.out.println("sleepThread:" + sleepThread.isInterrupted());
        System.out.println("busyThread:" + busyThread.isInterrupted());
    }
}
```

输出结果： sleepThread:false	busyThread:true

说明InterruptedException的抛出会清除Interrupted标记位置，一般在**结束线程时通过中断标志位或者标志位的方式可以有机会去清理资源，相对于武断而直接的结束线程，这种方式要优雅和安全。**

####2.join()

如果一个线程实例A执行了threadB.join(),其含义是：当前线程A会等待threadB线程终止后threadA才会继续执行。关于join方法一共提供如下这些方法:

```java
public final synchronized void join(long millis);
public final synchronized void join(long millis, int nanos);
public final void join() throws InterruptedException
```

Thread类除了提供join()方法外，另外还提供了超时等待的方法，如果线程threadB在等待的时间内还没有结束的话，threadA会在超时之后继续执行。

join方法源码关键是：

```java
while (isAlive()) {
    wait(0);
 }
```

当前等待线程会一直阻塞，直到等待的线程退出时，调用`notifyAll()`方法通知所有等待线程。

以下代码说明`join()`方法的使用：

```java

```

