package com.example.dacn.Controller;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dacn.Model.DanhMuc;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class catalogueController {
    private Context context;
    private DatabaseReference mDatabase;
    private List<DanhMuc> danhMucList;

    public catalogueController(Context context) {
        this.context = context;
        this.mDatabase = FirebaseDatabase.getInstance().getReference("Danh_muc");
        this.danhMucList = new ArrayList<>();
    }

    /**
     * Fetches all categories from Firebase.
     */
    public void getAllDanhMuc(final DanhMucListener listener) {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                danhMucList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DanhMuc danhMuc = snapshot.getValue(DanhMuc.class);
                    if (danhMuc != null) {
                        danhMucList.add(danhMuc);
                    }
                }
                listener.onDanhMucLoaded(danhMucList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("QL_DanhMucController", "Failed to load categories: " + databaseError.getMessage());
            }
        });
    }

    /**
     * Adds a new category to Firebase.
     */
    public void addDanhMuc(String tenDanhMuc) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int nextMaDanhMuc = 1; // Start with 1 if no categories exist
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DanhMuc existingDanhMuc = snapshot.getValue(DanhMuc.class);
                    if (existingDanhMuc != null) {
                        int maDanhMuc = existingDanhMuc.getMaDanhMuc();
                        try {
                            int currentMaDanhMuc = maDanhMuc;
                            if (currentMaDanhMuc >= nextMaDanhMuc) {
                                nextMaDanhMuc = currentMaDanhMuc + 1;
                            }
                        } catch (NumberFormatException e) {
                            Log.w("catalogueController", "MaDanhMuc not an integer: " + maDanhMuc);
                        }
                    }
                }

                int newMaDanhMuc = nextMaDanhMuc;
                Map<String, Object> danhMucValues = new HashMap<>();
                danhMucValues.put("maDanhMuc", newMaDanhMuc);
                danhMucValues.put("tenDanhMuc", tenDanhMuc);

                mDatabase.child(String.valueOf(newMaDanhMuc)).setValue(danhMucValues);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("QL_DanhMucController", "Failed to get last category ID: " + databaseError.getMessage());
            }
        });
    }

    /**
     * Updates an existing category in Firebase.
     */
    public void updateDanhMuc(DanhMuc danhMuc) {
        mDatabase.child(String.valueOf(danhMuc.getMaDanhMuc())).child("tenDanhMuc").setValue(danhMuc.getTenDanhMuc()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("QL_DanhMucController", "Category updated successfully");
            } else {
                Log.e("QL_DanhMucController", "Failed to update category", task.getException());
            }
        });
    }

    /**
     * Deletes a category from Firebase by its ID.
     */
    public void deleteDanhMuc(int maDanhMuc) {
        mDatabase.child(String.valueOf(maDanhMuc)).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("QL_DanhMucController", "Category deleted successfully");
            } else {
                Log.e("QL_DanhMucController", "Failed to delete category", task.getException());
            }
        });
    }

    public interface DanhMucListener {
        void onDanhMucLoaded(List<DanhMuc> danhMucList);
    }
}