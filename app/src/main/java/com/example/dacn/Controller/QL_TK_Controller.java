package com.example.dacn.Controller;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dacn.Model.TaiKhoan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QL_TK_Controller {
    private Context context;
    private DatabaseReference databaseReference;
    private List<TaiKhoan> taiKhoanList;

    public QL_TK_Controller(Context context) {
        this.context = context;
        this.databaseReference = FirebaseDatabase.getInstance().getReference("TaiKhoan");
        this.taiKhoanList = new ArrayList<>();
    }

    /**
     * Method to add a new account.
     */
    public void themTaiKhoan(TaiKhoan taiKhoan) {
        String id = databaseReference.push().getKey();
        if (id != null) {
            taiKhoan.setMaTaiKhoan(id);
            databaseReference.child(id).setValue(taiKhoan)
                    .addOnSuccessListener(aVoid -> Log.d("QL_TK_Controller", "Account added successfully"))
                    .addOnFailureListener(e -> Log.e("QL_TK_Controller", "Failed to add account", e));
        }
    }

    /**
     * Fetches all accounts from Firebase.
     */
    public void getAllTaiKhoan(final TaiKhoanListener listener) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                taiKhoanList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                    if (taiKhoan != null) {
                        taiKhoanList.add(taiKhoan);
                    }
                }
                listener.onTaiKhoanLoaded(taiKhoanList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("QL_TK_Controller", "Failed to load accounts: " + databaseError.getMessage());
            }
        });
    }

    /**
     * Updates an existing account.
     */
    public void suaTaiKhoan(TaiKhoan taiKhoan) {
        databaseReference.child(taiKhoan.getMaTaiKhoan()).setValue(taiKhoan)
                .addOnSuccessListener(aVoid -> Log.d("QL_TK_Controller", "Account updated successfully"))
                .addOnFailureListener(e -> Log.e("QL_TK_Controller", "Failed to update account", e));
    }

    /**
     * Deletes an account from Firebase by its ID.
     */
    public void xoaTaiKhoan(String maTaiKhoan) {
        databaseReference.child(maTaiKhoan).removeValue()
                .addOnSuccessListener(aVoid -> Log.d("QL_TK_Controller", "Account deleted successfully"))
                .addOnFailureListener(e -> Log.e("QL_TK_Controller", "Failed to delete account", e));
    }

    /**
     * Finds an account by its ID.
     */
    public TaiKhoan timTaiKhoanTheoMa(String maTaiKhoan) {
        for (TaiKhoan taiKhoan : taiKhoanList) {
            if (taiKhoan.getMaTaiKhoan().equals(maTaiKhoan)) {
                return taiKhoan;
            }
        }
        return null;
    }

    public interface TaiKhoanListener {
        void onTaiKhoanLoaded(List<TaiKhoan> taiKhoanList);
    }
}