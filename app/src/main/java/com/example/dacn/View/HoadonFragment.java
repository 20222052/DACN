package com.example.dacn.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dacn.R;

import java.util.ArrayList;
import java.util.List;

public class HoadonFragment extends Fragment {

    public HoadonFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_hoadon_staff_layout, container, false);

        // Khai báo nút "Huỷ" và "Xác nhận"
        Button btnHuy = view.findViewById(R.id.btn_huy);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button btnXacNhan = view.findViewById(R.id.btn_xacNhanThanhToan);

        // Xử lý sự kiện cho nút "Huỷ"
        btnHuy.setOnClickListener(v -> closeFragment());

        // Khởi tạo GridView và dữ liệu
        GridView gridView = view.findViewById(R.id.gridView_itemList_prd);

        // Dữ liệu mẫu
        List<HoadonListPrd_Adapter.HoaDonItem> hoadonItems = new ArrayList<>();
        hoadonItems.add(new HoadonListPrd_Adapter.HoaDonItem("Phở cuốn", 40000.0, 2));
        hoadonItems.add(new HoadonListPrd_Adapter.HoaDonItem("Bún chả", 35000.0, 1));
        hoadonItems.add(new HoadonListPrd_Adapter.HoaDonItem("Cơm tấm", 50000.0, 3));

        // Gán adapter cho GridView
        HoadonListPrd_Adapter adapter = new HoadonListPrd_Adapter(requireContext(), hoadonItems);
        gridView.setAdapter(adapter);

        return view;
    }

    // Hàm đóng fragment
    private void closeFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .remove(HoadonFragment.this)
                .commit();
    }
}
