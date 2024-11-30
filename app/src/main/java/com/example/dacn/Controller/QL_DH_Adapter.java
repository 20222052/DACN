package com.example.dacn.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dacn.Model.DonHang;
import com.example.dacn.R;

import java.util.List;
import java.util.Map;

public class QL_DH_Adapter extends BaseAdapter {
    private Context context;
    private List<DonHang> donHangList;
    private Map<Integer, String> nhanVienIdToNameMap;

    public QL_DH_Adapter(@NonNull Context context, @NonNull List<DonHang> donHangList, @NonNull Map<Integer, String> nhanVienIdToNameMap) {
        this.context = context;
        this.donHangList = donHangList;
        this.nhanVienIdToNameMap = nhanVienIdToNameMap;
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

        TextView tvNgayDatHang = convertView.findViewById(R.id.date_hd);
        TextView tvTongTien = convertView.findViewById(R.id.Tong_tien);
        TextView tvTrangThai = convertView.findViewById(R.id.trang_thai);
        TextView tvTenNhanVien = convertView.findViewById(R.id.name_NV);

        tvNgayDatHang.setText("Ngày đặt hàng : " + donHang.getNgayDatHang());
        tvTongTien.setText("Tổng tiền : " + donHang.getTongTien());
        tvTrangThai.setText(donHang.isTrangThai() ? "Trạng thái : Completed" : "Trạng thái : Pending");

        String tenNhanVien = nhanVienIdToNameMap.get(+donHang.getMaNhanVien());
        tvTenNhanVien.setText(tenNhanVien != null ? "Tên nhân viên : " +tenNhanVien : "Unknown");

        return convertView;
    }
}