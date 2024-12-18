package com.example.dacn.View;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dacn.Controller.CartAdapter;
import com.example.dacn.Controller.MessagingNotification;
import com.example.dacn.Controller.ProductAdapter;
import com.example.dacn.Controller.QL_menuController;
import com.example.dacn.Model.Cart;
import com.example.dacn.Model.CartViewModel;
import com.example.dacn.Model.SanPham;
import com.example.dacn.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class KhachHangActivity extends AppCompatActivity implements ProductAdapter.OnAddToCartListener {
    RecyclerView rcvProduct;
    SearchView searchView;
    ImageButton btn_cart, btn_staff;
    private List<SanPham> productList;
    private ProductAdapter productAdapter;
    private DatabaseReference database;
    private List<Cart> cartList;
    TextView cartCountText;
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
        btn_staff = findViewById(R.id.btn_staff);
        rcvProduct = findViewById(R.id.rcv_product);
        searchView = findViewById(R.id.search_food);
        btn_cart = findViewById(R.id.btn_cart);
        cartCountText = findViewById(R.id.cart_count);

        cartList = new ArrayList<>();
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, this);

        rcvProduct.setAdapter(productAdapter);
        rcvProduct.setLayoutManager(new GridLayoutManager(this, 3));

        btn_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KhachHangActivity.this, Staff.class);
                startActivity(intent);
                finish();
            }
        });

        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        // Load Firebase
        database = FirebaseDatabase.getInstance().getReference("SanPham");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productList.clear(); // Xóa danh sách cũ
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SanPham product = snapshot.getValue(SanPham.class);
                    if (product != null && product.getTrangThai()) { // Kiểm tra trạng thái sản phẩm
                        productList.add(product); // Chỉ thêm sản phẩm có trạng thái true
                    }
                }
                productAdapter.notifyDataSetChanged(); // Cập nhật adapter sau khi thay đổi danh sách
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "loadProduct:onCancelled", databaseError.toException());
            }
        });


        searchView.clearFocus();

        btn_cart.setOnClickListener(view -> showCartFragment());
    }

    @Override
    public void onAddToCart(SanPham product) {
        boolean productExists = false;
        Log.d("KhachHangActivity", "Adding product to cart: " + product.getTenSanPham());

        for (Cart cartItem : cartList) {
            if (cartItem.getId() == product.getMaSanPham()) {
                cartItem.setSoLuong(cartItem.getSoLuong() + 1);
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            Cart newCartItem = new Cart(
                    product.getMaSanPham(),
                    product.getTenSanPham(),
                    product.getHinhAnh(),
                    Double.parseDouble(String.valueOf(product.getGia())),
                    1
            );
            cartList.add(newCartItem);
        }

        cartCountText.setText(String.valueOf(cartList.size()));
    }

    private void showCartFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        CartFragment fragment = new CartFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("cart_items", (Serializable) cartList);
        fragment.setArguments(bundle);

        transaction.add(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void filter(String text) {
        List<SanPham> filteredList = new ArrayList<>();
        String normalizedText = removeAccents(text);
        for (SanPham sp : productList) {
            String normalizedName = removeAccents(sp.getTenSanPham());
            if (normalizedName.contains(normalizedText)){
                filteredList.add(sp);
            }
        }
        if (filteredList.isEmpty()) {
            return;
        } else {
            productAdapter.setFilteredList(filteredList);
        }
    }
    public static String removeAccents(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").toLowerCase();
    }

}
