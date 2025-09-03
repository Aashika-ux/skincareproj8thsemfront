package com.example.skincare.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.skincare.R;
import com.example.skincare.model.Product;

import java.util.List;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.ViewHolder> {

    private List<Product> products;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Product product);
        void onDeleteClick(Product product);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public RoutineAdapter(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_routine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.tvName.setText(product.getName());


        Glide.with(holder.itemView.getContext())
                .load(product.getImage() != null ? product.getImage() : R.drawable.amcare)
                .placeholder(R.drawable.amcare)
                .into(holder.ivImage);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(product);
        });

        // Click for delete
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(product);
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivImage;
        ImageView btnDelete;  // <-- delete button

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.product_name);
            ivImage = itemView.findViewById(R.id.product_image);
            btnDelete = itemView.findViewById(R.id.btn_delete_routine); // must add in layout

        }
    }

}
