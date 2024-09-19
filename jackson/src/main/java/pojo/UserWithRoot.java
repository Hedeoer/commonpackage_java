package pojo;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "user")
public class UserWithRoot {
    public int id;
    public String name;

    public UserWithRoot(int i, String john) {
        id = i;
        name = john;
    }
}