package pojo;

import java.util.Date;

public class Request
{
    private Car car;
    private Date datePurchased;

    // standard getters setters


    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Date getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(Date datePurchased) {
        this.datePurchased = datePurchased;
    }

    @Override
    public String toString() {
        return "Request{" +
                "car=" + car +
                ", datePurchased=" + datePurchased +
                '}';
    }

    public Request(Car car, Date datePurchased) {
        this.car = car;
        this.datePurchased = datePurchased;
    }

    public Request(){

    }

}
