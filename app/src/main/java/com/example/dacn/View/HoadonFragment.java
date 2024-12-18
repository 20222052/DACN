package com.example.dacn.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HoadonFragment extends Fragment {
    private TextView tvTongTien;
    private List<HoadonListPrd_Adapter.HoaDonItem> hoadonItems = new ArrayList<>();
    private List<ListItem> lstSp = new ArrayList<>();
    Integer MaDH;
    String nhanVienId;
    int tableId;

    public HoadonFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_hoadon_staff_layout, container, false);
        tvTongTien = view.findViewById(R.id.tv_total_price);

        nhanVienId = getArguments().getString("nhanVienId");

        RadioGroup radioGroupPTTT = view.findViewById(R.id.radioGroup_PTTT);
        RadioButton radioTienMat = view.findViewById(R.id.radioTienMat);
        RadioButton radioChuyenKhoan = view.findViewById(R.id.radioChuyenKhoan);
        ImageView imageView = view.findViewById(R.id.imageView);

        // Đặt mặc định chọn Tiền Mặt và ẩn ImageView
        radioTienMat.setChecked(true);
        imageView.setVisibility(View.GONE);

        // Lắng nghe thay đổi của RadioGroup
        radioGroupPTTT.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioTienMat) {
                imageView.setVisibility(View.GONE); // Ẩn ImageView
            } else if (checkedId == R.id.radioChuyenKhoan) {
                imageView.setVisibility(View.VISIBLE); // Hiển thị ImageView
                // Đặt link ảnh QR
                String imageUrl = "https://img.vietqr.io/image/BIDV-0976956191-compact2.png?amount="+getArguments().getDouble("totallPrice")+"&addInfo=Thanh%20Toán%20Hóa%20Đơn%20Ăn%20HaDiLao&accountName=HadilaoRestauron";
                Glide.with(requireContext()).load(imageUrl).into(imageView);
            }
        });



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
            // Xóa chi tiết đơn hàng cũ
            DatabaseReference chiTietDonHangRef = FirebaseDatabase.getInstance().getReference("Chi_Tiet_Don_Hang");
            DatabaseReference donHangRef = FirebaseDatabase.getInstance().getReference("Don_Hang");

            chiTietDonHangRef.orderByChild("maDonHang").equalTo(MaDH).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        data.getRef().removeValue(); // Xóa từng chi tiết đơn hàng
                    }

                    // Thêm lại chi tiết đơn hàng mới
                    for (ListItem item : lstSp) {
                        ChiTietDonHang chiTiet = new ChiTietDonHang(
                                Float.parseFloat(item.getPrice()),//don gia
                                generateUniqueKey(), // Tạo mã chi tiết đơn hàng tự động
                                MaDH,
                                item.getMaSanPham(),//masp
                                item.getQuantity()
                        );
                        Log.d("DEBUG", "Item: " + generateUniqueKey() +", Item: " + item.getMaSanPham() + ", Quantity: " + item.getQuantity() + ", Price: " + item.getPrice());
                        chiTietDonHangRef.push().setValue(chiTiet); // Lưu chi tiết đơn hàng vào Firebase
                    }

                    // Cập nhật trạng thái đơn hàng sang "true"
                    donHangRef.child(MaDH.toString()).child("trangThai").setValue(true).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Cập nhật đơn hàng thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Lỗi khi cập nhật đơn hàng", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Hiển thị thông báo thanh toán thành công
                    showFragment(nhanVienId);
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
            tableId = getArguments().getInt("tableId"); // Truyền Mã bàn
            nhanVienId = getArguments().getString("nhanVienId"); // Truyền Mã nhan vien
        }

        // Khởi tạo GridView và dữ liệu
        GridView gridView = view.findViewById(R.id.gridView_itemList_prd);
        NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = vndFormat.format(getArguments().getDouble("totallPrice"));
        tvTongTien.setText(
                String.format("Vui lòng thanh toán số tiền là: "+ formattedPrice)
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
            Log.d("DEBUG: ", "MaSP: " + item.getMaSanPham() + ", price: " + item.getPrice() + ", quantity: " + item.getQuantity() + ", id: " + item.getId() + ", MaChiTietDonHang: " + item.getMaChiTietDonHang() + ", MaDonHang: " + item.getMaDonHang());
        }
        return spItems;
    }

    // Hiển thị fragment thông báo thành công
    private void showFragment(String nhanVienId) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        String thongbaothanhcong = "Thanh toán thành công!";
        String thongbaokhachdoi = "Cảm ơn bạn đã sử dụng dịch vụ. Hẹn có gặp lại bạn.";

        FragmentAlertSuccesful fragment = FragmentAlertSuccesful.newInstance(thongbaothanhcong, thongbaokhachdoi, nhanVienId);

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
