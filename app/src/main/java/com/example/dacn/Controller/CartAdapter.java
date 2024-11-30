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

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.dacn.Model.Cart;
import com.example.dacn.R;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private List<Cart> cartList;
    private OnCartUpdateListener listener;

    public CartAdapter(Context context, List<Cart> cartList, OnCartUpdateListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.listener = listener;
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

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        convertView = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        holder = new ViewHolder();
        holder.imgProduct = convertView.findViewById(R.id.iv_product_image);
        holder.tvProductName = convertView.findViewById(R.id.tv_product_name);
        holder.tvProductPrice = convertView.findViewById(R.id.tv_product_price);
        holder.tvQuantity = convertView.findViewById(R.id.tv_quantity);
        holder.btnPlus = convertView.findViewById(R.id.btn_plus);
        holder.btnMinus = convertView.findViewById(R.id.btn_minus);

        Cart cart = cartList.get(position);
        holder.tvProductName.setText(cart.getTenSP());
        holder.tvProductPrice.setText(String.format("%,.2f VND", cart.getGia() * cart.getSoLuong()));
        holder.tvQuantity.setText(String.valueOf(cart.getSoLuong()));

        // Load image with Glide
        Glide.with(context).load(cart.getAnhSP()).into(holder.imgProduct);

        // Handle button clicks
        holder.btnPlus.setOnClickListener(v -> {
            cart.setSoLuong(cart.getSoLuong() + 1);
            notifyDataSetChanged();
            listener.onCartUpdated(cartList);
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (cart.getSoLuong() > 1) {
                cart.setSoLuong(cart.getSoLuong() - 1);
                notifyDataSetChanged();
                listener.onCartUpdated(cartList);
            } else {
                showDeleteConfirmationDialog(cart, position);
            }
        });

        return convertView;
    }

    private void showDeleteConfirmationDialog(Cart cart, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có muốn xóa " + cart.getTenSP() + " khỏi giỏ hàng không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    cartList.remove(position);
                    notifyDataSetChanged();
                    listener.onCartUpdated(cartList);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private static class ViewHolder {
        ImageView imgProduct;
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvQuantity;
        ImageButton btnPlus;
        ImageButton btnMinus;
    }

    public interface OnCartUpdateListener {
        void onCartUpdated(List<Cart> updatedCartList);
    }
}
