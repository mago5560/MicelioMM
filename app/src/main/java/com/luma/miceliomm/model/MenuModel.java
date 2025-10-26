package com.luma.miceliomm.model;

import android.graphics.drawable.Drawable;

public class MenuModel {

    private Drawable drawable ;
    private String nombre;
    private int id;

    public MenuModel(Drawable drawable, String nombre, int id) {
        this.drawable = drawable;
        this.nombre = nombre;
        this.id = id;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }
}
