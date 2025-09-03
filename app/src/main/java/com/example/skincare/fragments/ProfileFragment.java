package com.example.skincare.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.skincare.R;
import com.example.skincare.imp.PreferenceManager;
import com.example.skincare.network.RetrofitClient;
import com.example.skincare.views.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  ProfileFragment extends Fragment {

    private CardView logoutBtn,doctorsCard,skinProfileCard, routinecard;
    public TextView username, text_profile_name, email;
    private PreferenceManager preferenceManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize preferenceManager before using it
        preferenceManager = new PreferenceManager(requireContext());

        username = view.findViewById(R.id.username);
        text_profile_name = view.findViewById(R.id.text_profile_name);
        email = view.findViewById(R.id.email);
        logoutBtn = view.findViewById(R.id.logoutBtn);
        doctorsCard = view.findViewById(R.id.doctors);
        skinProfileCard = view.findViewById(R.id.userProfile);

        routinecard = view.findViewById(R.id.myroutine);

        routinecard.setOnClickListener(v -> {
            RoutinePageFragment routinePageFragment = new RoutinePageFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frameFragmentLayout, routinePageFragment)
                    .addToBackStack(null) // allows back navigation
                    .commit();
        });

        skinProfileCard.setOnClickListener(v -> {
            UserProfileFragment userProfileFragment = new UserProfileFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frameFragmentLayout, userProfileFragment)
                    .addToBackStack(null) // allows back navigation
                    .commit();
        });
        // Set click listener for Specialists Card
        doctorsCard.setOnClickListener(v -> {
            DoctorsFragment doctorsFragment = new DoctorsFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frameFragmentLayout, doctorsFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Load saved user info safely
        String savedUsername = preferenceManager.getUsername();
        String savedEmail = preferenceManager.getEmail();

        if (savedUsername != null && !savedUsername.isEmpty()) {
            username.setText("Welcome, " + savedUsername);
            text_profile_name.setText(savedUsername);
        } else {
            username.setText("Welcome, Guest");
            text_profile_name.setText("Guest");
        }

        if (savedEmail != null && !savedEmail.isEmpty()) {
            email.setText(savedEmail);
        } else {
            email.setText("example@mail.com");
        }

        logoutBtn.setOnClickListener(v -> performLogout());

        return view;
    }


    private void performLogout() {
        RetrofitClient.getApi(requireContext()).logoutUser().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    preferenceManager.clearToken();
                    preferenceManager.clearUser(); // Make sure user info is cleared on logout
                    Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Logout failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
