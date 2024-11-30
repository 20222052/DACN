package com.example.dacn.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dacn.Model.SanPham;
import com.example.dacn.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<SanPham> productList;
    private OnAddToCartListener onAddToCartListener;

    public ProductAdapter(List<SanPham> productList, OnAddToCartListener listener) {
        this.productList = productList;
        this.onAddToCartListener = listener;
    }

    // Set listener cho Activity hoặc Fragment
    public void setOnAddToCartListener(OnAddToCartListener listener) {
        this.onAddToCartListener = listener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        SanPham product = productList.get(position);

        holder.tvFoodName.setText(product.getTenSanPham());

        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        TextView tvFoodName, tvDescription, tvPrice;
        ImageButton btnAdd;

        public ProductViewHolder(View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.img_food);
            tvFoodName = itemView.findViewById(R.id.tv_foodName);
            tvDescription = itemView.findViewById(R.id.tv_discription);
            tvPrice = itemView.findViewById(R.id.tv_Price);
            btnAdd = itemView.findViewById(R.id.imgbtn_add);
        }

        public void bind(SanPham product) {
            tvFoodName.setText(product.getTenSanPham());
            tvDescription.setText(product.getMoTa());
            tvPrice.setText(String.valueOf(product.getGia()));
            // Giả sử hình ảnh được tải bằng Glide
            Glide.with(itemView.getContext()).load(product.getHinhAnh()).into(imgFood);

            btnAdd.setOnClickListener(v -> {
                if (onAddToCartListener != null) {
                    onAddToCartListener.onAddToCart(product); // Thêm sản phẩm vào giỏ hàng
                }
            });
        }
    }

    public interface OnAddToCartListener {
        void onAddToCart(SanPham product);
    }
}

