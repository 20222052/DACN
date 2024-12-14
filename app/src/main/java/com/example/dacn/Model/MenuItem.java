package com.example.dacn.Model;

public class MenuItem {
    private int idprd;
    private String imageResource;
    private String name;
    private String price;

    public MenuItem() {
    }

    public MenuItem(int idprd, String imageResource, String name, String price) {
        this.idprd = idprd;
        this.imageResource = imageResource;
        this.name = name;
        this.price = price;
    }

    public MenuItem(String imageResource, String name, String price) {
        this.imageResource = imageResource;
        this.name = name;
        this.price = price;
    }

    public int getIdprd() {
        return idprd;
    }

    public void setIdprd(int idprd) {
        this.idprd = idprd;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
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
}