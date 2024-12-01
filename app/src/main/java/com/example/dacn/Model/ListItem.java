package com.example.dacn.Model;

public class ListItem {
    private String name;
    private String price;
    private int quantity;

    public ListItem() {
    }

    public ListItem(String name, String price, String quantity) {
        this.name = name;
        this.price = price;
        this.quantity = Integer.parseInt(quantity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}