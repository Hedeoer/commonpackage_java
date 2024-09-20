package pojo;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.LinkedHashMap;
import java.util.Map;

public class Product {

    // common fields

    Map<String, Object> details = new LinkedHashMap<>();

    @JsonAnySetter
    void setDetail(String key, Object value) {
        details.put(key, value);
    }

    // standard getters and setters
    public Map<String, Object> getDetails() {
        return details;
    }
    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }
    @Override
    public String toString() {
        return "Product{" +
                "details=" + details +
                '}';
    }

}