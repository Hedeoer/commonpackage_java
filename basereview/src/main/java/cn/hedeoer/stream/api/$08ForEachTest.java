package cn.hedeoer.stream.api;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * for each 作为一个终止流处理的方法
 * 不保证处理的顺序
 */
public class $08ForEachTest {
    public static void main(String[] args) {
        // Creating a list of Integers
        List<Integer> list = Arrays.asList(2, 4, 6, 8, 10);

        // Using forEach(Consumer action) to
        // print the elements of stream
        // in reverse order
        list.stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);
    }
}
