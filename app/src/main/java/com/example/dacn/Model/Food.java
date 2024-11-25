package com.example.dacn.Model;

public class Food {
    private int id;
    private String name;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Food(String name, int id) {
        this.name = name;
        this.id = id;
    }
}