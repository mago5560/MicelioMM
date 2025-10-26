package com.luma.miceliomm.daos;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.luma.miceliomm.model.PilotoModel;

import java.util.ArrayList;

/**TODO: "CREATE TABLE TblPiloto (" +
         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
         "IdPersonal INTEGER NOT NULL, " +
         "nombre TEXT, " +
         "descripcion TEXT, " +
         "licencia TEXT, " +
         "activo INTEGER, " +
         "idVehiculo INTEGER, " +
         "fechaVencimiento TEXT, " +
         "fechaCreacion TEXT " +
         ");"
 **/
public class PilotoDao {
    private final SQLiteDatabase db;

    public PilotoDao(SQLiteDatabase db){
        this.db = db;
    }

    public int delete() {
        //return db.delete("TblVehiculos", "id = ?", new String[]{String.valueOf(id)});
        return db.delete("TblPiloto", null,null);
    }

    public long insert(PilotoModel cls){
        ContentValues values = new ContentValues();
        values.put("IdPersonal",cls.IdVehiculo);
        values.put("nombre",cls.Nombre);
        values.put("descripcion",cls.Descripcion);
        values.put("licencia",cls.Licencia);
        values.put("idVehiculo",cls.IdVehiculo);
        values.put("fechaVencimiento",cls.FechaVencimiento);
        if (cls.Activo){values.put("activo",1);}
        else{values.put("activo",0);}
        values.put("fechaCreacion",cls.FechaCreacion);

        return db.insert("TblPiloto",null,values);
    }

    public ArrayList<PilotoModel> select() {
        ArrayList<PilotoModel> lst = new ArrayList<>();
        Cursor cursor = db.query("TblPiloto", null, null, null, null, null, null);
        //cursor.moveToFirst();
        while (cursor.moveToNext()) {
            PilotoModel v = new PilotoModel();
            v.Id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            v.IdPersonal = cursor.getInt(cursor.getColumnIndexOrThrow("IdPersonal"));
            v.Nombre    = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            v.Descripcion  =cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
            v.Licencia = cursor.getString(cursor.getColumnIndexOrThrow("licencia"));
            v.IdVehiculo = cursor.getInt(cursor.getColumnIndexOrThrow("idVehiculo"));
            v.FechaVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("fechaVencimiento"));
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
                " FROM TblPiloto  " ;

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        total = cursor.getInt(cursor.getColumnIndexOrThrow("Total"));
        cursor.close();
        return total;
    }

}
