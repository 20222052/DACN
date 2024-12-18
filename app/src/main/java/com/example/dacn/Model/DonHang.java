package com.example.dacn.Model;
public class DonHang {
    private int idTable;
    private int maDonHang;
    private String ngayDatHang;
    private float tongTien;
    private boolean trangThai;

    public DonHang() {
    }

    public DonHang(int maDonHang, boolean trangThai, float tongTien, String ngayDatHang) {
        this.maDonHang = maDonHang;
        this.trangThai = trangThai;
        this.tongTien = tongTien;
        this.ngayDatHang = ngayDatHang;
    }
    public DonHang(int idTable, int maDonHang, boolean trangThai, float tongTien, String ngayDatHang) {
        this.idTable = idTable;
        this.maDonHang = maDonHang;
        this.trangThai = trangThai;
        this.tongTien = tongTien;
        this.ngayDatHang = ngayDatHang;
    }

    public int getIdTable() {
        return idTable;
    }

    public void setIdTable(int idTable) {
        this.idTable = idTable;
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
}
