package com.luma.miceliomm.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.luma.miceliomm.R;
import com.luma.miceliomm.controller.LogUbicacionController;
import com.luma.miceliomm.customs.ServicesExtrasCustoms;
import com.luma.miceliomm.model.LogUbicacionModel;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service implements
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks {


    private static final String TAG = "LocationService";
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "LocationServiceChannel";


    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    private LogUbicacionController controller;
    private LogUbicacionModel cls;
   private ServicesExtrasCustoms extras = ServicesExtrasCustoms.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();
        buildGoogleApiClient();
        createLocationRequest();
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, createNotification());
        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
        cls = new LogUbicacionModel();
        controller = new LogUbicacionController(getApplicationContext());
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!googleApiClient.isConnected())
            googleApiClient.connect();
        return START_STICKY;
    }


    /*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (!googleApiClient.isConnected())
                googleApiClient.connect();
        }, 1000);

        return START_STICKY;
    }
     */


    @Override
    public void onDestroy() {
        if (googleApiClient.isConnected()) {
          //  LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
        Log.d(TAG, "GoogleApiClient disconnect");
        super.onDestroy();
    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000); // 10 seconds
        //locationRequest.setFastestInterval(5000); // 5 seconds
      // locationRequest.setFastestInterval(300000); // 3 minutos
        locationRequest.setFastestInterval(500000); // 5 minutos
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "GoogleApiClient connected");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "GoogleApiClient connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "GoogleApiClient connection failed");
    }


    @Override
    public void onLocationChanged(android.location.Location location) {
        // Aquí recibes las actualizaciones de ubicación
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        // Hacer lo que necesites con la ubicación...
        cls.latitud = (int) latitude;
        cls.longitud = (int) longitude;
        cls.idVehiculo = extras.getIdVehiculo();
        cls.idPersonal = extras.getIdPiloto();
        controller.insertarLogUbicacion(cls);
        //Log.i(TAG, "Location: " + latitude +","+longitude);
        Log.i(TAG, cls.toString());
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Location Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    private Notification createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("MicelioApp")
                .setContentText("Inicio de ruta...")
                .setSmallIcon(R.mipmap.ic_launcher); // Reemplaza esto con tu propio ícono de notificación

        return builder.build();
    }


}
