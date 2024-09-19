package pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyDtoIgnoreUnknown {

    private String stringValue;
    private int intValue;
    private boolean booleanValue;

    // standard constructor, getters and setters

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public MyDtoIgnoreUnknown() {
    }

    public MyDtoIgnoreUnknown(String stringValue, int intValue, boolean booleanValue) {
        this.stringValue = stringValue;
        this.intValue = intValue;
        this.booleanValue = booleanValue;
    }

    @Override
    public String toString() {
        return "MyDto{" +
                "stringValue='" + stringValue + '\'' +
                ", intValue=" + intValue +
                ", booleanValue=" + booleanValue +
                '}';
    }


}
