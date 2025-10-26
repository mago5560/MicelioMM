package com.luma.miceliomm.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.luma.miceliomm.model.VehiculoModel;

import java.util.ArrayList;

/*TODO: Datos de la tabla
*     "CREATE TABLE TblVehiculos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "idVehiculo INTEGER NOT NULL, " +
                    "nombre TEXT, " +
                    "placa TEXT, " +
                    "color TEXT, " +
                    "fechaServicio TEXT, " +
                    "comentario TEXT, " +
                    "tercerizado INTEGER, " +
                    "activo INTEGER, " +
                    "fechaCreacion TEXT " +
*                   ");";
* */
public class VehiculoDao {
    private final SQLiteDatabase db;

    public VehiculoDao(SQLiteDatabase db){
        this.db = db;
    }

    public int delete() {
        //return db.delete("TblVehiculos", "id = ?", new String[]{String.valueOf(id)});
        return db.delete("TblVehiculos", null,null);
    }

    public long insert(VehiculoModel cls){
        ContentValues values = new ContentValues();
        values.put("idVehiculo",cls.IdVehiculo);
        values.put("nombre",cls.Nombre);
        values.put("placa",cls.Placa);
        values.put("color",cls.Color);
        values.put("fechaServicio",cls.FechaServicio);
        values.put("comentario",cls.Comentario);
        if (cls.Tercerizado){values.put("tercerizado",1);}
        else{values.put("tercerizado",0);}

        if (cls.Activo){values.put("activo",1);}
        else{values.put("activo",0);}

        values.put("fechaCreacion",cls.FechaCreacion);

        return db.insert("TblVehiculos",null,values);
    }

    public ArrayList<VehiculoModel> select() {
        ArrayList<VehiculoModel> lst = new ArrayList<>();
        Cursor cursor = db.query("TblVehiculos", null, null, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            VehiculoModel v = new VehiculoModel();
            v.Id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            v.IdVehiculo = cursor.getInt(cursor.getColumnIndexOrThrow("idVehiculo"));
            v.Nombre    = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            v.Placa  =cursor.getString(cursor.getColumnIndexOrThrow("placa"));
            v.Color = cursor.getString(cursor.getColumnIndexOrThrow("color"));

            v.FechaServicio = cursor.getString(cursor.getColumnIndexOrThrow("fechaServicio"));
            v.Comentario = cursor.getString(cursor.getColumnIndexOrThrow("comentario"));

            v.Tercerizado = cursor.getInt(cursor.getColumnIndexOrThrow("tercerizado")) == 1;
            v.Activo = cursor.getInt(cursor.getColumnIndexOrThrow("activo"))==1;

            v.FechaCreacion = cursor.getString(cursor.getColumnIndexOrThrow("fechaCreacion"));

            lst.add(v);
        }
        cursor.close();
        return lst;
    }


    public int totalRegistros(){
        int total=0;
        String sql = "SELECT Count(1) Total " +
                " FROM TblVehiculos  " ;

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        total = cursor.getInt(cursor.getColumnIndexOrThrow("Total"));
        cursor.close();
        return total;
    }
}
