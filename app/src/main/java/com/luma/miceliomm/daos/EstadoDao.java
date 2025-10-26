package com.luma.miceliomm.daos;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.luma.miceliomm.model.EstadoModel;
import java.util.ArrayList;

/**
 * TODO:     "CREATE TABLE TblEstados (" +
 *                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
 *                     "idEstado INTEGER NOT NULL, " +
 *                     "nombre TEXT, " +
 *                     "descripcion TEXT, " +
 *                     "fechaCreacion TEXT " +
 *                     ");";
 * **/
public class EstadoDao {

    private final SQLiteDatabase db;

    public EstadoDao(SQLiteDatabase db) {
        this.db = db;
    }

    public int delete() {
        //return db.delete("TblVehiculos", "id = ?", new String[]{String.valueOf(id)});
        return db.delete("TblEstados", null,null);
    }

    public long insert(EstadoModel cls){
        ContentValues values = new ContentValues();
        values.put("idEstado",cls.IdEstado);
        values.put("nombre",cls.Nombre);
        values.put("descripcion",cls.Descripcion);
        values.put("fechaCreacion",cls.FechaCreacion);
        return db.insert("TblEstados",null,values);
    }

    public ArrayList<EstadoModel> select() {
        ArrayList<EstadoModel> lst = new ArrayList<>();
        Cursor cursor = db.query("TblEstados", null, null, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            EstadoModel v = new EstadoModel();
            v.Id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            v.IdEstado = cursor.getInt(cursor.getColumnIndexOrThrow("idEstado"));
            v.Nombre    = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            v.Descripcion  =cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
            v.FechaCreacion = cursor.getString(cursor.getColumnIndexOrThrow("fechaCreacion"));

            lst.add(v);
        }
        cursor.close();
        return lst;
    }

    public int totalRegistros(){
        int total=0;
        String sql = "SELECT Count(1) Total " +
                " FROM TblEstados  " ;

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        total = cursor.getInt(cursor.getColumnIndexOrThrow("Total"));
        cursor.close();
        return total;
    }

}
