package com.luma.miceliomm.service;

import com.luma.miceliomm.model.ActualizaHojaDeRutaModel;
import com.luma.miceliomm.model.EstadoModel;
import com.luma.miceliomm.model.HojaRutaModel;
import com.luma.miceliomm.model.LogUbicacionModel;
import com.luma.miceliomm.model.LoginModel;
import com.luma.miceliomm.model.MotivoModel;
import com.luma.miceliomm.model.ImagenAdicionalModel;
import com.luma.miceliomm.model.PilotoModel;
import com.luma.miceliomm.model.SectorModel;
import com.luma.miceliomm.model.TrasladoLogisticoRecoleccionModel;
import com.luma.miceliomm.model.UsuarioModel;
import com.luma.miceliomm.model.VehiculoModel;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    @Headers({
            "Content-type: application/json"
    })
    @POST("LoginAPI/login")
    public Call<UsuarioModel> postLogin(@Body LoginModel loginModel);

    /*Maestros*/
    @Headers({
            "Content-type: application/json"
    })
    @GET("MicelioApi/ObtenerVehiculos")
    public Call<ArrayList<VehiculoModel>> getVehiculos();

    @Headers({
            "Content-type: application/json"
    })
    @GET("MicelioApi/ObtenerEstados")
    public Call<ArrayList<EstadoModel>> getEstados();

    @Headers({
            "Content-type: application/json"
    })
    @GET("MicelioApi/OtenerPilotos")
    public Call<ArrayList<PilotoModel>> getPilotos();

    @Headers({
            "Content-type: application/json"
    })
    @GET("MicelioApi/MotivosDeRechazo")
    public Call<ArrayList<MotivoModel>> getMotivos();

    @Headers({
            "Content-type: application/json"
    })
    @GET("MicelioApi/SectoresLogisticos")
    public Call<ArrayList<SectorModel>> getSectores();


    @Headers({
            "Content-type: application/json"
    })
    @GET("MicelioApi/ObtenerHojaDeRutaFecha")
    public Call<ArrayList<HojaRutaModel>> getHojaRutaFecha(@Query("Fecha") String Fecha);


    @Headers({
            "Content-type: application/json"
    })
    @GET("MicelioApi/ObtenerImagenesAdicionales")
    public Call<ArrayList<ImagenAdicionalModel>> getImagenesAdicionales(@Query("IdTRasladoLogistica") int IdTRasladoLogistica);


    @Headers({
            "Content-type: application/json"
    })
    @POST("MicelioApi/InsertarLogUbicacion")
    Call<ResponseBody> postInsertarLogUbicacion(@Body LogUbicacionModel logUbicacionModel);


    @Multipart
    @POST("MicelioApi/ActualizarTrasladoLogistica")
    Call<ResponseBody> postActualizarTrasladoLogistica(   @Part("IdTrasladoLogistica") RequestBody IdTrasladoLogistica,
                                                          @Part("IdTraslado") RequestBody IdTraslado,
                                                          @Part("RecibidoPor") RequestBody RecibidoPor ,
                                                          @Part("LatitudEntrega") RequestBody LatitudEntrega ,
                                                          @Part("LongitudEntrega") RequestBody LongitudEntrega ,
                                                          @Part("ObservacionesEntrega") RequestBody ObservacionesEntrega ,
                                                          @Part("IdMotivoDeRechazo") RequestBody IdMotivoDeRechazo ,
                                                          @Part MultipartBody.Part archivoImagen,
                                                          @Part("ImagenDeEntregado") RequestBody ImagenDeEntregado ,
                                                          @Part("FechaDeEntrega") RequestBody FechaDeEntrega  );


    @Multipart
    @POST("MicelioApi/TrasladoImagenesAdicionales")
    Call<ResponseBody> postTrasladoImagenAdicional(  @Part("IdTrasladoLogistica") RequestBody IdTrasladoLogistica  ,
                                                         @Part MultipartBody.Part archivoImagen);

    @Multipart
    @POST("MicelioApi/ActualizarImagenRecibido")
    Call<ResponseBody> postActualizarImagenRecibido(  @Part("IdTrasladoLogistica") RequestBody IdTrasladoLogistica  ,
                                                     @Part MultipartBody.Part archivoImagen,
                                                      @Part("Imagen ") RequestBody Imagen  );

    @Headers({
            "Content-type: application/json"
    })
    @POST("MicelioApi/ActualizarEstadoHojaDeRutaTraslado")
    Call<ResponseBody> postActualizarEstadoHojaDeRutaTraslado(@Body TrasladoLogisticoRecoleccionModel trasladoLogisticoRecoleccionModel);

    @Headers({
            "Content-type: application/json"
    })
    @POST("MicelioApi/ActualizaDatosHojaDeRuta")
    Call<ResponseBody> postActualizaDatosHojaDeRuta(@Body ActualizaHojaDeRutaModel actualizaHojaDeRutaModel);
}
