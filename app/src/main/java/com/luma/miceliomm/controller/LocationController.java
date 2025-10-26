package com.luma.miceliomm.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;


public class LocationController {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final long MIN_TIME_BETWEEN_UPDATES = 1000; // 1 segundo
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 1 metro

    private Context context;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private List<LocationUpdateListener> listeners;
    private Location lastKnownLocation;

    // Interfaz para recibir actualizaciones de ubicación
    public interface LocationUpdateListener {
        void onLocationUpdated(Location location);
        void onLocationError(String error);
        void onPermissionsDenied();
    }

    public LocationController(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.listeners = new ArrayList<>();
        setupLocationListener();
    }

    private void setupLocationListener() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastKnownLocation = location;
                notifyLocationUpdated(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {
                notifyLocationError("Proveedor de ubicación deshabilitado: " + provider);
            }
        };
    }

    // Verificar si los permisos están concedidos
    public boolean hasLocationPermissions() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // Solicitar permisos
    public void requestLocationPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    // Manejar resultado de permisos
    public boolean onRequestPermissionsResult(int requestCode, int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                notifyPermissionsDenied();
                return false;
            }
        }
        return false;
    }

    // Iniciar actualizaciones de ubicación
    public void startLocationUpdates() {
        if (!hasLocationPermissions()) {
            notifyLocationError("Permisos de ubicación no concedidos");
            return;
        }

        try {
            // Verificar proveedores disponibles
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BETWEEN_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        locationListener,
                        Looper.getMainLooper()
                );
            }

            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BETWEEN_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        locationListener,
                        Looper.getMainLooper()
                );
            }

            // Obtener última ubicación conocida inmediatamente
            getLastKnownLocation();

        } catch (SecurityException e) {
            notifyLocationError("Error de seguridad: " + e.getMessage());
        } catch (Exception e) {
            notifyLocationError("Error al iniciar ubicación: " + e.getMessage());
        }
    }

    // Detener actualizaciones de ubicación
    public void stopLocationUpdates() {
        try {
            locationManager.removeUpdates(locationListener);
        } catch (SecurityException e) {
            notifyLocationError("Error de seguridad al detener ubicación");
        }
    }

    // Obtener última ubicación conocida
    public Location getLastKnownLocation() {
        if (!hasLocationPermissions()) {
            return null;
        }

        try {
            Location gpsLocation = null;
            Location networkLocation = null;

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            // Determinar la mejor ubicación disponible
            if (gpsLocation != null && networkLocation != null) {
                if (gpsLocation.getAccuracy() > networkLocation.getAccuracy()) {
                    lastKnownLocation = networkLocation;
                } else {
                    lastKnownLocation = gpsLocation;
                }
            } else if (gpsLocation != null) {
                lastKnownLocation = gpsLocation;
            } else if (networkLocation != null) {
                lastKnownLocation = networkLocation;
            }

            if (lastKnownLocation != null) {
                notifyLocationUpdated(lastKnownLocation);
            }

            return lastKnownLocation;

        } catch (SecurityException e) {
            notifyLocationError("Error de seguridad al obtener ubicación");
            return null;
        }
    }

    // Métodos para agregar/remover listeners
    public void addLocationListener(LocationUpdateListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeLocationListener(LocationUpdateListener listener) {
        listeners.remove(listener);
    }

    public void removeAllListeners() {
        listeners.clear();
    }

    // Notificar a los listeners
    private void notifyLocationUpdated(Location location) {
        for (LocationUpdateListener listener : listeners) {
            listener.onLocationUpdated(location);
        }
    }

    private void notifyLocationError(String error) {
        for (LocationUpdateListener listener : listeners) {
            listener.onLocationError(error);
        }
    }

    private void notifyPermissionsDenied() {
        for (LocationUpdateListener listener : listeners) {
            listener.onPermissionsDenied();
        }
    }

    // Verificar si el GPS está habilitado
    public boolean isGPSEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    // Obtener ubicación actual (puede ser null)
    public Location getCurrentLocation() {
        return lastKnownLocation;
    }

    // Método utilitario para formatear coordenadas
    public static String formatCoordinates(Location location) {
        if (location == null) return "No disponible";
        return String.format("Lat: %.6f, Lng: %.6f",
                location.getLatitude(), location.getLongitude());
    }

    // Método para calcular distancia entre dos puntos
    public static float calculateDistance(Location start, Location end) {
        if (start == null || end == null) return 0;
        return start.distanceTo(end);
    }
}
