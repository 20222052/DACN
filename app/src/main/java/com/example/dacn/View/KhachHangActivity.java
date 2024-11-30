package com.example.dacn.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dacn.Controller.CartAdapter;
import com.example.dacn.Controller.ProductAdapter;
import com.example.dacn.Model.Cart;
import com.example.dacn.Model.CartViewModel;
import com.example.dacn.Model.SanPham;
import com.example.dacn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KhachHangActivity extends AppCompatActivity implements ProductAdapter.OnAddToCartListener {
    RecyclerView rcvProduct;
    SearchView searchView;
    ImageButton btn_cart;
    private List<SanPham> productList;
    private ProductAdapter productAdapter;
    private DatabaseReference database;
    private List<Cart> cartList;
    private int cartCount = 0;
    TextView cartCountText;
    private CartAdapter cartAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_khachhang);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.khachhang), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rcvProduct = findViewById(R.id.rcv_product);

        searchView = findViewById(R.id.search_food);

        btn_cart = findViewById(R.id.btn_cart);

        cartCountText = findViewById(R.id.cart_count);

        cartList = new ArrayList<>();  // Khởi tạo giỏ hàng
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, this);  // Gửi listener vào adapter
        rcvProduct.setAdapter(productAdapter);

        // Lấy dữ liệu từ Firebase
        database = FirebaseDatabase.getInstance().getReference("SanPham");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SanPham product = snapshot.getValue(SanPham.class);
                    productList.add(product);
                }
                // Cập nhật RecyclerView
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "loadProduct:onCancelled", databaseError.toException());
            }
        });
        // set layout
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        rcvProduct.setLayoutManager(gridLayoutManager);
        // SearchView
        searchView.clearFocus();

        productAdapter.setOnAddToCartListener(product -> {
            cartCount++;  // Tăng số lượng giỏ hàng
            cartCountText.setText(String.valueOf(cartCount));  // Cập nhật UI
        });

        CartViewModel cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        cartViewModel.setCartList(cartList); // Truyền dữ liệu vào ViewModel


        // Xem giỏ hàng
        btn_cart.setOnClickListener(view -> showCartFragment());
    }

    //show fragment
    private void showCartFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Tạo đối tượng CartFragment
        CartFragment fragment = new CartFragment();

        // Truyền dữ liệu giỏ hàng (cartList) vào CartFragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("cart_items", (Serializable) cartList);  // Truyền giỏ hàng vào Bundle
        fragment.setArguments(bundle);

        transaction.add(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onAddToCart(SanPham product) {
        boolean productExists = false;

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        for (Cart cartItem : cartList) {
            if (cartItem.getId() == product.getMaSanPham()) {
                cartItem.setSoLuong(cartItem.getSoLuong() + 1); // Tăng số lượng
                productExists = true;
                break;
            }
        }

        // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới
        if (!productExists) {
            Cart newCartItem = new Cart(
                    product.getMaSanPham(), // Mã sản phẩm
                    product.getTenSanPham(), // Tên sản phẩm
                    product.getHinhAnh(), // URL hình ảnh
                    (double) product.getGia(), // Giá sản phẩm

                    1 // Số lượng mặc định là 1
            );
            cartList.add(newCartItem);
        }

        // Cập nhật tổng số lượng sản phẩm trong giỏ hàng
        int cartCount = 0;
        for (Cart item : cartList) {
            cartCount += item.getSoLuong();
        }

        // Cập nhật giao diện người dùng (UI)
        cartCountText.setText(String.valueOf(cartCount));

        // Thông báo cho Adapter cập nhật lại giao diện giỏ hàng
        cartAdapter.notifyDataSetChanged();

        Log.d("Cart", "Added product: " + product.getTenSanPham());
    }

}