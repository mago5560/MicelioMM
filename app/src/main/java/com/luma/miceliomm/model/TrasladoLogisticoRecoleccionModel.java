package com.luma.miceliomm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;

public class TrasladoLogisticoRecoleccionModel {

    @SerializedName("idHojaRuta")
    @Expose
    public int idHojaDeRuta = 0;

    @SerializedName("idTraslado")
    @Expose
    public int idTraslado = 0;
    public int idTrasladoLogistico=0;

    @SerializedName("idEstado")
    @Expose
    public int idEstado = 0;

    @SerializedName("observaciones")
    @Expose
    public String observaciones="";
    public String rutaImagen="";
    public String nombreImagen="";
    public String fechaHoraRecoleccion="";

    public File fileImagen=null;
}
