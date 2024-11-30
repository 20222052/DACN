package com.example.dacn.Model;

import java.io.Serializable;

public class SanPham implements Serializable {
    private int maSanPham;
    private int maDanhMuc;
    private String tenSanPham;
    private String hinhAnh;
    private String moTa;
    private double gia;
    private boolean trangThai;
    private int quantity;

    // Constructor mặc định
    public SanPham() {
    }

    // Constructor có tham số để khởi tạo tất cả các thuộc tính bao gồm quantity
    public SanPham(boolean trangThai, float gia, String hinhAnh, String tenSanPham, int maDanhMuc, int maSanPham, String moTa, int quantity) {
        this.trangThai = trangThai;
        this.gia = gia;
        this.hinhAnh = hinhAnh;
        this.tenSanPham = tenSanPham;
        this.maDanhMuc = maDanhMuc;
        this.maSanPham = maSanPham;
        this.moTa = moTa;
        this.quantity = quantity;  // Khởi tạo quantity
    }

    // Getter và Setter cho các thuộc tính
    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public int getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(int maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(float gia) {
        this.gia = gia;
    }

    // Getter và Setter cho quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
