package cn.hedeoer.multithreading.sync;

public class $02AtomicTest4 {

    public static class Counter extends Thread {

        // Counter Variable
        volatile int count = 0;

        // method which would be called upon
        // the start of execution of a thread
        public  void run() {
            int max = 1_000_00_000;

            // incrementing counter
            // total of max times

            for (int i = 0; i < max; i++) {
                count++;
            }

        }
    }

    /**
     * 累计效果由于涉及多个两个线程的同时操作count，会出现条件竞态
        即使使用volatile关键字，也无法解决这个问题
     *  volatile能够使得线程间感知最新修改，但是不能解决线程对过时值进行计算，无法保证"读-改-写"的原子性
     *
     *  count++;
     * 这个看似简单的操作实际上包含三个步骤：
     * 读取：获取count的当前值
     * 修改：将值加1
     * 写入：将新值写回count
     * 即使count是volatile的，这三个步骤也不会作为一个不可分割的整体执行。
     *
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
