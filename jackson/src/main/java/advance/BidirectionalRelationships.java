package advance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pojo.Employee;
import pojo.Item;
import pojo.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

/**
 * jackson对于双向关系的处理,如果不处理很容易出现栈溢出，
 * 或者触发jackson的一些内部异常 com.fasterxml.jackson.databind.JsonMappingException: Document nesting depth (1002) exceeds the maximum allowed (10
 * 方法1: Use @JsonManagedReference, @JsonBackReference
 * 方法2：Use @JsonIgnore 推荐
 *
 * 注意以上两种方法的区别
 */
public class BidirectionalRelationships {

    /**
     * 方法1: Use @JsonManagedReference, @JsonBackReference
     * @throws JsonProcessingException
     */
    @Test
    public void givenBidirectionRelation_whenUsingJacksonReferenceAnnotationWithSerialization_thenCorrect() throws JsonProcessingException {
        Employee user = new Employee(1, "John");
        Item item = new Item(2, "book", user);
        user.addItem(item);

        final String itemJson = new ObjectMapper().writeValueAsString(item);
        final String userJson = new ObjectMapper().writeValueAsString(user);
        // {"id":1,"name":"John","employeeItems":[{"id":2,"itemName":"book"}]}
        System.out.println(userJson);
        // {"id":2,"itemName":"book"}
        System.out.println(itemJson);

        assertThat(itemJson, containsString("book"));
        assertThat(itemJson, not(containsString("John")));

        assertThat(userJson, containsString("John"));
        assertThat(userJson, containsString("employeeItems"));
        assertThat(userJson, containsString("book"));
    }

    /**
     * 方法2：Use @JsonIgnore
     * @throws JsonProcessingException
     */
    @Test
    public void givenBidirectionRelation_whenUsingJsonIgnore_thenCorrect()
            throws JsonProcessingException {

        Employee user = new Employee(1, "John");
        Item item = new Item(2, "book", user);
        user.addItem(item);

        final String itemJson = new ObjectMapper().writeValueAsString(item);
        final String userJson = new ObjectMapper().writeValueAsString(user);

        // {"id":1,"name":"John"}
        System.out.println(userJson);
        // {"id":2,"itemName":"book","employee":{"id":1,"name":"John"}}
        System.out.println(itemJson);


    }


}
