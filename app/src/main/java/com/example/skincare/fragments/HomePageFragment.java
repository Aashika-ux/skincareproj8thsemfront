package com.example.skincare.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.skincare.R;
import com.example.skincare.adapter.HighlightAdapter;
import com.example.skincare.adapter.ProductAdapter;
import com.example.skincare.adapter.SliderAdapter;
import com.example.skincare.model.ApiResponse;
import com.example.skincare.model.Doctor;
import com.example.skincare.model.Product;
import com.example.skincare.network.RetrofitClient;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageFragment extends Fragment {

    private RecyclerView recyclerProducts;
    private ProductAdapter productAdapter;
    private Spinner spinnerFilter;
    private AutoCompleteTextView searchInput;

    private List<Product> allProducts = new ArrayList<>();
    private List<Product> filteredProducts = new ArrayList<>();
    private List<String> productNames = new ArrayList<>();
    private HighlightAdapter suggestionAdapter;

    // Slider
    private ViewPager sliderViewPager;
    private SliderAdapter sliderAdapter;
    private List<Product> sliderProducts = new ArrayList<>();
    private Handler sliderHandler;
    private int currentSliderPosition = 0;

    public HomePageFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        recyclerProducts = view.findViewById(R.id.recycler_products);
        recyclerProducts.setLayoutManager(new GridLayoutManager(getContext(), 3));

        spinnerFilter = view.findViewById(R.id.spinner_filter);
        searchInput = view.findViewById(R.id.search_input);

        productAdapter = new ProductAdapter(getContext());
        recyclerProducts.setAdapter(productAdapter);

        // Search suggestions
        suggestionAdapter = new HighlightAdapter(getContext(),
                android.R.layout.simple_dropdown_item_1line, productNames);
        searchInput.setAdapter(suggestionAdapter);
        searchInput.setThreshold(1);

        searchInput.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedName = (String) parent.getItemAtPosition(position);
            for (Product p : allProducts) {
                if (p.getName().equalsIgnoreCase(selectedName)) {
                    openProductDetail(p);
                    searchInput.setText("");
                    break;
                }
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
                suggestionAdapter.setKeyword(s.toString());
                suggestionAdapter.notifyDataSetChanged();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        productAdapter.setOnItemClickListener(this::openProductDetail);

        // Spinner filter
        spinnerFilter.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                if (selected.equalsIgnoreCase("All")) {
                    filteredProducts = new ArrayList<>(allProducts);
                } else {
                    filteredProducts = new ArrayList<>();
                    for (Product p : allProducts) {
                        if (p.getRecommendedFor() != null &&
                                p.getRecommendedFor().trim().equalsIgnoreCase(selected.trim())) {
                            filteredProducts.add(p);
                        }
                    }
                }
                productAdapter.setProductList(filteredProducts);
                updateSearchSuggestions(filteredProducts);
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // Slider setup
        sliderViewPager = view.findViewById(R.id.sliderViewPager);
        sliderProducts = new ArrayList<>();
        sliderAdapter = new SliderAdapter(getContext(), sliderProducts);
        sliderViewPager.setAdapter(sliderAdapter);

        fetchProducts();

        return view;
    }

    private void fetchProducts() {
        Call<ApiResponse<List<Product>>> call = RetrofitClient.getApi(getContext()).getProducts();
        call.enqueue(new Callback<ApiResponse<List<Product>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Product>>> call,
                                   @NonNull Response<ApiResponse<List<Product>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    allProducts = response.body().getData();
                    filteredProducts = new ArrayList<>(allProducts);
                    productAdapter.setProductList(filteredProducts);

                    // Slider
                    sliderProducts.clear();
                    sliderProducts.addAll(allProducts);
                    sliderAdapter.notifyDataSetChanged();
                    startAutoSlider();

                    // Spinner
                    Set<String> recommendedSet = new LinkedHashSet<>();
                    recommendedSet.add("All");
                    for (Product p : allProducts) {
                        if (p.getRecommendedFor() != null && !p.getRecommendedFor().trim().isEmpty()) {
                            recommendedSet.add(p.getRecommendedFor().trim());
                        }
                    }
                    spinnerFilter.setAdapter(new android.widget.ArrayAdapter<>(getContext(),
                            R.layout.spinner_selected_item, new ArrayList<>(recommendedSet)));

                    updateSearchSuggestions(allProducts);

                } else {
                    Toast.makeText(getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Product>>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterProducts(String keyword) {
        if (keyword.isEmpty()) {
            productAdapter.setProductList(filteredProducts);
            return;
        }
        List<Product> tempList = new ArrayList<>();
        for (Product p : filteredProducts) {
            if (p.getName().toLowerCase().contains(keyword.toLowerCase())) {
                tempList.add(p);
            }
        }
        productAdapter.setProductList(tempList);
    }

    private void updateSearchSuggestions(List<Product> list) {
        productNames.clear();
        for (Product p : list) {
            productNames.add(p.getName());
        }
        suggestionAdapter.notifyDataSetChanged();
    }

    private void openProductDetail(Product product) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", product.getId());
        bundle.putString("name", product.getName());
        bundle.putString("description", product.getDescription());
        bundle.putString("expectedResults", product.getExpectedResults());
        bundle.putString("usageInstructions", product.getUsageInstructions());
        bundle.putString("timeOfUse", product.getTimeOfUse());
        bundle.putString("shelfLife", product.getShelfLife());
        bundle.putString("incompatibleProducts", product.getIncompatibleProducts());
        bundle.putString("image", product.getImage());
        bundle.putString("recommendedFor", product.getRecommendedFor());

        ArrayList<Doctor> doctorsList = new ArrayList<>();
        List<Doctor> doctors = product.getRecommendedByDoctors();
        if (doctors != null) doctorsList.addAll(doctors);
        bundle.putSerializable("recommendedByDoctorsList", doctorsList);

        ProductDetailsFragment detailsFragment = new ProductDetailsFragment();
        detailsFragment.setArguments(bundle);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.frameFragmentLayout, detailsFragment)
                .addToBackStack(null)
                .commit();
    }

    private void startAutoSlider() {
        if (sliderProducts.size() == 0) return;

        sliderHandler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (sliderProducts.size() == 0) return;

                if (currentSliderPosition >= sliderProducts.size()) {
                    currentSliderPosition = 0;
                }

                sliderViewPager.setCurrentItem(currentSliderPosition++, true);
                sliderHandler.postDelayed(this, 3000); // 3 seconds
            }
        };
        sliderHandler.postDelayed(runnable, 3000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (sliderHandler != null) {
            sliderHandler.removeCallbacksAndMessages(null);
        }
    }
}
