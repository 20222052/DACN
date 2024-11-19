package com.example.dacn.Model;

public class Cart {
    private int id;
    private String tenSP;
    private String anhSP;
    private Double gia;

    public Cart() {
    }


    public Cart(int id, String tenSP, String anhSP, Double gia) {
        this.id = id;
        this.tenSP = tenSP;
        this.anhSP = anhSP;
        this.gia = gia;
    }

    public Cart(String tenSP, String anhSP, Double gia) {
        this.tenSP = tenSP;
        this.anhSP = anhSP;
        this.gia = gia;
    }

    public Double getGia() {
        return gia;
    }

    public void setGia(Double gia) {
        this.gia = gia;
    }

    public String getAnhSP() {
        return anhSP;
    }

    public void setAnhSP(String anhSP) {
        this.anhSP = anhSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}