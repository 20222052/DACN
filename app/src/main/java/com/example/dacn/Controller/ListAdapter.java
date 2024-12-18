package com.example.dacn.Controller;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dacn.Model.ListItem;
import com.example.dacn.Model.SanPham;
import com.example.dacn.R;
import com.example.dacn.View.Staff;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ListAdapter extends BaseAdapter {
    private Context context;
    private List<ListItem> listItems;
    private DatabaseReference databaseReference;

    public ListAdapter(Context context, List<ListItem> listItems) {
        this.context = context;
        this.listItems = listItems;
        this.databaseReference = FirebaseDatabase.getInstance().getReference("Chi_Tiet_Don_Hang");
    }

    public void setFilteredList(List<ListItem> filteredList) {
        this.listItems = filteredList;
        notifyDataSetChanged();
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
        try {
            NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            Double price = Double.parseDouble(listItem.getPrice());
            String formattedPrice = vndFormat.format(price);
            itemPrice.setText(String.valueOf(formattedPrice));
        }catch (Exception ex){ex.printStackTrace();}

        tvQuantity.setText(String.valueOf(listItem.getQuantity()));

        // Cập nhật số lượng
        btnMinus.setOnClickListener(v -> {
            int quantity = listItem.getQuantity();
            if (quantity > 1) {
                listItem.setQuantity(quantity - 1);
                tvQuantity.setText(String.valueOf(listItem.getQuantity()));

                // Cập nhật số lượng trên Firebase
                databaseReference.child(String.valueOf(listItem.getId()))
                        .child("soLuong")
                        .setValue(listItem.getQuantity())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        });
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
            int newQuantity = listItem.getQuantity() + 1; // Tăng số lượng lên 1
            listItem.setQuantity(newQuantity); // Cập nhật số lượng mới
            tvQuantity.setText(String.valueOf(newQuantity)); // Cập nhật hiển thị số lượng

            // Gọi hàm updateOrAddChiTietDonHang để cập nhật chi tiết đơn hàng
            // Chuyển sang phương thức của Staff để xử lý Firebase
            ((Staff) context).updateOrAddChiTietDonHang(
                    Float.parseFloat(listItem.getPrice()),  // Dùng giá của sản phẩm
                    newQuantity,
                    listItem.getMaDonHang(),
                    listItem.getMaSanPham(),
                    listItem.getMaChiTietDonHang()
            );

            // Cập nhật giao diện và tính tổng tiền
            notifyDataSetChanged();
            ((Staff) context).updateTongTien();
        });

        return convertView;
    }
}
