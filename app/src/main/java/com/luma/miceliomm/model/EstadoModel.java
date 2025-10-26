package com.luma.miceliomm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EstadoModel {

    public int Id=0;
    @SerializedName("idEstado")
    @Expose
    public int IdEstado=0;

    @SerializedName("nombre")
    @Expose
    public String Nombre ="";

    @SerializedName("descripcion")
    @Expose
    public String Descripcion ="";

    @SerializedName("fechaCreacion")
    @Expose
    public String FechaCreacion ="";

    @Override
    public String toString() {
        return IdEstado + " " + Nombre +" " + Descripcion;
    }
}
