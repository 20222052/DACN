package com.example.dacn.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dacn.Model.Table;
import com.example.dacn.R;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {
    private List<Table> tableList;
    private OnTableClickListener listener;

    public TableAdapter(List<Table> tableList, OnTableClickListener listener) {
        this.tableList = tableList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        Table table = tableList.get(position);
        holder.tvTableNumber.setText(table.getNameTable());
        holder.tvTableStatus.setText(table.isStatus() ? "Trạng thái: Có khách" : "Trạng thái: Trống");

        // Cập nhật viền tùy vào trạng thái của bàn
        holder.itemView.setEnabled(!table.isStatus()); // Nếu có khách, không cho click
        holder.itemView.setSelected(table.isStatus()); // Nếu có khách, không chọn được

        // Cập nhật viền và các thông tin
        holder.itemView.setOnClickListener(v -> {
            if (!table.isStatus()) { // Bàn trống mới được click
                listener.onTableClick(table);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tableList.size();
    }

    public static class TableViewHolder extends RecyclerView.ViewHolder {
        TextView tvTableNumber, tvTableStatus;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTableNumber = itemView.findViewById(R.id.tvTableNumber);
            tvTableStatus = itemView.findViewById(R.id.tvTableStatus);
        }
    }

    public interface OnTableClickListener {
        void onTableClick(Table table); // Sự kiện click bàn
    }
}

