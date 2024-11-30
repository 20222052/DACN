package com.example.dacn.Model;

public class NhanVien {
    private String id;
    private String tenNhanVien;
    private String soDienThoai;
    private String address;
    private String vaiTro;

    // Default constructor required for calls to DataSnapshot.getValue(NhanVien.class)
    public NhanVien() {
    }

    public NhanVien(String id, String tenNhanVien, String soDienThoai, String address, String vaiTro) {
        this.id = id;
        this.tenNhanVien = tenNhanVien;
        this.soDienThoai = soDienThoai;
        this.address = address;
        this.vaiTro = vaiTro;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }
}
