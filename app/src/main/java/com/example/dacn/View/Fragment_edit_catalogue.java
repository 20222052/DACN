package com.example.dacn.View;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.dacn.Controller.catalogueAdapter;
import com.example.dacn.Controller.catalogueController;
import com.example.dacn.Model.DanhMuc;
import com.example.dacn.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_edit_catalogue extends DialogFragment implements catalogueAdapter.OnItemClickListener {

    private EditText edtCatalogue;
    private GridView gvCartItems;
    private Button btnAddCatalogue, btnEditCatalogue, btnRemoveCatalogue;
    private catalogueController controller;
    private DanhMuc danhmuc_update;
    private List<DanhMuc> danhMucList;

    public Fragment_edit_catalogue() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ql_catalogue, container, false);

        controller = new catalogueController(requireContext());
        danhMucList = new ArrayList<>();
        danhmuc_update = new DanhMuc();
        initViews(view);
        setupGridView();
        setupButtons();

        return view;
    }

    private void initViews(View view) {
        edtCatalogue = view.findViewById(R.id.edt_catalogue);
        gvCartItems = view.findViewById(R.id.gv_cart_items);
        btnAddCatalogue = view.findViewById(R.id.btn_add_catalogue);
        btnEditCatalogue = view.findViewById(R.id.btn_edit_catalogue);
        btnRemoveCatalogue = view.findViewById(R.id.btn_remove_catalogue);
    }

    private void setupGridView() {
        controller.getAllDanhMuc(new catalogueController.DanhMucListener() {
            @Override
            public void onDanhMucLoaded(List<DanhMuc> danhMucList) {
                if (isAdded()) { // Check if the fragment is attached to the activity
                    Fragment_edit_catalogue.this.danhMucList = danhMucList;
                    catalogueAdapter adapter = new catalogueAdapter(requireContext(), danhMucList);
                    adapter.setOnItemClickListener(Fragment_edit_catalogue.this); // Set listener
                    gvCartItems.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onItemClick(DanhMuc danhMuc) {
        if (danhMuc != null) {
            Log.d("Fragment_edit_catalogue", "Clicked on category with ID: " + danhMuc.getMaDanhMuc());

            // Only if you want to update danhmuc_update with the clicked item's data
            danhmuc_update.setMaDanhMuc(danhMuc.getMaDanhMuc());
            danhmuc_update.setTenDanhMuc(danhMuc.getTenDanhMuc());

            // Update the EditText with the selected category name
            edtCatalogue.setText(danhmuc_update.getTenDanhMuc());
        } else {
            Log.e("Fragment_edit_catalogue", "DanhMuc is null!");
        }
    }

    private void setupButtons() {
        btnAddCatalogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCatalogueName = edtCatalogue.getText().toString();
                if (!newCatalogueName.isEmpty()) {
                    controller.addDanhMuc(newCatalogueName);
                    edtCatalogue.setText("");
                    setupGridView();
                    Toast.makeText(getContext(), "Category added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Please enter a category name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnEditCatalogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedCatalogueName = edtCatalogue.getText().toString();
                    danhmuc_update.setTenDanhMuc(updatedCatalogueName);
                    controller.updateDanhMuc(danhmuc_update);
                    edtCatalogue.setText("");
                    setupGridView();
                    Toast.makeText(getContext(), "Category updated successfully", Toast.LENGTH_SHORT).show();
            }
        });

        btnRemoveCatalogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    controller.deleteDanhMuc(danhmuc_update.getMaDanhMuc());
                    edtCatalogue.setText("");
                    setupGridView();
                    Toast.makeText(getContext(), "Category removed successfully", Toast.LENGTH_SHORT).show();
            }
        });
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
}