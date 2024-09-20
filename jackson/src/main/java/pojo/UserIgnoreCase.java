package pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class UserIgnoreCase {
    private String firstName;
    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Date createdDate;

    // the rest is the same as the User class
    public UserIgnoreCase() {
    }
    public UserIgnoreCase(String firstName, String lastName) {
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
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "UserIgnoreCase{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
