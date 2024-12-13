package com.example.dacn.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
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
import com.example.dacn.Model.Cart;
import com.example.dacn.Model.ChiTietDonHang;
import com.example.dacn.Model.ListItem;
import com.example.dacn.Model.MenuItem;
import com.example.dacn.Model.OnOrderSelectedListener;
import com.example.dacn.Model.SanPham;
import com.example.dacn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Staff extends AppCompatActivity implements OnOrderSelectedListener {
    private GridView gridViewMenu;
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuItems;
    private GridView gridViewSelectedItems; // GridView cho danh sách sản phẩm đã chọn
    public ListAdapter listAdapter; // Adapter để hiển thị danh sách sản phẩm
    public List<ListItem> listItems; // Danh sách sản phẩm đã chọn
    private List<SanPham> lstSp = new ArrayList<>();
    ImageButton bellButton, btn_back;
    Button btn_huy, btn_xacnhan;
    private TextView tvTongTien;
    Integer MaDH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        initView();

        gridViewMenu = findViewById(R.id.gridView_menu);
        menuItems = new ArrayList<>();
        menuAdapter = new MenuAdapter(this, menuItems);
        gridViewMenu.setAdapter(menuAdapter);

        gridViewSelectedItems = findViewById(R.id.gridView_list);
        listItems = new ArrayList<>();
        listAdapter = new ListAdapter(this, listItems);
        gridViewSelectedItems.setAdapter(listAdapter);

        loadMenuData();

        bellButton.setOnClickListener(view -> showNotificationFragment());
        btn_xacnhan.setOnClickListener(view -> {
            double tongTien = 0.0; // Đổi sang kiểu double
            for (ListItem item : listItems) {
                tongTien += item.getQuantity() * Double.parseDouble(item.getPrice()); // Sử dụng Double.parseDouble
            }
            // Truyền danh sách đơn hàng (listItems) sang fragment HoadonFragment
            Bundle bundle = new Bundle();
            bundle.putSerializable("listItems", new ArrayList<>(listItems)); // Dùng putSerializable để truyền danh sách
            bundle.putSerializable("listPrd", new ArrayList<>(lstSp)); // Dùng putSerializable để truyền danh sách
            bundle.putDouble("totallPrice", tongTien); // Truyền tổng tiền
            bundle.putInt("orderCode", MaDH); // Truyền Mã đơn hàng

            HoadonFragment hoadonFragment = new HoadonFragment();
            hoadonFragment.setArguments(bundle);

            // Hiển thị HoadonFragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(android.R.id.content, hoadonFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        //trở về layout khach hang
        Intent intent = new Intent(this, KhachHangActivity.class);
        btn_back.setOnClickListener(view -> startActivity(intent));
    }

    public void addToListItem(ListItem listItem) {
        boolean exists = false;

        for (ListItem item : listItems) {
            if (item.getName().equals(listItem.getName())) { // Kiểm tra trùng sản phẩm
                item.setQuantity(item.getQuantity() + listItem.getQuantity()); // Tăng số lượng
                exists = true;
                break;
            }
        }

        if (!exists) {
            listItems.add(listItem); // Thêm sản phẩm mới nếu chưa tồn tại
        }

        listAdapter.notifyDataSetChanged(); // Cập nhật giao diện
        updateTongTien(); // Cập nhật tổng tiền
    }
    public void updateOrAddChiTietDonHang(float donGia, int soLuong, int maDonHang, int maChiTietDonHang, int maSanPham) {
        // Đảm bảo các biến này là final hoặc không thay đổi trong suốt quá trình
        final float finalDonGia = donGia;
        final int finalSoLuong = soLuong;
        final int finalMaDonHang = maDonHang;
        final int finalMaChiTietDonHang = maChiTietDonHang;
        final int finalMaSanPham = maSanPham;

        // Tham chiếu đến chi tiết đơn hàng trong Firebase
        DatabaseReference chiTietDonHangRef = FirebaseDatabase.getInstance().getReference("Chi_Tiet_Don_Hang").child(String.valueOf(finalMaChiTietDonHang));

        chiTietDonHangRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    chiTietDonHangRef.child("soLuong").setValue(finalSoLuong).addOnCompleteListener(updateTask -> {
                        if (updateTask.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Sử dụng các biến final để tạo đối tượng ChiTietDonHang
                    ChiTietDonHang newChiTiet = new ChiTietDonHang(finalDonGia, finalSoLuong, finalMaDonHang, finalMaChiTietDonHang, finalMaSanPham);
                    chiTietDonHangRef.setValue(newChiTiet).addOnCompleteListener(addTask -> {
                        if (addTask.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Thêm mới thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Thêm mới thất bại!", Toast.LENGTH_SHORT).show();
                            Log.e("Firebase", "Add task failed: " + addTask.getException());
                        }
                    });
                }
            } else {
                Log.e("Firebase", "Error retrieving data: " + task.getException());
                Toast.makeText(getApplicationContext(), "Lỗi khi truy xuất dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadOrderDetails(String orderCode) {
        DatabaseReference orderDetailsRef = FirebaseDatabase.getInstance().getReference("Chi_Tiet_Don_Hang");
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("SanPham");

        // Bước 1: Lấy tất cả sản phẩm trước
        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot productSnapshot) {
                // Map lưu thông tin sản phẩm: key = maSanPham, value = SanPham
                Map<Integer, SanPham> productMap = new HashMap<>();
                for (DataSnapshot productData : productSnapshot.getChildren()) {
                    Integer maSanPham = Integer.valueOf(productData.getKey());
                    SanPham sp = productData.getValue(SanPham.class);
                    productMap.put(maSanPham, sp);
                }

                // Bước 2: Lấy chi tiết đơn hàng
                orderDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot orderSnapshot) {
                        listItems.clear();
                        lstSp.clear();

                        for (DataSnapshot data : orderSnapshot.getChildren()) {
                            Integer maDonHang = data.child("maDonHang").getValue(Integer.class);
                            Integer maSanPham = data.child("maSanPham").getValue(Integer.class);
                            Integer soLuong = data.child("soLuong").getValue(Integer.class);
                            Integer donGia = data.child("donGia").getValue(Integer.class);

                            // Kiểm tra mã đơn hàng khớp
                            if (maDonHang != null && maDonHang.toString().equals(orderCode)) {
                                // Lấy thông tin sản phẩm từ Map
                                SanPham sp = productMap.get(maSanPham);
                                if (sp != null) {
                                    lstSp.add(sp); // Thêm vào danh sách sản phẩm

                                    // Tạo đối tượng ListItem
                                    ListItem listItem = new ListItem();
                                    listItem.setName(sp.getTenSanPham());
                                    listItem.setPrice(String.valueOf(donGia));
                                    listItem.setQuantity(soLuong);
                                    listItem.setMaChiTietDonHang(listItem.getMaChiTietDonHang());
                                    listItems.add(listItem);
                                }
                            }
                        }

                        // Cập nhật giao diện sau khi xử lý xong toàn bộ dữ liệu
                        listAdapter.notifyDataSetChanged();
                        updateTongTien();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Staff.this, "Không thể tải chi tiết đơn hàng", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Staff.this, "Không thể tải danh sách sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadMenuData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SanPham");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuItems.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    SanPham product = data.getValue(SanPham.class);
                    if (product != null && product.getTrangThai()) {
                        menuItems.add(new MenuItem(product.getHinhAnh(), product.getTenSanPham(), String.valueOf(product.getGia())));
                    }
                }
                menuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Staff.this, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("WrongViewCast")
    private void initView() {
        bellButton = findViewById(R.id.btn_bell);
        btn_huy = findViewById(R.id.btn_huy);
        btn_xacnhan = findViewById(R.id.btn_xacnhan);
        tvTongTien = findViewById(R.id.tv_tongtien); // Liên kết TextView hiển thị tổng tiền
        btn_back = findViewById(R.id.btn_back);
    }

    // Tính và cập nhật tổng tiền
    public void updateTongTien() {
        double tongTien = 0.0; // Đổi sang kiểu double
        for (ListItem item : listItems) {
            tongTien += item.getQuantity() * Double.parseDouble(item.getPrice()); // Sử dụng Double.parseDouble
        }
        tvTongTien.setText(String.format("%,.0f VNĐ", tongTien)); // Định dạng tiền tệ
    }


    private void showNotificationFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        NotificationFragment fragment = new NotificationFragment();
        transaction.add(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onOrderSelected(String orderCode) {
        Toast.makeText(this, "Đang tải chi tiết đơn hàng: " + orderCode, Toast.LENGTH_SHORT).show();
        loadOrderDetails(orderCode);
        MaDH = Integer.valueOf(orderCode);
    }
}
