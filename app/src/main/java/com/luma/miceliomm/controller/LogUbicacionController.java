package com.luma.miceliomm.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.luma.miceliomm.customs.FunctionCustoms;
import com.luma.miceliomm.customs.GlobalCustoms;
import com.luma.miceliomm.customs.HttpsInseguraHELP;
import com.luma.miceliomm.model.ActualizaHojaDeRutaModel;
import com.luma.miceliomm.model.LogUbicacionModel;
import com.luma.miceliomm.service.ApiClient;
import com.luma.miceliomm.service.ApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogUbicacionController {
    private FunctionCustoms util;
    private GlobalCustoms var = GlobalCustoms.getInstance();
    private Context context;

    public LogUbicacionController(Context context) {
        this.context = context;
        this.util = new FunctionCustoms();

    }


    // <editor-fold defaultstate="collapsed" desc="Actualizar Hoja de Ruta">
    public void insertarLogUbicacion(LogUbicacionModel cls) {
        ApiService authService = new Retrofit.Builder()
                .baseUrl(var.getURL_API())
                .client(HttpsInseguraHELP.getUnsafeOkHttpClient())  // ← Usar cliente inseguro
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);

        // Retrofit con Interceptor
        Retrofit retrofitAuth = ApiClient.getClient(context, authService, var.getURL_API());
        ApiService apiService = retrofitAuth.create(ApiService.class);

        Call<ResponseBody> call = apiService.postInsertarLogUbicacion(cls);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Log.d("Position","envio datos de coordenadas " + util.getFechaHoraActual());
                    //dbHandler.updateDeliveresLogTransferido(cls.getId());
                } else if (response.code() == 401){
                    Log.e("Position","Usuario No Autorizado " + util.getFechaHoraActual());
                } else {
                    Log.e("Position","no se envio datos de coordenadas "  + util.getFechaHoraActual());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Position","no se envio datos de coordenadas REF: "+ t.getMessage()  + util.getFechaHoraActual());
            }
        });
    }
    // </editor-fold>



}
