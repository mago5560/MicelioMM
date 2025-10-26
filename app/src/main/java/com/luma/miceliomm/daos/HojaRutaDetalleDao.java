package com.luma.miceliomm.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.luma.miceliomm.model.ActualizaHojaDeRutaModel;
import com.luma.miceliomm.model.HojaRutaDetalleModel;
import com.luma.miceliomm.model.HojaRutaEstadoModel;
import com.luma.miceliomm.model.HojaRutaResumenModel;
import com.luma.miceliomm.model.TrasladoLogisticaModel;
import com.luma.miceliomm.model.TrasladoLogisticoRecoleccionModel;

import java.util.ArrayList;

public class HojaRutaDetalleDao {

    private final SQLiteDatabase db;

    public HojaRutaDetalleDao(SQLiteDatabase db) {
        this.db = db;
    }

    public ArrayList<HojaRutaDetalleModel> selectDetalle(String IdHojaDeRuta){
        ArrayList<HojaRutaDetalleModel> arrayList = new ArrayList<>();
        // INNER JOIN entre Paquetes y Rutas
        String sql = "SELECT a.idHojaDeRuta,a.hojaDeRutaEstado, a.idTraslado,a.idTrasladoLogistica, " +

                " a.observaciones,  a.referencia, " +
                "   a.nombreHorarioTraslado, a.horarioEntregaTraslado, a.direccionEntregaTraslado, " +
                " a.nombreDepartamentoTraslado, a.nombreMunicipioTraslado, a.zonaTraslado, a.entregarATraslado, " +
                " a.recibidoPorTraslado, a.latitudEntregaTraslado, a.longitudEntregaTraslado, a.observacionesEntregaTraslado,"+

                "   a.idEstado, d.nombre as estadoNombre," +
                "  SUM(a.bultos) as totalBultos " +
                " FROM TblHojaRuta a " +
                " LEFT JOIN TblPiloto c ON a.idPiloto = c.IdPersonal " +
                " LEFT JOIN TblEstados d ON a.idEstado = d.idEstado " +
                      " WHERE a.idHojaDeRuta = ? " +
                " GROUP BY a.idHojaDeRuta,a.hojaDeRutaEstado, a.idTraslado,a.idTrasladoLogistica, " +
                " a.observaciones,  a.referencia, " +
                "   a.nombreHorarioTraslado, a.horarioEntregaTraslado, a.direccionEntregaTraslado, " +
                " a.nombreDepartamentoTraslado, a.nombreMunicipioTraslado, a.zonaTraslado, a.entregarATraslado, " +
                " a.recibidoPorTraslado, a.latitudEntregaTraslado, a.longitudEntregaTraslado, a.observacionesEntregaTraslado,"+
                "                   a.idEstado, d.nombre "
                ;

        Cursor cursor = db.rawQuery(sql, new String[]{IdHojaDeRuta});
        //cursor.moveToFirst();
        while (cursor.moveToNext()) {
            HojaRutaDetalleModel v = new HojaRutaDetalleModel();
            v.idHoraRuta = cursor.getInt(cursor.getColumnIndexOrThrow("idHojaDeRuta"));
            v.hojaDeRutaEstado = cursor.getInt(cursor.getColumnIndexOrThrow("hojaDeRutaEstado"));

            v.idTraslado = cursor.getInt(cursor.getColumnIndexOrThrow("idTraslado"));
            v.idTrasladoLogistica = cursor.getInt(cursor.getColumnIndexOrThrow("idTrasladoLogistica"));

            v.observaciones =cursor.getString(cursor.getColumnIndexOrThrow("observaciones"));
            v.referencia =cursor.getString(cursor.getColumnIndexOrThrow("referencia"));
            v.nombreHorarioTraslado =cursor.getString(cursor.getColumnIndexOrThrow("nombreHorarioTraslado"));
            v.horarioEntregaTraslado = cursor.getInt(cursor.getColumnIndexOrThrow("horarioEntregaTraslado"));
            v.direccionEntregaTraslado =cursor.getString(cursor.getColumnIndexOrThrow("direccionEntregaTraslado"));

            v.nombreDepartamentoTraslado =cursor.getString(cursor.getColumnIndexOrThrow("nombreDepartamentoTraslado"));
            v.nombreMunicipioTraslado =cursor.getString(cursor.getColumnIndexOrThrow("nombreMunicipioTraslado"));
            v.zonaTraslado =cursor.getString(cursor.getColumnIndexOrThrow("zonaTraslado"));
            v.entregarATraslado =cursor.getString(cursor.getColumnIndexOrThrow("entregarATraslado"));

            v.recibidoPorTraslado =cursor.getString(cursor.getColumnIndexOrThrow("recibidoPorTraslado"));
            v.latitudEntregaTraslado =cursor.getString(cursor.getColumnIndexOrThrow("latitudEntregaTraslado"));
            v.longitudEntregaTraslado =cursor.getString(cursor.getColumnIndexOrThrow("longitudEntregaTraslado"));
            v.observacionesEntregaTraslado =cursor.getString(cursor.getColumnIndexOrThrow("observacionesEntregaTraslado"));

            v.idEstado = cursor.getInt(cursor.getColumnIndexOrThrow("idEstado"));
            v.nombreEstado = cursor.getString(cursor.getColumnIndexOrThrow("estadoNombre"));
            v.totalBultos = cursor.getInt(cursor.getColumnIndexOrThrow("totalBultos"));
            arrayList.add(v);
        }

        cursor.close();
        return arrayList;

    }


