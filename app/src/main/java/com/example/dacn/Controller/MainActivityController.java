package com.example.dacn.Controller;

import com.example.dacn.Model.TaiKhoan;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivityController {
    private DatabaseReference mDatabase;
    public MainActivityController() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
    public void DK_TaiKhoan(int maTaiKhoan, int maNhanVien, String tenDangNhap, String matKhau, String vaiTro) {
        TaiKhoan user = new TaiKhoan(maTaiKhoan, maNhanVien, tenDangNhap, matKhau, vaiTro);
        mDatabase.child("users").push().setValue(user);
    }
}
