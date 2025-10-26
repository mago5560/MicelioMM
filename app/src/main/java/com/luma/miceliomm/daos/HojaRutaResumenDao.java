package com.luma.miceliomm.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.luma.miceliomm.model.ActualizaHojaDeRutaModel;
import com.luma.miceliomm.model.HojaRutaEstadoModel;
import com.luma.miceliomm.model.HojaRutaResumenModel;

import java.util.ArrayList;

/*TODO: Tablas relacionadas (TblHojaRuta, TblEstados,TblPiloto,TblSector)*/
public class HojaRutaResumenDao {

    private final SQLiteDatabase db;

    public HojaRutaResumenDao(SQLiteDatabase db) {
        this.db = db;
    }

    public ArrayList<HojaRutaResumenModel> selectResumen(String FechaInicial, String FechaFinal){
        ArrayList<HojaRutaResumenModel> arrayList = new ArrayList<>();
        // INNER JOIN entre Paquetes y Rutas
        //      " WHERE a.fecha = ? " +
        String sql = "SELECT a.idHojaDeRuta, a.hojaDeRutaEstado, c.nombre AS hojaDeRutaNombreEstado , a.idSectorLogistico, b.nombre AS nombreSectorLogistico," +
                "   a.idPiloto, a.nombrePiloto , a.fecha " +
                " , SUM(a.bultos) as totalBultos " +
                " ,a.hojaDeRutaVale, a.hojaDeRutaOtrosGastos ,a.hojaDeRutaFechaHoraSalida ,a.hojaDeRutaKmInicial,a.hojaDeRutaGalones " +
                " ,a.hojaDeRutaKmFinal ,a.hojaDeRutaFechaHoraRegreso "+
                " ,a.hojaDeRutaLatitudInicial , a.hojaDeRutaLongitudInicial "+
                " ,a.hojaDeRutaLatitudFinal , a.hojaDeRutaLongitudFinal "+
                " FROM TblHojaRuta a " +
                " LEFT JOIN TblSector b ON a.idSectorLogistico = b.idSectorLogistico" +
                " LEFT JOIN TblEstados c ON a.hojaDeRutaEstado = c.idEstado" +
                " WHERE a.fecha BETWEEN ? AND ? "+
                " GROUP BY a.idHojaDeRuta, a.hojaDeRutaEstado, c.nombre, " +
                " a.idSectorLogistico, b.nombre , a.idPiloto, a.nombrePiloto, a.fecha" +
                " ,a.hojaDeRutaVale, a.hojaDeRutaOtrosGastos ,a.hojaDeRutaFechaHoraSalida ,a.hojaDeRutaKmInicial,a.hojaDeRutaGalones " +
                " ,a.hojaDeRutaKmFinal ,a.hojaDeRutaFechaHoraRegreso "+
                " ,a.hojaDeRutaLatitudInicial , a.hojaDeRutaLongitudInicial "+
                " ,a.hojaDeRutaLatitudFinal , a.hojaDeRutaLongitudFinal "+
                " ORDER BY a.hojaDeRutaEstado asc, a.idHojaDeRuta asc"
                ;

        Cursor cursor = db.rawQuery(sql, new String[]{FechaInicial,FechaFinal});
        //Cursor cursor = db.rawQuery(sql, null);
        //cursor.moveToFirst();
        while (cursor.moveToNext()) {
            HojaRutaResumenModel v = new HojaRutaResumenModel();
            ActualizaHojaDeRutaModel cls = new ActualizaHojaDeRutaModel();

            v.idHoraRuta = cursor.getInt(cursor.getColumnIndexOrThrow("idHojaDeRuta"));

            v.hojaDeRutaEstado = cursor.getInt(cursor.getColumnIndexOrThrow("hojaDeRutaEstado"));
            v.hojaRutaNombreEstado = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaNombreEstado"));

            v.idSectorLogistico = cursor.getInt(cursor.getColumnIndexOrThrow("idSectorLogistico"));
            v.nombreSectorLogistico = cursor.getString(cursor.getColumnIndexOrThrow("nombreSectorLogistico"));
            v.idPiloto = cursor.getInt(cursor.getColumnIndexOrThrow("idPiloto"));
            v.nombrePiloto = cursor.getString(cursor.getColumnIndexOrThrow("nombrePiloto"));
            v.fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"));
            v.totalBultos = cursor.getInt(cursor.getColumnIndexOrThrow("totalBultos"));

            cls.vale  = cursor.getInt(cursor.getColumnIndexOrThrow("hojaDeRutaVale"));
            cls.otrosGastos = cursor.getInt(cursor.getColumnIndexOrThrow("hojaDeRutaOtrosGastos"));
            cls.fechaHoraSalida = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaFechaHoraSalida"));
            cls.fechaHoraRegreso = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaFechaHoraRegreso"));
            cls.kmInicial = cursor.getInt(cursor.getColumnIndexOrThrow("hojaDeRutaKmInicial"));
            cls.kmFinal = cursor.getInt(cursor.getColumnIndexOrThrow("hojaDeRutaKmFinal"));
            cls.galones = cursor.getInt(cursor.getColumnIndexOrThrow("hojaDeRutaGalones"));
            cls.latitudInicial = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaLatitudInicial"));
            cls.longitudInicial = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaLongitudInicial"));
            cls.latitudFinal = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaLatitudFinal"));
            cls.longitudFinal = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaLongitudFinal"));

            v.actualizaHojaDeRutaModel = cls;
            arrayList.add(v);
        }

        cursor.close();
        return arrayList;


    }

