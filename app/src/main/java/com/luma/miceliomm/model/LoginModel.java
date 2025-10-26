package com.luma.miceliomm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {
    @SerializedName("usuario")
    @Expose
    public String Usuario="";

    @SerializedName("password")
    @Expose
    public String Password="";


}
