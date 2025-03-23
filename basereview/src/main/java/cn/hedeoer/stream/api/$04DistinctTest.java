package cn.hedeoer.stream.api;

import java.util.Arrays;
import java.util.List;

public class $04DistinctTest {
    public static void main(String[] args) {
        // Creating a list of strings
        List<String> list = Arrays.asList("Geeks", "for", "Geeks",
                "GeeksQuiz", "for", "GeeksforGeeks");

        // Storing the count of distinct elements
        // in the list using Stream.distinct() method
        long Count = list.stream().distinct().count();

        // Displaying the count of distinct elements
        System.out.println("The count of distinct elements is : " + Count);
    }
}
