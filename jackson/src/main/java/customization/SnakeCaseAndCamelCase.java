package customization;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import pojo.User;
import pojo.UserFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
* jackson中 snakecase 和 camelcase的转化
* 1. Use @JsonProperty Annotation
* 2. Configure the ObjectMapper
* */
public class SnakeCaseAndCamelCase {
    public static void main(String[] args) throws JsonProcessingException {


        String jsonString = "{\"first_name\":\"Jackie\",\"last_name\":\"Chan\"}";
//        1. Use @JsonProperty Annotation
        ObjectMapper objectMapper = new ObjectMapper();


        // 2. Configure the ObjectMapper
//        ObjectMapper objectMapper = new ObjectMapper()
//                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        UserFormat user = objectMapper.readValue(jsonString, UserFormat.class);
        System.out.println(user);
    }
}
