package com.example.dacn.Controller;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dacn.Model.ChiTietDonHang;
import com.example.dacn.Model.DonHang;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QL_DHController {
    private Context context;
    private DatabaseReference mDatabase;
    private List<DonHang> donHangList;

    public QL_DHController(Context context) {
        this.context = context;
        this.mDatabase = FirebaseDatabase.getInstance().getReference("Don_Hang");
        this.donHangList = new ArrayList<>();
    }
    /**
     * Fetches all orders from Firebase.
     */
    public void getAllDonHang(final DonHangListener listener) {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                donHangList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DonHang donHang = snapshot.getValue(DonHang.class);
                    if (donHang != null) {
                        donHangList.add(donHang);
                    }
                }
                listener.onDonHangLoaded(donHangList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("QL_DHController", "Failed to load orders: " + databaseError.getMessage());
            }
        });
    }

    /**
     * Fetches order details by order ID from Firebase.
     */
    public void getChiTietDonHangByMaDonHang(int maDonHang, final ChiTietDonHangListener listener) {
        DatabaseReference chiTietDonHangRef = FirebaseDatabase.getInstance().getReference("Chi_Tiet_Don_Hang");
        chiTietDonHangRef.orderByChild("maDonHang").equalTo(maDonHang).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ChiTietDonHang> chiTietDonHangList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChiTietDonHang chiTietDonHang = snapshot.getValue(ChiTietDonHang.class);
                    if (chiTietDonHang != null) {
                        Log.d("QL_DHController", "Loaded order details: " + chiTietDonHang.getMaDonHang());
                        chiTietDonHangList.add(chiTietDonHang);
                    }
                }
                listener.onChiTietDonHangLoaded(chiTietDonHangList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("QL_DHController", "Failed to load order details: " + databaseError.getMessage());
            }
        });
    }

    /**
     * Deletes an order from Firebase by its ID.
     */
    public void xoaDonHang(int id) {
        // Delete order details first
        DatabaseReference chiTietDonHangRef = FirebaseDatabase.getInstance().getReference("Chi_Tiet_Don_Hang");
        chiTietDonHangRef.orderByChild("maDonHang").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("QL_DHController", "Deleting order details: " + snapshot.getKey());
                    snapshot.getRef().removeValue();
                }
                // Delete the order after deleting its details
                mDatabase.child(String.valueOf(id)).removeValue()
                        .addOnSuccessListener(aVoid -> Log.d("QL_DHController", "Order deleted successfully"))
                        .addOnFailureListener(e -> Log.e("QL_DHController", "Failed to delete order", e));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("QL_DHController", "Failed to delete order details: " + databaseError.getMessage());
            }
        });
    }

    public interface DonHangListener {
        void onDonHangLoaded(List<DonHang> donHangList);
    }

    public interface ChiTietDonHangListener {
        void onChiTietDonHangLoaded(List<ChiTietDonHang> chiTietDonHangList);
    }
}