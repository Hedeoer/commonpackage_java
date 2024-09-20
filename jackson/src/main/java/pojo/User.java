package pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

public class User {
    public int id;
    public Name name;

    public User(int i, Name name) {
        this.id = i;
        this.name = name;
    }

    public String getFirstName() {
        return null;
    }

    @JsonIgnoreType
    public static class Name {
        public String firstName;
        public String lastName;

        public Name(String john, String doe) {
            this.firstName = john;
            this.lastName = doe;
        }
    }
}