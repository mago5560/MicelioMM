package com.luma.miceliomm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PilotoModel {

    public int  Id=0;
    @SerializedName("idPersonal")
    @Expose
    public int IdPersonal = 0;

    @SerializedName("nombre")
    @Expose
    public String Nombre ="";

    @SerializedName("descripcion")
    @Expose
    public String Descripcion ="";

    @SerializedName("licencia")
    @Expose
    public String Licencia ="";

    @SerializedName("activo")
    @Expose
    public boolean Activo = true;

    @SerializedName("idVehiculo")
    @Expose
    public int IdVehiculo = 0 ;

    @SerializedName("fechaVencimiento")
    @Expose
    public String FechaVencimiento ="";

    @SerializedName("fechaCreacion")
    @Expose
    public String FechaCreacion ="";

    @Override
    public String toString() {
        return   IdPersonal +
                " " + Nombre  +
                " " + Descripcion +
                " " + Licencia  +
                " " + IdVehiculo ;
    }
}
