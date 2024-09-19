package pojo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "name", "id" })
public class MyBean {
    public int id;
    private String name;

    public MyBean(int i, String myBean) {
        this.id = i;
        this.name = myBean;
    }

    @JsonGetter("name")
    public String getTheName() {
        return name;
    }
}