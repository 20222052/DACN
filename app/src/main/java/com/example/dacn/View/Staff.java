package com.example.dacn.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dacn.Controller.ListAdapter;
import com.example.dacn.Controller.MenuAdapter;
import com.example.dacn.Model.ListItem;
import com.example.dacn.Model.MenuItem;
import com.example.dacn.Model.SanPham;
import com.example.dacn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Staff extends AppCompatActivity {
    private GridView gridViewMenu;
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuItems;
    private List<SanPham> productList;
    private GridView gridViewSelectedItems; // GridView cho danh sách sản phẩm đã chọn
    private ListAdapter listAdapter; // Adapter để hiển thị danh sách sản phẩm
    private List<ListItem> listItems; // Danh sách sản phẩm đã chọn
    ImageButton bellButton;
    Button btn_huy, btn_xacnhan;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_staff);

        initView();
        gridViewMenu = findViewById(R.id.gridView_menu);
        menuItems = new ArrayList<>();
        productList = new ArrayList<>();
        menuAdapter = new MenuAdapter(this, menuItems);
        gridViewMenu.setAdapter(menuAdapter);

        gridViewSelectedItems = findViewById(R.id.gridView_list);
        listItems = new ArrayList<>();
        listAdapter = new ListAdapter(this, listItems);
        gridViewSelectedItems.setAdapter(listAdapter);

        loadMenuData();

        bellButton.setOnClickListener(view -> showNotificationFragment());
        btn_xacnhan.setOnClickListener(view -> showHoadonFragment());

    }

    public void addToListItem(ListItem listItem) {
        listItems.add(listItem);
        listAdapter.notifyDataSetChanged();  // Cập nhật ListView
    }

    private void loadMenuData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SanPham");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuItems.clear(); // Xóa dữ liệu cũ
                productList = new ArrayList<>(); // Khởi tạo lại productList để tránh lỗi NullPointerException
                for (DataSnapshot data : snapshot.getChildren()) {
                    SanPham product = data.getValue(SanPham.class); // Đọc dữ liệu từ từng `data`
                    if (product != null) {
                        if (product.getTrangThai()) {
                            productList.add(product);
                            // Chuyển dữ liệu từ SanPham sang MenuItem
                            menuItems.add(new MenuItem(product.getHinhAnh(), product.getTenSanPham(), String.valueOf(product.getGia())));
                        }
                    }
                }
                menuAdapter.notifyDataSetChanged(); // Cập nhật GridView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Staff.this, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        bellButton = findViewById(R.id.btn_bell);
        btn_huy = findViewById(R.id.btn_huy);
        btn_xacnhan = findViewById(R.id.btn_xacnhan);
    }

    private void showNotificationFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        NotificationFragment fragment = new NotificationFragment();
        transaction.add(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showHoadonFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        HoadonFragment fragment = new HoadonFragment();
        transaction.add(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}