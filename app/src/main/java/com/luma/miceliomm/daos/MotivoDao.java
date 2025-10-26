package com.luma.miceliomm.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.luma.miceliomm.model.MotivoModel;

import java.util.ArrayList;

/**TODO: "CREATE TABLE TblMotivos (" +
         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
         "idMotivoDeRechazo INTEGER NOT NULL, " +
         "descripcion TEXT, " +
         "fechaCreacion TEXT " +
         ");"
 **/
public class MotivoDao {

    private final SQLiteDatabase db;

    public MotivoDao(SQLiteDatabase db){
        this.db = db;
    }

    public int delete() {
        //return db.delete("TblVehiculos", "id = ?", new String[]{String.valueOf(id)});
        return db.delete("TblMotivos", null,null);
    }

    public long insert(MotivoModel cls){
        ContentValues values = new ContentValues();
        values.put("idMotivoDeRechazo",cls.IdMotivoDeRechazo);
        values.put("descripcion",cls.Descripcion);
        values.put("fechaCreacion",cls.FechaCreacion);

        return db.insert("TblMotivos",null,values);
    }

    public ArrayList<MotivoModel> select() {
        ArrayList<MotivoModel> lst = new ArrayList<>();
        Cursor cursor = db.query("TblMotivos", null, null, null, null, null, null);
        //cursor.moveToFirst();
        while (cursor.moveToNext()) {
            MotivoModel v = new MotivoModel();
            v.Id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            v.IdMotivoDeRechazo = cursor.getInt(cursor.getColumnIndexOrThrow("idMotivoDeRechazo"));
            v.Descripcion    = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
            v.FechaCreacion = cursor.getString(cursor.getColumnIndexOrThrow("fechaCreacion"));

            lst.add(v);
        }
        cursor.close();
        return lst;
    }

    public int totalRegistros(){
        int total=0;
        String sql = "SELECT Count(1) Total " +
                " FROM TblMotivos  " ;

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        total = cursor.getInt(cursor.getColumnIndexOrThrow("Total"));
        cursor.close();
        return total;
    }
}
