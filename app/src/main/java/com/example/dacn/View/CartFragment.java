package com.example.dacn.View;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dacn.Controller.CartAdapter;
import com.example.dacn.Controller.MessagingNotification;
import com.example.dacn.Controller.OrderController;
import com.example.dacn.Model.Cart;
import com.example.dacn.Model.ChiTietDonHang;
import com.example.dacn.Model.DonHang;
import com.example.dacn.R;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment implements CartAdapter.OnCartUpdateListener {
    private List<Cart> cartItems;
    private TextView tvTotalPrice;
    private Button btnXacNhan;
    private OrderController orderController;
    private int tableId;
    private String nhanVienId;

    public CartFragment() {}

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cart_layout, container, false);
        // Nhận danh sách giỏ hàng từ Bundle
        if (getArguments() != null) {
            cartItems = (List<Cart>) getArguments().getSerializable("cart_items");
            tableId = getArguments().getInt("tableId");
            nhanVienId = getArguments().getString("nhanVienId");
        }

        Log.d("CartFragment", "Received cart items: " + cartItems.size());

        // Khởi tạo các thành phần giao diện
        tvTotalPrice = view.findViewById(R.id.tv_total_price);
        btnXacNhan = view.findViewById(R.id.btn_checkout);

        updateTotalPrice();
        updateButtonState();

        // Khởi tạo OrderController
        orderController = new OrderController(requireContext());

        // Lấy LinearLayout ngoài cùng
        View backgroundLayout = view.findViewById(R.id.background_layout);
        backgroundLayout.setOnClickListener(v -> closeFragment());

        View popupLayout = view.findViewById(R.id.popup_layout);
        popupLayout.setOnClickListener(v -> {
            // Chặn sự kiện click thoát fragment
        });

        // Xử lý sự kiện nhấn nút xác nhận
        btnXacNhan.setOnClickListener(v -> {
            if (cartItems == null || cartItems.isEmpty()) {
                Toast.makeText(requireContext(), "Giỏ hàng đang trống. Vui lòng thêm sản phẩm trước khi gọi món!", Toast.LENGTH_SHORT).show();
            } else {
                // Tạo đơn hàng trước
                DonHang donHang = createOrder();

                // Sau khi tạo đơn hàng, tạo chi tiết đơn hàng
                List<ChiTietDonHang> chiTietDonHangList = createOrderDetails(donHang);

                // Gọi OrderController để xử lý đơn hàng
                orderController.handleOrderButtonClick(chiTietDonHangList, donHang);

                // Xóa sạch giỏ hàng
                cartItems.clear();


                // Cập nhật giao diện
                updateCartUI();

                showOrderNotification();

                // Hiển thị thông báo thành công
                Toast.makeText(requireContext(), "Đặt món thành công! Giỏ hàng đã được làm trống.", Toast.LENGTH_SHORT).show();

                // Hiển thị thông báo thành công dưới dạng popup
                showCartFragment(nhanVienId);
            }
        });

        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        // Cập nhật GridView
        GridView gridView = view.findViewById(R.id.gv_cart_items);
        CartAdapter adapter = new CartAdapter(requireContext(), cartItems, this);
        gridView.setAdapter(adapter);

        return view;
    }

    // Tạo đơn hàng
    private DonHang createOrder() {
        // Tạo đơn hàng mới, mã đơn hàng có thể tự động tăng dần
        int maDonHang = generateMaDonHang(); // Giả sử có phương thức để tạo mã tự động
        String ngayDatHang = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());; // Lấy ngày hiện tại

        float tongTien = calculateTotalPrice(); // Tổng tiền từ giỏ hàng

        DonHang donHang = new DonHang(maDonHang, false, tongTien, ngayDatHang, tableId, nhanVienId);
        return donHang;
    }

    // Tạo chi tiết đơn hàng từ giỏ hàng
    private List<ChiTietDonHang> createOrderDetails(DonHang donHang) {
        List<ChiTietDonHang> chiTietDonHangList = new ArrayList<>();

        for (Cart cartItem : cartItems) {
            // Tạo chi tiết đơn hàng với maDonHang đã tạo trước
            int maChiTietDonHang = generateMaChiTietDonHang(); // Tạo mã chi tiết đơn hàng tự động
            ChiTietDonHang chiTiet = new ChiTietDonHang(
                    (float) cartItem.getGia(),
                    cartItem.getSoLuong(),
                    donHang.getMaDonHang(),
                    maChiTietDonHang,
                    cartItem.getId()
            );
            chiTietDonHangList.add(chiTiet);
        }

        // Lưu chi tiết đơn hàng vào cơ sở dữ liệu hoặc bộ nhớ
        // Ví dụ: ChiTietDonHangDAO.saveAll(chiTietDonHangList);

        return chiTietDonHangList;
    }

    // Phương thức tạo mã đơn hàng tự động
    private int generateMaDonHang() {
        // Giả sử mã đơn hàng được tạo tự động (có thể sử dụng số tăng dần hoặc ID tự động)
        return (int) (Math.random() * 10000); // Ví dụ: tạo mã ngẫu nhiên
    }

    // Phương thức tạo mã chi tiết đơn hàng tự động
    private int generateMaChiTietDonHang() {
        // Tạo mã chi tiết đơn hàng tự động
        return (int) (Math.random() * 100000); // Ví dụ: tạo mã ngẫu nhiên
    }

    // Cập nhật giao diện giỏ hàng sau khi thay đổi
    private void updateCartUI() {
        GridView gridView = getView().findViewById(R.id.gv_cart_items);

        // Cập nhật adapter với danh sách hiện tại (sau khi đã xóa sạch)
        CartAdapter adapter = new CartAdapter(requireContext(), cartItems, this);
        gridView.setAdapter(adapter);

        // Cập nhật tổng giá tiền và trạng thái nút
        updateTotalPrice();
        updateButtonState();
    }

    // Cập nhật trạng thái nút xác nhận dựa trên trạng thái giỏ hàng
    private void updateButtonState() {
        if (cartItems == null || cartItems.isEmpty()) {
            btnXacNhan.setEnabled(false);
            btnXacNhan.setAlpha(0.5f); // Làm mờ nút
        } else {
            btnXacNhan.setEnabled(true);
            btnXacNhan.setAlpha(1.0f); // Hiển thị bình thường
        }
    }

    @Override
    public void onCartUpdated(List<Cart> updatedCartList) {
        cartItems = updatedCartList;
        updateTotalPrice();
        updateButtonState();
    }

    // Hiển thị popup thông báo đặt hàng thành công
    private void showCartFragment(String nhanVienId) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        String thongbaothanhcong = "Cảm ơn bạn đã đặt hàng!";
        String thongbaokhachdoi = "Chúng tôi đang xử lý đơn hàng của bạn. Vui lòng đợi ít phút.";

        FragmentAlertSuccesful fragment = FragmentAlertSuccesful.newInstance(thongbaothanhcong, thongbaokhachdoi, nhanVienId);
        transaction.add(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void updateTotalPrice() {
        float totalPrice = 0;
        for (Cart item : cartItems) {
            totalPrice += (float) (item.getGia() * item.getSoLuong());
        }
        // Use %,.2f to format the float value with two decimal places
        NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = vndFormat.format(totalPrice);
        tvTotalPrice.setText(String.valueOf(formattedPrice));
    }

    // Tính toán tổng giá tiền của giỏ hàng
    private float calculateTotalPrice() {
        float totalPrice = 0;
        for (Cart item : cartItems) {
            totalPrice += item.getGia() * item.getSoLuong();
        }
        return totalPrice;
    }

    // Đóng fragment
    private void closeFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .remove(CartFragment.this)
                .commit();
    }

    @SuppressLint("MissingPermission")
    private void showOrderNotification() {
        Intent intent = new Intent(getActivity(), Staff.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        // Tạo channel cho Android 8.0 trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "order_channel_id",
                    "Order Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        // Tạo nội dung thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "order_channel_id")
                .setSmallIcon(R.drawable.ic_bell) // Thay icon theo dự án của bạn
                .setContentTitle("Nhà Hàng Hadilao")
                .setContentText("Có đơn hàng mới!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Hiển thị thông báo
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
        notificationManager.notify(1, builder.build());
    }
}
