package cn.hedeoer.stream.api;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// joining() is used to join various elements of a character or string array into a single string object
public class $16JoiningTest {
    public static void main(String[] args) {
        // Create a string list
        List<String> str = Arrays.asList("Geeks", "for", "Geeks");

        // Convert the string list into String
        // using Collectors.joining() method
        String chString = str.stream().collect(Collectors.joining(", ", " {", "} "));

        System.out.println(chString);
    }
}
