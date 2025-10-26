package com.luma.miceliomm.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.luma.miceliomm.adapter.PilotoMaestroAdapter;
import com.luma.miceliomm.customs.DbHandler;
import com.luma.miceliomm.customs.FunctionCustoms;
import com.luma.miceliomm.customs.GlobalCustoms;
import com.luma.miceliomm.customs.HttpsInseguraHELP;
import com.luma.miceliomm.daos.PilotoDao;
import com.luma.miceliomm.model.PilotoModel;
import com.luma.miceliomm.service.ApiClient;
import com.luma.miceliomm.service.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PilotoController {

    private Context context;
    //private ApiService apiService;
    private GlobalCustoms var = GlobalCustoms.getInstance();
    private FunctionCustoms util;
    private DbHandler dbHandler;
    private PilotoDao dao;

    private RecyclerView recyclerView;
    private LinearLayout recyclerViewEmpty;
    private PilotoMaestroAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public PilotoController(Context context) {
        this.context = context;
        this.util = new FunctionCustoms();
        this.dbHandler = new DbHandler(context, null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        this.dao = new PilotoDao(db);
    }

    public PilotoController(Context context, RecyclerView recyclerView, PilotoMaestroAdapter adapter, SwipeRefreshLayout swipeRefreshLayout, LinearLayout recyclerViewEmpty) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.recyclerViewEmpty = recyclerViewEmpty;
        this.util = new FunctionCustoms();
        this.dbHandler = new DbHandler(context, null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        this.dao = new PilotoDao(db);
    }

    public void getDatos(TextView Total) {
        ProgressDialog pDialog = ProgressDialog.show(context, "Descargando", "Espere...", true, false);
        ApiService authService = new Retrofit.Builder()
                .baseUrl(var.getURL_API())
                .client(HttpsInseguraHELP.getUnsafeOkHttpClient())  // ← Usar cliente inseguro
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);

        // Retrofit con Interceptor
        Retrofit retrofitAuth = ApiClient.getClient(context, authService, var.getURL_API());
        ApiService apiService = retrofitAuth.create(ApiService.class);

        Call<ArrayList<PilotoModel>> call = apiService.getPilotos();
        call.enqueue(new Callback<ArrayList<PilotoModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PilotoModel>> call, Response<ArrayList<PilotoModel>> response) {
                if (response.isSuccessful() && !response.body().isEmpty()) {
                    dao.delete();
                    pDialog.setTitle("Guardando Datos");
                    pDialog.setMessage("Espere...");
                    for (PilotoModel cls : response.body()) {
                        dao.insert(cls);
                    }
                    util.msgSnackBar("Descarga correctamente", context);
                    Total.setText("Total: " + dao.totalRegistros());
                } else {
                    util.mensaje("Error descargar pilotos (Response Code Referencia:  " + response.code() + ")", ((Activity) context)).show();
                }
                if (pDialog.isShowing()) pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<PilotoModel>> call, Throwable t) {
                util.mensajeError("Fallo: " + t.getMessage(), ((Activity) context)).show();
                if (pDialog.isShowing()) pDialog.dismiss();
            }
        });
    }

    public int totalRegistros(){
        return  dao.totalRegistros();
    }


    private AsyncBuscar asyncBuscar;

    public void buscar() {
        recyclerView.setScrollingTouchSlop(0);
        asyncBuscar = new AsyncBuscar();
        asyncBuscar.execute();
    }

    private class AsyncBuscar extends AsyncTask<String, Void, ArrayList> {
        private ProgressDialog pDialog;

        @Override
        protected ArrayList doInBackground(String... strings) {
            return dao.select();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(context, "Consultando...", "Espere....", true, true);
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            super.onPostExecute(arrayList);
            viewConsulta(arrayList);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
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
