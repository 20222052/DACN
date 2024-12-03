package com.example.dacn.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dacn.Model.NhanVien;
import com.example.dacn.R;

import java.util.ArrayList;
import java.util.List;

public class QL_NV_Adapter extends BaseAdapter {
    private Context context;
    private List<NhanVien> nhanVienList;

    public QL_NV_Adapter(@NonNull Context context, @NonNull List<NhanVien> nhanVienList) {
        this.context = context;
        this.nhanVienList = nhanVienList;
    }

    @Override
    public int getCount() {
        return nhanVienList.size();
    }

    @Override
    public Object getItem(int position) {
        return nhanVienList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_nv, parent, false);
        NhanVien nhanVien = nhanVienList.get(position);
        TextView tvMaNV = convertView.findViewById(R.id.idNV);
        TextView tvName = convertView.findViewById(R.id.name);
        TextView tvPhone = convertView.findViewById(R.id.SDT);
        TextView tvAddress = convertView.findViewById(R.id.DC);
        TextView tvRole = convertView.findViewById(R.id.CV);

        tvMaNV.setText(String.format("Mã nhân viên: %s", nhanVien.getId()));
        tvName.setText(String.format("Tên nhân viên :%s", nhanVien.getTenNhanVien()));
        tvPhone.setText(String.format("SDT :%s", nhanVien.getSoDienThoai()));
        tvAddress.setText(String.format("Địa chỉ :%s", nhanVien.getAddress()));
        tvRole.setText(String.format("Chức vụ :%s", nhanVien.getVaiTro()));

        return convertView;
    }

    public void updateList(List<NhanVien> newList) {
        nhanVienList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }
}