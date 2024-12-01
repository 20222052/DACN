package com.example.dacn.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dacn.Model.ListItem;
import com.example.dacn.R;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    private Context context;
    private List<ListItem> listItems;

    public ListAdapter(Context context, List<ListItem> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.activity_staff_item_listview, parent, false);
        }

        TextView itemName = convertView.findViewById(R.id.item_name_cart_list);
        TextView itemPrice = convertView.findViewById(R.id.item_price_cart_list);
        TextView itemQuantity = convertView.findViewById(R.id.item_quantity);
        ImageButton btnMinus = convertView.findViewById(R.id.btn_minus);
        ImageButton btnPlus = convertView.findViewById(R.id.btn_plus);
        TextView tvQuantity = convertView.findViewById(R.id.tv_quantity);

        // Kiểm tra các thành phần null
        if (itemName == null || itemPrice == null || itemQuantity == null ||
                btnMinus == null || btnPlus == null || tvQuantity == null) {
            throw new IllegalStateException("One or more views in activity_staff_item_listview.xml are missing or have incorrect IDs.");
        }

        ListItem listItem = listItems.get(position);

        // Thiết lập dữ liệu
        itemName.setText(listItem.getName());
        itemPrice.setText(String.valueOf(listItem.getPrice()));
        itemQuantity.setText(String.valueOf(listItem.getQuantity()));

        // Cập nhật số lượng
        btnMinus.setOnClickListener(v -> {
            int quantity = listItem.getQuantity();
            if (quantity > 1) {
                listItem.setQuantity(quantity - 1);
                tvQuantity.setText(String.valueOf(listItem.getQuantity()));
                notifyDataSetChanged();
            }
        });

        btnPlus.setOnClickListener(v -> {
            int quantity = listItem.getQuantity();
            listItem.setQuantity(quantity + 1);
            tvQuantity.setText(String.valueOf(listItem.getQuantity()));
            notifyDataSetChanged();
        });

        return convertView;
    }

}
