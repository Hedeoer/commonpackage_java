package customization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pojo.MyDto;
import pojo.MyDtoNullKeySerializer;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

/*
* jackson对null的处理,注意java基本数据类型没有赋值是有默认值的
* 1. @JsonInclude(Include.NON_NULL) 可以标注在类上，表示所有null的字段都不序列化；也可标注在字段上表示null的字段不会序列化
* 2. 通过 ObjectMapper 对象全局设置忽略 null 值 ；mapper.setSerializationInclusion(Include.NON_NULL);
* */
public class IgnoreNullProperties {
    @Test
    public void ignoreNullGlobal() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MyDto dtoObject = new MyDto();

        String dtoAsString = mapper.writeValueAsString(dtoObject);
        // {"intValue":0,"booleanValue":false}
        System.out.println(dtoAsString);
    }

    @Test
    public void ignoreNullLocal() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MyDto dtoObject = new MyDto();
        dtoObject.setIntValue(1);
        dtoObject.setBooleanValue(true);

        String dtoAsString = mapper.writeValueAsString(dtoObject);
        // 不加注解：{"stringValue":null,"intValue":1,"booleanValue":true}
        // 加注解：{"intValue":1,"booleanValue":true} null的字段序列化时被忽略
        System.out.println(dtoAsString);

    }

    /**
     * 对map类型中value值为null的忽略处理
     * 使用 object mapper 设置全局忽略null即可； mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
     * @throws JsonProcessingException
     */
    @Test
    public void givenIgnoringNullValuesInMap_whenWritingMapObjectWithNullValue_thenIgnored()
            throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        MyDto dtoObject1 = new MyDto();

        Map<String, MyDto> dtoMap = new HashMap<String, MyDto>();
        dtoMap.put("dtoObject1", dtoObject1);
        dtoMap.put("dtoObject2", null);

        String dtoMapAsString = mapper.writeValueAsString(dtoMap);

        assertThat(dtoMapAsString, containsString("dtoObject1"));
        assertThat(dtoMapAsString, not(containsString("dtoObject2")));
    }



    /*
    * jackson默认是不允许在序列化时map结构的数据出现null的情况的。但是可以通过自定义一个key的序列化器来处理。
    * 比如类MyDtoNullKeySerializer
    * */
    @Test
    public void givenAllowingMapObjectWithNullKey_whenWriting_thenCorrect()
            throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.getSerializerProvider().setNullKeySerializer(new MyDtoNullKeySerializer());

        MyDto dtoObject = new MyDto();
        dtoObject.setStringValue("dtoObjectString");

        Map<String, MyDto> dtoMap = new HashMap<String, MyDto>();
        dtoMap.put(null, dtoObject);

        String value = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dtoMap);
        /*
        * {
          "" : {
            "stringValue" : "dtoObjectString",
            "intValue" : 0,
            "booleanValue" : false
          }
        }
        * */
        System.out.println(value);

        assertThat(value, containsString("\"\""));
        assertThat(value, containsString("dtoObjectString"));
    }

}
