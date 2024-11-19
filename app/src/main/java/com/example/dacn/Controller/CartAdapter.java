package com.example.dacn.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dacn.Model.Cart;
import com.example.dacn.R;

import java.util.List;

public class CartAdapter extends BaseAdapter {



    private final Context context;
    private final List<Cart> cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }


    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int i) {
        return cartList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_cart, viewGroup, false);
        }

        Cart cart = cartList.get(i);
        //anh xa giao dien
        ImageView anh = view.findViewById(R.id.iv_product_image);
        TextView ten = view.findViewById(R.id.tv_product_name);
        TextView gia = view.findViewById(R.id.tv_product_price);
        //gan gia tri
        ten.setText(cart.getTenSP());
        gia.setText(String.valueOf(cart.getGia()));
        anh.setImageResource(R.drawable.lau_kimchi);
        return view;
    }
}
