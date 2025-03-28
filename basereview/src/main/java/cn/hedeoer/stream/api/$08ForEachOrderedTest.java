package cn.hedeoer.stream.api;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * for each ordered 作为一个终止流处理的方法
 * 保证处理的顺序
 */
public class $08ForEachOrderedTest {
    public static void main(String[] args) {
        // Creating a list of Integers
        List<Integer> list = Arrays.asList(2, 4, 6, 8, 10);

        // Using forEach(Consumer action) to
        // print the elements of stream
        // in reverse order
        list.stream().forEachOrdered(System.out::println);
    }
}