    public HojaRutaResumenModel selectResumenIdHojaDeRuta(int idHojaDeRuta ){
        HojaRutaResumenModel v = new HojaRutaResumenModel();

        String sql = "SELECT a.idHojaDeRuta, a.hojaDeRutaEstado, c.nombre hojaDeRutaNombreEstado , a.idSectorLogistico, b.nombre nombreSectorLogistico," +
                "   a.idPiloto, a.nombrePiloto , a.fecha " +
                " , SUM(a.bultos) as totalBultos " +
                " ,a.hojaDeRutaVale, a.hojaDeRutaOtrosGastos ,a.hojaDeRutaFechaHoraSalida ,a.hojaDeRutaKmInicial,a.hojaDeRutaGalones " +
                " ,a.hojaDeRutaKmFinal ,a.hojaDeRutaFechaHoraRegreso"+
                " ,a.hojaDeRutaLatitudInicial , a.hojaDeRutaLongitudInicial "+
                " ,a.hojaDeRutaLatitudFinal , a.hojaDeRutaLongitudFinal "+
                " FROM TblHojaRuta a " +
                " LEFT JOIN TblSector b ON a.idSectorLogistico = b.idSectorLogistico" +
                " LEFT JOIN TblEstados c ON a.hojaDeRutaEstado = c.idEstado" +
                " WHERE a.idHojaDeRuta  = ? " +
                " GROUP BY a.idHojaDeRuta, a.hojaDeRutaEstado, c.nombre, " +
                " a.idSectorLogistico, b.nombre , a.idPiloto, a.nombrePiloto, a.fecha" +
                " ,a.hojaDeRutaVale, a.hojaDeRutaOtrosGastos ,a.hojaDeRutaFechaHoraSalida ,a.hojaDeRutaKmInicial,a.hojaDeRutaGalones " +
                " ,a.hojaDeRutaKmFinal ,a.hojaDeRutaFechaHoraRegreso " +
                " ,a.hojaDeRutaLatitudInicial , a.hojaDeRutaLongitudInicial "+
                " ,a.hojaDeRutaLatitudFinal , a.hojaDeRutaLongitudFinal "
                ;


        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(idHojaDeRuta)});
        //cursor.moveToFirst();
        while (cursor.moveToNext()) {
            v = new HojaRutaResumenModel();
            ActualizaHojaDeRutaModel cls = new ActualizaHojaDeRutaModel();
            v.idHoraRuta = cursor.getInt(cursor.getColumnIndexOrThrow("idHojaDeRuta"));
            v.hojaDeRutaEstado = cursor.getInt(cursor.getColumnIndexOrThrow("hojaDeRutaEstado"));
            v.hojaRutaNombreEstado = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaNombreEstado"));
            v.idSectorLogistico = cursor.getInt(cursor.getColumnIndexOrThrow("idSectorLogistico"));
            v.nombreSectorLogistico = cursor.getString(cursor.getColumnIndexOrThrow("nombreSectorLogistico"));
            v.idPiloto = cursor.getInt(cursor.getColumnIndexOrThrow("idPiloto"));
            v.nombrePiloto = cursor.getString(cursor.getColumnIndexOrThrow("nombrePiloto"));
            v.fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"));
            v.totalBultos = cursor.getInt(cursor.getColumnIndexOrThrow("totalBultos"));

            cls.vale  = cursor.getInt(cursor.getColumnIndexOrThrow("hojaDeRutaVale"));
            cls.otrosGastos = cursor.getInt(cursor.getColumnIndexOrThrow("hojaDeRutaOtrosGastos"));
            cls.fechaHoraSalida = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaFechaHoraSalida"));
            cls.fechaHoraRegreso = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaFechaHoraRegreso"));
            cls.kmInicial = cursor.getInt(cursor.getColumnIndexOrThrow("hojaDeRutaKmInicial"));
            cls.kmFinal = cursor.getInt(cursor.getColumnIndexOrThrow("hojaDeRutaKmFinal"));
            cls.galones = cursor.getInt(cursor.getColumnIndexOrThrow("hojaDeRutaGalones"));

            cls.latitudInicial = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaLatitudInicial"));
            cls.longitudInicial = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaLongitudInicial"));
            cls.latitudFinal = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaLatitudFinal"));
            cls.longitudFinal = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaLongitudFinal"));


            v.actualizaHojaDeRutaModel = cls;
        }

        cursor.close();
        return v;
    }


    public long updateEstado (int idHojaDeRuta, int hojaDeRutaEstado, String hojaDeRutaNombreEstado){
        ContentValues values = new ContentValues();
        values.put("hojaDeRutaEstado", hojaDeRutaEstado);
        values.put("hojaDeRutaNombreEstado", hojaDeRutaNombreEstado);
        return db.update("TblHojaRuta",values,"idHojaDeRuta=?",
                new String[]{String.valueOf(idHojaDeRuta)});
    }

    public long updateEstadoRutaPaquetes (HojaRutaEstadoModel cls){
        ContentValues values = new ContentValues();
        values.put("hojaDeRutaEstado", cls.idEstado);
        values.put("hojaDeRutaNombreEstado", cls.nombreEstado);
        values.put("idEstado",cls.idEstado);
        values.put("observacionesEntregaTraslado",cls.observaciones);
        values.put("idMotivoDeRechazoTraslado",cls.idMotivoDeRechazo);
        return db.update("TblHojaRuta",values,"idHojaDeRuta=?",
                new String[]{String.valueOf(cls.idHojaDeRuta)});
    }


    public boolean existsHojaDeRutaNoIniciado(int IdHojaDeRuta){
        String sql = "SELECT Count(1) Total " +
                " FROM TblHojaRuta a " +
                " WHERE a.idHojaDeRuta = ? " +
                " AND a.hojaDeRutaEstado in (1,2,3) "
                ;

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(IdHojaDeRuta)});
        cursor.moveToFirst();
        if (cursor.getInt(cursor.getColumnIndexOrThrow("Total")) > 0 ){
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public boolean existsHojaEnRuta(){
        String sql = "SELECT Count(1) Total " +
                " FROM TblHojaRuta a " +
                " WHERE a.hojaDeRutaEstado = 5 "
                ;

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        if (cursor.getInt(cursor.getColumnIndexOrThrow("Total")) > 0 ){
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

}
