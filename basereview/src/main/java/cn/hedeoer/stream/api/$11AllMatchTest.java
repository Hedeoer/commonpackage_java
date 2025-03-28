package cn.hedeoer.stream.api;

import java.util.stream.Stream;

/**
 * 是否所有元素都满足条件
 */
public class $11AllMatchTest {
    public static void main(String[] args) {
        // Creating a Stream of Strings
        Stream<String> stream = Stream.of("Geeks", "for",
                "GeeksQuiz", "GeeksforGeeks");

        // Check if all elements of stream
        // have length greater than 2 using
        // Stream allMatch(Predicate predicate)
        boolean answer = stream.allMatch(str -> str.length() > 2);

        // Displaying the result
        System.out.println(answer);
    }
}
