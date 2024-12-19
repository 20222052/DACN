package com.example.dacn.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TableActivity extends AppCompatActivity {
    private RecyclerView rvTables;
    private TableAdapter tableAdapter;
    private List<Table> tables;
    private DatabaseReference tableRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        rvTables = findViewById(R.id.rvTables);
        tables = new ArrayList<>();

        // Firebase Realtime Database reference
        tableRef = FirebaseDatabase.getInstance().getReference("Table");

        // Lấy dữ liệu từ Firebase
        loadTablesFromFirebase();

        // Khởi tạo adapter và thiết lập RecyclerView
        tableAdapter = new TableAdapter(tables, table -> {
            // Xử lý khi click vào bàn
            Intent intent = new Intent(this, KhachHangActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Clicked on: " + table.getNameTable(), Toast.LENGTH_SHORT).show();
        });

        rvTables.setLayoutManager(new GridLayoutManager(this, 4)); // Hiển thị 4 cột
        rvTables.setAdapter(tableAdapter);
    }

    private void loadTablesFromFirebase() {
        tableRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tables.clear(); // Xóa danh sách cũ
                for (DataSnapshot tableSnapshot : snapshot.getChildren()) {
                    Table table = tableSnapshot.getValue(Table.class);
                    if (table != null) {
                        tables.add(table);
                    }
                }
                tableAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TableActivity.this, "Failed to load tables: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
