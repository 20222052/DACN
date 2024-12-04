package com.example.dacn.Controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.dacn.Model.ChiTietDonHang;
import com.example.dacn.Model.DonHang;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderController {
    private DatabaseReference database;
    private Context context;

    public OrderController(Context context) {
        this.context = context;
        this.database = FirebaseDatabase.getInstance().getReference();
    }

    // Hàm xử lý sự kiện khi nhấn nút "Xác nhận"
    public void handleOrderButtonClick(List<ChiTietDonHang> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            Toast.makeText(context, "Giỏ hàng trống. Vui lòng thêm sản phẩm trước khi đặt hàng!", Toast.LENGTH_SHORT).show();
            return;
        }

        int maDonHang = generateOrderID(); // Tạo mã đơn hàng
        float tongTien = calculateTotalPrice(cartItems);
        String ngayDatHang = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        // Tạo đơn hàng và gửi lên Firebase
        DonHang donHang = new DonHang(maDonHang, false, tongTien, ngayDatHang); // Trang thái đơn hàng là "false" (chưa xử lý)
        sendOrderToFirebase(donHang, cartItems);
    }

    // Gửi đơn hàng và chi tiết đơn hàng lên Firebase
    private void sendOrderToFirebase(DonHang donHang, List<ChiTietDonHang> cartItems) {
        String maDonHang = String.valueOf(donHang.getMaDonHang());

        // Lưu đơn hàng vào Firebase
        database.child("Don_Hang").child(maDonHang).setValue(donHang)
                .addOnSuccessListener(unused -> Log.d("Firebase", "Đơn hàng đã được lưu thành công."))
                .addOnFailureListener(e -> Log.e("Firebase", "Lỗi khi lưu đơn hàng", e));

        // Lưu chi tiết đơn hàng vào Firebase
        for (ChiTietDonHang item : cartItems) {
            String maChiTietDonHang = String.valueOf(item.getMaChiTietDonHang()); // Tạo mã chi tiết ngẫu nhiên
            item.setMaDonHang(donHang.getMaDonHang());
            database.child("Chi_Tiet_Don_Hang").child(maChiTietDonHang).setValue(item)
                    .addOnSuccessListener(unused -> Log.d("Firebase", "Chi tiết đơn hàng đã được lưu."))
                    .addOnFailureListener(e -> Log.e("Firebase", "Lỗi khi lưu chi tiết đơn hàng", e));
        }
    }

    // Tính tổng tiền từ giỏ hàng
    private float calculateTotalPrice(List<ChiTietDonHang> cartItems) {
        float total = 0;
        for (ChiTietDonHang item : cartItems) {
            total += item.getDonGia() * item.getSoLuong();
        }
        return total;
    }

    // Hàm tạo mã đơn hàng duy nhất
    private int generateOrderID() {
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }
}
