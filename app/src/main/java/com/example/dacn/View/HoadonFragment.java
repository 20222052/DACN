package com.example.dacn.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dacn.Controller.HoadonListPrd_Adapter;
import com.example.dacn.Model.ListItem;
import com.example.dacn.R;

import java.util.ArrayList;
import java.util.List;

public class HoadonFragment extends Fragment {
    private TextView tvTongTien;
    private List<HoadonListPrd_Adapter.HoaDonItem> hoadonItems = new ArrayList<>();

    public HoadonFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_hoadon_staff_layout, container, false);
        tvTongTien = view.findViewById(R.id.tv_total_price);

        // Nhận dữ liệu chi tiết đơn hàng từ StaffFragment
        if (getArguments() != null) {
            hoadonItems = (List<HoadonListPrd_Adapter.HoaDonItem>) getArguments().getSerializable("hoadonItems");
        }

        // Khai báo nút "Huỷ" và "Xác nhận"
        Button btnHuy = view.findViewById(R.id.btn_huy);
        Button btnXacNhan = view.findViewById(R.id.btn_xacNhanThanhToan);

        // Xử lý sự kiện cho nút "Huỷ"
        btnHuy.setOnClickListener(v -> closeFragment());

        // Xử lý sự kiện khi ấn vào layout rỗng sẽ đóng fragment
        View backgroundLayout = view.findViewById(R.id.hoadonlayout);
        backgroundLayout.setOnClickListener(v -> closeFragment());

        // Đảm bảo nội dung bên trong popup không bị ảnh hưởng
        View popupLayout = view.findViewById(R.id.hoadonitem);
        popupLayout.setOnClickListener(v -> {
            // Chặn sự kiện click thoát fragment
        });

        btnXacNhan.setOnClickListener(v -> {
//            updateOrderDetails(orderCode);
            // Thực hiện thanh toán và hiển thị thông báo
            showFragment();
            closeFragment();
        });

        // Nhận danh sách listItems từ Staff activity
        if (getArguments() != null) {
            ArrayList<ListItem> listItems = (ArrayList<ListItem>) getArguments().getSerializable("listItems");
            hoadonItems = convertToHoaDonItems(listItems); // Chuyển đổi thành HoaDonItem
        }

        // Khởi tạo GridView và dữ liệu
        GridView gridView = view.findViewById(R.id.gridView_itemList_prd);
        tvTongTien.setText(
                String.format("Vui lòng thanh toán số tiền là: %.0f VND", getArguments().getDouble("totallPrice"))
        );
        // Gán adapter cho GridView
        HoadonListPrd_Adapter adapter = new HoadonListPrd_Adapter(requireContext(), hoadonItems);
        gridView.setAdapter(adapter);

        return view;
    }

    // Chuyển listItems thành danh sách HoaDonItem để sử dụng trong adapter
    private List<HoadonListPrd_Adapter.HoaDonItem> convertToHoaDonItems(List<ListItem> listItems) {
        List<HoadonListPrd_Adapter.HoaDonItem> hoaDonItems = new ArrayList<>();
        for (ListItem item : listItems) {
            hoaDonItems.add(new HoadonListPrd_Adapter.HoaDonItem(item.getName(), Double.parseDouble(item.getPrice()), item.getQuantity()));
        }
        return hoaDonItems;
    }

    // Phương thức nhận dữ liệu chi tiết đơn hàng từ StaffFragment
    public static HoadonFragment newInstance(List<HoadonListPrd_Adapter.HoaDonItem> hoadonItems) {
        HoadonFragment fragment = new HoadonFragment();
        Bundle args = new Bundle();
        args.putSerializable("hoadonItems", new ArrayList<>(hoadonItems));
        fragment.setArguments(args);
        return fragment;
    }

    // Hiển thị fragment thông báo thành công
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
