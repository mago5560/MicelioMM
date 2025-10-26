package com.luma.miceliomm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActualizaHojaDeRutaModel {

    @SerializedName("idHojaDeRuta")
    @Expose
    public int idHojaDeRuta = 0;

    @SerializedName("idEstado")
    @Expose
    public int idEstado = 0;

    @SerializedName("vale")
    @Expose
    public int vale = 0;

    @SerializedName("galones")
    @Expose
    public double galones = 0;

    @SerializedName("otrosGastos")
    @Expose
    public double otrosGastos = 0;

    @SerializedName("fechaHoraSalida")
    @Expose
    public String fechaHoraSalida = "";

    @SerializedName("fechaHoraRegreso")
    @Expose
    public String fechaHoraRegreso = "";

    @SerializedName("kmInicial")
    @Expose
    public int kmInicial = 0;

    @SerializedName("kmFinal")
    @Expose
    public int kmFinal = 0;

    @SerializedName("latitudInicial")
    @Expose
    public String latitudInicial = "";

    @SerializedName("longitudInicial")
    @Expose
    public String longitudInicial = "";

    @SerializedName("latitudFinal")
    @Expose
    public String latitudFinal = "";

    @SerializedName("longitudFinal")
    @Expose
    public String longitudFinal = "";

}
