package pojo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;

public class ProductPartial {

    private String name;
    private String category;
    private JsonNode details;

    // standard getters and setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public JsonNode getDetails() {
        return details;
    }
    public void setDetails(JsonNode details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "ProductPartial{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", dataNode=" + details +
                '}';
    }
}