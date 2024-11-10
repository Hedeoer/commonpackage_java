package cn.hedeoer.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Measurement {
    @JsonProperty("city")
    private String city;
    @JsonProperty("temperature")
    private String temperature;

    public Measurement() {
    }
    public Measurement(String city, String temperature) {
        this.city = city;
        this.temperature = temperature;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "city='" + city + '\'' +
                ", temperature='" + temperature + '\'' +
                '}';
    }


}
