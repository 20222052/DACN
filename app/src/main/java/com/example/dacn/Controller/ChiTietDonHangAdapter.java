package com.example.dacn.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.dacn.Model.ChiTietDonHang;
import com.example.dacn.Model.SanPham;
import com.example.dacn.R;

import java.util.List;
import java.util.Map;

public class ChiTietDonHangAdapter extends BaseAdapter {
    private Context context;
    private List<ChiTietDonHang> chiTietDonHangList;
    private Map<Integer, SanPham> sanPhamMap;

    public ChiTietDonHangAdapter(@NonNull Context context, @NonNull List<ChiTietDonHang> chiTietDonHangList,@NonNull Map<Integer, SanPham> sanPhamMap) {
        this.context = context;
        this.chiTietDonHangList = chiTietDonHangList;
        this.sanPhamMap = sanPhamMap;
    }

    @Override
    public int getCount() {
        return chiTietDonHangList.size();
    }

    @Override
    public Object getItem(int position) {
        return chiTietDonHangList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chi_tiet_dh, parent, false);
        }

        ChiTietDonHang chiTietDonHang = chiTietDonHangList.get(position);
        SanPham sanPham = sanPhamMap.get(chiTietDonHang.getMaSanPham());

        TextView tvTenSanPham = convertView.findViewById(R.id.ten_san_pham);
        TextView tvSoLuong = convertView.findViewById(R.id.so_luong);
        TextView tvGia = convertView.findViewById(R.id.gia);
        ImageView hinh_anh = convertView.findViewById(R.id.hinh_anh);

        if (sanPham != null) {
            tvTenSanPham.setText(sanPham.getTenSanPham());
            Glide.with(context).load(sanPham.getHinhAnh()).into(hinh_anh);
        }
        tvSoLuong.setText(String.valueOf("Số lượng : " +chiTietDonHang.getSoLuong()));
        tvGia.setText(String.valueOf(chiTietDonHang.getDonGia()));

        return convertView;
    }
}