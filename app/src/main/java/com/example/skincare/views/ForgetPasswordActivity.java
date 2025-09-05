package com.example.skincare.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.skincare.R;
import com.example.skincare.network.ApiService;
import com.example.skincare.network.RetrofitClient;
import com.example.skincare.requests.GenericResponse;
import com.example.skincare.requests.OtpRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {

    TextInputEditText etEmail;
    MaterialButton btnSendOtp;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        apiService = RetrofitClient.getApi(this);

        etEmail = findViewById(R.id.emailEditText);
        btnSendOtp = findViewById(R.id.sendOtpBtn);

        btnSendOtp.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                etEmail.setError("Enter email");
                return;
            }
            sendOtp(email);
        });
    }

    private void sendOtp(String email) {
        apiService.sendOtp(new OtpRequest(email))
                .enqueue(new Callback<GenericResponse>() {
                    @Override
                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            Toast.makeText(ForgetPasswordActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgetPasswordActivity.this, OtpVerificationActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ForgetPasswordActivity.this,
                                    response.body() != null ? response.body().getMessage() : "Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        Toast.makeText(ForgetPasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
