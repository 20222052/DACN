package com.example.dacn.Model;

public class SanPham {
    private int maSanPham;
    private int maDanhMuc;
    private String tenSanPham;
    private String hinhAnh; // Resource ID for image
    private String moTa;
    private float gia;
    private int soLuong;
    private boolean trangThai;

    public SanPham() {
    }

    public SanPham(boolean trangThai, float gia, String hinhAnh, String tenSanPham, int maDanhMuc, int maSanPham, String moTa, int soLuong) {
        this.trangThai = trangThai;
        this.gia = gia;
        this.hinhAnh = hinhAnh;
        this.tenSanPham = tenSanPham;
        this.maDanhMuc = maDanhMuc;
        this.maSanPham = maSanPham;
        this.moTa = moTa;
        this.soLuong = soLuong;
    }
    public int getSoLuong() {
        return soLuong;
    }
    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

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

    // Accepts integer resource ID for image
    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public float getGia() {
        return gia;
    }

    public void setGia(float gia) {
        this.gia = gia;
    }
}
