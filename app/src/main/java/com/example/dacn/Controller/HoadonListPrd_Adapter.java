package com.example.dacn.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dacn.R;

import java.util.List;

public class HoadonListPrd_Adapter extends BaseAdapter {
    private final Context context;
    private final List<HoaDonItem> hoadonItems;

    public HoadonListPrd_Adapter(Context context, List<HoaDonItem> hoadonItems) {
        this.context = context;
        this.hoadonItems = hoadonItems;
    }

    @Override
    public int getCount() {
        return hoadonItems.size();
    }

    @Override
    public Object getItem(int position) {
        return hoadonItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_item_prd_hoadon, parent, false);
        }

        HoaDonItem item = hoadonItems.get(position);

        // Ánh xạ các view từ layout
        TextView itemName = convertView.findViewById(R.id.item_name);
        TextView itemPrice = convertView.findViewById(R.id.item_price);
        TextView itemQuantity = convertView.findViewById(R.id.item_quantity);

        // Đặt dữ liệu lên giao diện
        itemName.setText(item.getOrderName());
        itemPrice.setText(String.format("%,.0f VND", item.getPrice()));
        itemQuantity.setText(String.format("%dx", item.getQuantity()));

        return convertView;
    }

    // Lớp mô tả thông tin hoá đơn
    public static class HoaDonItem {
        private final String orderName;
        private final double price;
        private final int quantity;

        public HoaDonItem(String orderName, double price, int quantity) {
            this.orderName = orderName;
            this.price = price;
            this.quantity = quantity;
        }

        public String getOrderName() {
            return orderName;
        }

        public double getPrice() {
            return price;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
