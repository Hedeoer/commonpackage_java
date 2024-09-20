package basic.javabean_and_jsonobject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.junit.jupiter.api.Test;
import pojo.MyDto;
import pojo.MyDtoIgnoreUnknown;

import java.io.IOException;

import static com.sun.javafx.fxml.expression.Expression.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UnmarshallingUnknownProperties {

  @Test
    public void givenJsonHasUnknownValues_whenDeserializingAJsonToAClass_thenExceptionIsThrown()
            throws JsonParseException, JsonMappingException, IOException {
        String jsonAsString =
                "{\"stringValue\":\"a\"," +
                        "\"intValue\":1," +
                        "\"booleanValue\":true," +
                        "\"stringValue2\":\"something\"}";
        ObjectMapper mapper = new ObjectMapper();

        MyDto readValue = mapper.readValue(jsonAsString, MyDto.class);

        assertNotNull(readValue);
    }

    /**
     * 处理方式1：
     * 处理忽略未知属性，在转化时通过反序列化：DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES 配置
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @Test
    public void givenJsonHasUnknownValuesButJacksonIsIgnoringUnknownFields_whenDeserializing_thenCorrect()
            throws JsonParseException, JsonMappingException, IOException {

        String jsonAsString =
                "{\"stringValue\":\"a\"," +
                        "\"intValue\":1," +
                        "\"booleanValue\":true," +
                        "\"stringValue2\":\"something\"}";
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        MyDto readValue = mapper.readValue(jsonAsString, MyDto.class);

        assertNotNull(readValue);

    }

    /**
     * 处理方式2：在映射类上处理
     * 通过在映射的类上添加@JsonIgnoreProperties(ignoreUnknown = true)注解来允许忽略json串中的未能够映射上的属性
     *
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @Test
    public void givenJsonHasUnknownValuesButUnknownFieldsAreIgnoredOnClass_whenDeserializing_thenCorrect()
            throws JsonParseException, JsonMappingException, IOException {

        String jsonAsString =
                "{\"stringValue\":\"a\"," +
                        "\"intValue\":1," +
                        "\"booleanValue\":true," +
                        "\"stringValue2\":\"something\"}";
        ObjectMapper mapper = new ObjectMapper();

        MyDtoIgnoreUnknown readValue = mapper
                .readValue(jsonAsString, MyDtoIgnoreUnknown.class);

        assertNotNull(readValue);
        // MyDto{stringValue='a', intValue=1, booleanValue=true}
        System.out.println(readValue);
    }

    /**
     * 对应json串中属性值少于java bean属性的，默认情况下，未能映射的会取javabean字段的默认值
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @Test
    public void givenNotAllFieldsHaveValuesInJson_whenDeserializingAJsonToAClass_thenCorrect()
            throws JsonParseException, JsonMappingException, IOException {
        // 缺少字段：intValue
        String jsonAsString = "{\"stringValue\":\"a\"," +
                "\"booleanValue\":true" +"}";
        ObjectMapper mapper = new ObjectMapper();

        MyDto readValue = mapper.readValue(jsonAsString, MyDto.class);

        assertNotNull(readValue);
        // MyDto{stringValue='a', intValue=0, booleanValue=true}
        System.out.println(readValue);

    }
}
