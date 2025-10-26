package com.luma.miceliomm.customs;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AutoUpdate {

    private final Context context;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private int currentVersionCode;
    private String currentVersionName;
    private int latestVersionCode;
    private String latestVersionName;
    private String downloadURL;

    private long downloadId;
    private ProgressDialog progressDialog;

    private GlobalCustoms var = GlobalCustoms.getInstance();

    private BroadcastReceiver onDownloadComplete;

    public interface AutoUpdateListener {
        void onUpdateAvailable(String latestVersionName, String url);
        void onUpToDate();
        void onError(String error);
    }

    public AutoUpdate(Context context) {
        this.context = context;
    }

    /** 🔍 Verifica si hay una actualización disponible */
    public void checkForUpdate(String jsonUrl, AutoUpdateListener listener) {
        executor.execute(() -> {
            try {
                PackageInfo pkg = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                currentVersionCode = pkg.versionCode;
                currentVersionName = pkg.versionName;

                String data = downloadHttp(new URL(jsonUrl));
                JSONArray jsonArray = new JSONArray(data);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                latestVersionCode = jsonObject.getInt("versionCode");
                latestVersionName = jsonObject.getString("versionName");
                downloadURL = jsonObject.getString("downloadURL");

                mainHandler.post(() -> {
                    if (latestVersionCode > currentVersionCode || Double.valueOf(latestVersionName) > Double.valueOf(currentVersionName)) {
                        listener.onUpdateAvailable(latestVersionName, downloadURL);
                    } else {
                        listener.onUpToDate();
                    }
                    if (progressDialog != null) progressDialog.dismiss();
                    Toast.makeText(context, "Descarga completa. Instalando actualización...", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                Log.e("AutoUpdate", "Error verificando actualización", e);
                mainHandler.post(() -> listener.onError(e.getMessage()));
            }
        });
    }

    /** 🌐 Descarga JSON */
    private static String downloadHttp(URL url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) builder.append(line);
        reader.close();
        conn.disconnect();
        return builder.toString();
    }

    /** ⬇️ Descarga e instala la nueva versión */
    // ======================
// NUEVO MÉTODO COMPLETO
// ======================
    public void downloadAndInstall(String url) {
        ProgressDialog pDialog = ProgressDialog.show(context, "Actualizando", "Descargando nueva versión...", true, false);

        executor.execute(() -> {
            try {
                Uri uri = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setTitle("Descargando actualización");
                request.setDescription("Espere mientras se descarga la nueva versión...");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, var.getNAME_APP());

                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                long downloadId = manager.enqueue(request);

                // BroadcastReceiver para cuando la descarga termine
                onDownloadComplete = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context c, Intent intent) {
                        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                        if (id == downloadId) {
                            pDialog.dismiss();
                            context.unregisterReceiver(onDownloadComplete);

                            File apkFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), var.getNAME_APP());

                            if (apkFile.exists()) {
                                mainHandler.post(() -> {
                                    Toast.makeText(context, "Descarga completa. Instalando actualización...", Toast.LENGTH_SHORT).show();
                                    installApk(apkFile);
                                });
                            } else {
                                mainHandler.post(() -> {
                                    Toast.makeText(context, "Error: no se encontró el archivo descargado.", Toast.LENGTH_LONG).show();
                                });
                            }
                        }
                    }
                };

                // Registrar el receiver de forma segura
                context.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

            } catch (Exception e) {
                Log.e("AutoUpdate", "Error en descarga: ", e);
                mainHandler.post(() -> {
                    pDialog.dismiss();
                    Toast.makeText(context, "Error al descargar actualización: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }


    /** ⚙️ Instalación segura del APK */
    @SuppressLint("QueryPermissionsNeeded")
    private void installApk(File apkFile) {
        try {
            Uri apkUri = FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".fileprovider",
                    apkFile
            );

            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setData(apkUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        } catch (ActivityNotFoundException e) {
            Log.e("AutoUpdate", "Instalador no encontrado: " + e.getMessage());
        } catch (Exception e) {
            Log.e("AutoUpdate", "Error instalando APK", e);
        }
    }


    public BroadcastReceiver getOnDownloadCompleteReceiver() {
        return onDownloadComplete;
    }
}
