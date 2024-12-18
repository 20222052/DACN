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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        ImageView ivImage = convertView.findViewById(R.id.img_food);

        if (product.getTrangThai()) {
            convertView.setAlpha(1.0f);
            convertView.setEnabled(true);
        } else {
            convertView.setAlpha(0.3f);
            convertView.setEnabled(false);
        }

        tvName.setText(product.getTenSanPham());
        double gia = product.getGia();

// Sử dụng NumberFormat với Locale để định dạng tiền tệ VND
        NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = vndFormat.format(gia);

// Set text cho TextView
        tvPrice.setText("Giá: " + formattedPrice);
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