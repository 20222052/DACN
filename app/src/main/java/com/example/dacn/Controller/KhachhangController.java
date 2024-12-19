package com.example.dacn.Controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dacn.Model.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class KhachhangController {
    private DatabaseReference mDatabase;
    public KhachhangController() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference("SanPham");
    }
    public interface SanPhamListener {
        void onSanPhamLoaded(List<SanPham> sanPhamList);
        void onError(String errorMessage);
    }
    public void loadSanPhamDetails(final SanPhamListener listener) {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<SanPham> productList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SanPham product = snapshot.getValue(SanPham.class);
                    if (product != null && product.getTrangThai()) { // Kiểm tra trạng thái sản phẩm
                        productList.add(product); // Chỉ thêm sản phẩm có trạng thái true
                    }
                }
                listener.onSanPhamLoaded(productList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase", "loadProduct:onCancelled", databaseError.toException());
                listener.onError(databaseError.getMessage());
            }
        });
    }
}
