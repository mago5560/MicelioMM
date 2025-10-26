package com.luma.miceliomm;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.luma.miceliomm.customs.AutoUpdate;
import com.luma.miceliomm.customs.FunctionCustoms;
import com.luma.miceliomm.customs.GlobalCustoms;

public class MactyUpdate extends AppCompatActivity {

    private Intent intent;
    private FunctionCustoms util;
    private static final int REQUEST_PERMISSION_CODE = 1001;
    private ProgressBar progressBar;
    private TextView txtStatus;
    private Button btnCheckUpdate;

    private AutoUpdate updater;
    private ProgressDialog pDialog;
    private GlobalCustoms var = GlobalCustoms.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.macty_update);

        findViewsById();
        actions();
    }

    private void findViewsById(){
        progressBar = findViewById(R.id.progressBar);
        txtStatus = findViewById(R.id.txtStatus);
        btnCheckUpdate = findViewById(R.id.btnCheckUpdate);
        util = new FunctionCustoms();
        updater = new AutoUpdate(this);


        ((TextView) findViewById(R.id.lblTituloNavBar)).setText("Actualizador");
    }

    private void actions(){
        btnCheckUpdate.setOnClickListener(v -> {
            if (hasStoragePermission()) {
                checkForUpdates();
            } else {
                requestStoragePermission();
            }
        });


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

    }

    private void checkForUpdates() {
        // Muestra el progreso de búsqueda
        pDialog = ProgressDialog.show(this, "Buscando actualización", "Por favor espere...", true, false);

        // 👆 Reemplaza este enlace por el real de tu JSON (el que contiene versionCode, versionName, downloadURL)

        // Llamada al nuevo método con URL + listener
        updater.checkForUpdate(var.getINFO_FILE(), new AutoUpdate.AutoUpdateListener() {
            @Override
            public void onUpdateAvailable(String latestVersionName, String url) {
                pDialog.dismiss();
                txtStatus.setText("Nueva versión disponible: " + latestVersionName);
                showUpdateDialog(url, latestVersionName); // tu método para mostrar el diálogo de actualización
            }

            @Override
            public void onUpToDate() {
                pDialog.dismiss();
                txtStatus.setText("La aplicación está actualizada ✅");
                Toast.makeText(MactyUpdate.this, "Tu aplicación ya está en la última versión.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String error) {
                pDialog.dismiss();
                txtStatus.setText("Error al verificar actualización");
                Toast.makeText(MactyUpdate.this, "Error: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void showUpdateDialog(String downloadUrl, String versionName) {
        new AlertDialog.Builder(this)
                .setTitle("Actualización disponible")
                .setMessage("Hay una nueva versión (" + versionName + "). ¿Deseas instalarla?")
                .setCancelable(false)
                .setPositiveButton("Actualizar", (dialog, which) -> {
                    progressBar.setIndeterminate(true);
                    txtStatus.setText("Descargando nueva versión...");
                    updater.downloadAndInstall(downloadUrl);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private boolean hasStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return true; // Scoped Storage: no requiere permisos
        }
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE);
        } else {
            checkForUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkForUpdates();
            } else {
                Toast.makeText(this, "Se necesita permiso de almacenamiento para descargar la actualización.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(updater.getOnDownloadCompleteReceiver());
        } catch (Exception ignored) { }
    }


    // <editor-fold defaultstate="collapsed" desc="Menu, Opciones y regresar a principal">

    private void goHome() {
        intent = new Intent().setClass(this, MactyPrincipal.class);
        startActivity(intent);
        finish();
    }

    // </editor-fold>

}