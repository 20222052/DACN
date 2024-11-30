package com.example.dacn.Model;

public class TaiKhoan {
    private String maTaiKhoan;
    private String tenDangNhap;
    private String matKhau;
    private String nhanVienId;
    private String vaiTro;

    // Default constructor required for calls to DataSnapshot.getValue(TaiKhoan.class)
    public TaiKhoan() {
    }

    public TaiKhoan(String maTaiKhoan, String tenDangNhap, String matKhau, String nhanVienId, String vaiTro) {
        this.maTaiKhoan = maTaiKhoan;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.nhanVienId = nhanVienId;
        this.vaiTro = vaiTro;
    }

    // Getters and setters
    public String getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(String maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getNhanVienId() {
        return nhanVienId;
    }

    public void setNhanVienId(String nhanVienId) {
        this.nhanVienId = nhanVienId;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }
}