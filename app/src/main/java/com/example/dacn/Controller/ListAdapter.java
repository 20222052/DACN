package com.example.dacn.Controller;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dacn.Model.ListItem;
import com.example.dacn.R;
import com.example.dacn.View.Staff;

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
        ImageButton btnMinus = convertView.findViewById(R.id.btn_minus);
        ImageButton btnPlus = convertView.findViewById(R.id.btn_plus);
        TextView tvQuantity = convertView.findViewById(R.id.tv_quantity);

        ListItem listItem = listItems.get(position);

        // Thiết lập dữ liệu
        itemName.setText(listItem.getName());
        itemPrice.setText(String.valueOf(listItem.getPrice()));
        tvQuantity.setText(String.valueOf(listItem.getQuantity()));

        // Cập nhật số lượng
        btnMinus.setOnClickListener(v -> {
            int quantity = listItem.getQuantity();
            if (quantity > 1) {
                listItem.setQuantity(quantity - 1);
                tvQuantity.setText(String.valueOf(listItem.getQuantity()));
                notifyDataSetChanged();
                ((Staff) context).updateTongTien(); // Cập nhật tổng tiền
            } else {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có muốn xóa sản phẩm khỏi giỏ hàng không?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            listItems.remove(position);
                            notifyDataSetChanged();
                            ((Staff) context).updateTongTien(); // Cập nhật tổng tiền
                            dialog.dismiss();
                        })
                        .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });

        btnPlus.setOnClickListener(v -> {
            listItem.setQuantity(listItem.getQuantity() + 1);
            tvQuantity.setText(String.valueOf(listItem.getQuantity()));
            notifyDataSetChanged();
            ((Staff) context).updateTongTien(); // Cập nhật tổng tiền
        });


        return convertView;
    }
}
