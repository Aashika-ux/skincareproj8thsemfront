package com.example.skincare.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.skincare.R;
import com.example.skincare.imp.PreferenceManager;
import com.example.skincare.model.AuthResponse;
import com.example.skincare.network.RetrofitClient;
import com.example.skincare.requests.LoginRequest;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivityDebug";

    private EditText emailEt, passwordEt;
    private MaterialButton loginBtn;
    private PreferenceManager preferenceManager;
    private TextView reg ,forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize preference manager
        preferenceManager = new PreferenceManager(this);

        // Initialize UI
        emailEt = findViewById(R.id.email);
        passwordEt = findViewById(R.id.password);
        loginBtn = findViewById(R.id.btLogin);
        reg = findViewById(R.id.Register);
        forgot_password =findViewById(R.id.forgot_password);

        // Register click
        forgot_password.setOnClickListener(v -> {

            startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
        });


        // Register click
        reg.setOnClickListener(v -> {
            Log.d(TAG, "Register clicked");
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        // Login click
        loginBtn.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString();

        if (email.isEmpty()) {
            emailEt.setError("Email required");
            emailEt.requestFocus();
            Log.d(TAG, "Email is empty");
            return;
        }

        if (password.isEmpty()) {
            passwordEt.setError("Password required");
            passwordEt.requestFocus();
            Log.d(TAG, "Password is empty");
            return;
        }

        Log.d(TAG, "Attempting login with email: " + email);

        // Create request
        LoginRequest request = new LoginRequest(email, password);

        // Call API
        RetrofitClient.getApi(this).loginUser(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.d(TAG, "Response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse auth = response.body();
                    String token = auth.getAccessToken();
                    Log.d(TAG, "Login success. Token: " + token);

                    // Save token
                    preferenceManager.saveToken(token);

                    // Save user info
                    if (auth.getUser() != null) {
                        preferenceManager.saveUser(
                                auth.getUser().getUsername(),
                                auth.getUser().getEmail()
                        );
                        Log.d(TAG, "User saved: " + auth.getUser().getUsername());
                    }

                    Toast.makeText(LoginActivity.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();

                    // Navigate to HomePage
                    Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e(TAG, "Login failed: " + errorBody);
                        Toast.makeText(LoginActivity.this, "Login failed: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading errorBody", e);
                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "Network error: ", t);
                Toast.makeText(LoginActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
