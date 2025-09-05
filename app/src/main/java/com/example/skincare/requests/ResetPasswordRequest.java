package com.example.skincare.requests;

public class ResetPasswordRequest {
    private String email;
    private String password;
    private String password_confirmation;

    public ResetPasswordRequest(String email, String password, String password_confirmation) {
        this.email = email;
        this.password = password;
        this.password_confirmation = password_confirmation;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }
}
