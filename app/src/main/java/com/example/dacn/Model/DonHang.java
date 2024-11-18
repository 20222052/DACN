package com.example.dacn.Model;

public class DonHang {
    private int maDonHang;
    private int maNhanVien;
    private String ngayDatHang;
    private float tongTien;
    private boolean trangThai;

    public DonHang() {
    }

    public DonHang(int maDonHang, boolean trangThai, float tongTien, String ngayDatHang, int maNhanVien) {
        this.maDonHang = maDonHang;
        this.trangThai = trangThai;
        this.tongTien = tongTien;
        this.ngayDatHang = ngayDatHang;
        this.maNhanVien = maNhanVien;
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public float getTongTien() {
        return tongTien;
    }

    public void setTongTien(float tongTien) {
        this.tongTien = tongTien;
    }

    public String getNgayDatHang() {
        return ngayDatHang;
    }

    public void setNgayDatHang(String ngayDatHang) {
        this.ngayDatHang = ngayDatHang;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }
}
