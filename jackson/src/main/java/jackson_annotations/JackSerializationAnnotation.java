package jackson_annotations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import pojo.ExtendableBean;
import pojo.MyBean;
import pojo.RawBean;
import pojo.UserWithRoot;

/**
 * jackson常见的序列化注解
 */
public class JackSerializationAnnotation {

    @Test
    public void whenSerializingUsingJsonAnyGetter_thenCorrect()
            throws JsonProcessingException {

        // todo 1. @JsonAnyGetter 对map类型的属性进行序列化
        ExtendableBean bean = new ExtendableBean("My bean");
        bean.add("attr1", "val1");
        bean.add("attr2", "val2");

        String result = new ObjectMapper().writeValueAsString(bean);
        // {"name":"My bean","attr2":"val2","attr1":"val1"}
        System.out.println(result);


    }

    /**
     * todo 2. @JsonGetter 指定一个方法作为属性的getter方法
     * @throws JsonProcessingException
     */
    @Test
    public void whenSerializingUsingJsonGetter_thenCorrect()
            throws JsonProcessingException {

        MyBean bean = new MyBean(1, "My bean");

        String result = new ObjectMapper().writeValueAsString(bean);
        // {"id":1,"name":"My bean"}
        System.out.println(result);
    }


    /**
     * @JsonPropertyOrder 指定序列化时字段的顺序
     * @throws JsonProcessingException
     */
    @Test
    public void whenSerializingUsingJsonPropertyOrder_thenCorrect()
            throws JsonProcessingException {

        MyBean bean = new MyBean(1, "My bean");

        String result = new ObjectMapper().writeValueAsString(bean);
        // {"name":"My bean","id":1}
        System.out.println(result);
    }

    /**
     * @JsonRawValue 指定一个字段序列化时为json 字符串
     * @throws JsonProcessingException
     */
    @Test
    public void whenSerializingUsingJsonRawValue_thenCorrect()
            throws JsonProcessingException {

        RawBean bean = new RawBean("My bean", "{\"attr\":false}");

        String result = new ObjectMapper().writeValueAsString(bean);
        // {"name":"My bean","json":{"attr":false}}
        System.out.println(result);
    }

    /**
     * @JsonRootName 指定根节点名称,将序列化的数据再次以指定的名称包裹起来，形成新的json对象
     * @throws JsonProcessingException
     */
    @Test
    public void whenSerializingUsingJsonRootName_thenCorrect()
            throws JsonProcessingException {

        UserWithRoot user = new UserWithRoot(1, "John");

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        String result = mapper.writeValueAsString(user);
        // {"user":{"id":1,"name":"John"}}
        System.out.println(result );
    }

}
