package testDemo;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class NewThreadDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1.继承Thread，重写run()方法
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

        //2.实现runnable接口
        Thread newRunableThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("newRunableThread,实现runnable接口");
            }
        });
        newRunableThread.start();

        //3.实现callable接口(可以获取结果)
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
    }
}
