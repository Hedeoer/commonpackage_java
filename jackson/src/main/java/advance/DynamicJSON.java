package advance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pojo.Product;
import pojo.ProductPartial;

import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;

/*
* jackson对于动态json串的处理
* 方法1：如果动态json全部属性都是动态的。使用注解@JsonAnySetter；具体细节查看类pojo.Product的处理
* 方法2：如果动态json部分属性是固定的，部分属性是动态的。使用com.fasterxml.jackson.databind.JsonNode类型映射
*
* */
public class DynamicJSON {

    /**
     * 完全动态json
     * @throws JsonProcessingException
     */
    @Test
    public void dynamicPropertiesFully() throws JsonProcessingException {
        String json = "{\n" +
                "    \"name\": \"Pear yPhone 72\",\n" +
                "    \"category\": \"cellphone\",\n" +
                "    \"displayAspectRatio\": \"97:3\",\n" +
                "    \"audioConnector\": \"none\"\n" +
                "}";


        ObjectMapper mapper = new ObjectMapper();
        Product product = mapper.readValue(json, Product.class);

        Map<String, Object> details = product.getDetails();
        Set<Map.Entry<String, Object>> entries = details.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    /**
     * 部分动态json
     * @throws JsonProcessingException
     */
    @Test
    public void dynamicPropertiesPartially() throws JsonProcessingException {

        String json1 = "{\n" +
                "    \"name\": \"Pear yPhone 72\",\n" +
                "    \"category\": \"cellphone\",\n" +
                "    \"details\": {\n" +
                "        \"displayAspectRatio\": \"97:3\",\n" +
                "        \"audioConnector\": \"none\"\n" +
                "    }\n" +
                "}";

        String json2 = "{\n" +
                "    \"name\": \"Pear yPhone 72\",\n" +
                "    \"category\": \"cellphone\",\n" +
                "    \"details\": {\n" +
                "        \"displayAspectRatio\": \"97:3\",\n" +
                "        \"audioConnector\": \"none\",\n" +
                "        \"country_site\": \"killls\"\n" +
                "    }\n" +
                "}";
        ObjectMapper objectMapper = new ObjectMapper();
        ProductPartial product1 = objectMapper.readValue(json1, ProductPartial.class);
        ProductPartial product2 = objectMapper.readValue(json2, ProductPartial.class);
        // ProductPartial{name='Pear yPhone 72', category='cellphone', dataNode={"displayAspectRatio":"97:3","audioConnector":"none"}}
        System.out.println(product1);
        // ProductPartial{name='Pear yPhone 72', category='cellphone', dataNode={"displayAspectRatio":"97:3","audioConnector":"none","country_site":"killls"}}
        System.out.println(product2);
    }
}
