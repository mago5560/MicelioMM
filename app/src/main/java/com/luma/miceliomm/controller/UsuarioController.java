package com.luma.miceliomm.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.luma.miceliomm.MactyLogin;
import com.luma.miceliomm.MactyPrincipal;
import com.luma.miceliomm.customs.FunctionCustoms;
import com.luma.miceliomm.customs.GlobalCustoms;
import com.luma.miceliomm.customs.HttpsInseguraHELP;
import com.luma.miceliomm.customs.TokenManager;
import com.luma.miceliomm.model.LoginModel;
import com.luma.miceliomm.model.UsuarioModel;
import com.luma.miceliomm.service.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsuarioController {
    private Context context;
    private Intent intent;
    private ApiService apiService;
    private GlobalCustoms var = GlobalCustoms.getInstance();
    private FunctionCustoms util;

    private SharedPreferences sharedPreferences;
    private static final String Preferences = "usuario";

    public UsuarioController(Context context) {
        this.context = context;
        this.context = context;
        this.util = new FunctionCustoms();
        sharedPreferences = this.context.getSharedPreferences(Preferences, Context.MODE_PRIVATE);
    }


    public void serviceAppLogin(String Usuario, String Password, Boolean Recordar){
        ProgressDialog pDialog;
        pDialog = ProgressDialog.show(context, "Iniciando sesion", "Espere....", true, false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(var.getURL_API())
                .client(HttpsInseguraHELP.getUnsafeOkHttpClient())  // ← Usar cliente inseguro
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        LoginModel loginModel = new LoginModel();
        loginModel.Usuario = Usuario;
        loginModel.Password = Password;


        Call<UsuarioModel> call  = apiService.postLogin(loginModel);

        call.enqueue(new Callback<UsuarioModel>() {
            @Override
            public void onResponse(Call<UsuarioModel> call, Response<UsuarioModel> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    if (response.body() != null) {
                        response.body().setUsuario(Usuario);
                        response.body().setPassword(Password);
                        response.body().setRecordar(Recordar);

                        // Guardar tokens en SharedPreferences
                        TokenManager tokenManager = new TokenManager(context);
                        tokenManager.saveTokens(
                                response.body().getToken(),
                                "Token Now"
                        );

                        setLogin(response.body());
                        mactyPrincipal();
                    } else {
                        util.mensaje("Ruta o Contraseña incorrecta, favor de verificar y volver a intentar", ((Activity) context)).show();
                    }

                } else {
                    util.mensaje("Ocurrio un problema al iniciar aplicacion, verifique su conexion a internet y vuelva a intentar", ((Activity) context)).show();
                }
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<UsuarioModel> call, Throwable t) {
                util.mensajeError(t.getMessage() + "\nVuelva a intentar", ((Activity) context)).show();
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });


    }

    public UsuarioModel getUsuario() {
        UsuarioModel usuarioModel = new UsuarioModel();
        usuarioModel.setUsuario(sharedPreferences.getString("usuario", ""));
        usuarioModel.setPassword(sharedPreferences.getString("password", ""));
        usuarioModel.setToken(sharedPreferences.getString("token", ""));
        usuarioModel.setRecordar(sharedPreferences.getBoolean("recordar", false));
        return usuarioModel;
    }



    public void setLogout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usuario", "");
        editor.putString("password", "");
        editor.putString("token", "");
        editor.putBoolean("recordar", false);
        editor.commit();
        mactyLogin();
    }

    public void setLogin(UsuarioModel cls) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usuario", cls.getUsuario());
        editor.putString("password", cls.getPassword());
        editor.putBoolean("recordar", cls.isRecordar());

        editor.commit();
    }

    public void isLogin() {
        if (getUsuario().isRecordar()) {
            mactyPrincipal();
        }
    }

    private void mactyPrincipal() {
        intent = new Intent().setClass(context, MactyPrincipal.class);
        ((Activity) context).startActivity(intent);
        ((Activity) context).finish();
    }

    private void mactyLogin() {
        intent = new Intent().setClass(context, MactyLogin.class);
        ((Activity) context).startActivity(intent);
        ((Activity) context).finish();
    }

}
