package customization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JsonStringAndJsonNode {
    public static void main(String[] args) throws JsonProcessingException {
        String jsonString = "{\"k1\":\"v1\",\"k2\":\"v2\"}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);

        System.out.println(actualObj.get("k1"));
        assertNotNull(actualObj);

    }
}
