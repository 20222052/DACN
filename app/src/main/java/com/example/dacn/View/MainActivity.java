package com.example.dacn.View;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dacn.Controller.MainActivityController;
import com.example.dacn.Model.TaiKhoan;
import com.example.dacn.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private MainActivityController controller;
    Button btn_dk;
    EditText EditText_pass, EditText_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.khachhang), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText_pass = findViewById(R.id.EditText_pass);
        EditText_user = findViewById(R.id.EditText_user);
        controller = new MainActivityController(this);
        btn_dk = findViewById(R.id.btn_dk);
        TaiKhoan taiKhoan = new TaiKhoan();
        btn_dk.setOnClickListener(view -> {
            taiKhoan.setTenDangNhap(EditText_user.getText().toString());
            taiKhoan.setMatKhau(EditText_pass.getText().toString());
            controller.DN_TaiKhoan(taiKhoan.getTenDangNhap(), taiKhoan.getMatKhau());
            //controller.DK_TaiKhoan(2,2,"nhanvien1","123","nhanvien");
        });
    }
}