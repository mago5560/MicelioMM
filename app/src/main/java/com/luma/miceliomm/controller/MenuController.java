package com.luma.miceliomm.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.luma.miceliomm.R;
import com.luma.miceliomm.adapter.MenuAdapter;
import com.luma.miceliomm.model.MenuModel;

import java.util.ArrayList;

public class MenuController {

    RecyclerView recyclerView;
    MenuAdapter menuAdapter;
    ProgressDialog pDialog;
    Context context;
    UsuarioController usuarioController;

    public MenuController(Context context) {
        this.context = context;
        usuarioController = new UsuarioController(this.context);
    }

    public MenuController(RecyclerView recyclerView, MenuAdapter menuAdapter, Context context) {
        this.recyclerView = recyclerView;
        this.menuAdapter = menuAdapter;
        this.context = context;
        usuarioController = new UsuarioController(this.context);
    }

    public void fillOpciones(){
        ArrayList<MenuModel> list = new ArrayList<>();
        list.add(new MenuModel(context.getResources().getDrawable(R.drawable.truck),"Hoja Ruta",1 ));
        list.add(new MenuModel(context.getResources().getDrawable(R.drawable.downloader_rout),"Descargar Ruta",5 ));
        list.add(new MenuModel(context.getResources().getDrawable(R.drawable.master),"Maestros",2 ));
        list.add(new MenuModel(context.getResources().getDrawable(R.drawable.update),"Actualizar App",3 ));
        list.add(new MenuModel(context.getResources().getDrawable(R.drawable.share_app),"Compartir App",4 ));
        list.add(new MenuModel(context.getResources().getDrawable(R.drawable.logout),"Salir",0 ));

        menuAdapter.setInfo(list);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int numeroColumnas = (int) (dpWidth / 150); //para que solo existan 2 columnas si se require 3 son 100
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(numeroColumnas, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(menuAdapter);

    }
}
