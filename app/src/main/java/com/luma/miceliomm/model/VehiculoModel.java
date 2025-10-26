package com.luma.miceliomm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehiculoModel {
    public int Id;

    @SerializedName("idVehiculo")
    @Expose
    public int IdVehiculo = 0;

    @SerializedName("nombre")
    @Expose
    public String Nombre ="";

    @SerializedName("placa")
    @Expose
    public String Placa ="";

    @SerializedName("color")
    @Expose
    public String Color ="";

    @SerializedName("fechaServicio")
    @Expose
    public String FechaServicio ="";

    @SerializedName("comentario")
    @Expose
    public String Comentario ="";

    @SerializedName("tercerizado")
    @Expose
    public boolean Tercerizado=true;

    @SerializedName("activo")
    @Expose
    public boolean Activo=true;

    @SerializedName("fechaCreacion")
    @Expose
    public String FechaCreacion="";

    @Override
    public String toString() { return IdVehiculo + " " + Nombre + " " + Placa ;}
}
