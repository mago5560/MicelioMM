package com.luma.miceliomm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogUbicacionModel {
    @SerializedName("latitud")
    @Expose
    public Double latitud= 0.00;
    @SerializedName("longitud")
    @Expose
    public Double longitud= 0.00;
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
