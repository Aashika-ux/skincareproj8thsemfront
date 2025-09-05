package com.example.skincare.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.skincare.R;
import com.example.skincare.network.ApiService;
import com.example.skincare.network.RetrofitClient;
import com.example.skincare.requests.GenericResponse;
import com.example.skincare.requests.OtpVerifyRequest;
import com.example.skincare.requests.OtpRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerificationActivity extends AppCompatActivity {

    EditText etOtp1, etOtp2, etOtp3, etOtp4;
    Button btnVerifyOtp;
    TextView tvResendOtp;
    String email;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp);

        apiService = RetrofitClient.getApi(this);

        etOtp1 = findViewById(R.id.etOtp1);
        etOtp2 = findViewById(R.id.etOtp2);
        etOtp3 = findViewById(R.id.etOtp3);
        etOtp4 = findViewById(R.id.etOtp4);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        tvResendOtp = findViewById(R.id.tvResendOtp);

        email = getIntent().getStringExtra("email");

        btnVerifyOtp.setOnClickListener(v -> verifyOtp());
        tvResendOtp.setOnClickListener(v -> sendOtp());
    }

    private void sendOtp() {
        apiService.sendOtp(new OtpRequest(email)).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(OtpVerificationActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(OtpVerificationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyOtp() {
        String otp = etOtp1.getText().toString().trim() +
                etOtp2.getText().toString().trim() +
                etOtp3.getText().toString().trim() +
                etOtp4.getText().toString().trim();

        apiService.verifyOtp(new OtpVerifyRequest(email, otp)).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Intent intent = new Intent(OtpVerificationActivity.this, ChangePasswordActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(OtpVerificationActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(OtpVerificationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
