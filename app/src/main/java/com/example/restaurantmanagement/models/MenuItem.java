package com.example.restaurantmanagement.models;


public class MenuItem {
    public long id;
    public String name;
    public String category; // Starters, Main Course, Sides, Desserts, Cocktails & Drinks
    public double price;
    public int isNew; // 1 = new addition, 0 = not

    public MenuItem(long id, String name, String category, double price, int isNew) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.isNew = isNew;
    }
}
