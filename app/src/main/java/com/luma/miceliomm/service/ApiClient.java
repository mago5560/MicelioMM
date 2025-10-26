package com.luma.miceliomm.service;

import android.content.Context;

import com.luma.miceliomm.customs.AuthInterceptor;
import com.luma.miceliomm.customs.HttpsInseguraHELP;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiClient {
    private static Retrofit retrofit = null;

    // Inicializa Retrofit con Interceptor
    public static Retrofit getClient(Context context, ApiService apiService, String baseUrl) {
        if (retrofit == null) {
            OkHttpClient unsafeClient = HttpsInseguraHELP.getUnsafeOkHttpClient();

            /*
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context, apiService)) // nuestro Interceptor
                    .build();
                    */
            OkHttpClient client = unsafeClient.newBuilder()
                    .addInterceptor(new AuthInterceptor(context, apiService))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
