package com.luma.miceliomm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogUbicacionModel {
    @SerializedName("latitud")
    @Expose
    private int latitud= 0;
    @SerializedName("longitud")
    @Expose
    private int longitud= 0;
    @SerializedName("idPersonal")
    @Expose
    private int idPersonal= 0;
    @SerializedName("idVehiculo")
    @Expose
    private int idVehiculo= 0;
}
