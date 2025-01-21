package com.ufund.api.ufundapi;

import java.io.Serializable;
import java.util.*;

import org.springframework.stereotype.Component;

// basket class with constructors, getters, and setters
@Component
public class Basket implements Serializable {
    private int id; // unique to the basket... cuz apparantly we should have this
    private String name;
    private double funds; // number of funds available in basket
    private Set<Need> needs;

    public Basket() {
        this.needs = new HashSet<>();
    }

    public Basket(int id, String name, double funds) {
        this.id = id;
        this.name = name;
        this.funds = funds;
        this.needs = new HashSet<>();
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

    public double getFunds() {
        return funds;
    }

    public void setFunds(double amount) {
        this.funds = amount;
    }

    public Set<Need> getNeeds() {
        return needs;
    }

    public void setNeeds(Set<Need> needs) {
        this.needs = needs;
    }

    // add a need to the basket
    public void addNeed(Need need) {
        this.needs.add(need);
    }

    // remove a need from the basket
    public void removeNeed(int id) {
        for (Need n : needs){
            if (n.getId() == id){
                this.needs.remove(n);
            }
        }
        System.out.println("could not find need");
    }

    // calculate total cost of all needs in basket
    public double calculateTotalCost() {
        return needs.stream().mapToDouble(Need::getCost).sum();
    }

    // clear basket
    public void clearNeeds() {
        needs.clear();
    }
}
