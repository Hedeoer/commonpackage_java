package pojo;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

@JsonIncludeProperties({ "name" })
public class BeanWithInclude {
    public int id;
    public String name;

    public BeanWithInclude(int i, String myBean) {
        id = i;
        name = myBean;
    }
}