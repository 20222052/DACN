package com.example.dacn.View;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.dacn.Controller.Add_productController;
import com.example.dacn.Model.DanhMuc;
import com.example.dacn.Model.SanPham;
import com.example.dacn.R;

import java.util.List;

public class Fragment_add_product extends DialogFragment {
    private EditText editProductName, editProductPrice, editProductDescription, linkProductImage;
    private Spinner spinnerCategory;
    private Button btnAddProduct;
    private Add_productController addProductController;

    public Fragment_add_product() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_product_layout, container, false);

        editProductName = view.findViewById(R.id.edit_product_name);
        editProductPrice = view.findViewById(R.id.edit_product_price);
        editProductDescription = view.findViewById(R.id.edit_product_description);
        linkProductImage = view.findViewById(R.id.link_product_image);
        spinnerCategory = view.findViewById(R.id.spinner_category);
        btnAddProduct = view.findViewById(R.id.btn_checkout);

        addProductController = new Add_productController(requireContext());

        btnAddProduct.setOnClickListener(v -> {
            if (validateInputFields()) {
                createSanPhamFromInput(sanPham -> {
                    addProductController.addProduct(sanPham, new Add_productController.ProductAddListener() {
                        @Override
                        public void onProductAdded() {
                            showToast("Product added successfully");
                            dismiss();
                        }

                        @Override
                        public void onProductAddFailed(Exception e) {
                            showToast("Failed to add product: " + e.getMessage());
                        }
                    });
                });
            }
        });

        loadCategoriesAndProducts();
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        return dialog;
    }

    private void loadCategoriesAndProducts() {
        addProductController.getDanhMucSp(new Add_productController.DanhMucListener() {
            @Override
            public void onDanhMucLoaded(List<DanhMuc> danhMucList) {
                if (isAdded()) { // Check if the fragment is attached to the activity
                    ArrayAdapter<DanhMuc> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, danhMucList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(adapter);
                }
            }
        });
    }

    private boolean validateInputFields() {
        String name = editProductName.getText().toString().trim();
        String priceStr = editProductPrice.getText().toString().trim();
        String description = editProductDescription.getText().toString().trim();

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

    private void createSanPhamFromInput(ProductCountCallback callback) {
        String name = editProductName.getText().toString();
        float price = Float.parseFloat(editProductPrice.getText().toString());
        String description = editProductDescription.getText().toString();
        DanhMuc selectedCategory = (DanhMuc) spinnerCategory.getSelectedItem();
        String linkImage = linkProductImage.getText().toString();
        int categoryCode = selectedCategory != null ? selectedCategory.getMaDanhMuc() : -1;

        addProductController.getProductCount(count -> {
            SanPham sanPham = new SanPham(true, price, linkImage, name, categoryCode, count + 1, description, 123);
            callback.onProductCountReceived(sanPham);
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    public interface ProductCountCallback {
        void onProductCountReceived(SanPham sanPham);
    }
}