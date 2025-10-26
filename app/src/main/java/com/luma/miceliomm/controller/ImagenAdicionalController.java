package com.luma.miceliomm.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.luma.miceliomm.adapter.ImagenAdicionalAdapter;
import com.luma.miceliomm.customs.FunctionCustoms;
import com.luma.miceliomm.customs.GlobalCustoms;
import com.luma.miceliomm.customs.HttpsInseguraHELP;
import com.luma.miceliomm.model.ImagenAdicionalModel;
import com.luma.miceliomm.model.SectorModel;
import com.luma.miceliomm.service.ApiClient;
import com.luma.miceliomm.service.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImagenAdicionalController {

    private Context context;
    private GlobalCustoms var = GlobalCustoms.getInstance();
    private FunctionCustoms util;
    private RecyclerView recyclerView;
    private LinearLayout recyclerViewEmpty;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImagenAdicionalAdapter adapter;

    public ImagenAdicionalController(Context context) {
        this.context = context;
    }

    public ImagenAdicionalController(Context context, RecyclerView recyclerView, LinearLayout recyclerViewEmpty, SwipeRefreshLayout swipeRefreshLayout, ImagenAdicionalAdapter adapter) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.recyclerViewEmpty = recyclerViewEmpty;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.adapter = adapter;
        this.util = new FunctionCustoms();
    }

    public void getDatos(int IdTrasladoLogistica) {
        ProgressDialog pDialog = ProgressDialog.show(context, "Obteniendo Imagenes", "Espere...", true, false);
        ApiService authService = new Retrofit.Builder()
                .baseUrl(var.getURL_API())
                .client(HttpsInseguraHELP.getUnsafeOkHttpClient())  // ← Usar cliente inseguro
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);

        // Retrofit con Interceptor
        Retrofit retrofitAuth = ApiClient.getClient(context, authService, var.getURL_API());
        ApiService apiService = retrofitAuth.create(ApiService.class);

        Call<ArrayList<ImagenAdicionalModel>> call = apiService.getImagenesAdicionales(IdTrasladoLogistica);

        call.enqueue(new Callback<ArrayList<ImagenAdicionalModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ImagenAdicionalModel>> call, Response<ArrayList<ImagenAdicionalModel>> response) {
                if (response.isSuccessful() && !response.body().isEmpty()) {
                        viewConsulta(response.body());
                } else {
                    util.mensaje("No hay datos que obtener (Response Code Referencia:  " + response.code() + ")", ((Activity) context)).show();
                }
                if (pDialog.isShowing()) pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<ImagenAdicionalModel>> call, Throwable t) {
                util.mensajeError("Fallo: " + t.getMessage(), ((Activity) context)).show();
                if (pDialog.isShowing()) pDialog.dismiss();
            }
        });
    }

    private void viewConsulta(ArrayList arrayList) {
        adapter.setInfo(arrayList);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
        if (arrayList.isEmpty()) {
            //util.msgToast("No se encontraron registros", ((Activity) context));
            recyclerView.setVisibility(View.GONE);
            recyclerViewEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerViewEmpty.setVisibility(View.GONE);
        }
    }

}
