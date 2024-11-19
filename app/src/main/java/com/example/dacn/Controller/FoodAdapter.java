package com.example.dacn.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.dacn.Model.Food ;
import com.example.dacn.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private  List<Food> list;
    public FoodAdapter(List<Food> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = list.get(position);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setFilterList(List<Food> filteredList) {
        this.list = filteredList;
        notifyDataSetChanged(); // Cập nhật giao diện
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        CardView cv_food;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            cv_food = itemView.findViewById(R.id.cv_food);
        }
    }
}
