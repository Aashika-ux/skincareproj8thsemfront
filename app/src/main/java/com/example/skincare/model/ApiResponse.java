package com.example.skincare.model;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {

    private boolean success;
    private T data;

    @SerializedName("message")
    private String message; // add this


    // Getters
    public boolean isSuccess() { return success; }
    public T getData() { return data; }
    public String getMessage() { return message; } // getter for message

    // Setters
    public void setSuccess(boolean success) { this.success = success; }
    public void setData(T data) { this.data = data; }
}
