package jackson_annotations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pojo.BeanWithIgnore;
import pojo.BeanWithInclude;
import pojo.MyBean;
import pojo.User;

import java.io.IOException;

public class JacksonPropertyInclusionAnnotations {
    /**
     * @JsonIgnoreProperties 序列化时指定某些字段不用序列化,标记在类上
     * @throws JsonProcessingException
     */
    @Test
    public void whenSerializingUsingJsonIgnoreProperties_thenCorrect()
            throws JsonProcessingException {

        BeanWithIgnore bean = new BeanWithIgnore(1, "My bean");

        String result = new ObjectMapper()
                .writeValueAsString(bean);

        // {"name":"My bean"}
        System.out.println(result);
    }

    /**
     * @JsonIgnore 序列化时指定某些字段不用序列化,标记在字段上
     * @throws JsonProcessingException
     */
    @Test
    public void whenSerializingUsingJsonIgnore_thenCorrect()
            throws JsonProcessingException {

        BeanWithIgnore bean = new BeanWithIgnore(1, "My bean");

        String result = new ObjectMapper()
                .writeValueAsString(bean);
        // {"name":"My bean"}
        System.out.println(result);
    }

    /**
     * @JsonIgnoreType 作用对象：@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
     * @throws JsonProcessingException
     */
    @Test
    public void whenSerializingUsingJsonIgnoreType_thenCorrect()
            throws JsonProcessingException {

        User.Name name = new User.Name("John", "Doe");
        User user = new User(1, name);

        String result = new ObjectMapper()
                .writeValueAsString(user);

        // {"id":1}
        System.out.println(result);
    }

    /**
     * @JsonIncludeProperties  It was introduced in Jackson 2.12 and can be used to mark a property or a list of properties that Jackson will include during serialization and deserialization.
     * @throws JsonProcessingException
     */
    @Test
    public void whenSerializingUsingJsonIncludeProperties_thenCorrect() throws JsonProcessingException {
        final BeanWithInclude bean = new BeanWithInclude(1, "My bean");
        final String result = new ObjectMapper().writeValueAsString(bean);
        // {"name":"My bean"}
        System.out.println(result);
    }

    /**
     * 使得jackson的所有注解失效， disable MapperFeature.USE_ANNOTATIONS 即可
     * @throws IOException
     */
    @Test
    public void whenDisablingAllAnnotations_thenAllDisabled()
            throws IOException {
        MyBean bean = new MyBean(1, null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);
        String result = mapper.writeValueAsString(bean);
        // {"id":1,"theName":null}
        System.out.println(result);
    }


}
