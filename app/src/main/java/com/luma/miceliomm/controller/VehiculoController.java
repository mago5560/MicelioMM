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

import com.luma.miceliomm.R;
import com.luma.miceliomm.adapter.VehiculoMaestroAdapter;
import com.luma.miceliomm.customs.DbHandler;
import com.luma.miceliomm.customs.FunctionCustoms;
import com.luma.miceliomm.customs.GlobalCustoms;
import com.luma.miceliomm.customs.HttpsInseguraHELP;
import com.luma.miceliomm.daos.VehiculoDao;
import com.luma.miceliomm.model.VehiculoModel;
import com.luma.miceliomm.service.ApiClient;
import com.luma.miceliomm.service.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VehiculoController {

    private Context context;
    //private ApiService apiService;
    private GlobalCustoms var = GlobalCustoms.getInstance();
    private FunctionCustoms util;
    private DbHandler dbHandler;
    private VehiculoDao vehiculoDao;

    private RecyclerView recyclerView;
    private LinearLayout recyclerViewEmpty;
    private VehiculoMaestroAdapter vehiculoMaestroAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public VehiculoController(Context context) {
        this.context = context;
        this.util = new FunctionCustoms();
        this.dbHandler = new DbHandler(context, null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        this.vehiculoDao = new VehiculoDao(db);
    }

    public VehiculoController(Context context, RecyclerView recyclerView, VehiculoMaestroAdapter vehiculoMaestroAdapter, SwipeRefreshLayout swipeRefreshLayout, LinearLayout recyclerViewEmpty) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.vehiculoMaestroAdapter = vehiculoMaestroAdapter;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.recyclerViewEmpty = recyclerViewEmpty;
        this.util = new FunctionCustoms();
        this.dbHandler = new DbHandler(context, null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        this.vehiculoDao = new VehiculoDao(db);
    }

    public void getVehiculos(TextView Total) {
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

        Call<ArrayList<VehiculoModel>> call = apiService.getVehiculos();
        call.enqueue(new Callback<ArrayList<VehiculoModel>>() {
            @Override
            public void onResponse(Call<ArrayList<VehiculoModel>> call, Response<ArrayList<VehiculoModel>> response) {
                if (response.isSuccessful() && !response.body().isEmpty()) {
                    vehiculoDao.delete();
                    pDialog.setTitle("Guardando Datos");
                    pDialog.setMessage("Espere...");
                    for (VehiculoModel cls : response.body()) {
                        vehiculoDao.insert(cls);
                    }
                    util.msgSnackBar("Descarga correctamente", context);
                    Total.setText("Total: " + vehiculoDao.totalRegistros());
                } else {
                    util.mensaje("Error al obtener perfil", ((Activity) context)).show();
                }
                if (pDialog.isShowing()) pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<VehiculoModel>> call, Throwable t) {
                util.mensajeError("Fallo: " + t.getMessage(), ((Activity) context)).show();
                if (pDialog.isShowing()) pDialog.dismiss();
            }
        });
    }

    public int totalRegistros(){
      return  vehiculoDao.totalRegistros();
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
            return vehiculoDao.select();
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
        vehiculoMaestroAdapter.setInfo(arrayList);
        recyclerView.setAdapter(vehiculoMaestroAdapter);
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
