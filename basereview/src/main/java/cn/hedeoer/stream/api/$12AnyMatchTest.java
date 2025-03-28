package cn.hedeoer.stream.api;

import java.util.stream.Stream;

/**
 * 流 anyMatch() 操作用于检查流中是否至少存在一个元素，该元素与给定的谓词匹配。
 */
public class $12AnyMatchTest {
    public static void main(String[] args) {
        // Creating a Stream of Strings
        Stream<String> stream = Stream.of("geeks", "fOr",
                "gEEKSQUIZ", "geeksforGeeks");

        // Check if Character at 1st index is
        // UpperCase in any string or not using
        // Stream anyMatch(Predicate predicate)
        boolean answer = stream.anyMatch(str -> Character.isUpperCase(str.charAt(0)));

        // Displaying the result
        System.out.println(answer);
    }
}
