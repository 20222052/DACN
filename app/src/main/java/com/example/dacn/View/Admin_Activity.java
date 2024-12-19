package com.example.dacn.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.dacn.R;

public class Admin_Activity extends AppCompatActivity {
    Button btn_QL_menu, btn_QL_DH, btn_QL_NV, btn_QL_TK, btn_QL_BCTK, btn_QL_Table, btn_dangXuat;
    FrameLayout fragment_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        btn_QL_menu = findViewById(R.id.button_QL_menu);
        btn_QL_DH = findViewById(R.id.button_QL_DH);
        btn_QL_NV = findViewById(R.id.button_QL_KM);
        btn_QL_TK = findViewById(R.id.button_QL_TK);
        btn_QL_BCTK = findViewById(R.id.button_QL_BCTK);
        btn_QL_Table = findViewById(R.id.button_QL_Table);
        btn_dangXuat = findViewById(R.id.button_dangXuat);

        btn_QL_menu.setBackgroundResource(R.color.white);
        btn_QL_menu.setTextColor(Color.BLACK);
        btn_QL_DH.setBackgroundResource(R.color.primaryColor);
        btn_QL_DH.setTextColor(Color.WHITE);
        btn_QL_NV.setBackgroundResource(R.color.primaryColor);
        btn_QL_NV.setTextColor(Color.WHITE);
        btn_QL_TK.setBackgroundResource(R.color.primaryColor);
        btn_QL_TK.setTextColor(Color.WHITE);
        btn_QL_BCTK.setBackgroundResource(R.color.primaryColor);
        btn_QL_BCTK.setTextColor(Color.WHITE);
        btn_QL_Table.setBackgroundResource(R.color.primaryColor);
        btn_QL_Table.setTextColor(Color.WHITE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QL_menu_Fragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

        fragment_container = findViewById(R.id.fragment_container);
        btn_QL_menu.setOnClickListener(view -> {
            btn_QL_menu.setBackgroundResource(R.color.white);
            btn_QL_menu.setTextColor(Color.BLACK);
            btn_QL_DH.setBackgroundResource(R.color.primaryColor);
            btn_QL_DH.setTextColor(Color.WHITE);
            btn_QL_NV.setBackgroundResource(R.color.primaryColor);
            btn_QL_NV.setTextColor(Color.WHITE);
            btn_QL_TK.setBackgroundResource(R.color.primaryColor);
            btn_QL_TK.setTextColor(Color.WHITE);
            btn_QL_BCTK.setBackgroundResource(R.color.primaryColor);
            btn_QL_BCTK.setTextColor(Color.WHITE);
            btn_QL_Table.setBackgroundResource(R.color.primaryColor);
            btn_QL_Table.setTextColor(Color.WHITE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QL_menu_Fragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        });
        btn_QL_DH.setOnClickListener(view -> {
            btn_QL_menu.setBackgroundResource(R.color.primaryColor);
            btn_QL_menu.setTextColor(Color.WHITE);
            btn_QL_DH.setBackgroundResource(R.color.white);
            btn_QL_DH.setTextColor(Color.BLACK);
            btn_QL_NV.setBackgroundResource(R.color.primaryColor);
            btn_QL_NV.setTextColor(Color.WHITE);
            btn_QL_TK.setBackgroundResource(R.color.primaryColor);
            btn_QL_TK.setTextColor(Color.WHITE);
            btn_QL_BCTK.setBackgroundResource(R.color.primaryColor);
            btn_QL_BCTK.setTextColor(Color.WHITE);
            btn_QL_Table.setBackgroundResource(R.color.primaryColor);
            btn_QL_Table.setTextColor(Color.WHITE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QL_DH_Fragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        });
        btn_QL_NV.setOnClickListener(view -> {
            btn_QL_menu.setBackgroundResource(R.color.primaryColor);
            btn_QL_menu.setTextColor(Color.WHITE);
            btn_QL_DH.setBackgroundResource(R.color.primaryColor);
            btn_QL_DH.setTextColor(Color.WHITE);
            btn_QL_NV.setBackgroundResource(R.color.white);
            btn_QL_NV.setTextColor(Color.BLACK);
            btn_QL_TK.setBackgroundResource(R.color.primaryColor);
            btn_QL_TK.setTextColor(Color.WHITE);
            btn_QL_BCTK.setBackgroundResource(R.color.primaryColor);
            btn_QL_BCTK.setTextColor(Color.WHITE);
            btn_QL_Table.setBackgroundResource(R.color.primaryColor);
            btn_QL_Table.setTextColor(Color.WHITE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QL_NV_Fragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        });
        btn_QL_TK.setOnClickListener(view -> {
            btn_QL_menu.setBackgroundResource(R.color.primaryColor);
            btn_QL_menu.setTextColor(Color.WHITE);
            btn_QL_DH.setBackgroundResource(R.color.primaryColor);
            btn_QL_DH.setTextColor(Color.WHITE);
            btn_QL_NV.setBackgroundResource(R.color.primaryColor);
            btn_QL_NV.setTextColor(Color.WHITE);
            btn_QL_TK.setBackgroundResource(R.color.white);
            btn_QL_TK.setTextColor(Color.BLACK);
            btn_QL_BCTK.setBackgroundResource(R.color.primaryColor);
            btn_QL_BCTK.setTextColor(Color.WHITE);
            btn_QL_Table.setBackgroundResource(R.color.primaryColor);
            btn_QL_Table.setTextColor(Color.WHITE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QL_TK_Fragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        });
        btn_QL_BCTK.setOnClickListener(view -> {
            btn_QL_menu.setBackgroundResource(R.color.primaryColor);
            btn_QL_menu.setTextColor(Color.WHITE);
            btn_QL_DH.setBackgroundResource(R.color.primaryColor);
            btn_QL_DH.setTextColor(Color.WHITE);
            btn_QL_NV.setBackgroundResource(R.color.primaryColor);
            btn_QL_NV.setTextColor(Color.WHITE);
            btn_QL_TK.setBackgroundResource(R.color.primaryColor);
            btn_QL_TK.setTextColor(Color.WHITE);
            btn_QL_BCTK.setBackgroundResource(R.color.white);
            btn_QL_BCTK.setTextColor(Color.BLACK);
            btn_QL_Table.setBackgroundResource(R.color.primaryColor);
            btn_QL_Table.setTextColor(Color.WHITE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QL_BCTK_Fragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        });

        btn_QL_Table.setOnClickListener(view -> {
            btn_QL_menu.setBackgroundResource(R.color.primaryColor);
            btn_QL_menu.setTextColor(Color.WHITE);
            btn_QL_DH.setBackgroundResource(R.color.primaryColor);
            btn_QL_DH.setTextColor(Color.WHITE);
            btn_QL_NV.setBackgroundResource(R.color.primaryColor);
            btn_QL_NV.setTextColor(Color.WHITE);
            btn_QL_TK.setBackgroundResource(R.color.primaryColor);
            btn_QL_TK.setTextColor(Color.WHITE);
            btn_QL_BCTK.setBackgroundResource(R.color.primaryColor);
            btn_QL_BCTK.setTextColor(Color.WHITE);
            btn_QL_Table.setBackgroundResource(R.color.white);
            btn_QL_Table.setTextColor(Color.BLACK);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QL_Table_Fragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        });

        btn_dangXuat.setOnClickListener(view -> {
            // Tạo một AlertDialog để hỏi người dùng có muốn đăng xuất không
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất tài khoản không?")
                    .setPositiveButton("Đồng ý", (dialog, which) -> {
                        // Nếu người dùng chọn "Đồng ý", chuyển sang MainActivity và kết thúc Activity hiện tại
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Hủy", (dialog, which) -> {
                        // Nếu người dùng chọn "Hủy", không làm gì cả
                        dialog.dismiss();
                    })
                    .show();
        });
        fragment_container = findViewById(R.id.fragment_container);
    }
}