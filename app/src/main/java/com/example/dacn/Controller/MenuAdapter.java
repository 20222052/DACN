package com.example.dacn.Controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dacn.Model.ListItem;
import com.example.dacn.Model.MenuItem;
import com.example.dacn.R;
import com.example.dacn.View.Staff;

import java.util.List;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuAdapter extends BaseAdapter {
    private Context context;
    private List<MenuItem> menuItems;
    int maSanPham = 0;

    public MenuAdapter(Context context, List<MenuItem> menuItems) {
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
        TextView itemName = convertView.findViewById(R.id.item_name_cart_list);
        TextView itemPrice = convertView.findViewById(R.id.item_price_cart_list);

        MenuItem menuItem = menuItems.get(position);

        // Sử dụng Glide để tải ảnh
        Glide.with(context).load(menuItem.getImageResource()).into(itemImage);
        itemName.setText(menuItem.getName());
        itemPrice.setText(menuItem.getPrice());

        // Xử lý sự kiện bấm vào sản phẩm
        convertView.setOnClickListener(view -> {
            int id = menuItem.getIdprd();
            String name = menuItem.getName();
            String price = menuItem.getPrice();

            // Lấy danh sách listItems từ Staff
            Staff staffActivity = (Staff) context;

            // Kiểm tra nếu món đã tồn tại
            boolean found = false;
            for (ListItem listItem : staffActivity.listItems) {
                if (listItem.getName().equals(name)) {
                    listItem.setQuantity(listItem.getQuantity() + 1); // Tăng số lượng
                    found = true;
                    break;
                }
            }

            // Nếu món chưa tồn tại, thêm mới
            if (!found) {
                staffActivity.addToListItem(new ListItem(id, name, price, "1"));
            }

            // Cập nhật ListView
            staffActivity.listAdapter.notifyDataSetChanged();
        });

        return convertView;
    }


}
