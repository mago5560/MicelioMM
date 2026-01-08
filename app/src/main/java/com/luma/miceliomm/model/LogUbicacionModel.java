package com.luma.miceliomm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogUbicacionModel {
    @SerializedName("latitud")
    @Expose
    public int latitud= 0;
    @SerializedName("longitud")
    @Expose
    public int longitud= 0;
    @SerializedName("idPersonal")
    @Expose
    public int idPersonal= 0;
    @SerializedName("idVehiculo")
    @Expose
    public int idVehiculo= 0;

    @Override
    public String toString() {
        return "LogUbicacionModel{" +
                "latitud=" + latitud +
                ", longitud=" + longitud +
                ", idPersonal=" + idPersonal +
                ", idVehiculo=" + idVehiculo +
                '}';
    }
}
