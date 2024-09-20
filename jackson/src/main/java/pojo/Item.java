package pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class Item {
    public int id;
    public String itemName;
//    @JsonBackReference
    public Employee employee;
    public Item(int id, String itemName, Employee employee) {
        this.id = id;
        this.itemName = itemName;
        this.employee = employee;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public Employee getEmployee() {
        return employee;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", employee=" + employee +
                '}';
    }
}