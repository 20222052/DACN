package com.example.dacn.View;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class QL_DH_Fragment extends Fragment {
    private QL_DHController controller;
    private QL_NV_Controller nvController;
    private GridView gridViewDonHang;
    private QL_DH_Adapter donHangAdapter;
    private List<DonHang> donHangList;
    private EditText search_DH;
    private Spinner spinnerFilter, spinnerStatus;
    private Button btnChiTiet, btnXoa, btn_tim_kiem;
    private int selectedDonHangId = -1;
    private Map<Integer, String> nhanVienIdToNameMap;
    private List<ChiTietDonHang> chiTietDonHangList;
    private ChiTietDonHangAdapter chiTietDonHangAdapter;
    private Map<Integer, SanPham> sanPhamMap;
    private Map<Integer, String> tableIdToNameMap;

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
        tableIdToNameMap = new HashMap<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_l__d_h_, container, false);

        initializeViews(view);
        setupButtonListeners();
        loadNhanVienNames(() -> {
            if (!nhanVienIdToNameMap.isEmpty()) {
                loadTableNames(this::loadDonHangList);
            } else {
                Log.e("QL_DH_Fragment", "Employee data not loaded");
                // Optionally, handle this scenario, like showing an error message to the user
            }
        });        loadSanPhamDetails();
        setupSpinnerListeners();
        return view;
    }

    private void initializeViews(View view) {
        gridViewDonHang = view.findViewById(R.id.gridview_don_hang);
        search_DH = view.findViewById(R.id.search_DH);
        btnChiTiet = view.findViewById(R.id.btn_chi_tiet);
        btn_tim_kiem = view.findViewById(R.id.btn_tim_kiem);
        spinnerFilter = view.findViewById(R.id.sort_date); // Add this line
        spinnerStatus = view.findViewById(R.id.status); // Add this line
        btnXoa = view.findViewById(R.id.btn_xoa);
        search_DH.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @SuppressLint("DefaultLocale")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");
                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;
                    if (clean.length() < 8){
                        String ddmmyyyy = "DDMMYYYY";
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));
                        mon = mon < 1 ? 1 : Math.min(mon, 12);
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:Math.min(year, 2100);
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would fail, but with year set first, it's OK
                        day = Math.min(day, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                        clean = String.format("%02d%02d%04d",day, mon, year);
                    }
                    clean = String.format("%s-%s-%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));
                    sel = Math.max(sel, 0);
                    current = clean;
                    search_DH.setText(current);
                    search_DH.setSelection(Math.min(sel, current.length()));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupSpinnerListeners() {
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFiltersAndSort();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFiltersAndSort();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void applyFiltersAndSort() {
        String selectedStatus = spinnerStatus.getSelectedItem().toString();
        String selectedSortOption = spinnerFilter.getSelectedItem().toString();

        List<DonHang> filteredList = filterDonHangListByStatus(selectedStatus);
        List<DonHang> sortedList = sortDonHangList(filteredList, selectedSortOption);

        donHangAdapter.updateList(sortedList);
    }

    private List<DonHang> filterDonHangListByStatus(String option) {
        if (option.equals("Đã thanh toán")) {
            return donHangList.stream()
                    .filter(DonHang::isTrangThai)
                    .collect(Collectors.toList());
        } else if (option.equals("Chưa thanh toán")) {
            return donHangList.stream()
                    .filter(donHang -> !donHang.isTrangThai())
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>(donHangList);
        }
    }

    private List<DonHang> sortDonHangList(List<DonHang> list, String option) {
        if (donHangAdapter == null) {
            donHangAdapter = new QL_DH_Adapter(requireContext(), donHangList, nhanVienIdToNameMap, tableIdToNameMap);
            gridViewDonHang.setAdapter(donHangAdapter);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        Comparator<DonHang> comparator = (dh1, dh2) -> {
            try {
                Date date1 = dateFormat.parse(dh1.getNgayDatHang());
                Date date2 = dateFormat.parse(dh2.getNgayDatHang());
                return date1.compareTo(date2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        };

        if (option.equals("Mới nhất")) {
            Collections.sort(list, comparator);
        } else if (option.equals("Cũ nhất")) {
            Collections.sort(list, Collections.reverseOrder(comparator));
        }

        return list;
    }

    private void setupButtonListeners() {
        btnChiTiet.setOnClickListener(v -> {
            if (selectedDonHangId != -1) {
                Log.d("OrderDetail", "Selected Order ID: " + selectedDonHangId);
                controller.getChiTietDonHangByMaDonHang(selectedDonHangId, new QL_DHController.ChiTietDonHangListener() {
                    @Override
                    public void onChiTietDonHangLoaded(List<ChiTietDonHang> chiTietDonHangList) {
                        if (isAdded()) { // Check if the fragment is attached to the activity
                            QL_DH_Fragment.this.chiTietDonHangList = chiTietDonHangList;
                            showChiTietDonHangDialog();
                        }
                    }
                });
            } else {
                showToast("No order selected for details");
            }
        });

        btn_tim_kiem.setOnClickListener(v -> {
            String searchDate = search_DH.getText().toString().trim();

            // Check if the date format is correct
            if (searchDate.matches("\\d{2}-\\d{2}-\\d{4}")) {
                try {
                    // Convert the input to a Date object for comparison
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    Date searchDateObj = sdf.parse(searchDate);

                    // Filter the list based on the date
                    List<DonHang> filteredDonHangList = donHangList.stream()
                            .filter(donHang -> {
                                try {
                                    Date donHangDate = sdf.parse(donHang.getNgayDatHang());
                                    return donHangDate != null && searchDateObj != null &&
                                            sdf.format(donHangDate).equals(sdf.format(searchDateObj));
                                } catch (ParseException e) {
                                    Log.e("QL_DH_Fragment", "Error parsing order date: " + e.getMessage());
                                    return false;
                                }
                            })
                            .collect(Collectors.toList());

                    // Update the adapter with filtered results
                    if (donHangAdapter != null) {
                        donHangAdapter.updateList(filteredDonHangList);
                    } else {
                        Log.e("QL_DH_Fragment", "Adapter is null, cannot update list");
                    }

                } catch (ParseException e) {
                    Log.e("QL_DH_Fragment", "Error parsing search date: " + e.getMessage());
                    showToast("Ngày nhập không hợp lệ");
                }
            } else {
                loadDonHangList();
                showToast("Vui lòng nhập ngày theo định dạng dd-MM-yyyy");
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
                if (isAdded()) { // Check if the fragment is attached to the activity
                    QL_DH_Fragment.this.donHangList = donHangList;
                    donHangAdapter = new QL_DH_Adapter(requireContext(), donHangList, nhanVienIdToNameMap, tableIdToNameMap);
                    gridViewDonHang.setAdapter(donHangAdapter);
                    spinnerFilter.post(() -> {
                        spinnerFilter.setSelection(0);
                        applyFiltersAndSort();
                    });
                }
            }
        });
    }

    private void loadNhanVienNames(Runnable callback) {
        controller.loadNhanVienNames(new QL_DHController.NhanVienNameListener() {
            @Override
            public void onNhanVienNamesLoaded(Map<Integer, String> nhanVienMap) {
                nhanVienIdToNameMap = nhanVienMap;
                callback.run();
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("QL_DH_Fragment", "Error loading employee names: " + errorMessage);
                // Handle error, maybe show a toast or log it for debugging
                callback.run(); // Optionally run callback even on error to continue with other operations
            }
        });
    }

    private void loadSanPhamDetails() {
        controller.loadSanPhamDetails(new QL_DHController.SanPhamDetailsListener() {
            @Override
            public void onSanPhamDetailsLoaded(Map<Integer, SanPham> sanPhamMap) {
                QL_DH_Fragment.this.sanPhamMap = sanPhamMap;
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("QL_DH_Fragment", "Error loading product details: " + errorMessage);
            }
        });
    }
    private void loadTableNames(Runnable callback) {
        controller.loadTableNames(new QL_DHController.TableNameListener() {
            @Override
            public void onTableNamesLoaded(Map<Integer, String> tableMap) {
                tableIdToNameMap = tableMap;
                callback.run();
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("QL_DH_Fragment", "Error loading table names: " + errorMessage);
                callback.run(); // Optionally run callback even on error to continue with other operations
            }
        });
    }
    private void showChiTietDonHangDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Chi tiết đơn hàng : #" + selectedDonHangId);

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_chi_tiet_don_hang, null);
        ListView listView = dialogView.findViewById(R.id.listview_chi_tiet_don_hang);
        ChiTietDonHangAdapter chiTietDonHangAdapter = new ChiTietDonHangAdapter(requireContext(), chiTietDonHangList, sanPhamMap);
        listView.setAdapter(chiTietDonHangAdapter);

        builder.setView(dialogView);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}