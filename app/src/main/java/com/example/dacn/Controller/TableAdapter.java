package com.example.dacn.Controller;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dacn.Model.SanPham;
import com.example.dacn.Model.Table;
import com.example.dacn.R;

import java.util.ArrayList;
import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    private List<Table> tablesList;
    private OnAddToCartListener onAddToCartListener;

    public TableAdapter(List<Table> tablesList, OnAddToCartListener listener) {
        this.tablesList = tablesList;
        this.onAddToCartListener = listener;
    }

    public void setOnAddToCartListener(OnAddToCartListener listener) {
        this.onAddToCartListener = listener;
    }

    public void setFilteredList(List<Table> filteredList) {
        this.tablesList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        Table table = tablesList.get(position);
        if (table == null) {
            return;
        }

        holder.bind(table);
    }

    @Override
    public int getItemCount() {
        return tablesList != null ? tablesList.size() : 0;
    }

    public class TableViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTable;
        TextView tv_name_item;

        public TableViewHolder(View itemView) {
            super(itemView);
            imgTable = itemView.findViewById(R.id.img_table);
            tv_name_item = itemView.findViewById(R.id.tv_name_item);
        }

        public void bind(Table table) {
            tv_name_item.setText(table.getNameTable());

            // Thêm xử lý sự kiện click cho toàn bộ item
            itemView.setOnClickListener(v -> {
                if (onAddToCartListener != null) {
                    onAddToCartListener.onAddToCart(table);
                }
            });
        }
    }

    public interface OnAddToCartListener {
        void onAddToCart(Table table);
    }
}
