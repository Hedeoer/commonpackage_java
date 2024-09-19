package jackson_annotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pojo.AliasBean;
import pojo.BeanWithCreator;
import pojo.ExtendableBean;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JackDeSerializationAnnotation {

    /**
     * @JsonCreator 当json串反序列化时属性值和对应的javabean属性不一致时使用，范围为构造方法
     * @throws IOException
     */
    @Test
    public void whenDeserializingUsingJsonCreator_thenCorrect()
            throws IOException {

        String json = "{\"id\":1,\"theName\":\"My bean\"}";

        BeanWithCreator bean = new ObjectMapper()
                .readerFor(BeanWithCreator.class)
                .readValue(json);
        assertEquals("My bean", bean.name);
    }

    /**
     * @JsonAlias 当json串反序列化时属性值和对应的javabean属性不一致时使用,范围为单个字段
     * @throws IOException
     */
    @Test
    public void whenDeserializingUsingJsonAlias_thenCorrect() throws IOException {
        String json = "{\"fName\": \"John\", \"lastName\": \"Green\"}";
        AliasBean aliasBean = new ObjectMapper().readerFor(AliasBean.class).readValue(json);
        assertEquals("John", aliasBean.getFirstName());
    }

    /**
     * @JsonAnySetter 当json串反序列化时，映射的javabean字段为map类型时使用
     * @throws IOException
     */
    @Test
    public void whenDeserializingUsingJsonAnySetter_thenCorrect()
            throws IOException {
        String json
                = "{\"name\":\"My bean\",\"attr2\":\"val2\",\"attr1\":\"val1\"}";

        ExtendableBean bean = new ObjectMapper()
                .readerFor(ExtendableBean.class)
                .readValue(json);

        assertEquals("My bean", bean.name);
        assertEquals("val2", bean.getProperties().get("attr2"));
    }



}
