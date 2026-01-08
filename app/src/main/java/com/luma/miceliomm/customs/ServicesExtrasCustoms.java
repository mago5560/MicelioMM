package com.luma.miceliomm.customs;

public class ServicesExtrasCustoms {

    private static ServicesExtrasCustoms instance;

    public ServicesExtrasCustoms() {
    }

    public static  synchronized  ServicesExtrasCustoms getInstance(){
        if(instance == null){
            instance = new ServicesExtrasCustoms();
        }
        return instance;
    }


    private int idPiloto=0;
    private int idVehiculo =0;

    public int getIdPiloto() {
        return idPiloto;
    }

    public void setIdPiloto(int idPiloto) {
        this.idPiloto = idPiloto;
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }
}
