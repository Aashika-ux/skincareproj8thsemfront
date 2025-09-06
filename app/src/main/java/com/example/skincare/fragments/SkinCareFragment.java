package com.example.skincare.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skincare.R;
import com.example.skincare.adapter.SkinAdapter;
import com.example.skincare.model.SkinType;
import com.example.skincare.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SkinCareFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_skin_care, container, false);
        recyclerView = view.findViewById(R.id.skinTypeRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        fetchSkinTypes();
        return view;
    }

    private void fetchSkinTypes() {
        Call<List<SkinType>> call = RetrofitClient.getApi(getContext()).getSkins();

        call.enqueue(new Callback<List<SkinType>>() {
            @Override
            public void onResponse(@NonNull Call<List<SkinType>> call, @NonNull Response<List<SkinType>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SkinType> list = response.body();

                    SkinAdapter adapter = new SkinAdapter(getContext(), list, skinType -> {
                        // Navigate to SkinDetailFragment
                        SkinDetailFragment detailFragment = new SkinDetailFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", skinType.getId());
                        detailFragment.setArguments(bundle);

                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameFragmentLayout, detailFragment)
                                .addToBackStack(null)
                                .commit();
                    });

                    recyclerView.setAdapter(adapter);

                } else {
                    Toast.makeText(getContext(), "Failed to fetch skin types", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SkinType>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
