package com.luma.miceliomm.customs;

public class GlobalCustoms {

    private static GlobalCustoms instance;

    public GlobalCustoms() {
    }

    public static  synchronized  GlobalCustoms getInstance(){
        if(instance == null){
            instance = new GlobalCustoms();
        }
        return instance;
    }

    private String URL_API="https://intweb.mm.soul-t.net/api/";
    private String INFO_FILE="https://www.dropbox.com/scl/fi/vef1pu2ar0vn7uo676wzu/version.txt?rlkey=btl1qc69bxpt6571r4u5fu4u1&st=t9h1b1mn&dl=1";
    private String LINK_APP="https://dl.dropbox.com/scl/fi/4h3q48zyeb28kzjwgra8q/MicelioMM.apk?rlkey=g33rx3z8b5a8d51yg389w1e3o&st=zg609fuq";

    private String PATH_FILE_APP="/MicelioMM";
    private String URL_FIREBASE = "gs://rutas-marcas-mundiales.appspot.com/Imagenes";

    private String NAME_APP="MicelioMM.apk";


    public String getURL_API() {
        return URL_API;
    }

    public String getINFO_FILE() {
        return INFO_FILE;
    }

    public String getLINK_APP() {
        return LINK_APP;
    }

    public String getPATH_FILE_APP() {
        return PATH_FILE_APP;
    }

    public String getURL_FIREBASE() {
        return URL_FIREBASE;
    }

    public String getNAME_APP() {
        return NAME_APP;
    }

    // <editor-fold defaultstate="collapsed" desc="(VAR versiones Update)">
    private String VERSION_APP="";
    private String VERSION_APP_SERVER="";

    public String getVERSION_APP() {
        return VERSION_APP;
    }

    public void setVERSION_APP(String VERSION_APP) {
        this.VERSION_APP = VERSION_APP;
    }

    public String getVERSION_APP_SERVER() {
        return VERSION_APP_SERVER;
    }

    public void setVERSION_APP_SERVER(String VERSION_APP_SERVER) {
        this.VERSION_APP_SERVER = VERSION_APP_SERVER;
    }
    // </editor-fold>
}
