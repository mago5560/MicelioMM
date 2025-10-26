package com.luma.miceliomm.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.luma.miceliomm.MactyHojaRutaDetalle;
import com.luma.miceliomm.R;
import com.luma.miceliomm.adapter.HojaRutaDetalleAdapter;
import com.luma.miceliomm.adapter.HojaRutaResumenAdapter;
import com.luma.miceliomm.customs.DbHandler;
import com.luma.miceliomm.customs.FunctionCustoms;
import com.luma.miceliomm.customs.GlobalCustoms;
import com.luma.miceliomm.customs.HttpsInseguraHELP;
import com.luma.miceliomm.daos.HojaRutaDao;
import com.luma.miceliomm.daos.HojaRutaDetalleDao;
import com.luma.miceliomm.daos.HojaRutaResumenDao;
import com.luma.miceliomm.model.ActualizaHojaDeRutaModel;
import com.luma.miceliomm.model.HojaRutaDetalleModel;
import com.luma.miceliomm.model.HojaRutaModel;
import com.luma.miceliomm.model.HojaRutaResumenModel;
import com.luma.miceliomm.service.ApiClient;
import com.luma.miceliomm.service.ApiService;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HojaDeRutaController {

    private Context context;
    private GlobalCustoms var = GlobalCustoms.getInstance();
    private FunctionCustoms util;
    private DbHandler dbHandler;
    private HojaRutaDao dao;
    private HojaRutaResumenDao resumenDao;
    private HojaRutaDetalleDao detalleDao;
    private RecyclerView recyclerView;
    private LinearLayout recyclerViewEmpty;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HojaRutaResumenAdapter resumenAdapter;
    private HojaRutaDetalleAdapter detalleAdapter;

    public HojaDeRutaController(Context context) {
        this.context = context;
        this.util = new FunctionCustoms();
        this.dbHandler = new DbHandler(context, null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        this.dao = new HojaRutaDao(db);
        this.resumenDao = new HojaRutaResumenDao(db);
        this.detalleDao = new HojaRutaDetalleDao(db);
    }

    public HojaDeRutaController(Context context, RecyclerView recyclerView, LinearLayout recyclerViewEmpty, HojaRutaResumenAdapter resumenAdapter, SwipeRefreshLayout swipeRefreshLayout) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.recyclerViewEmpty = recyclerViewEmpty;
        this.resumenAdapter = resumenAdapter;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.util = new FunctionCustoms();
        this.dbHandler = new DbHandler(context, null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        this.dao = new HojaRutaDao(db);
        this.resumenDao = new HojaRutaResumenDao(db);
        this.detalleDao = new HojaRutaDetalleDao(db);
    }

    public HojaDeRutaController(Context context, RecyclerView recyclerView, LinearLayout recyclerViewEmpty, HojaRutaDetalleAdapter detalleAdapter, SwipeRefreshLayout swipeRefreshLayout) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.recyclerViewEmpty = recyclerViewEmpty;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.detalleAdapter = detalleAdapter;
        this.util = new FunctionCustoms();
        this.dbHandler = new DbHandler(context, null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        this.dao = new HojaRutaDao(db);
        this.resumenDao = new HojaRutaResumenDao(db);
        this.detalleDao = new HojaRutaDetalleDao(db);
    }

    // <editor-fold defaultstate="collapsed" desc="descarga datos">
    private void getDatos(String Fecha) {
        ProgressDialog pDialog = ProgressDialog.show(context, "Descargando Ruta", "Espere...", true, false);
        ApiService authService = new Retrofit.Builder()
                .baseUrl(var.getURL_API())
                .client(HttpsInseguraHELP.getUnsafeOkHttpClient())  // ← Usar cliente inseguro
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);

        // Retrofit con Interceptor
        Retrofit retrofitAuth = ApiClient.getClient(context, authService, var.getURL_API());
        ApiService apiService = retrofitAuth.create(ApiService.class);

        Call<ArrayList<HojaRutaModel>> call = apiService.getHojaRutaFecha(Fecha);
        call.enqueue(new Callback<ArrayList<HojaRutaModel>>() {
            @Override
            public void onResponse(Call<ArrayList<HojaRutaModel>> call, Response<ArrayList<HojaRutaModel>> response) {
                if (response.isSuccessful() && !response.body().isEmpty()) {
                    pDialog.setTitle("Guardando Datos");
                    pDialog.setMessage("Espere...");
                    for (HojaRutaModel cls : response.body()) {
                        dao.delete(cls.idHojaDeRuta,cls.idTraslado, cls.idTrasladoLogistica);
                        cls.fecha = util.formatDateWS(cls.fecha);
                        dao.insert(cls);
                    }
                    //buscar(Fecha);
                    util.msgSnackBar("Descarga correctamente", context);
                    util.mensaje("Total Registros obtenido: "+ response.body().size(),((Activity) context)).show();
                } else {
                    util.mensaje("No hay traslados que obtener (Response Code Referencia:  " + response.code() + ")", ((Activity) context)).show();
                }
                if (pDialog.isShowing()) pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<HojaRutaModel>> call, Throwable t) {
                util.mensajeError("Fallo: " + t.getMessage(), ((Activity) context)).show();
                if (pDialog.isShowing()) pDialog.dismiss();
            }
        });
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Buscar Resumen Local">
    private  AsyncBuscar asyncBuscar;
    public void buscar(String FechaInicio, String FechaFinal) {
        recyclerView.setScrollingTouchSlop(0);
        asyncBuscar = new AsyncBuscar();
        asyncBuscar.execute(FechaInicio,FechaFinal);
    }

    private class AsyncBuscar extends AsyncTask<String, Void, ArrayList> {
        private ProgressDialog pDialog;

        @Override
        protected ArrayList doInBackground(String... strings) {
            return resumenDao.selectResumen(strings[0],strings[1]);
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
        resumenAdapter.setInfo(arrayList);
        recyclerView.setAdapter(resumenAdapter);
        swipeRefreshLayout.setRefreshing(false);
        if (arrayList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            recyclerViewEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerViewEmpty.setVisibility(View.GONE);
        }
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Dialog Descargar Hoja Ruta">
    private AlertDialog alertDialog;
    public void dialogDescargaRuta(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setCancelable(false);

        final LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.content_dialog_descarga_hoja_ruta, null);
        dialogFindViewsByIds(dialogLayout);
        dialogActions(dialogLayout);

        builder.setView(dialogLayout);
        alertDialog = builder.show();
    }

    private void dialogFindViewsByIds(View view){
        ((TextView) view.findViewById(R.id.lblFechaBusqueda)).setText(util.getFechaActual());
    }

    private void dialogActions(View view){
        ((TextView) view.findViewById(R.id.lblFechaBusqueda)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                util.getFechaDialog(context,((TextView) v));
            }
        });

        ((Button) view.findViewById(R.id.btnCancelar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        ((Button) view.findViewById(R.id.btnDescargar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatos( util.formatDateDB( ((TextView) view.findViewById(R.id.lblFechaBusqueda)).getText().toString() ));
                alertDialog.dismiss();
            }
        });

    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="select resumen Id">
        public HojaRutaResumenModel selectHojaRutaId(int idHojaRuta){
            return resumenDao.selectResumenIdHojaDeRuta(idHojaRuta);
        }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="existe Hoja No recolectados">
    public boolean existsHojaNoRecolectado(int IdHojaDeRuta){
        return detalleDao.existsHojaDeRutaNoIniciado(IdHojaDeRuta);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Existe Hoja en Ruta">
    public boolean existsHojaEnRuta(){
        return detalleDao.existsHojaEnRuta();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="existe traslados No recolectados">
    public boolean existsTrasladoLogisticoNoRecolectado(int IdHojaDeRuta){
        return detalleDao.existsTrasladoLogisticoNoRecolectado(IdHojaDeRuta);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Existe Traslados en Ruta">
    public boolean existsTrasladoLogisticoEnRuta(int IdHojaDeRuta){
        return detalleDao.existsTrasladoLogisticoEnRuta(IdHojaDeRuta);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Buscar Detalle Local">
    private  AsyncBuscarDetalle asyncBuscarDetalle;
    public void buscarDetalle(int idHojaRuja) {
        recyclerView.setScrollingTouchSlop(0);
        asyncBuscarDetalle = new AsyncBuscarDetalle();
        asyncBuscarDetalle.execute(String.valueOf(idHojaRuja));
    }

    private class AsyncBuscarDetalle extends AsyncTask<String, Void, ArrayList> {
        private ProgressDialog pDialog;

        @Override
        protected ArrayList doInBackground(String... strings) {
            return detalleDao.selectDetalle(strings[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(context, "Consultando...", "Espere....", true, true);
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            super.onPostExecute(arrayList);
            viewConsultaDetalle(arrayList);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }

    private void viewConsultaDetalle(ArrayList arrayList) {
        detalleAdapter.setInfo(arrayList);
        recyclerView.setAdapter(detalleAdapter);
        swipeRefreshLayout.setRefreshing(false);
        if (arrayList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            recyclerViewEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerViewEmpty.setVisibility(View.GONE);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Actualizar Hoja de Ruta">
    public void actualizarHojaRuta(ActualizaHojaDeRutaModel cls , int Inicio, int Final) {
        ProgressDialog pDialog = ProgressDialog.show(context, "Enviando Actualizacion", "Espere...", true, false);
        ApiService authService = new Retrofit.Builder()
                .baseUrl(var.getURL_API())
                .client(HttpsInseguraHELP.getUnsafeOkHttpClient())  // ← Usar cliente inseguro
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);

        // Retrofit con Interceptor
        Retrofit retrofitAuth = ApiClient.getClient(context, authService, var.getURL_API());
        ApiService apiService = retrofitAuth.create(ApiService.class);

        Call<ResponseBody> call = apiService.postActualizaDatosHojaDeRuta(cls);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()==200 && response.isSuccessful()){
                    if (Inicio == 1){
                        detalleDao.updateHojaRutaIniciada(cls);
                    }else if (Final == 1){
                       detalleDao.updateHojaRutaFinal(cls);
                    }
                    util.msgSnackBar("Actualizacion enviada...", context);
                    hojaDeRutaDetalle(cls.idHojaDeRuta);
                }else {
                    util.mensaje("Error al enviar actualiacion (Response Code Referencia:  " + response.code() + ")", ((Activity) context)).show();
                }

                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                util.mensajeError("Fallo: " + t.getMessage(), ((Activity) context)).show();
                if (pDialog.isShowing()) pDialog.dismiss();
            }
        });
    }
    // </editor-fold>



    private void hojaDeRutaDetalle(int idHojaDeRuta){
        Intent intent = new Intent().setClass(context, MactyHojaRutaDetalle.class);
        intent.putExtra("idHojaRuta",idHojaDeRuta);
        ((Activity)context).startActivity(intent);
        ((Activity)context).finish();
    }




}
