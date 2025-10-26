package com.luma.miceliomm.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.luma.miceliomm.MactyHojaRutaDetalle;
import com.luma.miceliomm.customs.DbHandler;
import com.luma.miceliomm.customs.FunctionCustoms;
import com.luma.miceliomm.customs.GlobalCustoms;
import com.luma.miceliomm.customs.HttpsInseguraHELP;
import com.luma.miceliomm.daos.TrasladoImagenAdicionalDao;
import com.luma.miceliomm.model.TrasladoImagenAdicionalModel;
import com.luma.miceliomm.model.TrasladoLogisticaModel;
import com.luma.miceliomm.model.TrasladoLogisticoRecoleccionModel;
import com.luma.miceliomm.service.ApiClient;
import com.luma.miceliomm.service.ApiService;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrasladoImagenAdicionalController {

    private Context context;
    private GlobalCustoms var = GlobalCustoms.getInstance();
    private FunctionCustoms util;
    private DbHandler dbHandler;
    private TrasladoImagenAdicionalDao dao;


    public TrasladoImagenAdicionalController(Context context) {
        this.context = context;
        this.util = new FunctionCustoms();
        this.dbHandler = new DbHandler(context, null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        this.dao = new TrasladoImagenAdicionalDao(db);
    }

    // <editor-fold defaultstate="collapsed" desc="Entregar Traslado">
    public void setTrasladoImagenAdicional(TrasladoImagenAdicionalModel cls) {
        ProgressDialog pDialog = ProgressDialog.show(context, "Enviado Imagen Adicional", "Espere...", true, false);
        ApiService authService = new Retrofit.Builder()
                .baseUrl(var.getURL_API())
                .client(HttpsInseguraHELP.getUnsafeOkHttpClient())  // ← Usar cliente inseguro
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);

        // Retrofit con Interceptor
        Retrofit retrofitAuth = ApiClient.getClient(context, authService, var.getURL_API());
        ApiService apiService = retrofitAuth.create(ApiService.class);



        RequestBody archivoImagen = RequestBody.create(MediaType.parse("image/*"), cls.fileImagen);
        MultipartBody.Part multipartarchivoImagen = MultipartBody.Part.createFormData("ArchivoImagen",cls.fileImagen.getName(),archivoImagen);
        RequestBody IdTrasladoLogistica = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(cls.idTrasladoLogistica));

        Call<ResponseBody> call = apiService.postTrasladoImagenAdicional( IdTrasladoLogistica, multipartarchivoImagen);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()==200 && response.isSuccessful()){
                            dao.insert(cls);
                            mensajeRegistroEnviado("Imagen adicional para el traslado: "+ cls.idTrasladoLogistica +", enviada correctamente.",
                                    ((Activity)context ), cls.idHojaDeRuta);
                }else {
                    util.mensaje("Error al enviar imagen adicional (Response Code Referencia:  " + response.code() + ")", ((Activity) context)).show();
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

    private void mensajeRegistroEnviado(String Mensaje, Activity Macty, int idHojaDeRuta) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Macty);
        AlertDialog alerta;
        builder.setCancelable(false);

        builder.setTitle("Mensaje del Sistema");
        builder.setMessage( Mensaje);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hojaDeRutaDetalle(idHojaDeRuta);
                dialog.dismiss();
            }
        });

        alerta = builder.create();
        alerta.show();
    }

    // </editor-fold>


    public void alterObject(){
        dao.alterObject();
    }

    private void hojaDeRutaDetalle(int idHojaDeRuta){
        Intent intent = new Intent().setClass(context, MactyHojaRutaDetalle.class);
        intent.putExtra("idHojaRuta",idHojaDeRuta);
        ((Activity)context).startActivity(intent);
        ((Activity)context).finish();
    }


}
