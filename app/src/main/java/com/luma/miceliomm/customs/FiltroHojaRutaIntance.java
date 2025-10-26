package com.luma.miceliomm.customs;

public class FiltroHojaRutaIntance {


    private static FiltroHojaRutaIntance instance;

    public FiltroHojaRutaIntance() {
    }

    public static  synchronized FiltroHojaRutaIntance getInstance(){
        if(instance == null){
            instance = new FiltroHojaRutaIntance();
        }
        return instance;
    }

    private String FechaInicial="";
    private String FechaFinal="";
    private boolean Transferido= false;
    private boolean SinTransferir=true;

    public String getFechaInicial() {
        return FechaInicial;
    }

    public void setFechaInicial(String fechaInicial) {
        FechaInicial = fechaInicial;
    }

    public String getFechaFinal() {
        return FechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        FechaFinal = fechaFinal;
    }

    public boolean isTransferido() {
        return Transferido;
    }

    public void setTransferido(boolean transferido) {
        Transferido = transferido;
    }

    public boolean isSinTransferir() {
        return SinTransferir;
    }

    public void setSinTransferir(boolean sinTransferir) {
        SinTransferir = sinTransferir;
    }

}
