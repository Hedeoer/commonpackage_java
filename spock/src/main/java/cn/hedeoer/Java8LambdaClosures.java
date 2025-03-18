package cn.hedeoer;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.ArrayList;
import java.util.List;

public class Java8LambdaClosures {

    public static void main(String[] args) {
        // 示例1：创建一个简单的计数器
        System.out.println("示例1: 简单计数器");
        Supplier<Integer> counter = createCounter();
        System.out.println(counter.get()); // 输出: 1
        System.out.println(counter.get()); // 输出: 2
        System.out.println(counter.get()); // 输出: 3

        // 示例2：创建一个乘法器
        System.out.println("\n示例2: 乘法器");
        int factor = 2;
        Function<Integer, Integer> doubler = createMultiplier(factor);
        System.out.println(doubler.apply(5)); // 输出: 10
        System.out.println(factor + "==========");

        // 示例3：延迟执行
        System.out.println("\n示例3: 延迟执行");
        List<Runnable> tasks = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            tasks.add(createPrinter(i));
        }
        tasks.forEach(Runnable::run);

        // 示例4：数据隐藏和封装
        System.out.println("\n示例4: 数据隐藏和封装");
        BankAccount account = new BankAccount(1000);
        account.deposit(500);
        account.withdraw(200);
        System.out.println("Current balance: " + account.getBalance());
    }

    // 示例1：创建一个计数器
    private static Supplier<Integer> createCounter() {
        final int[] count = {0}; // 使用数组来模拟可变变量
        return () -> ++count[0];
    }

    // 示例2：创建一个乘法器
    private static Function<Integer, Integer> createMultiplier(int factor) {
        return x -> x * factor;
    }

    // 示例3：创建一个延迟执行的打印函数
    private static Runnable createPrinter(int num) {
        return () -> System.out.println("Number: " + num);
    }

    // 示例4：使用闭包实现数据隐藏和封装
    static class BankAccount {
        private int balance;

        public BankAccount(int initialBalance) {
            this.balance = initialBalance;
        }

        public void deposit(int amount) {
            balance += amount;
            System.out.println("Deposited: " + amount);
        }

        public void withdraw(int amount) {
            if (balance >= amount) {
                balance -= amount;
                System.out.println("Withdrawn: " + amount);
            } else {
                System.out.println("Insufficient funds");
            }
        }

        public int getBalance() {
            return balance;
        }
    }
}