package com.example.dacn.View;

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

import com.example.dacn.Controller.CartAdapter;
import com.example.dacn.Model.Cart;
import com.example.dacn.R;

import java.util.List;

public class CartFragment extends Fragment {
    private List<Cart> cartItems;
    public CartFragment(){
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cart_layout, container, false);

        if (getArguments() != null) {
            cartItems = (List<Cart>) getArguments().getSerializable("cart_items");  // Lấy danh sách sản phẩm từ Bundle
        }


        Button btnXacNhan = view.findViewById(R.id.btn_checkout);

        // Lấy LinearLayout ngoài cùng
        View backgroundLayout = view.findViewById(R.id.background_layout);
        backgroundLayout.setOnClickListener(v -> closeFragment());

        View popupLayout = view.findViewById(R.id.popup_layout);
        popupLayout.setOnClickListener(v -> {
            // Chặn sự kiện click thoát fragment
        });

        btnXacNhan.setOnClickListener(v -> {
            closeFragment();
            showCartFragment();
        });

        // Khởi tạo GridView và dữ liệu
        GridView gridView = view.findViewById(R.id.gv_cart_items);

        // Gán adapter cho GridView
        CartAdapter adapter = new CartAdapter(requireContext(), cartItems);

        gridView.setAdapter(adapter);

        return view;
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
}
