package com.example.dacn.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dacn.Controller.HoadonListPrd_Adapter;
import com.example.dacn.Model.Cart;
import com.example.dacn.Model.ChiTietDonHang;
import com.example.dacn.Model.DonHang;
import com.example.dacn.Model.ListItem;
import com.example.dacn.Model.SanPham;
import com.example.dacn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HoadonFragment extends Fragment {
    private TextView tvTongTien;
    private List<HoadonListPrd_Adapter.HoaDonItem> hoadonItems = new ArrayList<>();
    private List<ListItem> lstSp = new ArrayList<>();
    Integer MaDH;

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

//        btnXacNhan.setOnClickListener(v -> {
//            // Gọi phương thức xóa chi tiết đơn hàng theo mã đơn hàng trước
//            // Gọi phương thức thêm chi tiết đơn hàng với MaDH
//            // Thực hiện thanh toán và hiển thị thông báo
//            showFragment();
//            closeFragment();
//        });

        btnXacNhan.setOnClickListener(v -> {
            // Xóa chi tiết đơn hàng cũ
            DatabaseReference chiTietDonHangRef = FirebaseDatabase.getInstance().getReference("Chi_Tiet_Don_Hang");

            chiTietDonHangRef.orderByChild("maDonHang").equalTo(MaDH).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        data.getRef().removeValue(); // Xóa từng chi tiết đơn hàng
                    }

                    // Thêm lại chi tiết đơn hàng mới
                    for (ListItem item : lstSp) {
                        ChiTietDonHang chiTiet = new ChiTietDonHang(
                                generateUniqueKey(), // Tạo mã chi tiết đơn hàng tự động
                                item.getId(),//masp
                                MaDH,
                                item.getQuantity(),
                                Integer.parseInt(item.getPrice())
                        );

                        chiTietDonHangRef.push().setValue(chiTiet); // Lưu chi tiết đơn hàng vào Firebase
                    }

                    // Hiển thị thông báo thanh toán thành công
                    showFragment();
                    closeFragment();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(requireContext(), "Lỗi khi xoá chi tiết đơn hàng", Toast.LENGTH_SHORT).show();
                }
            });
        });


        // Nhận danh sách listItems từ Staff activity
        if (getArguments() != null) {
            ArrayList<ListItem> listItems = (ArrayList<ListItem>) getArguments().getSerializable("listItems");
            hoadonItems = convertToHoaDonItems(listItems); // Chuyển đổi thành HoaDonItem
            ArrayList<ListItem> ListSp = (ArrayList<ListItem>) getArguments().getSerializable("listItems");
            lstSp = convertToListSPItems(ListSp); // Chuyển đổi thành HoaDonItem
            MaDH = getArguments().getInt("orderCode"); // Truyền Mã đơn hàng

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

    private int generateUniqueKey() {
        return (int) (System.currentTimeMillis() / 1000); // Tạo mã duy nhất từ timestamp
    }

    // Chuyển listItems thành danh sách HoaDonItem để sử dụng trong adapter
    private List<HoadonListPrd_Adapter.HoaDonItem> convertToHoaDonItems(List<ListItem> listItems) {
        List<HoadonListPrd_Adapter.HoaDonItem> hoaDonItems = new ArrayList<>();
        for (ListItem item : listItems) {
            hoaDonItems.add(new HoadonListPrd_Adapter.HoaDonItem(item.getName(), Double.parseDouble(item.getPrice()), item.getQuantity()));
        }
        return hoaDonItems;
    }
    // Chuyển listItems thành danh sách HoaDonItem để sử dụng trong adapter
    private List<ListItem> convertToListSPItems(List<ListItem> listSp) {
        List<ListItem> spItems = new ArrayList<>();
        for (ListItem item : listSp) {
            spItems.add(
                    new ListItem(item.getName(),
                            String.valueOf(item.getPrice()),
                            String.valueOf(item.getQuantity()),
                            item.getId(),
                            item.getMaChiTietDonHang(),
                            item.getMaDonHang(),
                            item.getMaSanPham()
                    )
            );
        }
        return spItems;
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
