package cn.hedeoer;

import java.util.function.Function;

public class ClosureExample {
    public static Function<Integer, Integer> createMultiplier(int factor) {
        return x -> x * factor;
    }

    public static void main(String[] args) {
        Function<Integer, Integer> doubleNum = createMultiplier(2);
        Function<Integer, Integer> triple = createMultiplier(3);

        System.out.println(doubleNum.apply(5));  // 输出: 10
        System.out.println(triple.apply(5));  // 输出: 15

        long  logs []  = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    }
}