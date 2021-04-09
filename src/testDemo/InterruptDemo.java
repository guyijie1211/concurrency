package testDemo;

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
        /**
         * 输出结果
         * sleepThread:false
         * busyThread:true
         * 说明InterruptedException的抛出会清除Interrupted标记位置
         */
    }
}
