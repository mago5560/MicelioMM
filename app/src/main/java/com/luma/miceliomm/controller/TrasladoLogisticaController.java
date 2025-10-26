package com.luma.miceliomm.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.luma.miceliomm.MactyActualizarHojaRuta;
import com.luma.miceliomm.MactyHojaRutaDetalle;
import com.luma.miceliomm.customs.DbHandler;
import com.luma.miceliomm.customs.FunctionCustoms;
import com.luma.miceliomm.customs.GlobalCustoms;
import com.luma.miceliomm.customs.HttpsInseguraHELP;
import com.luma.miceliomm.daos.HojaRutaDao;
import com.luma.miceliomm.daos.HojaRutaDetalleDao;
import com.luma.miceliomm.daos.HojaRutaResumenDao;
import com.luma.miceliomm.model.HojaRutaDetalleModel;
import com.luma.miceliomm.model.HojaRutaModel;
import com.luma.miceliomm.model.TrasladoLogisticaModel;
import com.luma.miceliomm.model.TrasladoLogisticoRecoleccionModel;
import com.luma.miceliomm.service.ApiClient;
import com.luma.miceliomm.service.ApiService;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Part;

public class TrasladoLogisticaController {

    private Context context;
    private GlobalCustoms var = GlobalCustoms.getInstance();
    private FunctionCustoms util;
    private DbHandler dbHandler;
    private HojaRutaDetalleDao detalleDao;

