package com.example.dacn.View;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dacn.Controller.ChiTietDonHangAdapter;
import com.example.dacn.Controller.QL_DH_Adapter;
import com.example.dacn.Controller.QL_DHController;
import com.example.dacn.Controller.QL_NV_Controller;
import com.example.dacn.Model.ChiTietDonHang;
import com.example.dacn.Model.DonHang;
import com.example.dacn.Model.NhanVien;
import com.example.dacn.Model.SanPham;
import com.example.dacn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QL_DH_Fragment extends Fragment {
    private QL_DHController controller;
    private QL_NV_Controller nvController;
    private GridView gridViewDonHang;
    private QL_DH_Adapter donHangAdapter;
    private List<DonHang> donHangList;
    private SearchView searchView;
    private Spinner spinnerFilter;
    private Button btnChiTiet, btnTimKiem, btnXoa;
    private int selectedDonHangId = -1;
    private Map<Integer, String> nhanVienIdToNameMap;
    private List<ChiTietDonHang> chiTietDonHangList;
    private ChiTietDonHangAdapter chiTietDonHangAdapter;
    private Map<Integer, SanPham> sanPhamMap;

    public QL_DH_Fragment() {
        // Required empty public constructor
    }

    public static QL_DH_Fragment newInstance() {
        return new QL_DH_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new QL_DHController(getContext());
        nvController = new QL_NV_Controller(getContext());
        donHangList = new ArrayList<>();
        nhanVienIdToNameMap = new HashMap<>();
        chiTietDonHangList = new ArrayList<>();
        sanPhamMap = new HashMap<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_l__d_h_, container, false);

        initializeViews(view);
        setupButtonListeners();
        loadNhanVienNames();
        loadSanPhamDetails();
        loadDonHangList();

        return view;
    }

    private void initializeViews(View view) {
        gridViewDonHang = view.findViewById(R.id.gridview_don_hang);
        searchView = view.findViewById(R.id.search_employee);
        spinnerFilter = view.findViewById(R.id.tv_filter);
        btnChiTiet = view.findViewById(R.id.btn_chi_tiet);
        btnTimKiem = view.findViewById(R.id.btn_tim_kiem);
        btnXoa = view.findViewById(R.id.btn_xoa);
    }

    private void setupButtonListeners() {
        btnChiTiet.setOnClickListener(v -> {
            if (selectedDonHangId != -1) {
                controller.getChiTietDonHangByMaDonHang(selectedDonHangId, new QL_DHController.ChiTietDonHangListener() {
                    @Override
                    public void onChiTietDonHangLoaded(List<ChiTietDonHang> chiTietDonHangList) {
                        QL_DH_Fragment.this.chiTietDonHangList = chiTietDonHangList;
                        showChiTietDonHangDialog();
                    }
                });
            } else {
                showToast("No order selected for details");
            }
        });

        btnTimKiem.setOnClickListener(v -> {
            String query = searchView.getQuery().toString().trim();
            if (!query.isEmpty()) {
                filterDonHangList(query);
            } else {
                showToast("Please enter a search query");
            }
        });

        btnXoa.setOnClickListener(v -> {
            if (selectedDonHangId != -1) {
                controller.xoaDonHang(selectedDonHangId);
                loadDonHangList(); // Refresh list after deleting
            } else {
                showToast("No order selected for deletion");
            }
        });

        gridViewDonHang.setOnItemClickListener((parent, view, position, id) -> {
            DonHang selectedDonHang = donHangList.get(position);
            selectedDonHangId = selectedDonHang.getMaDonHang(); // Ensure the ID is set
        });
    }

    private void loadDonHangList() {
        controller.getAllDonHang(new QL_DHController.DonHangListener() {
            @Override
            public void onDonHangLoaded(List<DonHang> donHangList) {
                QL_DH_Fragment.this.donHangList = donHangList;
                donHangAdapter = new QL_DH_Adapter(requireContext(), donHangList, nhanVienIdToNameMap);
                gridViewDonHang.setAdapter(donHangAdapter);
            }
        });
    }

    private void loadNhanVienNames() {
        nvController.getNhanVienList(new QL_NV_Controller.NhanVienListener() {
            @Override
            public void onNhanVienLoaded(List<NhanVien> nhanVienList) {
                for (NhanVien nhanVien : nhanVienList) {
                    nhanVienIdToNameMap.put(Integer.valueOf(nhanVien.getId()), nhanVien.getTenNhanVien());
                }
                loadDonHangList(); // Load orders after loading employee names
            }
        });
    }

    private void loadSanPhamDetails() {
        DatabaseReference sanPhamRef = FirebaseDatabase.getInstance().getReference("SanPham");
        sanPhamRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SanPham sanPham = snapshot.getValue(SanPham.class);
                    if (sanPham != null) {
                        sanPhamMap.put(sanPham.getMaSanPham(), sanPham);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("QL_DH_Fragment", "Failed to load product details: " + databaseError.getMessage());
            }
        });
    }

    private void filterDonHangList(String query) {
        List<DonHang> filteredList = new ArrayList<>();
        for (DonHang donHang : donHangList) {
            if (String.valueOf(donHang.getMaNhanVien()).contains(query)) {
                filteredList.add(donHang);
            }
        }
        donHangAdapter = new QL_DH_Adapter(requireContext(), filteredList, nhanVienIdToNameMap);
        gridViewDonHang.setAdapter(donHangAdapter);
    }

    private void showChiTietDonHangDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Chi tiết đơn hàng");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_chi_tiet_don_hang, null);
        ListView listView = dialogView.findViewById(R.id.listview_chi_tiet_don_hang);
        chiTietDonHangAdapter = new ChiTietDonHangAdapter(requireContext(), chiTietDonHangList, sanPhamMap);
        listView.setAdapter(chiTietDonHangAdapter);

        builder.setView(dialogView);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}