package com.example.skincare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.skincare.R;
import com.example.skincare.model.SkinType;

import java.util.List;

public class SkinAdapter extends RecyclerView.Adapter<SkinAdapter.SkinViewHolder> {

    private Context context;
    private List<SkinType> skinList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SkinType skinType);
    }

    public SkinAdapter(Context context, List<SkinType> skinList, OnItemClickListener listener) {
        this.context = context;
        this.skinList = skinList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SkinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_skin_type, parent, false);
        return new SkinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkinViewHolder holder, int position) {
        SkinType skin = skinList.get(position);
        holder.txtName.setText(skin.getName());

        Glide.with(context)
                .load(skin.getImageUrl())
                .placeholder(R.drawable.acne)
                .into(holder.imgSkin);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(skin));
    }

    @Override
    public int getItemCount() {
        return skinList.size();
    }

    static class SkinViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSkin;
        TextView txtName;

        SkinViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSkin = itemView.findViewById(R.id.imgSkinType);
            txtName = itemView.findViewById(R.id.txtSkinName);
        }
    }
}
