package com.example.dacn.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dacn.Controller.DrinkAdapter;
import com.example.dacn.Controller.FoodAdapter;
import com.example.dacn.Model.Drink;
import com.example.dacn.Model.Food;
import com.example.dacn.R;

import java.util.ArrayList;
import java.util.List;

public class KhachHangActivity extends AppCompatActivity {
    RecyclerView rcv_food, rcv_drink;
    List<Food> list;
    List<Drink> drinksList;
    FoodAdapter foodAdapter;
    DrinkAdapter drinksAdapter;
    SearchView searchView;
    ImageButton btn_cart;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_khachhang);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.khachhang), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        rcv_food = findViewById(R.id.rcv_food);
        rcv_drink = findViewById(R.id.rcv_drink);
        searchView = findViewById(R.id.search_food);
        btn_cart = findViewById(R.id.btn_cart);
        int numberOfColumns = 3;
        int numberOfColumns2 = 3;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns);

        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, numberOfColumns2);
        // set layout cho các recycleView
        rcv_food.setLayoutManager(gridLayoutManager);
        rcv_drink.setLayoutManager(gridLayoutManager2);

        searchView.clearFocus();

        // Tìm kiếm
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        // food
        list = getListFood();
        foodAdapter = new FoodAdapter(list);
        rcv_food.setAdapter(foodAdapter);

        // drink
        drinksList = getListDrink();
        drinksAdapter = new DrinkAdapter(drinksList);
        rcv_drink.setAdapter(drinksAdapter);

        //xu ly btn_card de hienthi fragment
        btn_cart.setOnClickListener(view -> showCartFragment());
        int[] cartCount = {0}; // Dùng mảng để lưu số lượng

        TextView cartCountText = findViewById(R.id.cart_count);

        foodAdapter.setOnAddToCartListener(() -> {
            cartCount[0]++;
            cartCountText.setText(String.valueOf(cartCount[0]));
        });
    }

    //show fragment
    private void showCartFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        CartFragment fragment = new CartFragment();
        transaction.add(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Lọc và tìm kiếm
    private void filterList(String newText) {
        // Food
        List<Food> filteredFoodList = new ArrayList<>();
        for (Food itemFood : list) {
            if (itemFood.getName().toLowerCase().contains(newText.toLowerCase())) {
                filteredFoodList.add(itemFood);
            }
        }
        if (filteredFoodList.isEmpty()) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            foodAdapter.setFilterList(filteredFoodList);
        }
        //Drink
        List<Drink> filteredDrinkList = new ArrayList<>();
        for (Drink itemDrink : drinksList) {
            if (itemDrink.getName().toLowerCase().contains(newText.toLowerCase())) {
                filteredDrinkList.add(itemDrink);
            }
        }
        if (filteredDrinkList.isEmpty()) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            drinksAdapter.setFilterList2(filteredDrinkList);
        }
    }

    private List<Drink> getListDrink() {
        List<Drink> drinkList = new ArrayList<>();
        drinkList.add(new Drink(1, ""));
        drinkList.add(new Drink(2, ""));
        drinkList.add(new Drink(3, ""));
        drinkList.add(new Drink(4, ""));
        drinkList.add(new Drink(5, ""));
        drinkList.add(new Drink(6, ""));
        drinkList.add(new Drink(7, ""));
        drinkList.add(new Drink(8, ""));

        return drinkList;
    }

    private List<Food> getListFood() {
        List<Food> list = new ArrayList<>();
        list.add(new Food("", 1));
        list.add(new Food("", 2));
        list.add(new Food("", 3));
        list.add(new Food("", 4));
        list.add(new Food("", 5));
        list.add(new Food("", 6));
        list.add(new Food("", 7));
        return list;
    }
}