package com.luma.miceliomm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
        //NavHostFragment navHostFragment =  (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        //NavigationUI.setupWithNavController(((BottomNavigationView) findViewById(R.id.bottomNavigationView)), navHostFragment.getNavController());
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


    // <editor-fold defaultstate="collapsed" desc="(PERMISOS)">
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;
    private static final int  MY_PERMISSIONS_REQUEST_CAMERA= 2;
    private static  final int MY_PERMISSIONS_REQUEST_INTETNER = 3;
    private static  final int MY_PERMISSIONS_REQUEST_GPS = 4;


    private void permisosAPI(){
        int STORAGE = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int CAMERA = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int GPS = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION);

        if (STORAGE != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }

        if(GPS != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){

            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSIONS_REQUEST_GPS);
            }
        }

        if (STORAGE == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
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

        }
    }
    // </editor-fold>

}
