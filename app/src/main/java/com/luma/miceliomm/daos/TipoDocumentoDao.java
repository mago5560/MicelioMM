package com.luma.miceliomm.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.luma.miceliomm.model.MotivoModel;
import com.luma.miceliomm.model.TipoDocumentoModel;

import java.util.ArrayList;

public class TipoDocumentoDao {


    private final SQLiteDatabase db;

    public TipoDocumentoDao(SQLiteDatabase db){
        this.db = db;
    }
/*
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
*/
    public ArrayList<TipoDocumentoModel> select() {
        ArrayList<TipoDocumentoModel> lst = new ArrayList<>();

        String sql =
                " SELECT 1 AS idTipoDocumento, 'Factura Firmada' AS descripcion " +
                        " UNION ALL " +
                        " SELECT 2, 'Contraseña' " +
                        " UNION ALL " +
                        " SELECT 3, 'Albaran' " ;

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            TipoDocumentoModel v = new TipoDocumentoModel();
            v.id = cursor.getInt(cursor.getColumnIndexOrThrow("idTipoDocumento"));
            v.descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));

            lst.add(v);
        }

        cursor.close();
        return lst;
    }

    public int totalRegistros(){
        int total=0;
        String sql = "SELECT 3 Total " ;//+
                //" FROM TblMotivos  " ;

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        total = cursor.getInt(cursor.getColumnIndexOrThrow("Total"));
        cursor.close();
        return total;
    }

}
