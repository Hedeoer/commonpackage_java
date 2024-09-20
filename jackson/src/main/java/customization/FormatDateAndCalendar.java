package customization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pojo.UserFormat;
import pojo.UserIgnoreCase;

/*
 *
 * 1. jackson对于日期和时间的格式化处理，@JsonFormat注解使用
 * 2. jackson对属性大小写的处理
 * */
public class FormatDateAndCalendar {
    /**
     * jackson对于日期和时间的格式化处理，@JsonFormat注解使用
     *  默认情况下 Date类型为 距离utc 1970年1月1日的毫秒数，需要注意时区的设置
     * @throws JsonProcessingException
     */
    @Test
    public void jsonFormatDefault() throws JsonProcessingException {

        UserFormat userFormat = new UserFormat("John", "Doe");
        ObjectMapper mapper = new ObjectMapper();
        String value = mapper.writeValueAsString(userFormat);
        // 使用JsonFormat注解前{"firstName":"John","lastName":"Doe","createdDate":1726815737470}
        // 使用JsonFormat注解后{"firstName":"John","lastName":"Doe","createdDate":"2024-09-20@15:01:41.028+0800"}
        System.out.println(value);
    }


    /**
     * jackson对属性大小写的处理
     * -- @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
     * 以下例子中属性名 FIRSTNAME, lastname, cReAtEdDaTe都没有统一的命名规范，使用ACCEPT_CASE_INSENSITIVE_PROPERTIES依旧可以反序列化成功
     * 更多特性关注枚举类 com.fasterxml.jackson.annotation.JsonFormat.Feature
     * @throws JsonProcessingException
     */
    @Test
    public void caseInsensitive() throws JsonProcessingException {
        String JSON_STRING = "{\"FIRSTNAME\":\"John\",\"lastname\":\"Smith\",\"cReAtEdDaTe\":\"2016-12-18@07:53:34.740+0000\"}";
        ObjectMapper mapper = new ObjectMapper();
        UserIgnoreCase userIgnoreCase = mapper.readValue(JSON_STRING, UserIgnoreCase.class);
        // UserIgnoreCase{firstName='John', lastName='Smith', createdDate=Sun Dec 18 15:53:34 CST 2016}
        System.out.println(userIgnoreCase);
    }



}
