package com.example.dacn.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dacn.R;

import java.util.List;

public class NotificationAdapter extends BaseAdapter {
    private final Context context;
    private final List<NotificationItem> notificationItems;

    public NotificationAdapter(Context context, List<NotificationItem> notificationItems) {
        this.context = context;
        this.notificationItems = notificationItems;
    }

    @Override
    public int getCount() {
        return notificationItems.size();
    }

    @Override
    public Object getItem(int position) {
        return notificationItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        }

        NotificationItem item = notificationItems.get(position);

        TextView itemName = convertView.findViewById(R.id.item_name);
        TextView itemPrice = convertView.findViewById(R.id.item_price);

        itemName.setText(String.format("Đơn Hàng: %s", item.getOrderName()));
        itemPrice.setText(String.format("Số Lượng: %s", item.getQuantity()));

        return convertView;
    }

    public static class NotificationItem {
        private final String orderName;
        private final int quantity;

        public NotificationItem(String orderName, int quantity) {
            this.orderName = orderName;
            this.quantity = quantity;
        }

        public String getOrderName() {
            return orderName;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}

