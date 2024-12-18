package com.example.dacn.Controller;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dacn.Model.Table;
import com.example.dacn.R;
import com.example.dacn.View.KhachHangActivity;

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

        // Cập nhật tên bàn
        holder.tvTableNumber.setText(table.getNameTable());

        // Cập nhật trạng thái giao diện (alpha và enabled)
        if (table.isStatus()) {
            // Bàn đã có khách
            holder.itemView.setAlpha(0.3f); // Làm mờ
            holder.itemView.setClickable(false); // Không cho click
            holder.itemView.setEnabled(false);  // Không tương tác được
        } else {
            // Bàn trống
            holder.itemView.setAlpha(1.0f); // Hiển thị rõ
            holder.itemView.setClickable(true); // Cho phép click
            holder.itemView.setEnabled(true);  // Có thể tương tác
        }

        // Đặt sự kiện click cho bàn trống
        holder.itemView.setOnClickListener(v -> {
            if (!table.isStatus()) {
                // Xử lý khi click vào bàn trống
                Intent intent = new Intent(v.getContext(), KhachHangActivity.class);
                intent.putExtra("tableId", table.getIdTable());
                v.getContext().startActivity(intent);
            } else {
                // Thông báo nếu bàn đã có khách
                Toast.makeText(v.getContext(), "Bàn " + table.getNameTable() + " đã có khách!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tableList.size();
    }

    public static class TableViewHolder extends RecyclerView.ViewHolder {
        TextView tvTableNumber;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTableNumber = itemView.findViewById(R.id.tvTableNumber);
        }
    }

    public interface OnTableClickListener {
        void onTableClick(Table table); // Sự kiện click bàn
    }
}

