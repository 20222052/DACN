package com.example.dacn.View;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
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
    private String nhanVienId;

    ImageButton btnBackStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        btnBackStaff = findViewById(R.id.btn_back);
        rvTables = findViewById(R.id.rvTables);
        tables = new ArrayList<>();

        // Firebase Realtime Database reference
        tableRef = FirebaseDatabase.getInstance().getReference("Table");
        nhanVienId = getIntent().getStringExtra("nhanVienId");
        // Lấy dữ liệu từ Firebase
        loadTablesFromFirebase();

        tableAdapter = new TableAdapter(tables, table -> {
            if (table.isStatus()) {
                Toast.makeText(this, table.getNameTable() + " đã có khách!", Toast.LENGTH_SHORT).show();
            } else {
                // Chuyển sang màn hình order
                Intent intent = new Intent(this, KhachHangActivity.class);
                intent.putExtra("tableId", table.getIdTable()); // Truyền ID bàn
                intent.putExtra("nhanVienId", nhanVienId); // Truyền ID nhân viên
                startActivity(intent);
            }
        });


        rvTables.setLayoutManager(new GridLayoutManager(this, 4)); // Hiển thị 4 cột
        rvTables.setAdapter(tableAdapter);

        btnBackStaff.setOnClickListener(v -> {
            Intent intent = new Intent(this, Staff.class);
            intent.putExtra("nhanVienId", nhanVienId);
            startActivity(intent);
            finish();
        });
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
