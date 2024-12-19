package com.example.dacn.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dacn.Model.Table;
import com.example.dacn.R;

import java.util.ArrayList;
import java.util.List;

public class QL_Table_Adapter extends BaseAdapter {
    private Context context;
    private List<Table> tableList;
    private int selectedPosition = -1;
    public QL_Table_Adapter(@NonNull Context context, @NonNull List<Table> tableList) {
        this.context = context;
        this.tableList = tableList;
    }

    @Override
    public int getCount() {
        return tableList.size();
    }

    @Override
    public Object getItem(int position) {
        return tableList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_ql_table, parent, false);
        Table table = tableList.get(position);

        TextView tvMaTable = convertView.findViewById(R.id.id_Table);
        TextView tvName = convertView.findViewById(R.id.nameTB);
        TextView tvStatus = convertView.findViewById(R.id.is_Status);

        tvMaTable.setText(String.format("Mã bàn: %s", table.getIdTable()));
        tvName.setText(String.format("Tên bàn :%s", table.getNameTable()));
        tvStatus.setText(table.isStatus() ? "Trạng thái : Trống" : "Trạng thái : Đang có khách");

        if (position == selectedPosition) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.primaryColor));
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        }

        convertView.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged(); // Làm mới danh sách để cập nhật trạng thái
        });

        return convertView;
    }

    public void updateList(List<Table> newList) {
        tableList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @Nullable
    public String getSelectedTableId() {
        if (selectedPosition != -1) {
            return String.valueOf(tableList.get(selectedPosition).getIdTable()); // Lấy ID của mục được chọn
        }
        return null; // Không có mục nào được chọn
    }
}