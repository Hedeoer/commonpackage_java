package cn.hedeoer.stream.api;

import java.util.Arrays;
import java.util.Optional;

// reduce()
public class $14ReduceTest {
    public static void main(String[] args) {
        // String array to combine
        String[] array = { "Geeks", "for", "Geeks" };

        // Using reduce to concatenate strings with a hyphen
        Optional<String> combinedString = Arrays.stream(array)
                .reduce((str1, str2) -> str1 + "-" + str2);

        // Displaying the combined String
        combinedString.ifPresent(System.out::println);
    }
}
