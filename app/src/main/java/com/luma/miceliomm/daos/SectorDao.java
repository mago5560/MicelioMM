package com.luma.miceliomm.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.luma.miceliomm.model.SectorModel;

import java.util.ArrayList;

/**TODO: "CREATE TABLE TblSector (" +
         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
         "idSectorLogistico INTEGER NOT NULL, " +
         "nombre TEXT, " +
         "descripcion TEXT " +
         ");"
 **/
public class SectorDao {

    private final SQLiteDatabase db;

    public SectorDao(SQLiteDatabase db){
        this.db = db;
    }

    public int delete() {
        //return db.delete("TblVehiculos", "id = ?", new String[]{String.valueOf(id)});
        return db.delete("TblSector", null,null);
    }

    public long insert(SectorModel cls){
        ContentValues values = new ContentValues();
        values.put("idSectorLogistico",cls.IdSectorLogistico);
        values.put("nombre",cls.Nombre);
        values.put("descripcion",cls.Descripcion);

        return db.insert("TblSector",null,values);
    }

    public ArrayList<SectorModel> select() {
        ArrayList<SectorModel> lst = new ArrayList<>();
        Cursor cursor = db.query("TblSector", null, null, null, null, null, null);
        //cursor.moveToFirst();
        while (cursor.moveToNext()) {
            SectorModel v = new SectorModel();
            v.Id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            v.IdSectorLogistico = cursor.getInt(cursor.getColumnIndexOrThrow("idSectorLogistico"));
            v.Nombre    = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            v.Descripcion  =cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));

            lst.add(v);
        }
        cursor.close();
        return lst;
    }

    public int totalRegistros(){
        int total=0;
        String sql = "SELECT Count(1) Total " +
                " FROM TblSector  " ;

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        total = cursor.getInt(cursor.getColumnIndexOrThrow("Total"));
        cursor.close();
        return total;
    }
}
