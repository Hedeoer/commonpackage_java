package pojo;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class ExtendableBean {
    public String name;
    private Map<String, String> properties;

    @JsonAnyGetter
    public Map<String, String> getProperties() {
        return properties;
    }



    @JsonAnySetter
    public void add(String attr1, String val1) {
        properties.put(attr1, val1);
    }

    public ExtendableBean(String name) {
        properties = new HashMap<String,String>();
        this.name = name;
    }

    public ExtendableBean() {
        properties = new HashMap<String,String>();

    }

}