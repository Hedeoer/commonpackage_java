package pojo;

import com.fasterxml.jackson.annotation.JsonRawValue;

public class RawBean {
    public String name;

    @JsonRawValue
    public String json;

    public RawBean(String myBean, String s) {
        name = myBean;
        json = s;
    }
}