package com.example.skincare.requests;

public class GenericResponse {
    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
