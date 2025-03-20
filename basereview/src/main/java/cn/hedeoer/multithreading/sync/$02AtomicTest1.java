package cn.hedeoer.multithreading.sync;

public class $02AtomicTest1 {

    public static class Counter extends Thread {

        // Counter Variable
        int count = 0;

        // method which would be called upon
        // the start of execution of a thread
        public void run() {
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
