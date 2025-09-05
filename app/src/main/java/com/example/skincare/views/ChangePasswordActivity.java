package com.example.skincare.views;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.skincare.R;
import com.example.skincare.network.ApiService;
import com.example.skincare.network.RetrofitClient;
import com.example.skincare.requests.GenericResponse;
import com.example.skincare.requests.ResetPasswordRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText etNewPassword, etConfirmPassword;
    Button btnUpdatePassword;
    String email;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);

        apiService = RetrofitClient.getApi(this);

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);

        email = getIntent().getStringExtra("email");

        btnUpdatePassword.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String pass = etNewPassword.getText().toString().trim();
        String confirm = etConfirmPassword.getText().toString().trim();

        if (!pass.equals(confirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.resetPassword(new ResetPasswordRequest(email, pass, confirm))
                .enqueue(new Callback<GenericResponse>() {
                    @Override
                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            Toast.makeText(ChangePasswordActivity.this, "Password updated", Toast.LENGTH_SHORT).show();
                            finish(); // back to login
                        } else {
                            Toast.makeText(ChangePasswordActivity.this,
                                    response.body() != null ? response.body().getMessage() : "Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        Toast.makeText(ChangePasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
