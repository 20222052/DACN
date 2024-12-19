package com.example.dacn.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.dacn.Controller.QL_TK_Adapter;
import com.example.dacn.Controller.QL_TK_Controller;
import com.example.dacn.Controller.QL_NV_Controller;
import com.example.dacn.Model.TaiKhoan;
import com.example.dacn.Model.NhanVien;
import com.example.dacn.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QL_TK_Fragment extends Fragment {

    private QL_TK_Controller controller;
    private QL_NV_Controller nvController;
    private EditText edtTenDangNhap, edtMatKhau;
    private AutoCompleteTextView edtNhanVien;
    private Spinner spinnerVaiTro;
    private SearchView search_account;
    private ListView listViewTaiKhoan;
    private Button btnThem, btnSua, btnXoa;
    private List<TaiKhoan> taiKhoanList;
    private QL_TK_Adapter taiKhoanAdapter;
    private String selectedTaiKhoanId;
    private List<String> nhanVienNames;
    private Map<String, String> nhanVienNameToIdMap;
    private Map<String, String> nhanVienIdToNameMap;

    public QL_TK_Fragment() {
        // Required empty public constructor
    }

    public static QL_TK_Fragment newInstance() {
        return new QL_TK_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new QL_TK_Controller(getContext());
        nvController = new QL_NV_Controller(getContext());
        taiKhoanList = new ArrayList<>();
        nhanVienNames = new ArrayList<>();
        nhanVienNameToIdMap = new HashMap<>();
        nhanVienIdToNameMap = new HashMap<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_l__t_k_, container, false);

        initializeViews(view);
        setupButtonListeners();
        setupSearchView();
        loadTaiKhoanList();
        loadNhanVienNames();

        return view;
    }

    private void initializeViews(View view) {
        edtTenDangNhap = view.findViewById(R.id.edt_ten_dang_nhap);
        edtMatKhau = view.findViewById(R.id.edt_mat_khau);
        edtNhanVien = view.findViewById(R.id.edt_nhan_vien);
        spinnerVaiTro = view.findViewById(R.id.spinner_vai_tro);
        listViewTaiKhoan = view.findViewById(R.id.listview_TK);
        search_account = view.findViewById(R.id.search_account);
        btnThem = view.findViewById(R.id.btn_them);
        btnSua = view.findViewById(R.id.btn_sua);
        btnXoa = view.findViewById(R.id.btn_xoa);
    }

    private void setupButtonListeners() {
        btnThem.setOnClickListener(v -> {
            if (validateInputFields()) {
                TaiKhoan taiKhoan = createTaiKhoanFromInput();
                controller.themTaiKhoan(taiKhoan);
                loadTaiKhoanList(); // Refresh list after adding
            }
        });

        btnSua.setOnClickListener(v -> {
            if (validateInputFields() && selectedTaiKhoanId != null) {
                TaiKhoan taiKhoan = createTaiKhoanFromInput();
                taiKhoan.setMaTaiKhoan(selectedTaiKhoanId); // Ensure the ID is set
                controller.suaTaiKhoan(taiKhoan);
                loadTaiKhoanList(); // Refresh list after editing
            } else {
                showToast("No account selected for editing");
            }
        });

        btnXoa.setOnClickListener(v -> {
            if (selectedTaiKhoanId != null) {
                controller.xoaTaiKhoan(selectedTaiKhoanId);
                loadTaiKhoanList(); // Refresh list after deleting
            } else {
                showToast("No account selected for deletion");
            }
        });

        listViewTaiKhoan.setOnItemClickListener((parent, view, position, id) -> {
            TaiKhoan selectedTaiKhoan = taiKhoanList.get(position);
            selectedTaiKhoanId = selectedTaiKhoan.getMaTaiKhoan(); // Ensure the ID is set
            populateFormWithTaiKhoan(selectedTaiKhoan);
        });
    }

    private void setupSearchView() {
        search_account.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        List<TaiKhoan> filteredList = new ArrayList<>();
        for (TaiKhoan item : taiKhoanList) {
            if (item.getTenDangNhap().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        taiKhoanAdapter.updateList(filteredList);
    }

    private void loadTaiKhoanList() {
        controller.getAllTaiKhoan(new QL_TK_Controller.TaiKhoanListener() {
            @Override
            public void onTaiKhoanLoaded(List<TaiKhoan> taiKhoanList) {
                if (isAdded()) { // Check if the fragment is attached to the activity
                    QL_TK_Fragment.this.taiKhoanList = taiKhoanList;
                    taiKhoanAdapter = new QL_TK_Adapter(requireContext(), taiKhoanList, nhanVienIdToNameMap);
                    listViewTaiKhoan.setAdapter(taiKhoanAdapter);
                }
            }
        });
    }

    private void loadNhanVienNames() {
        nvController.getNhanVienList(new QL_NV_Controller.NhanVienListener() {
            @Override
            public void onNhanVienLoaded(List<NhanVien> nhanVienList) {
                if (isAdded()) { // Check if the fragment is attached to the activity
                    for (NhanVien nhanVien : nhanVienList) {
                        nhanVienNames.add(nhanVien.getTenNhanVien());
                        nhanVienNameToIdMap.put(nhanVien.getTenNhanVien(), nhanVien.getId());
                        nhanVienIdToNameMap.put(nhanVien.getId(), nhanVien.getTenNhanVien());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, nhanVienNames);
                    edtNhanVien.setAdapter(adapter);
                }
            }
        });
    }

    private boolean validateInputFields() {
        String tenDangNhap = edtTenDangNhap.getText().toString().trim();
        String matKhau = edtMatKhau.getText().toString().trim();
        String nhanVien = edtNhanVien.getText().toString().trim();

        if (tenDangNhap.isEmpty()) {
            showToast("Tên đăng nhập không được để trống");
            return false;
        }

        if (matKhau.isEmpty()) {
            showToast("Mật khẩu không được để trống");
            return false;
        }

        if (nhanVien.isEmpty()) {
            showToast("Nhân viên không được để trống");
            return false;
        }

        return true;
    }

    private TaiKhoan createTaiKhoanFromInput() {
        String tenDangNhap = edtTenDangNhap.getText().toString().trim();
        String matKhau = edtMatKhau.getText().toString().trim();
        String nhanVien = edtNhanVien.getText().toString().trim();
        String vaiTro = spinnerVaiTro.getSelectedItem().toString();

        String nhanVienId = nhanVienNameToIdMap.get(nhanVien);

        return new TaiKhoan(null, tenDangNhap, matKhau, nhanVienId, vaiTro); // ID will be set later
    }

    private void populateFormWithTaiKhoan(TaiKhoan taiKhoan) {
        edtTenDangNhap.setText(taiKhoan.getTenDangNhap());
        edtMatKhau.setText(taiKhoan.getMatKhau());
        edtNhanVien.setText(nhanVienIdToNameMap.get(taiKhoan.getNhanVienId()));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.vai_tro_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVaiTro.setAdapter(adapter);
        if (taiKhoan.getVaiTro() != null) {
            int spinnerPosition = adapter.getPosition(taiKhoan.getVaiTro());
            spinnerVaiTro.setSelection(spinnerPosition);
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}