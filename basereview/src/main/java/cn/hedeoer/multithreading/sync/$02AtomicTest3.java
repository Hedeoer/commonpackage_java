package cn.hedeoer.multithreading.sync;

import java.util.concurrent.atomic.AtomicInteger;

public class $02AtomicTest3 {

    public static class Counter extends Thread {

        // Counter Variable
        AtomicInteger count = new AtomicInteger(0);

        // method which would be called upon
        // the start of execution of a thread
        public void run() {
            int max = 1_000_00_000;

            // incrementing counter
            // total of max times

            for (int i = 0; i < max; i++) {
                count.incrementAndGet();
            }

        }
    }

    /**
     * 累计效果由于涉及多个两个线程的同时操作count，会出现条件竞态
     * 使用 atomic相关类型(AtomicInteger) 可以解决这个问题
     *CAS大概机制
     * 读取阶段：获取变量的当前值
     * 计算阶段：基于当前值计算新值
     * CAS尝试：原子性地比较并更新
     * 如果成功：退出循环，操作完成
     * 如果失败：整个过程重来，从读取最新值开始
     *
     * 此例中如果A线程在尝试更新count时，B线程已经更新了count的值，那么A线程会重新读取count的值，然后再次尝试更新count的值，
     * 直到A线程成功更新count的值为止，最终保证累计效果正确
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args)
            throws InterruptedException {
        // Instance of Counter Class
        Counter c = new Counter();

        // Defining Two different threads
        Thread first = new Thread(c, "First");
        Thread second = new Thread(c, "Second");

        // Threads start executing
        first.start();
        second.start();

        // main thread will wait for
        // both threads to get completed
        first.join();
        second.join();

        // Printing final value of count variable
        System.out.println(c.count);
    }

}
