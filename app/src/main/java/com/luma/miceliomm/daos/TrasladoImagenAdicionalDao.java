package com.luma.miceliomm.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.luma.miceliomm.model.SectorModel;
import com.luma.miceliomm.model.TrasladoImagenAdicionalModel;

import java.util.ArrayList;

/*TODO:   "CREATE TABLE TblTrasladoImagenAdicional ( "+
                    " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " idHojaDeRuta INTEGER NOT NULL, " +
                    " idTrasladoLogistica INTEGER, " +
                    " idTraslado INTEGER, " +
                    " latitud TEXT,  " +
                    " longitud TEXT,  " +
                    " imagen TEXT,  " +
                    " fecha TEXT  " +
                    " ); "
* */
public class TrasladoImagenAdicionalDao {
    private final SQLiteDatabase db;

    public TrasladoImagenAdicionalDao(SQLiteDatabase db){
        this.db = db;
    }


    public long insert(TrasladoImagenAdicionalModel cls){
        ContentValues values = new ContentValues();
        values.put("idHojaDeRuta",cls.idHojaDeRuta);
        values.put("idTrasladoLogistica",cls.idTrasladoLogistica);
        values.put("idTraslado",cls.idTraslado);
        values.put("latitud",cls.latitud);
        values.put("longitud",cls.longitud);
        values.put("imagen",cls.imagen);
        values.put("fecha",cls.fecha);

        return db.insert("TblTrasladoImagenAdicional",null,values);
    }

    public ArrayList<TrasladoImagenAdicionalModel> select(int idHojaDeRuta, int idTraslado, int idTrasladoLogistica ) {
        ArrayList<TrasladoImagenAdicionalModel> lst = new ArrayList<>();
        Cursor cursor = db.query("TblTrasladoImagenAdicional", null,
                "idHojaDeRuta = ? AND idTRaslado = ? AND idTrasladoLogistica = ?",
                new String[]{String.valueOf(idHojaDeRuta),String.valueOf(idTraslado),String.valueOf(idTrasladoLogistica)},
                null, null, null);
        //cursor.moveToFirst();
        while (cursor.moveToNext()) {
            TrasladoImagenAdicionalModel v = new TrasladoImagenAdicionalModel();
            v.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            v.idHojaDeRuta = cursor.getInt(cursor.getColumnIndexOrThrow("idHojaDeRuta"));
            v.idTrasladoLogistica = cursor.getInt(cursor.getColumnIndexOrThrow("idTrasladoLogistica"));
            v.idTraslado = cursor.getInt(cursor.getColumnIndexOrThrow("idTraslado"));
            v.latitud    = cursor.getString(cursor.getColumnIndexOrThrow("latitud"));
            v.longitud  =cursor.getString(cursor.getColumnIndexOrThrow("longitud"));
            v.imagen    = cursor.getString(cursor.getColumnIndexOrThrow("imagen"));
            v.fecha  =cursor.getString(cursor.getColumnIndexOrThrow("fecha"));

            lst.add(v);
        }
        cursor.close();
        return lst;
    }


    private static  final String TrasladoImagenAdicional =
            "CREATE TABLE TblTrasladoImagenAdicional ( "+
                    " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " idHojaDeRuta INTEGER NOT NULL, " +
                    " idTrasladoLogistica INTEGER, " +
                    " idTraslado INTEGER, " +
                    " latitud TEXT,  " +
                    " longitud TEXT,  " +
                    " imagen TEXT,  " +
                    " fecha TEXT  " +
                    " ); ";

    public void alterObject(){
        db.execSQL("DROP TABLE IF EXISTS TblTrasladoImagenAdicional");
    }


}
