package com.example.dacn.Controller;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dacn.Model.NhanVien;
import com.example.dacn.Model.Table;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QL_Table_Controller {
    private Context context;
    private DatabaseReference mDatabase;
    private List<Table> tableList;

    public QL_Table_Controller(Context context) {
        this.context = context;
        this.mDatabase = FirebaseDatabase.getInstance().getReference(); // Initialize Firebase reference
        this.tableList = new ArrayList<>(); // Initialize the employee list
    }

    /**
     * Method to register a new employee.
     */
    public void DK_TB(Table table) {
        // Fetch the current count of employees
        getTableCount(count -> {
            // Generate a new employee ID based on the count
            String newTableId = String.valueOf((count + 1));
            table.setIdTable(Integer.parseInt(newTableId));
            // Push the new employee to Firebase
            mDatabase.child("Table").child(newTableId).setValue(table)
                    .addOnSuccessListener(aVoid -> Log.d("QL_Table_Controller", "Table added successfully"))
                    .addOnFailureListener(e -> Log.e("QL_Table_Controller", "Failed to add table", e));
        });
    }

    /**
     * Deletes an employee from Firebase by its ID.
     */
    public void deleteTable(String id) {
        mDatabase.child("Table").child(id).removeValue()
                .addOnSuccessListener(aVoid -> Log.d("QL_Table_Controller", "Employee deleted successfully"))
                .addOnFailureListener(e -> Log.e("QL_Table_Controller", "Failed to delete employee", e));
    }

    /**
     * Fetches all employees from Firebase.
     */
    public void getTableList(final TableListener listener) {
        mDatabase.child("Table").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Table> tableList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Table table = snapshot.getValue(Table.class);
                    if (table != null) {
                        tableList.add(table);
                    }
                }
                // Callback the listener with the loaded employees
                listener.onTableLoaded(tableList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("QL_Table_Controller", "Failed to load employees: " + databaseError.getMessage());
            }
        });
    }

    /**
     * Fetches the number of employees from Firebase.
     */
    public void getTableCount(final TableCountListener listener) {
        mDatabase.child("Table").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                listener.onTableCountReceived(count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("QL_Table_Controller", "Failed to get table count: " + databaseError.getMessage());
            }
        });
    }

    public interface TableListener {
        void onTableLoaded(List<Table> tableList);
    }

    public interface TableCountListener {
        void onTableCountReceived(long count);
    }
}