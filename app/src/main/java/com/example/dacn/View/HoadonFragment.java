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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dacn.Controller.HoadonListPrd_Adapter;
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

        // Xử lý sự kiện khi an vao layout rong se dong frahment
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) View backgroundLayout = view.findViewById(R.id.hoadonlayout);
        backgroundLayout.setOnClickListener(v -> closeFragment());
        // Đảm bảo nội dung bên trong popup không bị ảnh hưởng
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) View popupLayout = view.findViewById(R.id.hoadonitem);
        popupLayout.setOnClickListener(v -> {
            // Chặn sự kiện click thoát fragment
        });

        btnXacNhan.setOnClickListener(v->{
            showFragment();
            closeFragment();
        });
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

    //show fragment
    private void showFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        String thongbaothanhcong = "Thanh toán thành công!";
        String thongbaokhachdoi = "Cảm ơn bạn đã sử dụng dịch vụ. Hẹn có gặp lại bạn.";

        FragmentAlertSuccesful fragment = FragmentAlertSuccesful.newInstance(thongbaothanhcong, thongbaokhachdoi);

        transaction.add(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Hàm đóng fragment
    private void closeFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .remove(HoadonFragment.this)
                .commit();
    }
}
