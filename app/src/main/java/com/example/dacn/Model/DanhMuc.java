package com.example.dacn.Model;

import androidx.annotation.NonNull;

public class DanhMuc {
    private int maDanhMuc;
    private String tenDanhMuc;

    // Constructor
    public DanhMuc() {
    }

    public DanhMuc(int maDanhMuc, String tenDanhMuc) {
        this.maDanhMuc = maDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
    }

    // Getter and Setter
    public int getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(int maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    @NonNull
    @Override
    public String toString() {
        return this.tenDanhMuc; // Ensure this returns the category name
    }

}
