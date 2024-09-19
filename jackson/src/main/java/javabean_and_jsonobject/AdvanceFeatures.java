package javabean_and_jsonobject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pojo.Car;
import pojo.Request;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdvanceFeatures {
    @Test
    public void testSomeFeatures() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString
                = "{ \"color\" : \"Black\", \"type\" : \"Fiat\", \"year\" : \"1970\" }";

        /*
         * 当json串中java对象中没有的属性名,
         * 可以设置反序列化时候忽略属性名缺失错误,DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
         * 否则会UnrecognizedPropertyException
         * */
        // com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException: Unrecognized field "year" (class pojo.Car), not marked as ignorable (2 known properties: "type", "color"])
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Car car = mapper.readValue(jsonString, Car.class);

        JsonNode jsonNodeRoot = mapper.readTree(jsonString);
        JsonNode jsonNodeYear = jsonNodeRoot.get("year");
        String year = jsonNodeYear.asText();
        assert year.equals("1970");

        // 诸如缺失属性名的错误,都可以通过相关配置抑制，比如FAIL_ON_NULL_FOR_PRIMITIVES，FAIL_ON_NUMBERS_FOR_ENUMS等
    }

    @Test
    public void handingDateFormat() throws JsonProcessingException {

        /*
         *
         * 处理日期类型数据
         * */

        Car car = new Car("yellow", "renault");
        Request request = new Request(car, new Date());
        ObjectMapper objectMapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a z", Locale.CHINA);
        objectMapper.setDateFormat(df);
        String carAsString = objectMapper.writeValueAsString(request);
        System.out.println(carAsString);
        // output: {"car":{"color":"yellow","type":"renault"},"datePurchased":"2016-07-03 11:43 AM CEST"}

        /*
         * 处理集合类型
         * */

        String jsonCarArray =
                "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"FIAT\" }]";
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
        List<Car> cars = objectMapper.readValue(jsonCarArray, new TypeReference<List<Car>>() {});

        // [Car{color='Black', type='BMW'}, Car{color='Red', type='FIAT'}]
        System.out.println(cars);
    }
}
