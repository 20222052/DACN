package com.example.dacn.Model;

import java.io.Serializable;

public class Cart implements Serializable {
    private int id;
    private String tenSP;
    private String anhSP;
    private Double gia;
    private int soLuong;

    public Cart() {
    }

    public Cart(int id, String tenSP, String anhSP, Double gia, int soLuong) {
        this.id = id;
        this.tenSP = tenSP;
        this.anhSP = anhSP;
        this.gia = gia;
        this.soLuong = soLuong;
    }

    public Cart(String tenSP, String anhSP, Double gia) {
        this.tenSP = tenSP;
        this.anhSP = anhSP;
        this.gia = gia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getGia() {
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

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", tenSP='" + tenSP + '\'' +
                ", anhSP='" + anhSP + '\'' +
                ", gia=" + gia +
                ", soLuong=" + soLuong +
                '}';
    }
}