package com.example.dacn.Controller;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dacn.Model.NhanVien;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QL_NV_Controller {
    private Context context;
    private DatabaseReference mDatabase;
    private List<NhanVien> nhanVienList;

    public QL_NV_Controller(Context context) {
        this.context = context;
        this.mDatabase = FirebaseDatabase.getInstance().getReference(); // Initialize Firebase reference
        this.nhanVienList = new ArrayList<>(); // Initialize the employee list
    }

    /**
     * Method to register a new employee.
     */
    public void DK_NV(NhanVien nhanVien) {
        // Fetch the current count of employees
        getEmployeeCount(count -> {
            // Generate a new employee ID based on the count
            String newEmployeeId = String.valueOf((count + 1));
            nhanVien.setId(newEmployeeId);
            // Push the new employee to Firebase
            mDatabase.child("NhanVien").child(newEmployeeId).setValue(nhanVien)
                    .addOnSuccessListener(aVoid -> Log.d("QL_NV_Controller", "Employee added successfully"))
                    .addOnFailureListener(e -> Log.e("QL_NV_Controller", "Failed to add employee", e));
        });
    }

    /**
     * Updates an existing employee.
     */
    public void updateNhanVien(NhanVien nhanVien, String id) {
        Log.d("QL_NV_Controller", "Updating employee with ID: " + id);
        // Update the employee data in the database using the provided id
        mDatabase.child("NhanVien").child(id).setValue(nhanVien)
                .addOnSuccessListener(aVoid -> Log.d("QL_NV_Controller", "Employee updated successfully"))
                .addOnFailureListener(e -> Log.e("QL_NV_Controller", "Failed to update employee", e));
    }

    /**
     * Deletes an employee from Firebase by its ID.
     */
    public void deleteNhanVien(String id) {
        mDatabase.child("NhanVien").child(id).removeValue()
                .addOnSuccessListener(aVoid -> Log.d("QL_NV_Controller", "Employee deleted successfully"))
                .addOnFailureListener(e -> Log.e("QL_NV_Controller", "Failed to delete employee", e));
    }

    /**
     * Fetches all employees from Firebase.
     */
    public void getNhanVienList(final NhanVienListener listener) {
        mDatabase.child("NhanVien").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<NhanVien> nhanVienList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NhanVien nhanVien = snapshot.getValue(NhanVien.class);
                    if (nhanVien != null) {
                        nhanVienList.add(nhanVien);
                    }
                }
                // Callback the listener with the loaded employees
                listener.onNhanVienLoaded(nhanVienList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("QL_NV_Controller", "Failed to load employees: " + databaseError.getMessage());
            }
        });
    }

    /**
     * Fetches the number of employees from Firebase.
     */
    public void getEmployeeCount(final EmployeeCountListener listener) {
        mDatabase.child("NhanVien").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                listener.onEmployeeCountReceived(count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("QL_NV_Controller", "Failed to get employee count: " + databaseError.getMessage());
            }
        });
    }

    public interface NhanVienListener {
        void onNhanVienLoaded(List<NhanVien> nhanVienList);
    }

    public interface EmployeeCountListener {
        void onEmployeeCountReceived(long count);
    }
}