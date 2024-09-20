package pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Date;
import java.util.Locale;


public class UserFormat {
    @JsonProperty(value = "first_name")
    private String firstName;
    @JsonProperty(value = "last_name")
    private String lastName;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", locale = "zh_CN",timezone = "GMT+8")
    private Date createdDate = new Date();

    // standard constructor, setters and getters
    public UserFormat() {
    }
    public UserFormat(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
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
    public Date getCreatedDate() {
        return createdDate;
    }

    @Override
    public String toString() {
        return "UserFormat{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}