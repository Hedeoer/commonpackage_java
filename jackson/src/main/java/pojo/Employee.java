package pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    public int id;
    public String name;
//    @JsonManagedReference
    @JsonIgnore
    public List<Item> employeeItems;

    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
        this.employeeItems = new ArrayList<Item>();
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Item> getEmployeeItems() {
        return employeeItems;
    }
    public void setEmployeeItems(List<Item> employeeItems) {
        this.employeeItems = employeeItems;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", employeeItems=" + employeeItems +
                '}';
    }

    public void addItem(Item item) {
        employeeItems.add(item);
    }
}