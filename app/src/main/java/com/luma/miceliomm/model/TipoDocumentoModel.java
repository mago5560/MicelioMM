package com.luma.miceliomm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TipoDocumentoModel {

    public int id=0;

    @SerializedName("descripcion")
    @Expose
    public String descripcion ="";

}
