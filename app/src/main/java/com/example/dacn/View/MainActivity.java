package com.example.dacn.View;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dacn.Controller.MainActivityController;
import com.example.dacn.R;

public class MainActivity extends AppCompatActivity {
    Button btn_dk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        MainActivityController controller = new MainActivityController();
        new MainActivityController();
        btn_dk = findViewById(R.id.btn_dk);
        btn_dk.setOnClickListener(view -> {
            controller.DK_TaiKhoan(1, 1, "thinh", "123", "admin");
        });
    }
}