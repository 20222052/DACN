package com.example.dacn.View;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;
import java.text.Normalizer;
import java.util.Collections;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dacn.Controller.QL_menuAdapter;
import com.example.dacn.Controller.QL_menuController;
import com.example.dacn.Model.DanhMuc;
import com.example.dacn.Model.SanPham;
import com.example.dacn.Model.StringUtils;
import com.example.dacn.R;

import java.util.ArrayList;
import java.util.List;

public class QL_menu_Fragment extends Fragment {
    private Button btn_add_product, btn_edit_product, btn_delete_product, btn_clear_product,btn_edit_catalogue;
    private EditText edit_product_name, edit_product_price, edit_product_description;
    private Spinner spinner_category,Sort_name,Sort_price;
    private RadioGroup radio_group_visibility;
    private QL_menuController qlMenuController;
    private GridView grid_view_products;
    private QL_menuAdapter productsAdapter;
    private List<SanPham> productList;
    public SanPham sanPhamud;
    private SearchView search_food;

    public QL_menu_Fragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qlMenuController = new QL_menuController(requireContext());
        productList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_l_menu_, container, false);

        initializeViews(view);
        setupButtonListeners();
        setupSearchView();
        loadCategoriesAndProducts();
        setupSortSpinners();
        return view;
    }

    private void initializeViews(View view) {
        edit_product_name = view.findViewById(R.id.edit_product_name);
        edit_product_price = view.findViewById(R.id.edit_product_price);
        edit_product_description = view.findViewById(R.id.edit_product_description);
        spinner_category = view.findViewById(R.id.spinner_category);
        radio_group_visibility = view.findViewById(R.id.radio_group_visibility);
        btn_add_product = view.findViewById(R.id.btn_add_product);
        btn_edit_product = view.findViewById(R.id.btn_edit_product);
        btn_delete_product = view.findViewById(R.id.btn_delete_product);
        btn_clear_product = view.findViewById(R.id.btn_clear_product);
        btn_edit_catalogue = view.findViewById(R.id.btn_edit_catalogue);
        grid_view_products = view.findViewById(R.id.grid_view_products);
        search_food = view.findViewById(R.id.search_food_menu);
        Sort_name = view.findViewById(R.id.Sort_name);
        Sort_price = view.findViewById(R.id.Sort_price);
    }
    private void setupSortSpinners() {
        Sort_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                if (selectedItem.equals("A-Z")) {
                    sortProductsAZ();
                } else if (selectedItem.equals("Z-A")) {
                    sortProductsZA();
                }
                else {
                    loadCategoriesAndProducts(); // Default sorting
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                loadCategoriesAndProducts(); // Default sorting
            }
        });

        Sort_price.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                if (selectedItem.equals("Giá Thấp - Cao")) {
                    sortProductsPriceLowToHigh();
                } else if (selectedItem.equals("Giá Cao - Thấp")) {
                    sortProductsPriceHighToLow();
                }
                else {
                    loadCategoriesAndProducts(); // Default sorting
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                loadCategoriesAndProducts(); // Default sorting
            }
        });
    }

    private void sortProductsAZ() {
        productList.sort((p1, p2) -> p1.getTenSanPham().compareToIgnoreCase(p2.getTenSanPham()));
        productsAdapter.notifyDataSetChanged();
    }

    private void sortProductsZA() {
        productList.sort((p1, p2) -> p2.getTenSanPham().compareToIgnoreCase(p1.getTenSanPham()));
        productsAdapter.notifyDataSetChanged();
    }

    private void sortProductsPriceLowToHigh() {
        productList.sort((p1, p2) -> Float.compare(p1.getGia(), p2.getGia()));
        productsAdapter.notifyDataSetChanged();
    }

    private void sortProductsPriceHighToLow() {
        productList.sort((p1, p2) -> Float.compare(p2.getGia(), p1.getGia()));
        productsAdapter.notifyDataSetChanged();
    }
    private void setupButtonListeners() {
        btn_add_product.setOnClickListener(v -> {
            Fragment_add_product addProductDialogFragment = new Fragment_add_product();
            addProductDialogFragment.show(getParentFragmentManager(), "Fragment_add_product");
            loadCategoriesAndProducts();
        });
        btn_edit_catalogue.setOnClickListener(v -> {
            Fragment_edit_catalogue addProductDialogFragment = new Fragment_edit_catalogue();
            addProductDialogFragment.show(getParentFragmentManager(), "Fragment_edit_catalogue");
        });
        btn_edit_product.setOnClickListener(v -> {
            if (validateInputFields()) {
                updateSanPhamFromInput(sanPham -> {
                    sanPhamud.setTenSanPham(edit_product_name.getText().toString());
                    sanPhamud.setGia(Float.parseFloat(edit_product_price.getText().toString()));
                    sanPhamud.setMoTa(edit_product_description.getText().toString());
                    sanPhamud.setTrangThai(radio_group_visibility.getCheckedRadioButtonId() == R.id.radio_show);
                    DanhMuc selectedCategory = (DanhMuc) spinner_category.getSelectedItem();
                    sanPhamud.setMaDanhMuc(selectedCategory != null ? selectedCategory.getMaDanhMuc() : -1);
                    qlMenuController.updateProduct(sanPhamud);
                    loadCategoriesAndProducts();
                });
            }
        });

        btn_delete_product.setOnClickListener(v -> {
            try {
                qlMenuController.deleteProduct(sanPhamud.getMaSanPham());
                loadCategoriesAndProducts(); // Refresh list after deleting
            } catch (NumberFormatException e) {
                showToast("Invalid product ID");
            }
        });

        btn_clear_product.setOnClickListener(v -> clearForm());
    }
    private void setupSearchView() {
        search_food.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    private void filter(String text) {
        String normalizedText = StringUtils.removeDiacritics(text.toLowerCase());
        List<SanPham> filteredList = new ArrayList<>();
        for (SanPham item : productList) {
            String normalizedProductName = StringUtils.removeDiacritics(item.getTenSanPham().toLowerCase());
            if (normalizedProductName.contains(normalizedText)) {
                filteredList.add(item);
            }
        }
        productsAdapter.filterList(filteredList);
    }
    private void loadCategoriesAndProducts() {
        // Load categories first
        qlMenuController.getDanhMucSp(danhMucList -> {
            // Populate category spinner
            ArrayAdapter<DanhMuc> categoryAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    danhMucList
            );
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_category.setAdapter(categoryAdapter);

            // Load products
            qlMenuController.getProductList(products -> {
                if (products != null && !products.isEmpty()) {
                    productList = products;
                    productsAdapter = new QL_menuAdapter(requireContext(), productList);
                    grid_view_products.setAdapter(productsAdapter);

                    // Optional: Add item click listener to populate form when a product is selected
                    grid_view_products.setOnItemClickListener((parent, view, position, id) -> {
                        SanPham selectedProduct = productList.get(position);
                        sanPhamud = selectedProduct;
                        Log.d("QL_menu_Fragment", "Selected product: " + position);
                        populateForm(selectedProduct);
                    });
                } else {
                    showToast("No products found");
                }
            });
        });
    }

    private void populateForm(SanPham product) {
        edit_product_name.setText(String.valueOf(product.getTenSanPham()));
        edit_product_price.setText(String.valueOf(product.getGia()));
        edit_product_description.setText(product.getMoTa());

        // Set visibility radio button
        radio_group_visibility.clearCheck();
        if (product.getTrangThai()) {
            radio_group_visibility.check(R.id.radio_show);
        } else {
            radio_group_visibility.check(R.id.radio_hide);
        }

        // Set category spinner
        for (int i = 0; i < spinner_category.getCount(); i++) {
            DanhMuc category = (DanhMuc) spinner_category.getItemAtPosition(i);
            if (category.getMaDanhMuc() == product.getMaDanhMuc()) {
                spinner_category.setSelection(i);
                break;
            }
        }
    }

    private boolean validateInputFields() {
        String name = edit_product_name.getText().toString().trim();
        String priceStr = edit_product_price.getText().toString().trim();
        String description = edit_product_description.getText().toString().trim();

        if (name.isEmpty()) {
            showToast("Product name cannot be empty");
            return false;
        }

        try {
            float price = Float.parseFloat(priceStr);
            if (price <= 0) {
                showToast("Price must be a positive number");
                return false;
            }
        } catch (NumberFormatException e) {
            showToast("Invalid price format");
            return false;
        }

        if (description.isEmpty()) {
            showToast("Product description cannot be empty");
            return false;
        }

        return true;
    }

    private void updateSanPhamFromInput(ProductCountCallback callback) {
        String name = edit_product_name.getText().toString();
        float price = Float.parseFloat(edit_product_price.getText().toString());
        String description = edit_product_description.getText().toString();
        DanhMuc selectedCategory = (DanhMuc) spinner_category.getSelectedItem();
        int categoryCode = selectedCategory != null ? selectedCategory.getMaDanhMuc() : -1;
        boolean isVisible = radio_group_visibility.getCheckedRadioButtonId() == R.id.radio_show;
        SanPham sanPham = new SanPham(isVisible, price, sanPhamud.getHinhAnh(), name, categoryCode, sanPhamud.getMaSanPham(), description, sanPhamud.getSoLuong());
        callback.onProductCountReceived(sanPham);
    }

    private void clearForm() {
        edit_product_name.setText("");
        edit_product_price.setText("");
        edit_product_description.setText("");
        radio_group_visibility.clearCheck();
        radio_group_visibility.check(R.id.radio_show);
        if(sanPhamud == null){
            spinner_category.setSelection(0);
        }
        else {
            sanPhamud.setMaSanPham(-1);
            sanPhamud.setTenSanPham("");
            sanPhamud.setGia(0);
            sanPhamud.setMoTa("");
            sanPhamud.setTrangThai(true);
            sanPhamud.setMaDanhMuc(-1);
            sanPhamud.setSoLuong(0);
            sanPhamud.setHinhAnh("");
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    public interface ProductCountCallback {
        void onProductCountReceived(SanPham sanPham);
    }
}