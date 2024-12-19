package com.example.dacn.View;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import androidx.appcompat.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dacn.Controller.QL_NV_Adapter;
import com.example.dacn.Controller.QL_NV_Controller;
import com.example.dacn.Model.NhanVien;
import com.example.dacn.R;

import java.util.ArrayList;
import java.util.List;

public class QL_NV_Fragment extends Fragment {
    private EditText edtTenNhanVien, edtSoDienThoai, edtAddress;
    private Spinner spinnerVaiTro;
    private Button btnThem, btnSua, btnXoa;
    private SearchView searchEmployee;
    private GridView gridViewNhanVien;
    private QL_NV_Controller qlNvController;
    private List<NhanVien> nhanVienList;
    private QL_NV_Adapter nhanVienAdapter;
    private String selectedNhanVienId;

    public QL_NV_Fragment() {
    }

    public static QL_NV_Fragment newInstance() {
        return new QL_NV_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qlNvController = new QL_NV_Controller(requireContext());
        nhanVienList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qlnv, container, false);

        initializeViews(view);
        setupButtonListeners();
        setupSearchView();
        loadNhanVienList();
        setTextWatcher();
        return view;
    }

    private void initializeViews(View view) {
        edtTenNhanVien = view.findViewById(R.id.edt_ten_nhan_vien);
        edtSoDienThoai = view.findViewById(R.id.edt_so_dien_thoai);
        edtAddress = view.findViewById(R.id.edt_address);
        spinnerVaiTro = view.findViewById(R.id.spinner_vai_tro);
        btnThem = view.findViewById(R.id.btn_them);
        btnSua = view.findViewById(R.id.btn_sua);
        btnXoa = view.findViewById(R.id.btn_xoa);
        searchEmployee = view.findViewById(R.id.search_employee);
        gridViewNhanVien = view.findViewById(R.id.gridview_nhan_vien);
    }

    private void setTextWatcher() {
        edtTenNhanVien.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (input.length() < 5) {
                    edtTenNhanVien.setError("Nhập tối thiểu 5 kí tự");
                }
            }
        });

        edtSoDienThoai.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (input.length() < 5 || input.length() > 15 || !input.matches("\\d+") || input.charAt(0) != '0') {
                    edtSoDienThoai.setError("Nhập đúng định dạng!");
                }
            }
        });

        edtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (input.length() < 10) {
                    edtAddress.setError("Nhập đúng định dạng.");
                }
            }
        });
    }

    private void setupButtonListeners() {
        btnThem.setOnClickListener(v -> {
            if (validateInputFields()) {
                NhanVien nhanVien = createNhanVienFromInput();
                qlNvController.DK_NV(nhanVien);
                loadNhanVienList(); // Refresh list after adding
            }
        });

        btnSua.setOnClickListener(v -> {
            if (validateInputFields() && selectedNhanVienId != null) {
                NhanVien nhanVien = createNhanVienFromInput();
                nhanVien.setId(selectedNhanVienId); // Ensure the ID is set
                qlNvController.updateNhanVien(nhanVien, selectedNhanVienId);
                loadNhanVienList(); // Refresh list after editing
            } else {
                showToast("No employee selected for editing");
            }
        });

        btnXoa.setOnClickListener(v -> {
            if (selectedNhanVienId != null) {
                qlNvController.deleteNhanVien(selectedNhanVienId);
                loadNhanVienList(); // Refresh list after deleting
            } else {
                showToast("No employee selected for deletion");
            }
        });

        gridViewNhanVien.setOnItemClickListener((parent, view, position, id) -> {
            NhanVien selectedNhanVien = nhanVienList.get(position);
            selectedNhanVienId = selectedNhanVien.getId(); // Ensure the ID is set
            populateFormWithNhanVien(selectedNhanVien);
        });
    }

    private void setupSearchView() {
        searchEmployee.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        List<NhanVien> filteredList = new ArrayList<>();
        for (NhanVien item : nhanVienList) {
            if (item.getTenNhanVien().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        nhanVienAdapter.updateList(filteredList);
    }

    private void loadNhanVienList() {
        qlNvController.getNhanVienList(new QL_NV_Controller.NhanVienListener() {
            @Override
            public void onNhanVienLoaded(List<NhanVien> nhanVienList) {
                if (isAdded()) { // Check if the fragment is attached to the activity
                    QL_NV_Fragment.this.nhanVienList = nhanVienList;
                    nhanVienAdapter = new QL_NV_Adapter(requireContext(), nhanVienList);
                    gridViewNhanVien.setAdapter(nhanVienAdapter);
                }
            }
        });
    }

    private boolean validateInputFields() {
        String tenNhanVien = edtTenNhanVien.getText().toString().trim();
        String soDienThoai = edtSoDienThoai.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        if (tenNhanVien.isEmpty()) {
            showToast("Tên nhân viên không được để trống");
            return false;
        }

        if (soDienThoai.isEmpty()) {
            showToast("Số điện thoại không được để trống");
            return false;
        }

        if (address.isEmpty()) {
            showToast("Địa chỉ không được để trống");
            return false;
        }

        return true;
    }

    private NhanVien createNhanVienFromInput() {
        String tenNhanVien = edtTenNhanVien.getText().toString().trim();
        String soDienThoai = edtSoDienThoai.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String vaiTro = spinnerVaiTro.getSelectedItem().toString();

        return new NhanVien(null, tenNhanVien, soDienThoai, address, vaiTro); // ID will be set later
    }

    private void populateFormWithNhanVien(NhanVien nhanVien) {
        edtTenNhanVien.setText(nhanVien.getTenNhanVien());
        edtSoDienThoai.setText(nhanVien.getSoDienThoai());
        edtAddress.setText(nhanVien.getAddress());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.chuc_vu_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVaiTro.setAdapter(adapter);
        if (nhanVien.getVaiTro() != null) {
            int spinnerPosition = adapter.getPosition(nhanVien.getVaiTro());
            spinnerVaiTro.setSelection(spinnerPosition);
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}