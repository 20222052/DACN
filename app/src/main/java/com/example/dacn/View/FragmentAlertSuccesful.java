package com.example.dacn.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.dacn.R;

import java.io.Serializable;

public class FragmentAlertSuccesful extends Fragment {

    private String message;  // Biến nhận dữ liệu từ CartFragment
    private String message2; // Biến nhận dữ liệu từ CartFragment
    private static String nhanVienId; // Biến nhận dữ liệu từ CartFragment


    public FragmentAlertSuccesful() {
        // Constructor mặc định
    }

    public static FragmentAlertSuccesful newInstance(String thongbaothanhcong, String thongbaokhachdoi, String nhanVienIdd) {
        FragmentAlertSuccesful fragment = new FragmentAlertSuccesful();
        Bundle args = new Bundle();
        args.putString("thongbaothanhcong", thongbaothanhcong);
        args.putString("thongbaokhachdoi", thongbaokhachdoi);
        args.putString("nhanVienId", nhanVienIdd);
        nhanVienId = nhanVienIdd;
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentAlertSuccesful newInstanceImage(String thongbaothanhcong, String thongbaokhachdoi, ImageView imageView) {
        FragmentAlertSuccesful fragment = new FragmentAlertSuccesful();
        Bundle args = new Bundle();
        args.putString("thongbaothanhcong", thongbaothanhcong);
        args.putString("thongbaokhachdoi", thongbaokhachdoi);
        args.putSerializable("imageView", (Serializable) imageView);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_alert_succesful_layout, container, false);

        if (getArguments() != null) {
            message = getArguments().getString("thongbaothanhcong");
            message2 = getArguments().getString("thongbaokhachdoi");
        }

        TextView textView = view.findViewById(R.id.tv_datHangThanhCong);
        textView.setText(message); // Gán thông báo cho TextView

        TextView textView2 = view.findViewById(R.id.tv_thongBaoKhach);
        textView2.setText(message2); // Gán thông báo cho TextView

        ImageView imageView = view.findViewById(R.id.img_alert);

        Button btn_dong = view.findViewById(R.id.btn_dong);
        btn_dong.setOnClickListener(v -> closeFragment());

        View bg = view.findViewById(R.id.main);
        bg.setOnClickListener(v -> closeFragment());

        View popup = view.findViewById(R.id.popup_layout);
        popup.setOnClickListener(v -> {
            // Chặn sự kiện click thoát fragment
        });

        return view;
    }

    private void closeFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .remove(FragmentAlertSuccesful.this)
                .commit();

        requireActivity().finish();
        Intent intent = new Intent(this.getActivity(), Staff.class);
        intent.putExtra("nhanVienId", nhanVienId);
        startActivity(intent);
    }
}
