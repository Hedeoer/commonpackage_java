package cn.hedeoer.stream.api;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class $06CollectStreamToImmutableCollection {
    // Java Program to Collect Stream to Immutable Collection
    // 此处演示 java10之前的做法，本质为遍历stream的结果，然后添加到不可变的集合中
    public static void main(String[] args) {
        // Custom inputs integer elements in List

        List<Integer> unmodifiableList = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        Collections::unmodifiableList));

        System.out.println(unmodifiableList);

        // Operations like this will result in an exception
        unmodifiableList.add(12);
    }
}
