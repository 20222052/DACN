package com.example.dacn.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dacn.R;
import com.example.dacn.View.Staff;

import java.util.List;

public class MenuAdapter extends BaseAdapter {
    private Context context;
    private List<Staff.MenuItem> menuItems;

    public MenuAdapter(Context context, List<Staff.MenuItem> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return menuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.activity_staff_item_gridview, parent, false);
        }

        ImageView itemImage = convertView.findViewById(R.id.item_image);
        TextView itemName = convertView.findViewById(R.id.item_name);
        TextView itemPrice = convertView.findViewById(R.id.item_price);

        Staff.MenuItem menuItem = menuItems.get(position);

        itemImage.setImageResource(menuItem.getImageResource());
        itemName.setText(menuItem.getName());
        itemPrice.setText(menuItem.getPrice());

        return convertView;
    }
}
