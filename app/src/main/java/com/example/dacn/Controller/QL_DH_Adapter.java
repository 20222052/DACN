package com.example.dacn.Controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dacn.Model.DonHang;
import com.example.dacn.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QL_DH_Adapter extends BaseAdapter {
    private Context context;
    private List<DonHang> donHangList;
    private Map<Integer, String> nhanVienIdToNameMap;
    private Map<Integer, String> tableIdToNameMap;
    public QL_DH_Adapter(@NonNull Context context, @NonNull List<DonHang> donHangList, @NonNull Map<Integer, String> nhanVienIdToNameMap, @NonNull Map<Integer, String> tableIdToNameMap) {
        this.context = context;
        this.donHangList = donHangList;
        this.nhanVienIdToNameMap = nhanVienIdToNameMap;
        this.tableIdToNameMap = tableIdToNameMap;
    }

    @Override
    public int getCount() {
        return donHangList.size();
    }

    @Override
    public Object getItem(int position) {
        return donHangList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dh, parent, false);
        }

        DonHang donHang = donHangList.get(position);

        TextView tvMaDonHang = convertView.findViewById(R.id.ma_DH);
        TextView tvNgayDatHang = convertView.findViewById(R.id.date_hd);
        TextView tvTongTien = convertView.findViewById(R.id.Tong_tien);
        TextView tvTrangThai = convertView.findViewById(R.id.trang_thai);
        TextView tvTenNV = convertView.findViewById(R.id.name_NV);
        TextView tvTenBan = convertView.findViewById(R.id.name_table);

        tvMaDonHang.setText("Mã đơn hàng: #" + donHang.getMaDonHang());
        tvNgayDatHang.setText("Ngày đặt hàng : " + donHang.getNgayDatHang());
        tvTongTien.setText("Tổng tiền : " + donHang.getTongTien());

        String tenNhanVien = nhanVienIdToNameMap.get(Integer.parseInt(String.valueOf(donHang.getNhanVienId())));
        tvTenNV.setText("Tên nhân viên : " + (tenNhanVien != null ? tenNhanVien : "Unknown"));

        String tenBan = tableIdToNameMap.get(donHang.getTableId());
        tvTenBan.setText("Tên bàn : " + (tenBan != null ? tenBan : "Unknown"));

        tvTrangThai.setText(donHang.isTrangThai() ? "Trạng thái : Completed" : "Trạng thái : Pending");

        return convertView;
    }

    public void updateList(List<DonHang> newList) {
        donHangList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }
}