package cn.hedeoer.stream.api;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

// ofNullable() 优雅地处理可能为 null 的元素
// jdk 9引入
public class $15OfNullableTest {
    public static void main(String[] args) {

        List<String> names = Arrays.asList("Alice", null, "Bob", null, "Charlie");

        List<String> nonNullNames = names.stream()
//                .flatMap(Stream::ofNullable)
                .filter(e -> e != null)
                .collect(Collectors.toList());
// 结果: ["Alice", "Bob", "Charlie"]

    }
}
