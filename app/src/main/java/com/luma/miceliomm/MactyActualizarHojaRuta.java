package com.luma.miceliomm;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.luma.miceliomm.controller.HojaDeRutaController;
import com.luma.miceliomm.controller.LocationController;
import com.luma.miceliomm.controller.TrasladoLogisticaController;
import com.luma.miceliomm.customs.FunctionCustoms;
import com.luma.miceliomm.model.ActualizaHojaDeRutaModel;
import com.luma.miceliomm.model.HojaRutaResumenModel;
import com.luma.miceliomm.model.TrasladoLogisticaModel;

public class MactyActualizarHojaRuta extends AppCompatActivity implements LocationController.LocationUpdateListener {

    private Intent intent;
    private FunctionCustoms util;
    private String Latitud ="0", Longitud="0";
    private int idHojaDeRuta=0,iniciar=0,finalizar=0;
    private String LatitudInicial="",LongitudInicial="",FechaHoraSalidad;
    private LocationController locationController;
    private Location currentLocation;

    private HojaDeRutaController hojaDeRutaController;
    private ActualizaHojaDeRutaModel actualizaHojaDeRutaModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.macty_actualizar_hoja_ruta);

        findViewsById();
        actions();
        getParametros();
        // Manejar permisos y ubicación
        handleLocationPermissions();
    }

    private void findViewsById(){
        util = new FunctionCustoms();
        ((TextView) findViewById(R.id.lblTituloNavBar)).setText("Inicio Hoja De Ruta");
        hojaDeRutaController = new HojaDeRutaController(this);
        actualizaHojaDeRutaModel = new ActualizaHojaDeRutaModel();

        // Inicializar controlador de ubicación
        locationController = new LocationController(this);
        locationController.addLocationListener(this);


    }

    private void actions(){

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                goHome();
            }
        });

        ((ImageView) findViewById(R.id.imgvwRetroceder)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });

        ((Button) findViewById(R.id.btnGrabar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grabar();
            }
        });

    }


    private void getParametros(){
        Bundle parametros = this.getIntent().getExtras();
        if(parametros !=null){
            idHojaDeRuta = parametros.getInt("idHojaRuta");
            iniciar  = parametros.getInt("iniciar");
            finalizar = parametros.getInt("finalizar");
            fillDatos();
        }else{
            util.mensaje("No se obtuvo id de la hoja de ruta correctamente",this).show();
        }
    }

    private void grabar(){
        if (validarCampos()){
            actualizaHojaDeRutaModel = new ActualizaHojaDeRutaModel();
            actualizaHojaDeRutaModel.idHojaDeRuta = idHojaDeRuta;
            if (iniciar == 1){
                actualizaHojaDeRutaModel.latitudInicial = Latitud;
                actualizaHojaDeRutaModel.longitudInicial = Longitud;
                actualizaHojaDeRutaModel.latitudFinal = "0";
                actualizaHojaDeRutaModel.longitudFinal = "0";
                actualizaHojaDeRutaModel.kmInicial =  Integer.valueOf(((EditText) findViewById(R.id.txtkmInicial)).getText().toString());

                actualizaHojaDeRutaModel.idEstado   = 5;
                actualizaHojaDeRutaModel.fechaHoraSalida = util.getFechaHoraActualJson();
                actualizaHojaDeRutaModel.fechaHoraRegreso = util.getFechaHoraActualJson();
            }else if (finalizar == 1){
                actualizaHojaDeRutaModel.latitudInicial = LatitudInicial;
                actualizaHojaDeRutaModel.longitudInicial = LongitudInicial ;
                actualizaHojaDeRutaModel.latitudFinal = Latitud;
                actualizaHojaDeRutaModel.longitudFinal = Longitud;
                actualizaHojaDeRutaModel.kmFinal =  Integer.valueOf(((EditText) findViewById(R.id.txtkmFinal)).getText().toString());
                actualizaHojaDeRutaModel.fechaHoraSalida = FechaHoraSalidad;
                actualizaHojaDeRutaModel.fechaHoraRegreso = util.getFechaHoraActualJson();
                actualizaHojaDeRutaModel.idEstado   = 8;
            }



            String galones = "",vale="",otrosGastos="";

            if (  ((EditText) findViewById(R.id.txtGalones)).getText().toString().isEmpty() )
            {
                galones = "0";
            }else{
                galones = ((EditText) findViewById(R.id.txtGalones)).getText().toString();
            }

            if (  ((EditText) findViewById(R.id.txtVale)).getText().toString().isEmpty() )
            {
                vale = "0";
            }else{
                vale = ((EditText) findViewById(R.id.txtVale)).getText().toString();
            }

            if (  ((EditText) findViewById(R.id.txtOtrosGastos)).getText().toString().isEmpty() )
            {
                otrosGastos = "0";
            }else{
                otrosGastos = ((EditText) findViewById(R.id.txtOtrosGastos)).getText().toString();
            }

            actualizaHojaDeRutaModel.galones =  Double.valueOf(galones);
            actualizaHojaDeRutaModel.vale =  Integer.valueOf(vale);
            actualizaHojaDeRutaModel.otrosGastos =  Double.valueOf(otrosGastos);
            hojaDeRutaController.actualizarHojaRuta(actualizaHojaDeRutaModel, iniciar,finalizar);

        }
    }

    private boolean validarCampos(){
        if (iniciar == 1){
            if ( util.validarCampoVacio(((EditText) findViewById(R.id.txtkmInicial))) ){
                return false;
            }
        } else if ( finalizar == 1){
            if ( util.validarCampoVacio(((EditText) findViewById(R.id.txtkmFinal))) ){
                return false;
            }
        }
        return true;
    }

    private void fillDatos(){
        hojaDeRutaController = new HojaDeRutaController(this);
        HojaRutaResumenModel v =  hojaDeRutaController.selectHojaRutaId(idHojaDeRuta);

        ((TextView) findViewById(R.id.lblCVidHojaDeRuta))  .setText(String.valueOf(v.idHoraRuta));
        ((TextView) findViewById(R.id.lblCVnombreSectorLogistico)) .setText(v.nombreSectorLogistico);
        ((TextView) findViewById(R.id.lblCVnombrePiloto)) .setText(v.nombrePiloto);
        ((TextView) findViewById(R.id.lblCVfecha)) .setText(v.fecha);
        ((TextView) findViewById(R.id.lblCVTotalBultos)) .setText(String.valueOf(v.totalBultos));
        ((TextView) findViewById(R.id.lblCVnombreEstado)).setText(v.hojaRutaNombreEstado);

        //ConfiguracionDeColor
        configurarEstado(((TextView) findViewById(R.id.lblCVnombreEstado)),v.hojaDeRutaEstado, ((LinearLayout) findViewById(R.id.llyCVColores)));



        if (iniciar == 1){
            ((TextView) findViewById(R.id.lblTituloNavBar)).setText("Inicio Hoja De Ruta");
            ((EditText) findViewById(R.id.txtkmInicial)).setVisibility(View.VISIBLE);
            ((EditText) findViewById(R.id.txtkmFinal)).setVisibility(View.GONE);

        }else if (finalizar == 1){
            LatitudInicial = v.actualizaHojaDeRutaModel.latitudInicial;
            LongitudInicial = v.actualizaHojaDeRutaModel.longitudInicial;
            FechaHoraSalidad = v.actualizaHojaDeRutaModel.fechaHoraSalida;

            ((TextView) findViewById(R.id.lblTituloNavBar)).setText("Finalizar Hoja De Ruta");
            ((EditText) findViewById(R.id.txtkmInicial)).setVisibility(View.GONE);
            ((EditText) findViewById(R.id.txtkmFinal)).setVisibility(View.VISIBLE);

            ((EditText) findViewById(R.id.txtGalones)).setText(String.valueOf(v.actualizaHojaDeRutaModel.galones));
            ((EditText) findViewById(R.id.txtVale)).setText(String.valueOf(v.actualizaHojaDeRutaModel.vale) );
            ((EditText) findViewById(R.id.txtOtrosGastos)).setText(String.valueOf(v.actualizaHojaDeRutaModel.otrosGastos));

        }

    }

    // <editor-fold defaultstate="collapsed" desc="Colocar Color">
    private void configurarEstado(TextView textView , int idEstado , LinearLayout linearLayout) {
        int colorRes;
        int colorText;

        switch (idEstado) {
            case 1:
                colorRes = R.color.color_borrador;
                colorText =R.color.white;
                break;
            case 2:
                colorRes = R.color.color_cerrado;
                colorText =R.color.black;
                break;
            case 3:
                colorRes = R.color.color_listo_recoger;
                colorText =R.color.black;
                break;
            case 4:
                colorRes = R.color.color_programado;
                colorText =R.color.black;
                break;
            case 5:
                colorRes = R.color.color_recibido_piloto;
                colorText =R.color.white;
                break;
            case 6:
                colorRes = R.color.color_ruta;
                colorText =R.color.white;
                break;
            case 7:
                colorRes = R.color.color_entregado;
                colorText =R.color.white;
                break;
            case 8:
                colorRes = R.color.color_re_programado;
                colorText =R.color.black;
                break;
            case 9:
                colorRes = R.color.color_liquidado;
                colorText =R.color.black;
                break;
            default:
                colorRes = R.color.color_borrador;
                colorText =R.color.white;
                break;
        }

        textView.setBackgroundColor(ContextCompat.getColor(this, colorRes));
        textView.setTextColor( ContextCompat.getColor(this, colorText));
        linearLayout.setBackgroundColor(ContextCompat.getColor(this,colorRes));
    }
    // </editor-fold>

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (locationController.onRequestPermissionsResult(requestCode, grantResults)) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationController != null) {
            locationController.stopLocationUpdates();
            locationController.removeAllListeners();
        }
    }


    // <editor-fold defaultstate="collapsed" desc="Menu, Opciones y regresar a principal">

    private void goHome() {
        intent = new Intent().setClass(this, MactyHojaRutaDetalle.class);
        intent.putExtra("idHojaRuta",idHojaDeRuta);
        startActivity(intent);
        finish();
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Manejo de permisos y ubicacion">
    @Override
    public void onLocationUpdated(Location location) {
        currentLocation = location;
        Latitud = String.valueOf(location.getLatitude());
        Longitud =  String.valueOf(location.getLongitude());
    }

    @Override
    public void onLocationError(String error) {
        ((TextView) findViewById(R.id.tvUbicacion)) .setText("Error: " + error);
        util.msgToast(error,this);
    }

    @Override
    public void onPermissionsDenied() {
        ((TextView) findViewById(R.id.tvUbicacion)) .setText("Permisos denegados - No se puede obtener ubicación");
        util.msgToast("Los permisos de ubicación son necesarios",this);
    }

    private void handleLocationPermissions() {
        if (locationController.hasLocationPermissions()) {
            startLocationUpdates();
        } else {
            locationController.requestLocationPermissions(this);
        }
    }

    private void startLocationUpdates() {
        if (locationController.isGPSEnabled()) {
            locationController.startLocationUpdates();
        } else {
            ((TextView) findViewById(R.id.tvUbicacion)) .setText("GPS deshabilitado - Active el GPS");
            util.msgToast("Active el GPS para obtener ubicación precisa",this);
        }
    }

    // </editor-fold>

}