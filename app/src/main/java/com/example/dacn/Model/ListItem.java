package com.example.dacn.Model;

public class ListItem {
    private int id;
    private int maChiTietDonHang;
    private String name;
    private String price;
    private int quantity;
    private int maDonHang;
    private int maSanPham;

    public ListItem() {
    }

    public ListItem(int maSanPham, String name, String price, String quantity) {
        this.maSanPham = maSanPham;
        this.name = name;
        this.price = price;
        this.quantity = Integer.parseInt(quantity);
    }

    public ListItem(String name, String price, String quantity) {
        this.name = name;
        this.price = price;
        this.quantity = Integer.parseInt(quantity);
    }

    public ListItem(String name, String price, String quantity, int id, int maChiTietDonHang, int maDonHang, int maSanPham ) {
        this.name = name;
        this.price = price;
        this.quantity = Integer.parseInt(quantity);
        this.id = id;
        this.maChiTietDonHang = maChiTietDonHang;
        this.maDonHang = maDonHang;
        this.maSanPham = maSanPham;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public int getMaChiTietDonHang() {
        return maChiTietDonHang;
    }

    public void setMaChiTietDonHang(int maChiTietDonHang) {
        this.maChiTietDonHang = maChiTietDonHang;
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

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

}