    public HojaRutaDetalleModel selectDetalleId(int IdHojaDeRuta, int IdTraslado, int IdTrasladoLogistica){
        HojaRutaDetalleModel v = new HojaRutaDetalleModel();
        String sql = "SELECT a.idHojaDeRuta, a.idTraslado,a.idTrasladoLogistica, " +

                " a.observaciones,  a.referencia, " +
                "   a.nombreHorarioTraslado, a.horarioEntregaTraslado, a.direccionEntregaTraslado, " +
                " a.nombreDepartamentoTraslado, a.nombreMunicipioTraslado, a.zonaTraslado, a.entregarATraslado, " +
                " a.recibidoPorTraslado, a.latitudEntregaTraslado, a.longitudEntregaTraslado, a.observacionesEntregaTraslado,"+

                "   a.idEstado, d.nombre as estadoNombre," +
                "  a.bultos" +
                " FROM TblHojaRuta a " +
                " LEFT JOIN TblPiloto c ON a.idPiloto = c.IdPersonal " +
                " LEFT JOIN TblEstados d ON a.idEstado = d.idEstado " +
                " WHERE a.idHojaDeRuta = ? " +
                " AND a.idTraslado = ? " +
                " AND a.idTrasladoLogistica = ? "
                ;

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(IdHojaDeRuta),String.valueOf(IdTraslado),String.valueOf(IdTrasladoLogistica)});
        //cursor.moveToFirst();
        while (cursor.moveToNext()) {
             v = new HojaRutaDetalleModel();
            v.idHoraRuta = cursor.getInt(cursor.getColumnIndexOrThrow("idHojaDeRuta"));
            v.idTraslado = cursor.getInt(cursor.getColumnIndexOrThrow("idTraslado"));
            v.idTrasladoLogistica = cursor.getInt(cursor.getColumnIndexOrThrow("idTrasladoLogistica"));

            v.observaciones =cursor.getString(cursor.getColumnIndexOrThrow("observaciones"));
            v.referencia =cursor.getString(cursor.getColumnIndexOrThrow("referencia"));
            v.nombreHorarioTraslado =cursor.getString(cursor.getColumnIndexOrThrow("nombreHorarioTraslado"));
            v.horarioEntregaTraslado = cursor.getInt(cursor.getColumnIndexOrThrow("horarioEntregaTraslado"));
            v.direccionEntregaTraslado =cursor.getString(cursor.getColumnIndexOrThrow("direccionEntregaTraslado"));

            v.nombreDepartamentoTraslado =cursor.getString(cursor.getColumnIndexOrThrow("nombreDepartamentoTraslado"));
            v.nombreMunicipioTraslado =cursor.getString(cursor.getColumnIndexOrThrow("nombreMunicipioTraslado"));
            v.zonaTraslado =cursor.getString(cursor.getColumnIndexOrThrow("zonaTraslado"));
            v.entregarATraslado =cursor.getString(cursor.getColumnIndexOrThrow("entregarATraslado"));

            v.recibidoPorTraslado =cursor.getString(cursor.getColumnIndexOrThrow("recibidoPorTraslado"));
            v.latitudEntregaTraslado =cursor.getString(cursor.getColumnIndexOrThrow("latitudEntregaTraslado"));
            v.longitudEntregaTraslado =cursor.getString(cursor.getColumnIndexOrThrow("longitudEntregaTraslado"));
            v.observacionesEntregaTraslado =cursor.getString(cursor.getColumnIndexOrThrow("observacionesEntregaTraslado"));

            v.idEstado = cursor.getInt(cursor.getColumnIndexOrThrow("idEstado"));
            v.nombreEstado = cursor.getString(cursor.getColumnIndexOrThrow("estadoNombre"));
            v.totalBultos = cursor.getInt(cursor.getColumnIndexOrThrow("bultos"));
        }

        cursor.close();
        return v;

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


    public boolean existsTrasladoLogisticoNoRecolectado(int IdHojaDeRuta){
        String sql = "SELECT Count(1) Total " +
                " FROM TblHojaRuta a " +
                " WHERE a.idHojaDeRuta = ? " +
                " AND a.idEstado in (1,2,3) "
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


    public boolean existsTrasladoLogisticoEnRuta(int IdHojaDeRuta){
        String sql = "SELECT Count(1) Total " +
                " FROM TblHojaRuta a " +
                " WHERE a.idHojaDeRuta = ? " +
                " AND a.idEstado = 5 "
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

    public long updateTrasladoLogistico(TrasladoLogisticaModel cls){
        ContentValues values = new ContentValues();
        values.put("recibidoPorTraslado", cls.recibidoPorTraslado);
        //values.put("latitudEntregaTraslado", cls.latitudEntregaTraslado);
        //values.put("longitudEntregaTraslado", cls.longitudEntregaTraslado);
        values.put("observacionesEntregaTraslado", cls.observacionesEntregaTraslado);
        values.put("idMotivoDeRechazoTraslado", cls.idMotivoDeRechazoTraslado);
        values.put("imagenDeRecibidoTraslado", cls.imagenDeRecibidoTraslado);
        values.put("imagenDeEntregadoTraslado", cls.imagenDeEntregadoTraslado);
        values.put("fechaDeEntregaTraslado", cls.fechaDeEntregaTraslado);
        return db.update("TblHojaRuta",values,
                "idHojaDeRuta= ? AND idTraslado = ? AND idTrasladoLogistica=?",
                new String[]{String.valueOf(cls.idHojaDeRuta),String.valueOf(cls.idTraslado),String.valueOf(cls.idTrasladoLogistica)});
    }

    public long updateRecoleccionTrasladoLogistico(TrasladoLogisticoRecoleccionModel cls){
        ContentValues values = new ContentValues();
        values.put("fechaRecoleccion",cls.fechaHoraRecoleccion);
        values.put("observacionRecoleccion", cls.observaciones);
        values.put("idEstado",cls.idEstado);
        return db.update("TblHojaRuta",values,
                "idHojaDeRuta= ? AND idTraslado = ? AND idTrasladoLogistica=?",
                new String[]{String.valueOf(cls.idHojaDeRuta),String.valueOf(cls.idTraslado),String.valueOf(cls.idTrasladoLogistico)});
    }


    public long updateEstadoPaquete (HojaRutaEstadoModel cls){
        ContentValues values = new ContentValues();
        values.put("idEstado",cls.idEstado);
        return db.update("TblHojaRuta",values,
                "idHojaDeRuta= ? AND idTraslado = ? AND idTrasladoLogistica=?",
                new String[]{String.valueOf(cls.idHojaDeRuta),String.valueOf(cls.idTraslado),String.valueOf(cls.idTrasladoLogistico)});
    }


    public long updateHojaRutaIniciada (ActualizaHojaDeRutaModel cls){
        ContentValues values = new ContentValues();
        values.put("hojaDeRutaEstado", cls.idEstado);
        values.put("idEstado",cls.idEstado);
        values.put("hojaDeRutaVale",cls.vale);
        values.put("hojaDeRutaOtrosGastos",cls.otrosGastos);
        values.put("hojaDeRutaFechaHoraSalida",cls.fechaHoraSalida);
        values.put("hojaDeRutaKmInicial",cls.kmInicial);
        values.put("hojaDeRutaGalones",cls.galones);
        values.put("hojaDeRutaLatitudInicial",cls.latitudInicial);
        values.put("hojaDeRutaLongitudInicial",cls.longitudInicial);

        return db.update("TblHojaRuta",values,"idHojaDeRuta=?",
                new String[]{String.valueOf(cls.idHojaDeRuta)});
    }

    public long updateHojaRutaFinal (ActualizaHojaDeRutaModel cls){
        ContentValues values = new ContentValues();
        values.put("hojaDeRutaEstado", cls.idEstado);
        values.put("idEstado",cls.idEstado);
        values.put("hojaDeRutaVale",cls.vale);
        values.put("hojaDeRutaOtrosGastos",cls.otrosGastos);
        values.put("hojaDeRutaFechaHoraRegreso",cls.fechaHoraRegreso);
        values.put("hojaDeRutaKmFinal",cls.kmFinal);
        values.put("hojaDeRutaGalones",cls.galones);
        values.put("hojaDeRutaLatitudFinal",cls.latitudFinal);
        values.put("hojaDeRutaLongitudFinal",cls.longitudFinal);

        return db.update("TblHojaRuta",values,"idHojaDeRuta=?",
                new String[]{String.valueOf(cls.idHojaDeRuta)});
    }



}
