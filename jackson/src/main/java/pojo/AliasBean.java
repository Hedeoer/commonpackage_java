package pojo;

import com.fasterxml.jackson.annotation.JsonAlias;

public class AliasBean {
    @JsonAlias({ "fName", "f_name" })
    private String firstName;   
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public AliasBean() {
    }

    public AliasBean(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}