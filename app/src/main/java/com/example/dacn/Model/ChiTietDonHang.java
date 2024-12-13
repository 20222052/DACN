package com.example.dacn.Model;

public class ChiTietDonHang {
    private int maChiTietDonHang;
    private int maSanPham;
    private int maDonHang;
    private int soLuong;
    private float donGia;

    public ChiTietDonHang() {
    }

    public ChiTietDonHang(float donGia, int soLuong, int maDonHang, int maChiTietDonHang, int maSanPham) {
        this.donGia = donGia;
        this.soLuong = soLuong;
        this.maDonHang = maDonHang;
        this.maChiTietDonHang = maChiTietDonHang;
        this.maSanPham = maSanPham;
    }

    public ChiTietDonHang(int donGia, int id, Integer maDH, int quantity, String price) {
    }

    public int getMaChiTietDonHang() {
        return maChiTietDonHang;
    }

    public void setMaChiTietDonHang(int maChiTietDonHang) {
        this.maChiTietDonHang = maChiTietDonHang;
    }

    public float getDonGia() {
        return donGia;
    }

    public void setDonGia(float donGia) {
        this.donGia = donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }
}
