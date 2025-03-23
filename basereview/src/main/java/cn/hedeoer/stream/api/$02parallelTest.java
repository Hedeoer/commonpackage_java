package cn.hedeoer.stream.api;

import java.util.Arrays;
import java.util.List;

public class $02parallelTest {
    public static void main(String[] args) {
        // 并行流的使用主要当元素的处理顺序和最终结果无关时，就可以使用并行流


        // create a list
        List<String> list = Arrays.asList( "Hello ",
                "G", "E", "E", "K", "S!");

        // we are using stream() method
        // for sequential stream
        // Iterate and print each element
        // of the stream
        list.stream().forEach(System.out::print);

        list.parallelStream().forEach(System.out::print);


    }
}
