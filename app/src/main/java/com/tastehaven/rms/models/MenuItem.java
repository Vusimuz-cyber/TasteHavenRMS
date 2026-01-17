package com.tastehaven.rms.models;

public class MenuItem {
    private String name;
    private String description;
    private double price;
    private boolean availability;

    public MenuItem() {}

    public MenuItem(String name, String description, double price, boolean availability) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.availability = availability;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public boolean isAvailability() { return availability; }
    public void setAvailability(boolean availability) { this.availability = availability; }
}
