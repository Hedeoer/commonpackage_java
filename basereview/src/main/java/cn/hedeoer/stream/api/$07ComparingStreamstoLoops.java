package cn.hedeoer.stream.api;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

import java.util.Arrays;
import java.util.TreeSet;

public class $07ComparingStreamstoLoops {
    public static void main(String[] args) {
        //如果列表很小，for 循环的性能会更好；如果列表很大，并行流的性能会更好。
        // 由于并行流的开销相当大，除非你确定值得这么大的开销，否则不建议使用并行流。

        $07ComparingStreamstoLoops.testForStream();
        $07ComparingStreamstoLoops.testForLoop();

    }

    private static void testForLoop() {
        String[] names = { "AI",        "Matlab",
                "Scikit",    "TensorFlow",
                "OpenCV",    "DeepLearning",
                "NLP",       "NeuralNetworks",
                "Regression" , "Segression"};
        System.out.println(
                "Stream from Array, sort, filter and print : ");

        TreeSet<String> orderSet = new TreeSet<>();
        for (int index = 0; index < names.length; index++) {
            if(names[index].startsWith("S")){
                orderSet.add(names[index]);
            }
        }
        System.out.println(orderSet);
    }

    private static void testForStream() {
        String[] names = {"AI", "Matlab",
                "Scikit", "TensorFlow",
                "OpenCV", "DeepLearning",
                "NLP", "NeuralNetworks",
                "Regression", "Segression"};
        System.out.println(
                "Stream from Array, sort, filter and print : ");
        Arrays
                .stream(names) // same as Stream.of(names)
                .filter(x -> x.startsWith("S"))
                .sorted()
                .forEach(System.out::println);

        Arrays.binarySearch(names, "Scikit");
    }

}
