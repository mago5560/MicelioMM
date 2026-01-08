package com.luma.miceliomm;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.luma.miceliomm.adapter.MenuAdapter;
import com.luma.miceliomm.controller.HojaDeRutaController;
import com.luma.miceliomm.controller.MenuController;
import com.luma.miceliomm.controller.UsuarioController;
import com.luma.miceliomm.customs.FunctionCustoms;
import com.luma.miceliomm.customs.GlobalCustoms;
import com.luma.miceliomm.model.MenuModel;
import com.luma.miceliomm.service.LocationService;

import java.util.ArrayList;

public class MactyPrincipal extends AppCompatActivity  {

    private Intent intent;
    FunctionCustoms util;
    GlobalCustoms vars = GlobalCustoms.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.macty_principal);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        findViewsByIds();
        actions();
        setUpNavigation();
        permisosAPI();

    }

    private void findViewsByIds() {
        util = new FunctionCustoms();

        ((BottomNavigationView) findViewById(R.id.bottomNavigationView)).setBackground( null);
        ((BottomNavigationView) findViewById(R.id.bottomNavigationView)).getMenu().getItem(2).setEnabled(true);
        this.util = new FunctionCustoms();
        hideKeyboard(getWindow().getDecorView().getRootView());

    }

    private void actions() {
        ((FloatingActionButton) findViewById(R.id.fabNuevaVenta)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descargarRuta();
            }
        });
    }

    public void setUpNavigation(){
        NavHostFragment navHostFragment =  (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(((BottomNavigationView) findViewById(R.id.bottomNavigationView)), navHostFragment.getNavController());
    }

    //*****************************************************
    // Ocultar el teclado virtual
    //*****************************************************
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void descargarRuta(){
        HojaDeRutaController controller = new HojaDeRutaController(this);
        controller.dialogDescargaRuta();
    }

    private void inicioServicio(){
        Log.i("MicelioApp","inicio de ruta");
        //startService(new Intent(this, LocationService.class));

        Intent intent = new Intent(this, LocationService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="(PERMISOS)">
    // <editor-fold defaultstate="collapsed" desc="(PERMISOS CORREGIDOS Y ESTABLES)">

    private static final int LOCATION_FOREGROUND_REQUEST = 100;
    private static final int LOCATION_BACKGROUND_REQUEST = 101;
    private static final int CAMERA_REQUEST = 102;
    private static final int PHONE_REQUEST = 103;
    private static final int STORAGE_REQUEST = 104;

    /* ===================== MÉTODO PRINCIPAL ===================== */
    private void permisosAPI() {

        // 1️ Ubicación en primer plano (OBLIGATORIO)
        if (!tieneUbicacionForeground()) {
            pedirUbicacionForeground();
            return;
        }

        //  Ubicación en segundo plano (SOLO Android 10+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!tieneUbicacionBackground()) {
                pedirUbicacionBackground();
                return;
            }
        }

        //  Cámara
        if (!tienePermiso(Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST
            );
            return;
        }

        //  Teléfono
        if (!tienePermiso(Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PHONE_REQUEST
            );
            return;
        }

        //  Almacenamiento (solo hasta Android 10)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            if (!tienePermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        },
                        STORAGE_REQUEST
                );
                return;
            }
        }

        // TODOS LOS PERMISOS CONCEDIDOS
        inicioServicio();
    }

    /* ===================== VALIDACIONES ===================== */

    private boolean tienePermiso(String permiso) {
        return ContextCompat.checkSelfPermission(this, permiso)
                == PackageManager.PERMISSION_GRANTED;
    }

    private boolean tieneUbicacionForeground() {
        return tienePermiso(Manifest.permission.ACCESS_FINE_LOCATION)
                || tienePermiso(Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private boolean tieneUbicacionBackground() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;
    }

    /* ===================== SOLICITUDES ===================== */

    private void pedirUbicacionForeground() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                LOCATION_FOREGROUND_REQUEST
        );
    }

    private void pedirUbicacionBackground() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                LOCATION_BACKGROUND_REQUEST
        );
    }

    /* ===================== RESULTADO ===================== */

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length == 0) return;

        switch (requestCode) {

            case LOCATION_FOREGROUND_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permisosAPI(); // Continúa flujo
                } else {
                    util.msgSnackBar("Permiso de ubicación requerido", this);
                }
                break;

            case LOCATION_BACKGROUND_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permisosAPI();
                } else {
                    util.msgSnackBar("Permiso de ubicación en segundo plano requerido", this);
                }
                break;

            case CAMERA_REQUEST:
            case PHONE_REQUEST:
            case STORAGE_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permisosAPI();
                } else {
                    util.msgSnackBar("Debe conceder todos los permisos", this);
                }
                break;
        }
    }

   /* private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;
    private static final int  MY_PERMISSIONS_REQUEST_CAMERA= 2;
    private static  final int MY_PERMISSIONS_REQUEST_INTETNER = 3;
    private static  final int MY_PERMISSIONS_REQUEST_GPS = 4;
    private static final int REQUEST_CALL_PHONE = 5;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 6;


    private void permisosAPI(){
        int STORAGE = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int CAMERA = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int GPS = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION);
        int PHONE = ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE);

        int GPS_BACKGROUND = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION);


        if (GPS_BACKGROUND!= PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        if (GPS_BACKGROUND == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }


        if(GPS != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){

            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSIONS_REQUEST_GPS);
            }
        }

        if(GPS == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSIONS_REQUEST_GPS);
        }


        if (CAMERA!= PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }

        if (CAMERA == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }

        if (PHONE!= PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
            }
        }

        if (PHONE == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
        }

       // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {   // API 33+
            // targetSdk >= 33 → WRITE_EXTERNAL_STORAGE no existe
            // Pide solo los que uses

            if (STORAGE != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
            }
            if (STORAGE == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
       // }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permisosAPI();
                } else {
                    util.msgSnackBar("El Permiso de Escritura en memoria es requerida", this);
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permisosAPI();
                } else {
                    util.msgSnackBar("El Permiso de Camara es requerida", this);
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_INTETNER: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permisosAPI();
                } else {
                    util.msgSnackBar("El Permiso de Internet es requerida", this);
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_GPS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permisosAPI();
                } else {
                    util.msgSnackBar("El Permiso de GPS es requerida", this);
                }
                return;
            }
            case REQUEST_CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permisosAPI();
                } else {
                    util.msgSnackBar("El Permiso de Telefono es requerida", this);
                }
                return;
            }
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permisosAPI();
                } else {
                    util.msgSnackBar("El Permiso de GPS en segundo plano es requerida", this);
                }
                return;
            }

        }
    }
    */
    // </editor-fold>

}
