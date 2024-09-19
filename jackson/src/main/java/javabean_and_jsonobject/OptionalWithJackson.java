package javabean_and_jsonobject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pojo.Book;

import java.util.Optional;

public class OptionalWithJackson {
    @Test
    public void dealOptionalProblemInJackson() throws JsonProcessingException {
        /*
        * 处理java8中引入的Optional处理null带来的问题,
        * 解决方式：需要额外引入依赖：jackson-datatype-jdk8
        *
        * */

        Book book = new Book();
        book.setTitle("Oliver Twist");
        book.setSubTitle(Optional.of("The Parish Boy's Progress"));

        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new Jdk8Module());
        System.out.println(mapper.writeValueAsString(book));

    }

    public static void main(String[] args) {
        Book book = new Book();
        book.setTitle("Oliver Twist");
        book.setSubTitle(Optional.of("The Parish Boy's Progress"));

        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new Jdk8Module());

    }
}
