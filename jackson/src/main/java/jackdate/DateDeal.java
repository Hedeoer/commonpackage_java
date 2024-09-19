package jackdate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pojo.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateDeal {
    /**
     * 使用@JsonFormat注解处理日期格式
     * 方式1： ObjectMapper全局设置日期格式
     *
     * @throws ParseException
     * @throws JsonProcessingException
     */
    @Test
    public void withJsonFormatAnnotation_thenDealDate() throws ParseException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        /* todo 1. 方式1*/
        String toParse = "2014-01-12 02:30:009";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date parse = format.parse(toParse);
        Event party = new Event("party", parse);
        // ObjectMapper全局设置日期格式,需要注意jackson默认使用的是 number of milliseconds since January 1st, 1970, UTC
        // 需要注意时区的设置，否则时间会偏移
        mapper.setDateFormat(format);
        // 未设置mapper时间格式：{"name":"party","eventDate":1389493809000}
        // 设置mapper时间格式后：{"name":"party","eventDate":"2014-01-12 02:30:09"}
        System.out.println(mapper.writeValueAsString(party));



    }

    /**
     * todo 方式2 方式2：使用注解配置映射的实体类 @JsonFormat
     * @throws ParseException
     * @throws JsonProcessingException
     */
    @Test
    public void withJsonFormatAnnotation_thenDealDate2() throws ParseException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        /* todo 1. 方式2*/
        String toParse = "2014-01-12 02:30:009";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date parse = format.parse(toParse);
        Event party = new Event("party", parse);
        // {"name":"party","eventDate":"2014-01-12 02:30:09"}
        System.out.println(mapper.writeValueAsString(party));


    }

    /*todo 3. 自定义时间序列化*/
}
