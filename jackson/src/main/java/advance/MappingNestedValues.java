package advance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pojo.ProductPartial;

/*
* 使用jackson处理嵌套的json结构数据
* 推荐使用 com.fasterxml.jackson.databind.JsonNode 类映射
* */
public class MappingNestedValues {
    public static void main(String[] args) throws JsonProcessingException {

        String str = "{\n" +
                "    \"id\": \"957c43f2-fa2e-42f9-bf75-6e3d5bb6960a\",\n" +
                "    \"name\": \"The Best Product\",\n" +
                "    \"brand\": {\n" +
                "        \"id\": \"9bcd817d-0141-42e6-8f04-e5aaab0980b6\",\n" +
                "        \"name\": \"ACME Products\",\n" +
                "        \"owner\": {\n" +
                "            \"id\": \"b21a80b1-0c09-4be3-9ebd-ea3653511c13\",\n" +
                "            \"name\": \"Ultimate Corp, Inc.\"\n" +
                "        }\n" +
                "    }  \n" +
                "}\n";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(str);
        System.out.println(jsonNode.get("brand").get("owner").get("name"));
    }
}
