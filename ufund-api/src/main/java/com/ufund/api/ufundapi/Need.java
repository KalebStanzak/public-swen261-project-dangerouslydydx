package com.ufund.api.ufundapi;

import java.io.Serializable;
import java.util.Objects;

// needs class, has a cost, quantity, and type along with getters and setters for both
// also has constructor (duh)
public class Need implements Serializable {
    private int id;
    private String name; // unique
    private Double cost;
    private Integer quantity;
    private String type;

    public Need() {
    } // empty constructor cux otherewise it won't run in spring

    public Need(int id, String name, Double cost, Integer quantity, String type) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // may not be needed, just here in case of things
    public String toString() {
        return "Need " + id + ":\nName: " + name + "\nCost: $" + cost + "\nQuantity: " + quantity + "\nType: " + type + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Need need = (Need) obj;
        return id == need.id; // Assumes ID uniquely identifies a Need
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Use the ID for the hash
    }

}
