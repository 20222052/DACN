package com.example.dacn.View;

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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dacn.Controller.CartAdapter;
import com.example.dacn.Model.Cart;
import com.example.dacn.R;

import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.OnCartUpdateListener {
    private List<Cart> cartItems;
    private TextView tvTotalPrice;

    public CartFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cart_layout, container, false);

        if (getArguments() != null) {
            cartItems = (List<Cart>) getArguments().getSerializable("cart_items");
        }

        Log.d("CartFragment", "Received cart items: " + cartItems.size());
        tvTotalPrice = view.findViewById(R.id.tv_total_price);
        updateTotalPrice();

        Button btnXacNhan = view.findViewById(R.id.btn_checkout);

        // Lấy LinearLayout ngoài cùng
        View backgroundLayout = view.findViewById(R.id.background_layout);
        backgroundLayout.setOnClickListener(v -> closeFragment());

        View popupLayout = view.findViewById(R.id.popup_layout);
        popupLayout.setOnClickListener(v -> {
            // Chặn sự kiện click thoát fragment
        });

        btnXacNhan.setOnClickListener(v -> {
            if (cartItems == null || cartItems.isEmpty()) {
                // Hiển thị thông báo giỏ hàng rỗng
                Toast.makeText(requireContext(), "Giỏ hàng đang trống. Vui lòng thêm sản phẩm trước khi gọi món!", Toast.LENGTH_SHORT).show();
            } else {
                // Xử lý gọi món nếu giỏ hàng không rỗng
                closeFragment();
                showCartFragment();
            }
        });


        GridView gridView = view.findViewById(R.id.gv_cart_items);
        CartAdapter adapter = new CartAdapter(requireContext(), cartItems, this);
        gridView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCartUpdated(List<Cart> updatedCartList) {
        cartItems = updatedCartList;
        updateTotalPrice();
    }

    private void showCartFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        String thongbaothanhcong = "Cảm ơn bạn đã đặt hàng!";
        String thongbaokhachdoi = "Chúng tôi đang xử lý đơn hàng của bạn. Vui lòng đợi ít phút";

        FragmentAlertSuccesful fragment = FragmentAlertSuccesful.newInstance(thongbaothanhcong, thongbaokhachdoi);

        transaction.add(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Hàm đóng fragment
    private void closeFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .remove(CartFragment.this)
                .commit();
    }

    private void updateTotalPrice() {
        int totalPrice = 0;
        for (Cart item : cartItems) {
            totalPrice += item.getGia() * item.getSoLuong();
        }
        tvTotalPrice.setText(String.format("Tổng: %,d VND", totalPrice));
    }
}
