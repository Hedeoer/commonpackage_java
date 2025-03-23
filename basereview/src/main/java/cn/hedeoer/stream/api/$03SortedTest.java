package cn.hedeoer.stream.api;

import java.util.ArrayList;
import java.util.List;

public class $03SortedTest {
    public static void main(String[] args) {

        List<Point> aList = new ArrayList<>();
        aList.add(new Point(10, 20));
        aList.add(new Point(5, 10));
        aList.add(new Point(1, 100));
        aList.add(new Point(50, 2000));

        aList.stream()
                .sorted((o1, o2) -> o1.x.compareTo(o2.x) )
                .forEach(System.out::println);
    }

    static class Point
    {
        Integer x, y;
        Point(Integer x, Integer y) {
            this.x = x;
            this.y = y;
        }

        public String toString() {
            return this.x + ", "+ this.y;
        }
    }
}
