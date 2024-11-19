package com.example.dacn.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dacn.R;

import java.util.ArrayList;
import java.util.List;

public class Staff extends AppCompatActivity {

    ImageButton bellButton;
    Button btn_huy, btn_xacnhan;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_staff);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.khachhang), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        loadData();

        bellButton.setOnClickListener(view -> showNotificationFragment());
        btn_xacnhan.setOnClickListener(view -> showHoadonFragment());

    }

    private void loadData() {
        GridView gridViewMenu = findViewById(R.id.gridView_menu);
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(R.drawable.pho_cuon, "Phở cuốn", "80.000"));
        menuItems.add(new MenuItem(R.drawable.lau_ga_la_e, "Lẩu gà lá é", "70.000"));
        menuItems.add(new MenuItem(R.drawable.lau_bo, "Lẩu bò", "70.000"));
        menuItems.add(new MenuItem(R.drawable.lau_kimchi, "Lẩu kim chi", "70.000"));
        menuItems.add(new MenuItem(R.drawable.lau_kimchi, "Lẩu kim chi", "70.000"));
        menuItems.add(new MenuItem(R.drawable.lau_kimchi, "Lẩu kim chi", "70.000"));
        menuItems.add(new MenuItem(R.drawable.lau_kimchi, "Lẩu kim chi", "70.000"));
        menuItems.add(new MenuItem(R.drawable.lau_kimchi, "Lẩu kim chi", "70.000"));
        menuItems.add(new MenuItem(R.drawable.lau_kimchi, "Lẩu kim chi", "70.000"));
        menuItems.add(new MenuItem(R.drawable.lau_kimchi, "Lẩu kim chi", "70.000"));
        menuItems.add(new MenuItem(R.drawable.lau_kimchi, "Lẩu kim chi", "70.000"));
        menuItems.add(new MenuItem(R.drawable.lau_kimchi, "Lẩu kim chi", "70.000"));
        menuItems.add(new MenuItem(R.drawable.lau_kimchi, "Lẩu kim chi", "70.000"));
        MenuAdapter adapter = new MenuAdapter(this, menuItems);
        gridViewMenu.setAdapter(adapter);

        GridView listView = findViewById(R.id.gridView_list);
        List<ListItem> listItems = new ArrayList<>();
        listItems.add(new ListItem("Phở cuốn", "80.000", "1"));
        listItems.add(new ListItem("Phở cuốn", "80.000", "1"));
        listItems.add(new ListItem("Phở cuốn", "80.000", "1"));
        listItems.add(new ListItem("Phở cuốn", "80.000", "1"));
        listItems.add(new ListItem("Phở cuốn", "80.000", "1"));
        listItems.add(new ListItem("Phở cuốn", "80.000", "1"));
        listItems.add(new ListItem("Phở cuốn", "80.000", "1"));
        listItems.add(new ListItem("Phở cuốn", "80.000", "1"));
        listItems.add(new ListItem("Phở cuốn", "80.000", "1"));
        listItems.add(new ListItem("Phở cuốn", "80.000", "1"));
        listItems.add(new ListItem("Phở cuốn", "80.000", "1"));
        listItems.add(new ListItem("Phở cuốn", "80.000", "1"));
        listItems.add(new ListItem("Phở cuốn", "80.000", "1"));
        listItems.add(new ListItem("Phở cuốn", "80.000", "1"));
        listItems.add(new ListItem("Phở cuốn", "80.000", "1"));
        listItems.add(new ListItem("Phở cuốn", "80.000", "1"));

        ListAdapter lst_adapter = new ListAdapter(this, listItems);
        listView.setAdapter(lst_adapter);
    }

    private void initView() {
        bellButton = findViewById(R.id.btn_bell);
        btn_huy = findViewById(R.id.btn_huy);
        btn_xacnhan = findViewById(R.id.btn_xacnhan);
    }

    private void showNotificationFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        NotificationFragment fragment = new NotificationFragment();
        transaction.add(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showHoadonFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        HoadonFragment fragment = new HoadonFragment();
        transaction.add(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public class MenuItem {
        private int imageResource;
        private String name;
        private String price;

        public MenuItem() {
        }

        public MenuItem(int imageResource, String name, String price) {
            this.imageResource = imageResource;
            this.price = price;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getImageResource() {
            return imageResource;
        }

        public void setImageResource(int imageResource) {
            this.imageResource = imageResource;
        }
    }

    public class ListItem {
        private String name;
        private String price;
        private int quantity;

        public ListItem() {
        }

        public ListItem(String name, String price, String quantity) {
            this.name = name;
            this.price = price;
            this.quantity = Integer.parseInt(quantity);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}