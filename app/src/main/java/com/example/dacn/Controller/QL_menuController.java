package com.example.dacn.Controller;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dacn.Model.DanhMuc;
import com.example.dacn.Model.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QL_menuController {
    private Context context;
    private DatabaseReference mDatabase;
    private List<SanPham> productList;

    public QL_menuController(Context context) {
        this.context = context;
        this.mDatabase = FirebaseDatabase.getInstance().getReference(); // Initialize Firebase reference
        this.productList = new ArrayList<>(); // Initialize the product list
    }

    /**
     * Method to register a new product.
     */
    public void DK_SP(SanPham sanPham) {
        // Push a new product to Firebase
        mDatabase.child("SanPham").push().setValue(sanPham)
                .addOnSuccessListener(aVoid -> Log.d("QL_menuController", "Product added successfully"))
                .addOnFailureListener(e -> Log.e("QL_menuController", "Failed to add product", e));
    }

    /**
     * Fetches all product categories from Firebase.
     */
    public void getDanhMucSp(final DanhMucListener listener) {
        mDatabase.child("Danh_muc").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<DanhMuc> danhMucList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DanhMuc danhMuc = snapshot.getValue(DanhMuc.class);
                    if (danhMuc != null) {
                        danhMucList.add(danhMuc);
                    }
                }
                // Callback the listener with the loaded categories
                listener.onDanhMucLoaded(danhMucList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("QL_menuController", "Failed to load categories: " + databaseError.getMessage());
            }
        });
    }

    public interface DanhMucListener {
        void onDanhMucLoaded(List<DanhMuc> danhMucList);
    }

    /**
     * Updates an existing product.
     */
    public void updateProduct(SanPham sanPham) {
        // Get the product ID from the SanPham object
        String productId = String.valueOf(sanPham.getMaSanPham());
        // Update the product data in the database
        mDatabase.child("SanPham").child(productId).setValue(sanPham)
                .addOnSuccessListener(aVoid -> {
                    Log.d("QL_menuController", "Product updated successfully");
                    // You can optionally trigger a callback or notification here
                })
                .addOnFailureListener(e -> {
                    Log.e("QL_menuController", "Failed to update product", e);
                    // Handle the error case, e.g., show an error message to the user
                });
    }

    /**
     * Deletes a product from Firebase by its ID.
     */
    public void deleteProduct(int productId) {
        mDatabase.child("SanPham").child(String.valueOf(productId)).removeValue()
                .addOnSuccessListener(aVoid -> Log.d("QL_menuController", "Product deleted successfully"))
                .addOnFailureListener(e -> Log.e("QL_menuController", "Failed to delete product", e));
    }

    /**
     * Fetches all products from Firebase and stores them in the product list.
     *
     * @return
     */
    public void getProductList(ListCallback callback) {
        DatabaseReference productsRef = mDatabase.child("SanPham");
        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<SanPham> products = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    if (sanPham != null) {
                        products.add(sanPham);
                    }
                }
                callback.onProductListLoaded(products);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("QL_menuController", "Failed to load product list: " + error.getMessage());
                callback.onProductListLoaded(new ArrayList<>());
            }
        });
    }

    public interface ListCallback {
        void onProductListLoaded(List<SanPham> products);
    }

    /**
     * Gets the count of products in the Firebase database.
     */
    public void getProductCount(final ProductCountCallback callback) {
        DatabaseReference productsRef = mDatabase.child("SanPham"); // Reference to products in Firebase
        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Count the number of products (children)
                int productCount = (int) dataSnapshot.getChildrenCount();
                // Pass the product count to the callback
                callback.onProductCountReceived(productCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
                Log.e("FirebaseError", "Error: " + databaseError.getMessage());
                callback.onProductCountReceived(0); // Return 0 if there was an error
            }
        });
    }

    public interface ProductCountCallback {
        void onProductCountReceived(int count);
    }
}
