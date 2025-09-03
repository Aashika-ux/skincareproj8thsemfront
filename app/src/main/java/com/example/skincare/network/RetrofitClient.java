package com.example.skincare.network;

import android.content.Context;

import com.example.skincare.imp.PreferenceManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient {

    private static final String BASE_URL = "http://192.168.100.47:8000/api/";
    private static Retrofit retrofit = null;

    public static ApiService getApi(Context context) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder();

                        // Get token from SharedPreferences
                        PreferenceManager preferenceManager = new PreferenceManager(context);
                        String token = preferenceManager.getToken();
                        if (token != null) {
                            builder.addHeader("Authorization", "Bearer " + token);
                        }

                        Request request = builder.build();
                        return chain.proceed(request);
                    }).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
