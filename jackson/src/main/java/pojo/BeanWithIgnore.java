package pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "id" })
public class BeanWithIgnore {
    @JsonIgnore
    public int id;
    public String name;

    public BeanWithIgnore(int i, String myBean) {
        id = i;
        name = myBean;
    }
}