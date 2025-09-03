package com.example.skincare.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skincare.R;
import com.example.skincare.adapter.RoutineAdapter;
import com.example.skincare.model.ApiResponse;
import com.example.skincare.model.Product;
import com.example.skincare.network.ApiService;
import com.example.skincare.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutinePageFragment extends Fragment {

    private RecyclerView recyclerView;
    private RoutineAdapter adapter;
    private List<Product> routineProducts = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_routine_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_View_Routine);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RoutineAdapter(routineProducts);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RoutineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                // Open ProductDetailFragment without Add button
                ProductDetailsFragment fragment = new ProductDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id", product.getId());
                bundle.putBoolean("fromRoutine", true);
                fragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameFragmentLayout, fragment)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onDeleteClick(Product product) {
                ApiService apiService = RetrofitClient.getApi(requireContext());
                Call<ApiResponse<Void>> call = apiService.deleteRoutineProduct(product.getId());

                call.enqueue(new Callback<ApiResponse<Void>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Deleted from routine", Toast.LENGTH_SHORT).show();
                            routineProducts.remove(product);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Failed to delete", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                        Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        fetchRoutineProducts();
    }

    public void fetchRoutineProducts() {
        ApiService apiService = RetrofitClient.getApi(requireContext());
        Call<ApiResponse<List<Product>>> call = apiService.getRoutineProducts();

        call.enqueue(new Callback<ApiResponse<List<Product>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Product>>> call, Response<ApiResponse<List<Product>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    routineProducts.clear();
                    routineProducts.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Failed to load routine", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Product>>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
