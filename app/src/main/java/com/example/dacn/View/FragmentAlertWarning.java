package com.example.dacn.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dacn.R;

import java.io.Serializable;

import android.os.CountDownTimer;  // Đảm bảo import thư viện CountDownTimer

public class FragmentAlertWarning extends Fragment {

    private String message;  // Biến nhận dữ liệu từ CartFragment
    private String message2; // Biến nhận dữ liệu từ CartFragment

    private Button btn_dong;  // Nút "Đóng"
    private CountDownTimer countDownTimer;
    private OnHuyDonHangListener mListener;

    // Gọi phương thức này khi muốn gọi huyDonHang từ Activity
    public interface OnHuyDonHangListener {
        void huyDonHang();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Gán listener cho Activity
            mListener = (OnHuyDonHangListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnHuyDonHangListener");
        }
    }
    public FragmentAlertWarning() {
        // Constructor mặc định
    }

    public static FragmentAlertWarning newInstance(String thongbaothanhcong, String thongbaokhachdoi) {
        FragmentAlertWarning fragment = new FragmentAlertWarning();
        Bundle args = new Bundle();
        args.putString("thongbaothanhcong", thongbaothanhcong);
        args.putString("thongbaokhachdoi", thongbaokhachdoi);
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
        imageView.setImageResource(R.drawable.ic_warning);

        btn_dong = view.findViewById(R.id.btn_dong); // Lấy tham chiếu đến nút "Đóng"

        // Vô hiệu hóa nút "Đóng" khi bắt đầu đếm ngược
        btn_dong.setEnabled(false);
        btn_dong.setAlpha(0.5f); // Làm mờ nút

        // Khởi tạo đếm ngược
        startCountdownTimer();

        btn_dong.setOnClickListener(v -> {
            // Khi nhấn nút đóng, gọi huyDonHang
            if (mListener != null) {
                mListener.huyDonHang();
            }
            // Đóng Fragment
            getActivity().getSupportFragmentManager().beginTransaction().remove(FragmentAlertWarning.this).commit();
        });

        View bg = view.findViewById(R.id.main);
        bg.setOnClickListener(v -> closeFragment());

        View popup = view.findViewById(R.id.popup_layout);
        popup.setOnClickListener(v -> {
            // Chặn sự kiện click thoát fragment
        });

        return view;
    }

    // Hàm đếm ngược
    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btn_dong.setText("Đóng (" + millisUntilFinished / 1000 + ")");
            }

            @Override
            public void onFinish() {
                // Sau khi đếm ngược hoàn thành, khôi phục nút "Đóng"
                btn_dong.setText("Đóng"); // Hiển thị lại "Đóng"
                btn_dong.setAlpha(1.0f); // Làm mờ nút
                btn_dong.setEnabled(true); // Kích hoạt lại nút "Đóng"
            }
        };
        countDownTimer.start();
    }

    private void closeFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .remove(FragmentAlertWarning.this)
                .commit();

        requireActivity().finish();

        Intent intent = new Intent(this.getActivity(), Staff.class);
        startActivity(intent);
    }
}
