package cn.hedeoer.stream.api;

import com.sun.org.apache.xpath.internal.operations.Variable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class $01Filter {
    // 1. 通过对象属性进行筛选
    // 2. 通过索引进行筛选
    public static void main(String[] args) {
        $01Filter.filterByIndexUsingAtomic();
    }

    // 2. 通过索引进行筛选
    private static void filterByIndexUsingStream() {
        // create an array of Strings
        String[] myArray
                = new String[] { "stream",   "is",  "a",
                "sequence", "of",  "elements",
                "like",     "list" };

        IntStream.rangeClosed(0, myArray.length - 1)
                .filter(index -> index % 2 == 0)
                .mapToObj(index -> myArray[index])
                .forEach(System.out::println);
    }


    // 2. 通过索引进行索引为偶数的元素
    private static void filterByIndexUsingAtomic(){
        // create a string array
        String[] myArray
                = new String[] { "stream",   "is",  "a",
                "sequence", "of",  "elements",
                "like",     "list" };

        /*
        lambda中参数要求为final，此处使用AtomicInteger
        “Variable used in lambda expression should be final or effectively final”
        final: 一旦被赋值就不能改变
        effectively final: 虽然没有被final关键字修饰，但在初始化后从未被修改过的变量

        要求：final或者 effectively final 的原因
            Java的lambda表达式在JVM中是通过匿名内部类来实现的。
            在Java中，内部类引用外部局部变量时，这些变量必须是final的。这是因为局部变量存在于栈上，当方法返回后可能就不存在了，而内部类的对象可能会继续存在。
            为了解决这个问题，编译器会复制一份外部局部变量的值，而不是直接引用原变量。这样即使原变量已经不存在，内部类也能访问它的值。
            为了确保这份复制的值与原变量一致，原变量必须是不可变的。*/

        AtomicInteger at = new AtomicInteger(0);
        int acc = 0;
        Stream.of(myArray)
                .filter(x -> at.getAndIncrement() % 2 == 0)
//                .filter(x -> {++acc; return acc % 2 ==0})
                .forEach(System.out::println);
    }
}
