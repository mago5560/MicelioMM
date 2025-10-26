package com.luma.miceliomm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.luma.miceliomm.controller.UsuarioController;
import com.luma.miceliomm.customs.FunctionCustoms;

public class MactyLogin extends AppCompatActivity {

    UsuarioController usuarioController;
    FunctionCustoms util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.macty_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewsById();
        actions();
    }

    private void findViewsById(){
        util = new FunctionCustoms();
        usuarioController = new UsuarioController(this);
        usuarioController.isLogin();
        ((TextView) findViewById(R.id.lblVersion)).setText(util.getVersion(this));
    }

    private void actions(){
        ((Button) findViewById(R.id.btnIngreso)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesion();
            }
        });
    }

    private void iniciarSesion(){
        if(validarCampos()){
            usuarioController.serviceAppLogin(
                    ((EditText) findViewById(R.id.txtUsuario)).getText().toString() ,
                    ((EditText) findViewById(R.id.txtPassword)).getText().toString(),
                    ((CheckBox) findViewById(R.id.chkRecordadIngreso)).isChecked()
            );
        }
    }
    private boolean validarCampos(){
        if(util.validarCampoVacio(((EditText) findViewById(R.id.txtUsuario)))){
            return false;
        }
        if(util.validarCampoVacio(((EditText) findViewById(R.id.txtPassword)))){
            return false;
        }

        return true;
    }

}