    public TrasladoLogisticaController(Context context) {
        this.context = context;
        this.util = new FunctionCustoms();
        this.dbHandler = new DbHandler(context, null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        this.detalleDao = new HojaRutaDetalleDao(db);
    }


    // <editor-fold defaultstate="collapsed" desc="select detalle Id">
    public HojaRutaDetalleModel selectHojaRutaDetalleId(int idHojaRuta, int idTraslado, int idTrasladoLogistica){
        return detalleDao.selectDetalleId (idHojaRuta,idTraslado,idTrasladoLogistica);
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="update TrasladoLogistico Id">
    public long updateTrasladoLogistico(TrasladoLogisticaModel cls){
        return detalleDao.updateTrasladoLogistico(cls);
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Entregar Traslado">
    public void setTrasladoLogistico(TrasladoLogisticaModel cls) {
        ProgressDialog pDialog = ProgressDialog.show(context, "Enviado Datos", "Espere...", true, false);
        ApiService authService = new Retrofit.Builder()
                .baseUrl(var.getURL_API())
                .client(HttpsInseguraHELP.getUnsafeOkHttpClient())  // ← Usar cliente inseguro
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);

        // Retrofit con Interceptor
        Retrofit retrofitAuth = ApiClient.getClient(context, authService, var.getURL_API());
        ApiService apiService = retrofitAuth.create(ApiService.class);


        MultipartBody.Part multipartarchivoImagen = null;

        if (!cls.rutaImagen.isEmpty()){
            File _file = new File(cls.rutaImagen);
            RequestBody archivoImagen = RequestBody.create(MediaType.parse("image/*"),_file);
            multipartarchivoImagen = MultipartBody.Part.createFormData("archivoImagen",_file.getName(),archivoImagen);
            cls.imagenDeEntregadoTraslado = _file.getName();
        }
        RequestBody IdTrasladoLogistica = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(cls.idTrasladoLogistica));
        RequestBody IdTraslado = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(cls.idTraslado));
        RequestBody RecibidoPor = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(cls.recibidoPorTraslado));
        RequestBody LatitudEntrega = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(cls.latitudEntregaTraslado));
        RequestBody LongitudEntrega = RequestBody.create(MediaType.parse("text/plain"),cls.longitudEntregaTraslado);
        RequestBody ObservacionesEntrega = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(cls.observacionesEntregaTraslado));
        RequestBody IdMotivoDeRechazo = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(cls.idMotivoDeRechazoTraslado));

        RequestBody ImagenDeEntregado = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(cls.imagenDeEntregadoTraslado));
        RequestBody FechaDeEntrega = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(cls.fechaDeEntregaTraslado));


        Call<ResponseBody> call = apiService.postActualizarTrasladoLogistica( IdTrasladoLogistica,IdTraslado,RecibidoPor,LatitudEntrega,LongitudEntrega,
                ObservacionesEntrega,IdMotivoDeRechazo, multipartarchivoImagen,ImagenDeEntregado,FechaDeEntrega);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()==200 && response.isSuccessful()){
                    TrasladoLogisticoRecoleccionModel  trasladoLogisticoRecoleccionModel = new TrasladoLogisticoRecoleccionModel();
                    trasladoLogisticoRecoleccionModel.idHojaDeRuta = cls.idHojaDeRuta;
                    trasladoLogisticoRecoleccionModel.idTrasladoLogistico =  cls.idTrasladoLogistica;
                    trasladoLogisticoRecoleccionModel.idTraslado = cls.idTraslado;
                    trasladoLogisticoRecoleccionModel.idEstado = cls.idEstado;
                    trasladoLogisticoRecoleccionModel.observaciones = cls.observacionesEntregaTraslado;
                    setEntegarTrasladoLogisticoEstado(trasladoLogisticoRecoleccionModel,cls);
                }else {

                    util.mensaje("Error al actualizar traslado logistica (Response Code Referencia:  " + response.code() + ")", ((Activity) context)).show();
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

    private void setEntegarTrasladoLogisticoEstado(TrasladoLogisticoRecoleccionModel cls, TrasladoLogisticaModel trasladoLogisticaModel) {
        ProgressDialog pDialog = ProgressDialog.show(context, "Enviado Datos", "Espere...", true, false);
        ApiService authService = new Retrofit.Builder()
                .baseUrl(var.getURL_API())
                .client(HttpsInseguraHELP.getUnsafeOkHttpClient())  // ← Usar cliente inseguro
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);

        // Retrofit con Interceptor
        Retrofit retrofitAuth = ApiClient.getClient(context, authService, var.getURL_API());
        ApiService apiService = retrofitAuth.create(ApiService.class);


        Call<ResponseBody> call = apiService.postActualizarEstadoHojaDeRutaTraslado( cls);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()==200 && response.isSuccessful()){
                    detalleDao.updateRecoleccionTrasladoLogistico(cls);
                    if (detalleDao.existsTrasladoLogisticoEnRuta(cls.idHojaDeRuta)){
                        detalleDao.updateTrasladoLogistico(trasladoLogisticaModel);
                        util.msgSnackBar(" Traslado Logistico Enviado...", context);
                        hojaDeRutaDetalle(cls.idHojaDeRuta);
                    }else {
                        finalizarHojaDeRuta(cls.idHojaDeRuta);
                    }
                }else {
                    util.mensaje("Error al Actualizar Estado Hoja de Ruta Traslado (Response Code Referencia:  " + response.code() + ")", ((Activity) context)).show();
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


    // <editor-fold defaultstate="collapsed" desc="Recolectar Traslado">
    public void setRecoleccionTrasladoLogistico(TrasladoLogisticoRecoleccionModel cls) {
        ProgressDialog pDialog = ProgressDialog.show(context, "Enviado Imagen", "Espere...", true, false);
        ApiService authService = new Retrofit.Builder()
                .baseUrl(var.getURL_API())
                .client(HttpsInseguraHELP.getUnsafeOkHttpClient())  // ← Usar cliente inseguro
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);

        // Retrofit con Interceptor
        Retrofit retrofitAuth = ApiClient.getClient(context, authService, var.getURL_API());
        ApiService apiService = retrofitAuth.create(ApiService.class);


        MultipartBody.Part multipartarchivoImagen = null;

        if (!cls.rutaImagen.isEmpty()){
            File _file = new File(cls.rutaImagen);
            RequestBody archivoImagen = RequestBody.create(MediaType.parse("image/*"),_file);
            multipartarchivoImagen = MultipartBody.Part.createFormData("archivoImagen",_file.getName(),archivoImagen);
            cls.nombreImagen = _file.getName();
        }
        RequestBody IdTrasladoLogistica = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(cls.idTrasladoLogistico));

        RequestBody Imagen = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(cls.nombreImagen));


        Call<ResponseBody> call = apiService.postActualizarImagenRecibido( IdTrasladoLogistica, multipartarchivoImagen,Imagen);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()==200 && response.isSuccessful()){
                    setRecoleccionTrasladoLogisticoEstado(cls);
                }else {
                    util.mensaje("Error al actualizar imagen de recibido (Response Code Referencia:  " + response.code() + ")", ((Activity) context)).show();
                }
                if (pDialog.isShowing()) pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                util.mensajeError("Fallo: " + t.getMessage() , ((Activity) context)).show();
                if (pDialog.isShowing()) pDialog.dismiss();
            }
        });

    }

    private void setRecoleccionTrasladoLogisticoEstado(TrasladoLogisticoRecoleccionModel cls) {
        ProgressDialog pDialog = ProgressDialog.show(context, "Enviado Datos", "Espere...", true, false);
        ApiService authService = new Retrofit.Builder()
                .baseUrl(var.getURL_API())
                .client(HttpsInseguraHELP.getUnsafeOkHttpClient())  // ← Usar cliente inseguro
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);

        // Retrofit con Interceptor
        Retrofit retrofitAuth = ApiClient.getClient(context, authService, var.getURL_API());
        ApiService apiService = retrofitAuth.create(ApiService.class);



        Call<ResponseBody> call = apiService.postActualizarEstadoHojaDeRutaTraslado( cls);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()==200 && response.isSuccessful()){
                    detalleDao.updateRecoleccionTrasladoLogistico(cls);
                    util.msgSnackBar(" Recoleccion Logistico Enviado...", context);
                    if (detalleDao.existsTrasladoLogisticoNoRecolectado(cls.idHojaDeRuta)){
                        hojaDeRutaDetalle(cls.idHojaDeRuta);
                    }else {
                        iniciarHojaDeRuta(cls.idHojaDeRuta);
                    }
                }else {
                    util.mensaje("Error al actualizar estado hoja ruta (Response Code Referencia:  " + response.code() + ")", ((Activity) context)).show();
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


    public boolean existsTrasladoLogisticoEnRuta(int IdHojaDeRuta){
        return detalleDao.existsTrasladoLogisticoEnRuta(IdHojaDeRuta);
    }

    private void hojaDeRutaDetalle(int idHojaDeRuta){
        Intent intent = new Intent().setClass(context, MactyHojaRutaDetalle.class);
        intent.putExtra("idHojaRuta",idHojaDeRuta);
        ((Activity)context).startActivity(intent);
        ((Activity)context).finish();
    }

    private void iniciarHojaDeRuta(int idHojaDeRuta){
        Intent intent = new Intent().setClass(context, MactyActualizarHojaRuta.class);
        intent.putExtra("idHojaRuta",idHojaDeRuta);
        intent.putExtra("iniciar", 1);
        intent.putExtra("finalizar", 0);
        ((Activity)context).startActivity(intent);
        ((Activity)context).finish();
    }

    private void finalizarHojaDeRuta(int idHojaDeRuta){
        Intent intent = new Intent().setClass(context, MactyActualizarHojaRuta.class);
        intent.putExtra("idHojaRuta",idHojaDeRuta);
        intent.putExtra("iniciar", 0);
        intent.putExtra("finalizar", 1);
        ((Activity)context).startActivity(intent);
        ((Activity)context).finish();
    }

}
