package com.example.dacn.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dacn.Controller.TableAdapter;
import com.example.dacn.Model.Table;
import com.example.dacn.R;

import java.util.ArrayList;
import java.util.List;

public class TableActivity extends AppCompatActivity {
    private RecyclerView rvTables;
    private TableAdapter tableAdapter;
    private List<Table> tables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        rvTables = findViewById(R.id.rvTables);

        // Danh sách số bàn
        List<Table> tables = new ArrayList<>();

        for (int i = 1; i <= 12; i++) { // 20 bàn
            boolean hasCustomer = (i % 3 == 0);
            tables.add(new Table(i, "Bàn " + i, hasCustomer));
        }

        tableAdapter = new TableAdapter(tables, table -> {

        });

        rvTables.setLayoutManager(new GridLayoutManager(this, 4)); // Hiển thị cột
        rvTables.setAdapter(tableAdapter);
    }
}
