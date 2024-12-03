package com.example.dacn.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import java.util.List;

public class Staff extends AppCompatActivity implements OnOrderSelectedListener {
    private GridView gridViewMenu;
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuItems;
    private GridView gridViewSelectedItems; // GridView cho danh sách sản phẩm đã chọn
    public ListAdapter listAdapter; // Adapter để hiển thị danh sách sản phẩm
    public List<ListItem> listItems; // Danh sách sản phẩm đã chọn
    ImageButton bellButton;
    Button btn_huy, btn_xacnhan;
    private TextView tvTongTien;

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
            bundle.putDouble("totallPrice", tongTien); // Truyền tổng tiền

            HoadonFragment hoadonFragment = new HoadonFragment();
            hoadonFragment.setArguments(bundle);


            // Hiển thị HoadonFragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(android.R.id.content, hoadonFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

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

    private void loadOrderDetails(String orderCode) {
        DatabaseReference orderDetailsRef = FirebaseDatabase.getInstance().getReference("Chi_Tiet_Don_Hang");

        orderDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listItems.clear(); // Xóa danh sách hiện tại trước khi tải dữ liệu mới

                for (DataSnapshot data : snapshot.getChildren()) {
                    Integer maDonHang = data.child("maDonHang").getValue(Integer.class);
                    Integer maSanPham = data.child("maSanPham").getValue(Integer.class);
                    Integer soLuong = data.child("soLuong").getValue(Integer.class);
                    Integer donGia = data.child("donGia").getValue(Integer.class);

                    if (maDonHang != null && maDonHang.toString().equals(orderCode)) {
                        // Chỉ thêm các mục khớp với `orderCode`
                        // Truy vấn để lấy tên sản phẩm từ "SanPham"
                        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("SanPham").child(maSanPham.toString());
                        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot productSnapshot) {
                                String productName = productSnapshot.child("tenSanPham").getValue(String.class);
                                if (productName != null) {
                                    String price = String.valueOf(donGia);
                                    String quantity = String.valueOf(soLuong);

                                    // Thêm tên sản phẩm, giá, số lượng vào danh sách
                                    listItems.add(new ListItem(productName, price, quantity));
                                    listAdapter.notifyDataSetChanged(); // Cập nhật giao diện
                                    updateTongTien(); // Tính lại tổng tiền
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(Staff.this, "Không thể tải tên sản phẩm", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Staff.this, "Không thể tải chi tiết đơn hàng", Toast.LENGTH_SHORT).show();
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

    private void initView() {
        bellButton = findViewById(R.id.btn_bell);
        btn_huy = findViewById(R.id.btn_huy);
        btn_xacnhan = findViewById(R.id.btn_xacnhan);
        tvTongTien = findViewById(R.id.tv_tongtien); // Liên kết TextView hiển thị tổng tiền
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

    private void showHoadonFragment(HoadonFragment hoadonFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        HoadonFragment fragment = new HoadonFragment();
        transaction.add(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onOrderSelected(String orderCode) {
        Toast.makeText(this, "Đang tải chi tiết đơn hàng: " + orderCode, Toast.LENGTH_SHORT).show();
        loadOrderDetails(orderCode);
    }

}
