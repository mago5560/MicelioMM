package com.luma.miceliomm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImagenAdicionalModel {


    @SerializedName("idTrasladoLogistica")
    @Expose
    public int idTrasladoLogistica=0;
    @SerializedName("archivoImagen")
    @Expose
    public String archivoImagen="";
}
