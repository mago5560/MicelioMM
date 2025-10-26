package com.luma.miceliomm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsuarioModel {
    private String Usuario="";
    private String Password="";
    @SerializedName("token")
    @Expose
    private String Token="";
    private boolean Recordar=true;

    public String getUsuario() {
        return Usuario;
    }
    public void setUsuario(String usuario) {
        Usuario = usuario;
    }
    public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        Password = password;
    }
    public String getToken() {
        return Token;
    }
    public void setToken(String token) {
        Token = token;
    }
    public boolean isRecordar() {
        return Recordar;
    }
    public void setRecordar(boolean recordar) {
        Recordar = recordar;
    }
}

