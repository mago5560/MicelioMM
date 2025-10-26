package com.luma.miceliomm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SectorModel {

    public int Id=0;
    @SerializedName("idSectorLogistico")
    @Expose
    public int IdSectorLogistico=0;

    @SerializedName("nombre")
    @Expose
    public String Nombre ="";

    @SerializedName("descripcion")
    @Expose
    public String Descripcion ="";

    @Override
    public String toString() {
        return   IdSectorLogistico + " " + Nombre + " " + Descripcion ;
    }
}
