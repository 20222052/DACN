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

public class Add_productController {
    private Context context;
    private List<SanPham> productList;
    private DatabaseReference mDatabase;

    public Add_productController(Context context) {
        this.context = context;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.productList = new ArrayList<>();
    }

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
                Log.e("Add_productController", "Failed to load categories: " + databaseError.getMessage());
            }
        });
    }

    public void addProduct(SanPham sanPham, final ProductAddListener listener) {
        String productId = String.valueOf(sanPham.getMaSanPham());
        mDatabase.child("SanPham").child(productId).setValue(sanPham)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onProductAdded();
                    } else {
                        listener.onProductAddFailed(task.getException());
                    }
                });
    }
    public void getProductCount(final QL_menuController.ProductCountCallback callback) {
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
    public interface DanhMucListener {
        void onDanhMucLoaded(List<DanhMuc> danhMucList);
    }

    public interface ProductAddListener {
        void onProductAdded();
        void onProductAddFailed(Exception e);
    }
}