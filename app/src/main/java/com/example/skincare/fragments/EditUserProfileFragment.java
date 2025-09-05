package com.example.skincare.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.skincare.R;
import com.example.skincare.model.UpdateProfileRequest;
import com.example.skincare.model.UserResponse;
import com.example.skincare.network.ApiService;
import com.example.skincare.network.RetrofitClient;
import com.example.skincare.imp.PreferenceManager;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserProfileFragment extends Fragment {

    private ImageView profileImageEdit;
    private EditText editName, editEmail, editAge, editGender, editPassword, editConfirmPassword,
            editSkinType, editRoutine;
    private MaterialButton btnSaveProfile;
    private PreferenceManager preferenceManager;

    public EditUserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user_profile, container, false);

        // Bind views
        profileImageEdit = view.findViewById(R.id.profile_image_edit);
        editName = view.findViewById(R.id.edit_name);
        editEmail = view.findViewById(R.id.edit_email);
        editAge = view.findViewById(R.id.edit_age);
        editGender = view.findViewById(R.id.edit_gender);
        editPassword = view.findViewById(R.id.edit_password);
        editConfirmPassword = view.findViewById(R.id.edit_confirm_password);
        editSkinType = view.findViewById(R.id.edit_skin_type);
        editRoutine = view.findViewById(R.id.edit_routine);
        btnSaveProfile = view.findViewById(R.id.btn_save_profile);

        // Initialize PreferenceManager
        preferenceManager = new PreferenceManager(requireContext());

        // Fetch user profile and populate fields
        fetchUserProfile();

        // Handle Save Changes
        btnSaveProfile.setOnClickListener(v -> saveProfile());

        return view;
    }

    private void fetchUserProfile() {
        ApiService apiService = RetrofitClient.getApi(requireContext());
        Call<UserResponse> call = apiService.getProfile();

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse.User user = response.body().getUser();
                    populateFields(user);
                } else {
                    Toast.makeText(getContext(), "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateFields(UserResponse.User user) {
        editName.setText(user.getUsername());
        editEmail.setText(user.getEmail());
        editAge.setText(user.getAge() != null ? String.valueOf(user.getAge()) : "");
        editGender.setText(user.getGender() != null ? user.getGender() : "");
        editSkinType.setText("Normal to Dry"); // replace with backend field if available
        editRoutine.setText("Morning: Cleanser, Vitamin C, Sunscreen\nEvening: Cleanser, Moisturizer, Retinol"); // replace if backend field
        // Optional: load profile image if backend provides URL
        // Glide.with(this).load(user.getProfileImageUrl()).into(profileImageEdit);
    }

    private void saveProfile() {
        String username = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String ageStr = editAge.getText().toString().trim();
        String gender = editGender.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();
        String skinType = editSkinType.getText().toString().trim();
        String routine = editRoutine.getText().toString().trim();

        // Basic validation
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Username and Email cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(password) && !password.equals(confirmPassword)) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        Integer age = null;
        if (!TextUtils.isEmpty(ageStr)) {
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid age", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Create request object
        UpdateProfileRequest request = new UpdateProfileRequest(
                username,
                email,
                age,
                gender,
                !TextUtils.isEmpty(password) ? password : null,
                !TextUtils.isEmpty(password) ? confirmPassword : null
        );

        ApiService apiService = RetrofitClient.getApi(requireContext());
        Call<UserResponse> call = apiService.updateProfile(request);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse.User updatedUser = response.body().getUser();

                    // Update PreferenceManager with latest data
                    preferenceManager.saveUser(
                            updatedUser.getUsername(),
                            updatedUser.getEmail()
                    );


                    Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();

                    // Go back to ProfileFragment
                    getParentFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
