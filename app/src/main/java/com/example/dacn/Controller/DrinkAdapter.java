package com.example.dacn.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dacn.Model.Drink;
import com.example.dacn.R;

import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> {
    private List<Drink> drinkList;

    public DrinkAdapter(List<Drink> list) {
        this.drinkList = list;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drink, parent, false);
        return new DrinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, int position) {;
        Drink drink = drinkList.get(position);
    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }

    public void setFilterList2(List<Drink> filterList) {
        this.drinkList = filterList;
        notifyDataSetChanged();
    }

    public class DrinkViewHolder extends RecyclerView.ViewHolder {
        CardView cv_drinks;
        public DrinkViewHolder(@NonNull View itemView) {
            super(itemView);
            cv_drinks = itemView.findViewById(R.id.cv_drinks);
        }
    }
}
