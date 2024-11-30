package com.example.dacn.Controller;

import android.annotation.SuppressLint;
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
import com.example.dacn.Model.SanPham;
import com.example.dacn.R;

import java.util.ArrayList;
import java.util.List;

public class QL_menuAdapter extends BaseAdapter {
    private final Context context;
    private List<SanPham> sanPhamList;

    public QL_menuAdapter(@NonNull Context context, @NonNull List<SanPham> products) {
        this.context = context;
        this.sanPhamList = products;
    }

    @Override
    public int getCount() {
        return sanPhamList.size();
    }

    @Override
    public Object getItem(int position) {
        return sanPhamList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        SanPham product = sanPhamList.get(position);
        TextView tvName = convertView.findViewById(R.id.tv_food_name);
        TextView tvPrice = convertView.findViewById(R.id.tv_gia);
        TextView tvQuantity = convertView.findViewById(R.id.tv_so_luong);
        ImageView ivImage = convertView.findViewById(R.id.img_food);
        ImageView ivTT = convertView.findViewById(R.id.img_TT);

        if (product.getTrangThai()) {
            Glide.with(context)
                    .load(product.getHinhAnh())
                    .placeholder(R.drawable.ic_launcher_background) // Add a placeholder image
                    .error(R.drawable.ic_launcher_background) // Add an error image
                    .into(ivTT);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_launcher_background)
                    .placeholder(R.drawable.ic_launcher_background) // Add a placeholder image
                    .error(R.drawable.ic_launcher_background) // Add an error image
                    .into(ivTT);
        }

        tvName.setText(product.getTenSanPham());
        tvPrice.setText(String.format("Price: %.2f", product.getGia()));
        tvQuantity.setText(String.format("Quantity: %d", product.getSoLuong()));
        Glide.with(context)
                .load(product.getHinhAnh())
                .placeholder(R.drawable.ic_launcher_background) // Add a placeholder image
                .error(R.drawable.ic_launcher_background) // Add an error image
                .into(ivImage);

        return convertView;
    }

    public void filterList(List<SanPham> filteredList) {
        sanPhamList = new ArrayList<>(filteredList);
        notifyDataSetChanged();
    }
}