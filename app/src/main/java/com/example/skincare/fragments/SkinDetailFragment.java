package com.example.skincare.fragments;

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
import com.example.skincare.model.SkinType;
import com.example.skincare.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SkinDetailFragment extends Fragment {

    private ImageView imageView;
    private TextView nameView, descView;
    private ImageButton btnReturn; // Added button


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_skin_detail, container, false);

        // Initialize views
        imageView = view.findViewById(R.id.detail_image);
        nameView = view.findViewById(R.id.detail_name);
        descView = view.findViewById(R.id.detail_description);
        btnReturn = view.findViewById(R.id.btn_return); // Initialize button


        // Button click listener
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to the previous fragment (SkinCareFragment)
                if (getActivity() != null) {
                    getParentFragmentManager().popBackStack();
                }
            }
        });

        // Get skin ID from bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            int skinId = bundle.getInt("id");
            fetchSkinDetail(skinId);
        }

        return view;
    }

    private void fetchSkinDetail(int id) {
        Call<SkinType> call = RetrofitClient.getApi(getContext()).getSkin(id);

        call.enqueue(new Callback<SkinType>() {
            @Override
            public void onResponse(@NonNull Call<SkinType> call, @NonNull Response<SkinType> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SkinType skin = response.body();
                    nameView.setText(skin.getName());
                    descView.setText(skin.getDescription());
                    Glide.with(getContext())
                            .load(skin.getImageUrl())
                            .placeholder(R.drawable.acne)
                            .into(imageView);


                } else {
                    Toast.makeText(getContext(), "Failed to load details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SkinType> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
