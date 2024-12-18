package com.example.dacn.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dacn.Model.TaiKhoan;
import com.example.dacn.View.Admin_Activity;
import com.example.dacn.View.KhachHangActivity;
import com.example.dacn.View.Staff;
import com.example.dacn.View.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivityController {
    private Context context;
    private DatabaseReference mDatabase;
    private String nhanvienid;
    public MainActivityController(Context context) {
        this.context = context;
    }
    public void DN_TaiKhoan(String tenDangNhap, String matKhau) {
        if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {
            Toast.makeText(context, "Tài Khoản hoặc mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
        } else {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("TaiKhoan").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean userFound = false;
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String username = userSnapshot.child("tenDangNhap").getValue(String.class);
                        if (tenDangNhap.equals(username)) {
                            userFound = true;
                            String getpassword = userSnapshot.child("matKhau").getValue(String.class);
                            nhanvienid = userSnapshot.child("nhanVienId").getValue(String.class);
                            Log.d("MainActivityController", "nhanvienid: " + nhanvienid);
                            if (getpassword != null && getpassword.equals(matKhau)) {
                                String role = userSnapshot.child("vaiTro").getValue(String.class);
                                if (role != null) {
                                    navigateToActivity(role, nhanvienid);
                                }
                            }
                            else {
                                Toast.makeText(context, "Mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                    }
                    if (!userFound) {
                        Toast.makeText(context, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
    private void navigateToActivity(String role, String nhanvienid) {
        Intent intent;
        if (role.equals("Quản lý")) {
            intent = new Intent(context, Admin_Activity.class);
            intent.putExtra("nhanVienId", nhanvienid);
        }
        else if (role.equals("Nhân viên")) {
            intent = new Intent(context, Staff.class);
            intent.putExtra("nhanVienId", nhanvienid);
        }
        else {
            Toast.makeText(context, "Vai trò không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }
        context.startActivity(intent);
    }
}
