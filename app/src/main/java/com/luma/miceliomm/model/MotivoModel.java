package com.luma.miceliomm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MotivoModel {

    public int Id=0;
    @SerializedName("idMotivoDeRechazo")
    @Expose
    public int IdMotivoDeRechazo=0;

    @SerializedName("descripcion")
    @Expose
    public String Descripcion ="";

    @SerializedName("fechaCreacion")
    @Expose
    public String FechaCreacion ="";

    @Override
    public String toString() {
        return   IdMotivoDeRechazo + " " + Descripcion ;
    }
}
