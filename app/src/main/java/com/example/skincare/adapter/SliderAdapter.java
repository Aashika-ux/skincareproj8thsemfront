package com.example.skincare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.skincare.R;
import com.example.skincare.model.Product;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private List<Product> productList;

    public SliderAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_item, container, false);

        ImageView imageView = view.findViewById(R.id.slider_image);
        TextView nameView = view.findViewById(R.id.slider_name);
        TextView whenUseView = view.findViewById(R.id.slider_when_to_use);

        Product product = productList.get(position);

        nameView.setText(product.getName());
        whenUseView.setText(product.getTimeOfUse());

        Glide.with(context)
                .load(product.getImage())
                .placeholder(R.drawable.slider)
                .into(imageView);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
