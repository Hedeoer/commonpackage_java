package cn.hedeoer.stream.api;


import java.util.stream.Stream;

/**
 * 流 concat() 操作用于合并两个流。
 * 如果两个输入流都是有序流，则生成的流就是有序流
 */
public class $13ConcatTest {
    public static void main(String[] args) {
        // Creating two Streams
        Stream<String> stream1 = Stream.of("Geeks", "for");
        Stream<String> stream2 = Stream.of("GeeksQuiz", "GeeksforGeeks");

        Stream.concat(stream1, stream2).forEach(System.out::println);

    }
}
