package cn.hedeoer.multithreading.sync;

public class $01VolatileTest {

    // volatile 关键字用于修饰共享变量，它可以确保多个线程之间对该变量的读写操作是可见的。
    // volatile 变量的值永远不会被缓存，所有的写入和读取都将在主内存中完成
    // 此处不使用volatile关键字，会导致线程 changeListener 无法及时获取到最新的值，陷入死循环（changeListener
    // 会在其启用的线程中缓存counter的起始值）
    // 然而，volatile 变量的使用仅限于非常有限的情况，因为大多数情况下我们需要的是原子性
    // 一个简单的递增语句（如 x = x + 1; 或 x++）看似是一个单一的操作，但实际上是一个必须以原子方式执行的读-修改-写复合操作序列
    private static volatile int counter = 0;
    public static void main(String[] args) {

        new changeListener().start();
        new changeMaker().start();

        /*
        Make Change for counter : 1
        Got Change for counter : 1
        Make Change for counter : 2
        Got Change for counter : 2
        Make Change for counter : 3
        Got Change for counter : 3
        Make Change for counter : 4
        Got Change for counter : 4
        Make Change for counter : 5
        Got Change for counter : 5
        * */
    }

    /**
     * 这个线程会一直监听counter的值，直到counter的值为5
     */
    public static class changeListener extends Thread {
        @Override
        public void run() {
            int localCounter = counter;
            while(localCounter <5){
                if(localCounter != counter){
                    System.out.println("Got Change for counter : " + counter);
                    localCounter = counter;
                }
            }
        }
    }

    /**
     * 这个线程会一直修改counter的值，直到counter的值为5
     */
    public static class changeMaker extends Thread {
        @Override
        public void run() {
            int localCounter = counter;
            while(counter <5){
                System.out.println("Change for counter to : " +  (localCounter + 1));
                counter = ++ localCounter;

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }



        }
    }
}
