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

import com.example.dacn.Controller.NotificationAdapter;
import com.example.dacn.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_layout, container, false);

        Button closeButton = view.findViewById(R.id.btn_close_notification);
        closeButton.setOnClickListener(v -> closeFragment());

        GridView gridView = view.findViewById(R.id.gridView_notification);

        // Dữ liệu mẫu
        List<NotificationAdapter.NotificationItem> notificationItems = new ArrayList<>();
        notificationItems.add(new NotificationAdapter.NotificationItem("101", 3));
        notificationItems.add(new NotificationAdapter.NotificationItem("102", 5));
        notificationItems.add(new NotificationAdapter.NotificationItem("103", 2));
        notificationItems.add(new NotificationAdapter.NotificationItem("104", 4));
        notificationItems.add(new NotificationAdapter.NotificationItem("101", 3));
        notificationItems.add(new NotificationAdapter.NotificationItem("102", 5));
        notificationItems.add(new NotificationAdapter.NotificationItem("103", 2));
        notificationItems.add(new NotificationAdapter.NotificationItem("104", 4));
        notificationItems.add(new NotificationAdapter.NotificationItem("101", 3));
        notificationItems.add(new NotificationAdapter.NotificationItem("102", 5));
        notificationItems.add(new NotificationAdapter.NotificationItem("103", 2));
        notificationItems.add(new NotificationAdapter.NotificationItem("104", 4));
        notificationItems.add(new NotificationAdapter.NotificationItem("101", 3));
        notificationItems.add(new NotificationAdapter.NotificationItem("102", 5));
        notificationItems.add(new NotificationAdapter.NotificationItem("103", 2));
        notificationItems.add(new NotificationAdapter.NotificationItem("104", 4));

        // Áp dụng adapter
        NotificationAdapter adapter = new NotificationAdapter(requireContext(), notificationItems);
        gridView.setAdapter(adapter);

        return view;
    }

    private void closeFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .remove(NotificationFragment.this)
                .commit();
    }
}
