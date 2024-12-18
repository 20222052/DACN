package com.example.dacn.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dacn.Model.DanhMuc;
import com.example.dacn.R;

import java.util.List;

public class catalogueAdapter extends BaseAdapter {

    private Context context;
    private List<DanhMuc> danhMucList;
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    public catalogueAdapter(Context context, List<DanhMuc> danhMucList) {
        this.context = context;
        this.danhMucList = danhMucList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return danhMucList.size();
    }

    @Override
    public Object getItem(int position) {
        return danhMucList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_catalogue, null);
            holder = new ViewHolder();
            holder.idCatalogue = convertView.findViewById(R.id.id_catalogue);
            holder.nameCatalogue = convertView.findViewById(R.id.name_catalogue);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final DanhMuc currentDanhMuc = danhMucList.get(position);

        // Set the data for the views
        // Convert maDanhMuc to a string explicitly
        holder.idCatalogue.setText(String.valueOf(currentDanhMuc.getMaDanhMuc()));
        holder.nameCatalogue.setText(currentDanhMuc.getTenDanhMuc());

        // Set up click listener for each item
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(currentDanhMuc);
                }
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView idCatalogue;
        TextView nameCatalogue;
    }

    // Method to update the list of categories
    public void updateData(List<DanhMuc> newList) {
        this.danhMucList = newList;
        notifyDataSetChanged();
    }

    // Interface for click events
    public interface OnItemClickListener {
        void onItemClick(DanhMuc danhMuc);
    }

    // Method to set the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}