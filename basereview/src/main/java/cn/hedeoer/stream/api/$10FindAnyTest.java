package cn.hedeoer.stream.api;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 流 findAny() 操作的行为是明确的非确定性，即可以自由选择流中的任何元素。
 * 对同一数据源的多次调用可能不会返回相同的结果
 */
public class $10FindAnyTest {
    public static void main(String[] args) {
        // Creating a List of Integers
        List<Integer> list = Arrays.asList(2, 4, 6, 8, 10);

        // Using Stream findAny() to return
        // an Optional describing some element
        // of the stream
        Optional<Integer> answer = list.stream().findAny();

        // if the stream is empty, an empty
        // Optional is returned.
        if (answer.isPresent()) {
            System.out.println(answer.get());
        }
        else {
            System.out.println("no value");
        }
    }
}
