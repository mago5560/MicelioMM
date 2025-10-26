package com.luma.miceliomm.model;

public class HojaRutaResumenModel {

    public int idHoraRuta =0;
    public int hojaDeRutaEstado = 0;
    public String hojaRutaNombreEstado = "";
    public int idSectorLogistico =0;
    public String nombreSectorLogistico="";
    public int idPiloto=0;
    public String nombrePiloto ="";
    public int totalBultos=0;
    public String fecha="";

    public  ActualizaHojaDeRutaModel actualizaHojaDeRutaModel= new ActualizaHojaDeRutaModel();


    @Override
    public String toString() {
        return  idHoraRuta + " " + idSectorLogistico + " " + nombreSectorLogistico +  " " + idPiloto + " " + nombrePiloto ;
    }
}
