package com.example.dacn.View;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dacn.Controller.NotificationAdapter;
import com.example.dacn.Model.DonHang;
import com.example.dacn.Model.OnOrderSelectedListener;
import com.example.dacn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {
    private List<DonHang> donhang;
    private NotificationAdapter adapter;
    private OnOrderSelectedListener listener; // Tham chiếu giao diện

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnOrderSelectedListener) {
            listener = (OnOrderSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " phải triển khai OnOrderSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_layout, container, false);

        donhang = new ArrayList<>();
        List<NotificationAdapter.NotificationItem> notificationItems = new ArrayList<>();
        adapter = new NotificationAdapter(requireContext(), notificationItems);

        Button closeButton = view.findViewById(R.id.btn_close_notification);
        closeButton.setOnClickListener(v -> closeFragment());

        GridView gridView = view.findViewById(R.id.gridView_notification);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            NotificationAdapter.NotificationItem item = adapter.getNotificationItems().get(position);
            String orderCode = item.getOrderName();
            int tableId = item.getTableId();
            String nhanVienId = item.getNhanVienId();
            Log.d("NotificationFragment", "NhanVienId: " + nhanVienId+" - TableId: " + tableId + "");
            if (listener != null) {
                listener.onOrderSelected(orderCode, tableId, nhanVienId); // Gửi mã đơn hàng qua giao diện
            }
        });

        loadMenuData();
        return view;
    }

    private void loadMenuData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Don_Hang");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donhang.clear();
                adapter.getNotificationItems().clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    DonHang data_dh = data.getValue(DonHang.class);
                    if (data_dh != null && data_dh.isTrangThai()==false) {
                        DonHang donHang = new DonHang(data_dh.getMaDonHang(), data_dh.isTrangThai(), data_dh.getTongTien(), data_dh.getNgayDatHang(), data_dh.getTableId(), data_dh.getNhanVienId());
                        donhang.add(donHang);
                        adapter.getNotificationItems().add(new NotificationAdapter.NotificationItem(
                                String.valueOf(data_dh.getMaDonHang()),
                                data_dh.getTongTien(),
                                data_dh.getTableId(),
                                data_dh.getNhanVienId()
                        ));
                        Log.d("NotificationFragment", "MaDonHang: " + data_dh.getMaDonHang() +
                                ", TongTien: " + data_dh.getTongTien() +
                                ", TableId: " + data_dh.getTableId() +
                                ", NhanVienId: " + data_dh.getNhanVienId());
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void closeFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .remove(NotificationFragment.this)
                .commit();
    }
}
