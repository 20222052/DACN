package com.example.dacn.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dacn.R;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    private Context context;
    private List<Staff.ListItem> listItems; // Đổi Staff.ListItem thành ListItem

    public ListAdapter(Context context, List<Staff.ListItem> listItems) {
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

        TextView itemName = convertView.findViewById(R.id.item_name);
        TextView itemPrice = convertView.findViewById(R.id.item_price);
        TextView itemQuantity = convertView.findViewById(R.id.item_quantity);

        Staff.ListItem listitems = listItems.get(position);

        itemName.setText(String.valueOf(listitems.getName()));
        itemPrice.setText(String.valueOf(listitems.getPrice()));
        itemQuantity.setText(String.valueOf(listitems.getQuantity()));



        return convertView;
    }
}
