package com.example.skincare.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.skincare.R;
import com.example.skincare.model.ApiResponse;
import com.example.skincare.model.Doctor;
import com.example.skincare.model.Product;
import com.example.skincare.network.ApiService;
import com.example.skincare.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsFragment extends Fragment {

    private ImageButton btnBack;
    private ImageView productImage;
    private TextView productName, recommendedFor, productDescription, productUsageContent,
            contentExpectedResults, timeOfUse, shelfLife, tvRecommendedBy;
    private ChipGroup chipGroupIncompatible;
    private MaterialButton btnAddToRoutine;
    private Product product;
    private int productId;
    private boolean openedFromRoutine = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnBack = view.findViewById(R.id.btn_back);
        productImage = view.findViewById(R.id.product_image);
        productName = view.findViewById(R.id.product_name);
        recommendedFor = view.findViewById(R.id.recommended_for);
        productDescription = view.findViewById(R.id.product_description);
        productUsageContent = view.findViewById(R.id.product_usage_content);
        contentExpectedResults = view.findViewById(R.id.content_expected_results);
        timeOfUse = view.findViewById(R.id.time_of_use);
        shelfLife = view.findViewById(R.id.shelf_life);
        chipGroupIncompatible = view.findViewById(R.id.chipgroup_incompatible_products);
        tvRecommendedBy = view.findViewById(R.id.tv_recommended_by);
        btnAddToRoutine = view.findViewById(R.id.btn_add_to_routine);

        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        if (getArguments() != null) {
            productId = getArguments().getInt("id");
            openedFromRoutine = getArguments().getBoolean("fromRoutine", false);

            if (openedFromRoutine) {
                btnAddToRoutine.setVisibility(View.GONE);
            } else {
                btnAddToRoutine.setOnClickListener(v -> addToRoutine());
            }

            fetchProductDetailsFromBackend(productId);
        }
    }

    private void fetchProductDetailsFromBackend(int id) {
        ApiService apiService = RetrofitClient.getApi(getContext());
        Call<ApiResponse<Product>> call = apiService.getProductDetails(id);

        call.enqueue(new Callback<ApiResponse<Product>>() {
            @Override
            public void onResponse(Call<ApiResponse<Product>> call, Response<ApiResponse<Product>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    product = response.body().getData();
                    showProductDetails(product);
                    showRecommendedDoctors(product.getRecommendedByDoctors());
                } else {
                    Toast.makeText(getContext(), "Failed to load product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProductDetails(Product product) {
        productName.setText(product.getName());
        recommendedFor.setText("Skin type: " + product.getRecommendedFor());
        productDescription.setText(product.getDescription());
        productUsageContent.setText(product.getUsageInstructions());
        contentExpectedResults.setText(product.getExpectedResults());
        timeOfUse.setText(product.getTimeOfUse());
        shelfLife.setText(product.getShelfLife());

        Glide.with(getContext())
                .load(product.getImage() != null ? product.getImage() : R.drawable.amcare)
                .placeholder(R.drawable.amcare)
                .into(productImage);

        chipGroupIncompatible.removeAllViews();
        if (product.getIncompatibleProducts() != null && !product.getIncompatibleProducts().isEmpty()) {
            String[] items = product.getIncompatibleProducts().split(",");
            for (String item : items) {
                Chip chip = new Chip(getContext());
                chip.setText(item.trim());
                chip.setTextColor(Color.WHITE);
                chip.setChipBackgroundColorResource(R.color.primary_color);
                ShapeAppearanceModel shapeModel = chip.getShapeAppearanceModel()
                        .toBuilder()
                        .setAllCorners(CornerFamily.ROUNDED, 16f)
                        .build();
                chip.setShapeAppearanceModel(shapeModel);
                chip.setClickable(false);
                chipGroupIncompatible.addView(chip);
            }
        }
    }

    private void showRecommendedDoctors(List<Doctor> doctors) {
        if (doctors != null && !doctors.isEmpty()) {
            StringBuilder names = new StringBuilder();
            for (Doctor d : doctors) {
                if (names.length() > 0) names.append(", ");
                names.append(d.getName()).append(" (").append(d.getSpecialization()).append(")");
            }
            tvRecommendedBy.setText("Recommended by: " + names.toString());
        } else {
            tvRecommendedBy.setText("No doctors recommended this product.");
        }
    }

    private void addToRoutine() {
        if (product == null) return;

        ApiService apiService = RetrofitClient.getApi(requireContext());
        apiService.addToRoutine(product.getId()).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    // Refresh Routine page if visible
                    Fragment fragment = getParentFragmentManager().findFragmentById(R.id.frameFragmentLayout);
                    if (fragment instanceof RoutinePageFragment) {
                        ((RoutinePageFragment) fragment).fetchRoutineProducts();
                    }
                } else {
                    Toast.makeText(getContext(), " product already added to routine", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
