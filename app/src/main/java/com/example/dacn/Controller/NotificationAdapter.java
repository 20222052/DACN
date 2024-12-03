package com.example.dacn.Controller;

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

        TextView itemName = convertView.findViewById(R.id.item_name_cart_list);
        TextView itemPrice = convertView.findViewById(R.id.item_price_cart_list);

        itemName.setText(String.format("Đơn Hàng: %s", item.getOrderName()));
        itemPrice.setText(String.format(""));

        return convertView;
    }



    // Thêm phương thức này để trả về danh sách notificationItems
    public List<NotificationItem> getNotificationItems() {
        return notificationItems;
    }

    public static class NotificationItem {
        private final String orderName;
        private final float totalPrice;

        public NotificationItem(String orderName, float totalPrice) {
            this.orderName = orderName;
            this.totalPrice = totalPrice;
        }

        public String getOrderName() {
            return orderName;
        }

        public float getTotalPrice() {
            return totalPrice;
        }

    }
}
