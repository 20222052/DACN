package com.example.dacn.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import androidx.appcompat.widget.SearchView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dacn.Controller.QL_Table_Adapter;
import com.example.dacn.Controller.QL_Table_Controller;

import com.example.dacn.Model.Table;
import com.example.dacn.R;

import java.util.ArrayList;
import java.util.List;

public class QL_Table_Fragment extends Fragment {
    private EditText edtMaBan, edtTenBan;
    private Button btnThem, btnXoa;
    private SearchView searchTable;
    private GridView gridViewTable;
    private QL_Table_Controller qlTBController;
    private List<Table> tableList;
    private QL_Table_Adapter tableAdapter;
    private String selectedTableId;

    public QL_Table_Fragment() {
    }

    public static QL_Table_Fragment newInstance() {
        return new QL_Table_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qlTBController = new QL_Table_Controller(requireContext());
        tableList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ql_table, container, false);

        initializeViews(view);
        setupButtonListeners();
        setupSearchView();
        loadTableList();
        return view;
    }

    @SuppressLint("WrongViewCast")
    private void initializeViews(View view) {
        edtMaBan = view.findViewById(R.id.id_Table);
        edtTenBan = view.findViewById(R.id.nameTB);
        btnThem = view.findViewById(R.id.btn_them);
        btnXoa = view.findViewById(R.id.btn_xoa);
        searchTable = view.findViewById(R.id.search_table);
        gridViewTable = view.findViewById(R.id.gridview_table);
    }

    private void setupButtonListeners() {
        btnThem.setOnClickListener(v -> {
            if (validateInputFields()) {
                Table table = createTableFromInput();
                qlTBController.DK_TB(table);
                loadTableList(); // Refresh list after adding
            }
        });

        btnXoa.setOnClickListener(v -> {
            if (selectedTableId != null) {
                qlTBController.deleteTable(selectedTableId);
                loadTableList(); // Refresh list after deleting
            } else {
                showToast("No employee selected for deletion");
            }
        });

        gridViewTable.setOnItemClickListener((parent, view, position, id) -> {
            Table selectedTable = tableList.get(position);
            selectedTableId = String.valueOf(selectedTable.getIdTable()); // Ensure the ID is set
//            populateFormWithNhanVien(selectedTable);
        });
    }

    private void setupSearchView() {
        searchTable.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    private void filter(String text) {
        List<Table> filteredList = new ArrayList<>();
        for (Table tb : tableList) {
            if (tb.getNameTable().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(tb);
            }
        }
        tableAdapter.updateList(filteredList);
    }

    private void loadTableList() {
        qlTBController.getTableList(new QL_Table_Controller.TableListener() {
            @Override
            public void onTableLoaded(List<Table> tableList) {
                QL_Table_Fragment.this.tableList = tableList;
                tableAdapter = new QL_Table_Adapter(requireContext(), tableList);
                gridViewTable.setAdapter(tableAdapter);
            }
        });
    }

    private boolean validateInputFields() {
        String tenBan = edtTenBan.getText().toString().trim();

        if (tenBan.isEmpty()) {
            showToast("Tên bàn không được để trống");
            return false;
        }
        Log.e("QL_Table_Fragment", "Ten ban: ");

        return true;
    }

    private Table createTableFromInput() {
        int maBan = Integer.parseInt(edtMaBan.getText().toString().trim());
        String tenBan = edtTenBan.getText().toString().trim();

        return new Table(maBan, tenBan, false); // ID will be set later
    }

//    private void populateFormWithNhanVien(Table table) {
//        edtMaBan.setText(table.getIdTable());
//        edtTenBan.setText(table.getNameTable());
//    }


    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}