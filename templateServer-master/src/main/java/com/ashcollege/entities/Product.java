package com.ashcollege.entities;

public class Product {
    private int id;
    private String description;
    private float price;
    private int count;

    public Product(String description, float price, int count) {
        this.description = description;
        this.price = price;
        this.count = count;
    }

    public Product(String description, float price) {
        this.description = description;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
