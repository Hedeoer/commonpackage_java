package javabean_and_jsonobject;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pojo.Car;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JavaObjectAndJSON {
    @Test
    public void beanToJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Car car = new Car("red", "BMW");
        // 序列化
        // TODO 1.写出到文件
//        mapper.writeValue(new File("target/car.json"), car);
        // todo 2. 写出为字符串
        System.out.println(mapper.writeValueAsString(car));


    }
    @Test
    public void jsonToBean() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        // todo 反序列化
        Car car = mapper.readValue(json, Car.class);
        System.out.println(car);
        // todo 从文件读取
        Car car1 = mapper.readValue(new File("target/car.json"), Car.class);
        System.out.println(car1);

    }
    @Test
    public void jsonToJsonNode() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{ \"color\" : \"Black\", \"type\" : \"FIAT\" }";
        JsonNode jsonNode = objectMapper.readTree(json);
        String color = jsonNode.get("color").asText();
        System.out.println(jsonNode);
        // Output: color -> Black

    }

    @Test
    public void jsonToJsonArray() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonCarArray =
                "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"FIAT\" }]";
        List<Car> listCar = objectMapper.readValue(jsonCarArray, new TypeReference<List<Car>>(){});
        // [Car{color='Black', type='BMW'}, Car{color='Red', type='FIAT'}]
        System.out.println(listCar);

        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        Map<String, Object> map = objectMapper.readValue(json, new TypeReference<Map<String,Object>>(){});
        // {color=Black, type=BMW}
        System.out.println(map);



    }
}
