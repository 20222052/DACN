package com.example.dacn.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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


        rcvProduct = findViewById(R.id.rcv_product);
        btn_cart = findViewById(R.id.btn_cart);
        cartCountText = findViewById(R.id.cart_count);

        productList = new ArrayList<>();
        cartList = new ArrayList<>();

        productAdapter = new ProductAdapter(productList, this);
        rcvProduct.setAdapter(productAdapter);

        rcvProduct.setLayoutManager(new GridLayoutManager(this, 3));

        database = FirebaseDatabase.getInstance().getReference("SanPham");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SanPham product = snapshot.getValue(SanPham.class);
                    productList.add(product);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "loadProduct:onCancelled", databaseError.toException());
            }
        });

        btn_cart.setOnClickListener(v -> showCartFragment());
    }

    //show fragment
    private void showCartFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Tạo đối tượng CartFragment
        CartFragment fragment = new CartFragment();

        // Truyền dữ liệu giỏ hàng (cartList) vào CartFragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("cart_items", (Serializable) cartList);
        fragment.setArguments(bundle);

        transaction.add(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onAddToCart(SanPham product) {
        Log.d("KhachHangActivity", "Adding product to cart: " + product.getTenSanPham());

        boolean productExists = false;
        for (Cart cartItem : cartList) {
            if (cartItem.getId() == product.getMaSanPham()) {
                cartItem.setSoLuong(cartItem.getSoLuong() + 1);
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            Cart newCart = new Cart(
                    product.getMaSanPham(),
                    product.getTenSanPham(),
                    product.getHinhAnh(),
                    product.getGia(),
                    1
            );
            cartList.add(newCart);
        }

        cartCount++;
        cartCountText.setText(String.valueOf(cartCount));
    }
}