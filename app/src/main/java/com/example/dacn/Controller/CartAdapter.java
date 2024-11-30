package com.example.dacn.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dacn.Model.Cart;
import com.example.dacn.R;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private List<Cart> cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int position) {
        return cartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
            holder = new ViewHolder();
            holder.imgProduct = convertView.findViewById(R.id.iv_product_image);
            holder.tvProductName = convertView.findViewById(R.id.tv_product_name);
            holder.tvProductPrice = convertView.findViewById(R.id.tv_product_price);
            holder.tvQuantity = convertView.findViewById(R.id.tv_quantity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Cart cart = cartList.get(position);
        holder.tvProductName.setText(cart.getTenSP());
        holder.tvProductPrice.setText(String.format("%s VND", cart.getGia()));
        holder.tvQuantity.setText(String.valueOf(cart.getSoLuong()));

        // Load image with Glide (if applicable)
        Glide.with(context).load(cart.getAnhSP()).into(holder.imgProduct);

        return convertView;
    }

    private static class ViewHolder {
        ImageView imgProduct;
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvQuantity;
    }
}

