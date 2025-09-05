package com.example.skincare;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApp extends Application {

    private static MyApp instance;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Initialize Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("https://your-api-base-url.com/") // Replace with your backend URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static MyApp getInstance() {
        return instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
