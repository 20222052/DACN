package com.example.dacn.View;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.dacn.R;

public class Admin_Activity extends AppCompatActivity {
    Button btn_QL_menu, btn_QL_DH, btn_QL_KM, btn_QL_TK, btn_QL_BCTK;
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
        btn_QL_KM = findViewById(R.id.button_QL_KM);
        btn_QL_TK = findViewById(R.id.button_QL_TK);
        btn_QL_BCTK = findViewById(R.id.button_QL_BCTK);
        fragment_container = findViewById(R.id.fragment_container);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QL_menu_Fragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        btn_QL_menu.setOnClickListener(view -> {
            btn_QL_menu.setBackgroundResource(R.color.primaryColor);
            btn_QL_menu.setTextColor(Color.WHITE);
            btn_QL_DH.setBackgroundResource(R.color.Darkgray);
            btn_QL_DH.setTextColor(Color.BLACK);
            btn_QL_KM.setBackgroundResource(R.color.Darkgray);
            btn_QL_KM.setTextColor(Color.BLACK);
            btn_QL_TK.setBackgroundResource(R.color.Darkgray);
            btn_QL_TK.setTextColor(Color.BLACK);
            btn_QL_BCTK.setBackgroundResource(R.color.Darkgray);
            btn_QL_BCTK.setTextColor(Color.BLACK);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QL_menu_Fragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        });
        btn_QL_DH.setOnClickListener(view -> {
            btn_QL_menu.setBackgroundResource(R.color.Darkgray);
            btn_QL_menu.setTextColor(Color.BLACK);
            btn_QL_DH.setBackgroundResource(R.color.primaryColor);
            btn_QL_DH.setTextColor(Color.WHITE);
            btn_QL_KM.setBackgroundResource(R.color.Darkgray);
            btn_QL_KM.setTextColor(Color.BLACK);
            btn_QL_TK.setBackgroundResource(R.color.Darkgray);
            btn_QL_TK.setTextColor(Color.BLACK);
            btn_QL_BCTK.setBackgroundResource(R.color.Darkgray);
            btn_QL_BCTK.setTextColor(Color.BLACK);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QL_DH_Fragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        });
        btn_QL_KM.setOnClickListener(view -> {
            btn_QL_menu.setBackgroundResource(R.color.Darkgray);
            btn_QL_menu.setTextColor(Color.BLACK);
            btn_QL_DH.setBackgroundResource(R.color.Darkgray);
            btn_QL_DH.setTextColor(Color.BLACK);
            btn_QL_KM.setBackgroundResource(R.color.primaryColor);
            btn_QL_KM.setTextColor(Color.WHITE);
            btn_QL_TK.setBackgroundResource(R.color.Darkgray);
            btn_QL_TK.setTextColor(Color.BLACK);
            btn_QL_BCTK.setBackgroundResource(R.color.Darkgray);
            btn_QL_BCTK.setTextColor(Color.BLACK);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QL_KM_Fragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        });
        btn_QL_TK.setOnClickListener(view -> {
            btn_QL_menu.setBackgroundResource(R.color.Darkgray);
            btn_QL_menu.setTextColor(Color.BLACK);
            btn_QL_DH.setBackgroundResource(R.color.Darkgray);
            btn_QL_DH.setTextColor(Color.BLACK);
            btn_QL_KM.setBackgroundResource(R.color.Darkgray);
            btn_QL_KM.setTextColor(Color.BLACK);
            btn_QL_TK.setBackgroundResource(R.color.primaryColor);
            btn_QL_TK.setTextColor(Color.WHITE);
            btn_QL_BCTK.setBackgroundResource(R.color.Darkgray);
            btn_QL_BCTK.setTextColor(Color.BLACK);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QL_TK_Fragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        });
        btn_QL_BCTK.setOnClickListener(view -> {
            btn_QL_menu.setBackgroundResource(R.color.Darkgray);
            btn_QL_menu.setTextColor(Color.BLACK);
            btn_QL_DH.setBackgroundResource(R.color.Darkgray);
            btn_QL_DH.setTextColor(Color.BLACK);
            btn_QL_KM.setBackgroundResource(R.color.Darkgray);
            btn_QL_KM.setTextColor(Color.BLACK);
            btn_QL_TK.setBackgroundResource(R.color.Darkgray);
            btn_QL_TK.setTextColor(Color.BLACK);
            btn_QL_BCTK.setBackgroundResource(R.color.primaryColor);
            btn_QL_BCTK.setTextColor(Color.WHITE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QL_BCTK_Fragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        });
        fragment_container = findViewById(R.id.fragment_container);
    }
}