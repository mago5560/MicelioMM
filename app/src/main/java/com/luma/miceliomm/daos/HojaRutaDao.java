package com.luma.miceliomm.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.luma.miceliomm.model.HojaRutaModel;

import java.util.ArrayList;

/**
 * TODO:  "CREATE TABLE TblHojaRuta ("+
         * " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
         * " idHojaDeRuta INTEGER NOT NULL, " +
         * " hojaDeRutaEstado INTEGER, " +
         * " hojaDeRutaNombreEstado TEXT,  " +
         * " fecha TEXT,  " +
         * " idSectorLogistico INTEGER, " +
         * " nombreSectorLogistico TEXT,  " +
         * " idVehiculo INTEGER, " +
         * " nombreVehiculo TEXT,  " +
         * " placaVehiculo TEXT,  " +
         * " idPiloto INTEGER, " +
         * " nombrePiloto TEXT,  " +
         * " idAuxiliar INTEGER,  " +
         * " nombreAuxiliar TEXT,  " +
         * " hojaDeRutaVale TEXT,  " +
         * " hojaDeRutaOtrosGastos TEXT,  " +
         * " hojaDeRutaFechaHoraSalida TEXT,  " +
         * " hojaDeRutaFechaHoraRegreso TEXT,  " +
         * " hojaDeRutaKmInicial TEXT,  " +
         * " hojaDeRutaKmFinal TEXT,  " +
         * " hojaDeRutaValeCombustible TEXT,  " +
         * " hojaDeRutaGalones TEXT,  " +
         * " hojaDeRutaLatitudInicial TEXT,  " +
         * " hojaDeRutaLongitudInicial TEXT,  " +
         * " hojaDeRutaLatitudFinal TEXT,  " +
         * " hojaDeRutaLongitudFinal TEXT,  " +
         * " idTraslado INTEGER, " +
         * " idSociedad TEXT,  " +
         * " idTipoMovimiento INTEGER, " +
         * " identificador TEXT,  " +
         * " idEstado INTEGER, " +
         * " idUbicacionOrigen INTEGER, " +
         * " nombreUbicacionOrigen TEXT,  " +
         * " direccionUbicacionOrigen TEXT,  " +
         * " latitudUbicacionOrigen TEXT,  " +
         * " longitudUbicacionOrigen TEXT,  " +
         * " referenciaInternaUbicacionOrigen TEXT,  " +
         * " codigoClienteUbicacionOrigen TEXT,  " +
         * " departamentoUbicacionOrigen TEXT,  " +
         * " nombreDepartamentoUbicacionOrigen TEXT,  " +
         * " nombreMunicipioUbicacionOrigen TEXT,  " +
         * " telefonoUbicacionOrigen TEXT,  " +
         * " correoElectronicoUbicacionOrigen TEXT,  " +
         * " idSectorLogisticoUbicacionOrigen TEXT,  " +
         * " nombreSectorLogisticoUbicacionOrigen TEXT,  " +
         * " idUbicacionDestino INTEGER, " +
         * " nombreUbicacionDestino TEXT,  " +
         * " direccionUbicacionDestino TEXT,  " +
         * " latitudUbicacionDestino TEXT,  " +
         * " longitudUbicacionDestino TEXT,  " +
         * " referenciaInternaUbicacionDestino TEXT,  " +
         * " codigoClienteUbicacionDestino TEXT,  " +
         * " departamentoUbicacionDestino TEXT,  " +
         * " nombreDepartamentoUbicacionDestino TEXT,  " +
         * " nombreMunicipioUbicacionDestino TEXT,  " +
         * " telefonoUbicacionDestino TEXT,  " +
         * " correoElectronicoUbicacionDestino TEXT,  " +
         * " idSectorLogisticoUbicacionDestino TEXT,  " +
         * " nombreSectorLogisticoUbicacionDestino TEXT,  " +
         * " fechaRecoleccion TEXT,  " +
         * " fechaEntrega TEXT,  " +
         * " valorDocumento TEXT,  " +
         * " referencia TEXT,  " +
         * " observaciones TEXT,  " +
         * " idTrasladoLogistica INTEGER, " +
         * " bultos INTEGER, " +
         * " idSectorLogisticoTraslado INTEGER, " +
         * " nombreSectorLogisticoTraslado TEXT,  " +
         * " horarioRecoleccionTraslado TEXT,  " +
         * " idHorarioTraslado INTEGER, " +
         * " nombreHorarioTraslado TEXT,  " +
         * " horarioEntregaTraslado TEXT,  " +
         * " direccionEntregaTraslado TEXT,  " +
         * " departamentoTraslado TEXT,  " +
         * " nombreDepartamentoTraslado TEXT,  " +
         * " municipioTraslado TEXT,  " +
         * " nombreMunicipioTraslado TEXT,  " +
         * " zonaTraslado TEXT,  " +
         * " entregarATraslado TEXT,  " +
         * " recibidoPorTraslado TEXT,  " +
         * " latitudEntregaTraslado TEXT,  " +
         * " longitudEntregaTraslado TEXT,  " +
         * " observacionesEntregaTraslado TEXT,  " +
         * " idMotivoDeRechazoTraslado INTEGER,  " +
         * " imagenDeRecibidoTraslado TEXT,  " +
         * " imagenDeEntregadoTraslado TEXT,  " +
         * " fechaDeEntregaTraslado TEXT  " +
         * " );"
 **/
public class HojaRutaDao {

    private final SQLiteDatabase db;

    public HojaRutaDao(SQLiteDatabase db) {
        this.db = db;
    }

    public int delete(int IdHojaDeRuta, int IdTraslado, int IdTrasladoLogistico) {
        return db.delete("TblHojaRuta", "idHojaDeRuta = ? AND idTraslado = ? AND idTrasladoLogistica =? ", new String[]{String.valueOf(IdHojaDeRuta),String.valueOf(IdTraslado),String.valueOf(IdTrasladoLogistico)});
    }


    public long insert(HojaRutaModel cls) {
        ContentValues values = new ContentValues();
        values.put("idHojaDeRuta", cls.idHojaDeRuta);
        values.put("hojaDeRutaEstado", cls.hojaDeRutaEstado);
        values.put("hojaDeRutaNombreEstado", cls.hojaDeRutaNombreEstado);
        values.put("fecha", cls.fecha);
        values.put("idSectorLogistico", cls.idSectorLogistico);
        values.put("nombreSectorLogistico", cls.nombreSectorLogistico);
        values.put("idVehiculo", cls.idVehiculo);
        values.put("nombreVehiculo", cls.nombreVehiculo);
        values.put("placaVehiculo", cls.placaVehiculo);
        values.put("idPiloto", cls.idPiloto);
        values.put("nombrePiloto", cls.nombrePiloto);
        values.put("idAuxiliar", cls.idAuxiliar);
        values.put("nombreAuxiliar", cls.nombreAuxiliar);
        values.put("hojaDeRutaVale", cls.hojaDeRutaVale);
        values.put("hojaDeRutaOtrosGastos", cls.hojaDeRutaOtrosGastos);
        values.put("hojaDeRutaFechaHoraSalida", cls.hojaDeRutaFechaHoraSalida);
        values.put("hojaDeRutaFechaHoraRegreso", cls.hojaDeRutaFechaHoraRegreso);
        values.put("hojaDeRutaKmInicial", cls.hojaDeRutaKmInicial);
        values.put("hojaDeRutaKmFinal", cls.hojaDeRutaKmFinal);
        values.put("hojaDeRutaValeCombustible", cls.hojaDeRutaValeCombustible);
        values.put("hojaDeRutaGalones", cls.hojaDeRutaGalones);
        values.put("hojaDeRutaLatitudInicial", cls.hojaDeRutaLatitudInicial);
        values.put("hojaDeRutaLongitudInicial", cls.hojaDeRutaLongitudInicial);
        values.put("hojaDeRutaLatitudFinal", cls.hojaDeRutaLatitudFinal);
        values.put("hojaDeRutaLongitudFinal", cls.hojaDeRutaLongitudFinal);
        values.put("idTraslado", cls.idTraslado);
        values.put("idSociedad", cls.idSociedad);
        values.put("idTipoMovimiento", cls.idTipoMovimiento);
        values.put("identificador", cls.identificador);
        values.put("idEstado", cls.idEstado);
        values.put("idUbicacionOrigen", cls.idUbicacionOrigen);
        values.put("nombreUbicacionOrigen", cls.nombreUbicacionOrigen);
        values.put("direccionUbicacionOrigen", cls.direccionUbicacionOrigen);
        values.put("latitudUbicacionOrigen", cls.latitudUbicacionOrigen);
        values.put("longitudUbicacionOrigen", cls.longitudUbicacionOrigen);
        values.put("referenciaInternaUbicacionOrigen", cls.referenciaInternaUbicacionOrigen);
        values.put("codigoClienteUbicacionOrigen", cls.codigoClienteUbicacionOrigen);
        values.put("departamentoUbicacionOrigen", cls.departamentoUbicacionOrigen);
        values.put("nombreDepartamentoUbicacionOrigen", cls.nombreDepartamentoUbicacionOrigen);
        values.put("nombreMunicipioUbicacionOrigen", cls.nombreMunicipioUbicacionOrigen);
        values.put("telefonoUbicacionOrigen", cls.telefonoUbicacionOrigen);
        values.put("correoElectronicoUbicacionOrigen", cls.correoElectronicoUbicacionOrigen);
        values.put("idSectorLogisticoUbicacionOrigen", cls.idSectorLogisticoUbicacionOrigen);
        values.put("nombreSectorLogisticoUbicacionOrigen", cls.nombreSectorLogisticoUbicacionOrigen);
        values.put("idUbicacionDestino", cls.idUbicacionDestino);
        values.put("nombreUbicacionDestino", cls.nombreUbicacionDestino);
        values.put("direccionUbicacionDestino", cls.direccionUbicacionDestino);
        values.put("latitudUbicacionDestino", cls.latitudUbicacionDestino);
        values.put("longitudUbicacionDestino", cls.longitudUbicacionDestino);
        values.put("referenciaInternaUbicacionDestino", cls.referenciaInternaUbicacionDestino);
        values.put("codigoClienteUbicacionDestino", cls.codigoClienteUbicacionDestino);
        values.put("departamentoUbicacionDestino", cls.departamentoUbicacionDestino);
        values.put("nombreDepartamentoUbicacionDestino", cls.nombreDepartamentoUbicacionDestino);
        values.put("nombreMunicipioUbicacionDestino", cls.nombreMunicipioUbicacionDestino);
        values.put("telefonoUbicacionDestino", cls.telefonoUbicacionDestino);
        values.put("correoElectronicoUbicacionDestino", cls.correoElectronicoUbicacionDestino);
        values.put("idSectorLogisticoUbicacionDestino", cls.idSectorLogisticoUbicacionDestino);
        values.put("nombreSectorLogisticoUbicacionDestino", cls.nombreSectorLogisticoUbicacionDestino);
        values.put("fechaRecoleccion", cls.fechaRecoleccion);
        values.put("fechaEntrega", cls.fechaEntrega);
        values.put("valorDocumento", cls.valorDocumento);
        values.put("referencia", cls.referencia);
        values.put("observaciones", cls.observaciones);
        values.put("idTrasladoLogistica", cls.idTrasladoLogistica);
        values.put("bultos", cls.bultos);
        values.put("idSectorLogisticoTraslado", cls.idSectorLogisticoTraslado);
        values.put("nombreSectorLogisticoTraslado", cls.nombreSectorLogisticoTraslado);
        values.put("horarioRecoleccionTraslado", cls.horarioRecoleccionTraslado);
        values.put("idHorarioTraslado", cls.idHorarioTraslado);
        values.put("nombreHorarioTraslado", cls.nombreHorarioTraslado);
        values.put("horarioEntregaTraslado", cls.horarioEntregaTraslado);
        values.put("direccionEntregaTraslado", cls.direccionEntregaTraslado);
        values.put("departamentoTraslado", cls.departamentoTraslado);
        values.put("nombreDepartamentoTraslado", cls.nombreDepartamentoTraslado);
        values.put("municipioTraslado", cls.municipioTraslado);
        values.put("nombreMunicipioTraslado", cls.nombreMunicipioTraslado);
        values.put("zonaTraslado", cls.zonaTraslado);
        values.put("entregarATraslado", cls.entregarATraslado);
        values.put("recibidoPorTraslado", cls.recibidoPorTraslado);
        values.put("latitudEntregaTraslado", cls.latitudEntregaTraslado);
        values.put("longitudEntregaTraslado", cls.longitudEntregaTraslado);
        values.put("observacionesEntregaTraslado", cls.observacionesEntregaTraslado);
        values.put("idMotivoDeRechazoTraslado", cls.idMotivoDeRechazoTraslado);
        values.put("imagenDeRecibidoTraslado", cls.imagenDeRecibidoTraslado);
        values.put("imagenDeEntregadoTraslado", cls.imagenDeEntregadoTraslado);
        values.put("fechaDeEntregaTraslado", cls.fechaDeEntregaTraslado);

        return db.insert("TblHojaRuta", null, values);
    }

    public ArrayList<HojaRutaModel> select() {
        ArrayList<HojaRutaModel> lst = new ArrayList<>();
        Cursor cursor = db.query("TblHojaRuta", null, null, null, null, null, null);
        //cursor.moveToFirst();
        while (cursor.moveToNext()) {
            HojaRutaModel v = new HojaRutaModel();
            v.Id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            v.idHojaDeRuta = cursor.getInt(cursor.getColumnIndexOrThrow("idHojaDeRuta"));
            v.hojaDeRutaEstado = cursor.getInt(cursor.getColumnIndexOrThrow("hojaDeRutaEstado"));
            v.hojaDeRutaNombreEstado = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaNombreEstado"));
            v.fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"));
            v.idSectorLogistico = cursor.getInt(cursor.getColumnIndexOrThrow("idSectorLogistico"));
            v.nombreSectorLogistico = cursor.getString(cursor.getColumnIndexOrThrow("nombreSectorLogistico"));
            v.idVehiculo = cursor.getInt(cursor.getColumnIndexOrThrow("idVehiculo"));
            v.nombreVehiculo = cursor.getString(cursor.getColumnIndexOrThrow("nombreVehiculo"));
            v.placaVehiculo = cursor.getString(cursor.getColumnIndexOrThrow("placaVehiculo"));
            v.idPiloto = cursor.getInt(cursor.getColumnIndexOrThrow("idPiloto"));
            v.nombrePiloto = cursor.getString(cursor.getColumnIndexOrThrow("nombrePiloto"));
            v.idAuxiliar = cursor.getInt(cursor.getColumnIndexOrThrow("idAuxiliar"));
            v.nombreAuxiliar = cursor.getString(cursor.getColumnIndexOrThrow("nombreAuxiliar"));
            v.hojaDeRutaVale = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaVale"));
            v.hojaDeRutaOtrosGastos = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaOtrosGastos"));
            v.hojaDeRutaFechaHoraSalida = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaFechaHoraSalida"));
            v.hojaDeRutaFechaHoraRegreso = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaFechaHoraRegreso"));
            v.hojaDeRutaKmInicial = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaKmInicial"));
            v.hojaDeRutaKmFinal = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaKmFinal"));
            v.hojaDeRutaValeCombustible = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaValeCombustible"));
            v.hojaDeRutaGalones = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaGalones"));
            v.hojaDeRutaLatitudInicial = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaLatitudInicial"));
            v.hojaDeRutaLongitudInicial = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaLongitudInicial"));
            v.hojaDeRutaLatitudFinal = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaLatitudFinal"));
            v.hojaDeRutaLongitudFinal = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaLongitudFinal"));
            v.idTraslado = cursor.getInt(cursor.getColumnIndexOrThrow("idTraslado"));
            v.idSociedad = cursor.getString(cursor.getColumnIndexOrThrow("idSociedad"));
            v.idTipoMovimiento = cursor.getInt(cursor.getColumnIndexOrThrow("idTipoMovimiento"));
            v.identificador = cursor.getString(cursor.getColumnIndexOrThrow("identificador"));
            v.idEstado = cursor.getInt(cursor.getColumnIndexOrThrow("idEstado"));
            v.idUbicacionOrigen = cursor.getInt(cursor.getColumnIndexOrThrow("idUbicacionOrigen"));
            v.nombreUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("nombreUbicacionOrigen"));
            v.direccionUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("direccionUbicacionOrigen"));
            v.latitudUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("latitudUbicacionOrigen"));
            v.longitudUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("longitudUbicacionOrigen"));
            v.referenciaInternaUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("referenciaInternaUbicacionOrigen"));
            v.codigoClienteUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("codigoClienteUbicacionOrigen"));
            v.departamentoUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("departamentoUbicacionOrigen"));
            v.nombreDepartamentoUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("nombreDepartamentoUbicacionOrigen"));
            v.nombreMunicipioUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("nombreMunicipioUbicacionOrigen"));
            v.telefonoUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("telefonoUbicacionOrigen"));
            v.correoElectronicoUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("correoElectronicoUbicacionOrigen"));
            v.idSectorLogisticoUbicacionOrigen = cursor.getInt(cursor.getColumnIndexOrThrow("idSectorLogisticoUbicacionOrigen"));
            v.nombreSectorLogisticoUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("nombreSectorLogisticoUbicacionOrigen"));
            v.idUbicacionDestino = cursor.getInt(cursor.getColumnIndexOrThrow("idUbicacionDestino"));
            v.nombreUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("nombreUbicacionDestino"));
            v.direccionUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("direccionUbicacionDestino"));
            v.latitudUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("latitudUbicacionDestino"));
            v.longitudUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("longitudUbicacionDestino"));
            v.referenciaInternaUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("referenciaInternaUbicacionDestino"));
            v.codigoClienteUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("codigoClienteUbicacionDestino"));
            v.departamentoUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("departamentoUbicacionDestino"));
            v.nombreDepartamentoUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("nombreDepartamentoUbicacionDestino"));
            v.nombreMunicipioUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("nombreMunicipioUbicacionDestino"));
            v.telefonoUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("telefonoUbicacionDestino"));
            v.correoElectronicoUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("correoElectronicoUbicacionDestino"));
            v.idSectorLogisticoUbicacionDestino = cursor.getInt(cursor.getColumnIndexOrThrow("idSectorLogisticoUbicacionDestino"));
            v.nombreSectorLogisticoUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("nombreSectorLogisticoUbicacionDestino"));
            v.fechaRecoleccion = cursor.getString(cursor.getColumnIndexOrThrow("fechaRecoleccion"));
            v.fechaEntrega = cursor.getString(cursor.getColumnIndexOrThrow("fechaEntrega"));
            v.valorDocumento = cursor.getString(cursor.getColumnIndexOrThrow("valorDocumento"));
            v.referencia = cursor.getString(cursor.getColumnIndexOrThrow("referencia"));
            v.observaciones = cursor.getString(cursor.getColumnIndexOrThrow("observaciones"));
            v.idTrasladoLogistica = cursor.getInt(cursor.getColumnIndexOrThrow("idTrasladoLogistica"));
            v.bultos = cursor.getInt(cursor.getColumnIndexOrThrow("bultos"));
            v.idSectorLogisticoTraslado = cursor.getInt(cursor.getColumnIndexOrThrow("idSectorLogisticoTraslado"));
            v.nombreSectorLogisticoTraslado = cursor.getString(cursor.getColumnIndexOrThrow("nombreSectorLogisticoTraslado"));
            v.horarioRecoleccionTraslado = cursor.getString(cursor.getColumnIndexOrThrow("horarioRecoleccionTraslado"));
            v.idHorarioTraslado = cursor.getInt(cursor.getColumnIndexOrThrow("idHorarioTraslado"));
            v.nombreHorarioTraslado = cursor.getString(cursor.getColumnIndexOrThrow("nombreHorarioTraslado"));
            v.horarioEntregaTraslado = cursor.getInt(cursor.getColumnIndexOrThrow("horarioEntregaTraslado"));
            v.direccionEntregaTraslado = cursor.getString(cursor.getColumnIndexOrThrow("direccionEntregaTraslado"));
            v.departamentoTraslado = cursor.getString(cursor.getColumnIndexOrThrow("departamentoTraslado"));
            v.nombreDepartamentoTraslado = cursor.getString(cursor.getColumnIndexOrThrow("nombreDepartamentoTraslado"));
            v.municipioTraslado = cursor.getString(cursor.getColumnIndexOrThrow("municipioTraslado"));
            v.nombreMunicipioTraslado = cursor.getString(cursor.getColumnIndexOrThrow("nombreMunicipioTraslado"));
            v.zonaTraslado = cursor.getString(cursor.getColumnIndexOrThrow("zonaTraslado"));
            v.entregarATraslado = cursor.getString(cursor.getColumnIndexOrThrow("entregarATraslado"));
            v.recibidoPorTraslado = cursor.getString(cursor.getColumnIndexOrThrow("recibidoPorTraslado"));
            v.latitudEntregaTraslado = cursor.getString(cursor.getColumnIndexOrThrow("latitudEntregaTraslado"));
            v.longitudEntregaTraslado = cursor.getString(cursor.getColumnIndexOrThrow("longitudEntregaTraslado"));
            v.observacionesEntregaTraslado = cursor.getString(cursor.getColumnIndexOrThrow("observacionesEntregaTraslado"));
            v.idMotivoDeRechazoTraslado = cursor.getInt(cursor.getColumnIndexOrThrow("idMotivoDeRechazoTraslado"));
            v.imagenDeRecibidoTraslado = cursor.getString(cursor.getColumnIndexOrThrow("imagenDeRecibidoTraslado"));
            v.imagenDeEntregadoTraslado = cursor.getString(cursor.getColumnIndexOrThrow("imagenDeEntregadoTraslado"));
            v.fechaDeEntregaTraslado = cursor.getString(cursor.getColumnIndexOrThrow("fechaDeEntregaTraslado"));

            lst.add(v);
        }
        cursor.close();
        return lst;
    }




    public ArrayList<HojaRutaModel> select(int IdHojaDeRuta) {
        ArrayList<HojaRutaModel> lst = new ArrayList<>();
        Cursor cursor = db.query("TblHojaRuta", null, "idHojaDeRuta = ?",new String[]{String.valueOf(IdHojaDeRuta)} , null, null, null);
        //cursor.moveToFirst();
        while (cursor.moveToNext()) {
            HojaRutaModel v = new HojaRutaModel();
            v.Id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            v.idHojaDeRuta = cursor.getInt(cursor.getColumnIndexOrThrow("idHojaDeRuta"));
            v.hojaDeRutaEstado = cursor.getInt(cursor.getColumnIndexOrThrow("hojaDeRutaEstado"));
            v.hojaDeRutaNombreEstado = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaNombreEstado"));
            v.fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"));
            v.idSectorLogistico = cursor.getInt(cursor.getColumnIndexOrThrow("idSectorLogistico"));
            v.nombreSectorLogistico = cursor.getString(cursor.getColumnIndexOrThrow("nombreSectorLogistico"));
            v.idVehiculo = cursor.getInt(cursor.getColumnIndexOrThrow("idVehiculo"));
            v.nombreVehiculo = cursor.getString(cursor.getColumnIndexOrThrow("nombreVehiculo"));
            v.placaVehiculo = cursor.getString(cursor.getColumnIndexOrThrow("placaVehiculo"));
            v.idPiloto = cursor.getInt(cursor.getColumnIndexOrThrow("idPiloto"));
            v.nombrePiloto = cursor.getString(cursor.getColumnIndexOrThrow("nombrePiloto"));
            v.idAuxiliar = cursor.getInt(cursor.getColumnIndexOrThrow("idAuxiliar"));
            v.nombreAuxiliar = cursor.getString(cursor.getColumnIndexOrThrow("nombreAuxiliar"));
            v.hojaDeRutaVale = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaVale"));
            v.hojaDeRutaOtrosGastos = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaOtrosGastos"));
            v.hojaDeRutaFechaHoraSalida = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaFechaHoraSalida"));
            v.hojaDeRutaFechaHoraRegreso = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaFechaHoraRegreso"));
            v.hojaDeRutaKmInicial = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaKmInicial"));
            v.hojaDeRutaKmFinal = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaKmFinal"));
            v.hojaDeRutaValeCombustible = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaValeCombustible"));
            v.hojaDeRutaGalones = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaGalones"));
            v.hojaDeRutaLatitudInicial = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaLatitudInicial"));
            v.hojaDeRutaLongitudInicial = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaLongitudInicial"));
            v.hojaDeRutaLatitudFinal = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaLatitudFinal"));
            v.hojaDeRutaLongitudFinal = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaLongitudFinal"));
            v.idTraslado = cursor.getInt(cursor.getColumnIndexOrThrow("idTraslado"));
            v.idSociedad = cursor.getString(cursor.getColumnIndexOrThrow("idSociedad"));
            v.idTipoMovimiento = cursor.getInt(cursor.getColumnIndexOrThrow("idTipoMovimiento"));
            v.identificador = cursor.getString(cursor.getColumnIndexOrThrow("identificador"));
            v.idEstado = cursor.getInt(cursor.getColumnIndexOrThrow("idEstado"));
            v.idUbicacionOrigen = cursor.getInt(cursor.getColumnIndexOrThrow("idUbicacionOrigen"));
            v.nombreUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("nombreUbicacionOrigen"));
            v.direccionUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("direccionUbicacionOrigen"));
            v.latitudUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("latitudUbicacionOrigen"));
            v.longitudUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("longitudUbicacionOrigen"));
            v.referenciaInternaUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("referenciaInternaUbicacionOrigen"));
            v.codigoClienteUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("codigoClienteUbicacionOrigen"));
            v.departamentoUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("departamentoUbicacionOrigen"));
            v.nombreDepartamentoUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("nombreDepartamentoUbicacionOrigen"));
            v.nombreMunicipioUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("nombreMunicipioUbicacionOrigen"));
            v.telefonoUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("telefonoUbicacionOrigen"));
            v.correoElectronicoUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("correoElectronicoUbicacionOrigen"));
            v.idSectorLogisticoUbicacionOrigen = cursor.getInt(cursor.getColumnIndexOrThrow("idSectorLogisticoUbicacionOrigen"));
            v.nombreSectorLogisticoUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("nombreSectorLogisticoUbicacionOrigen"));
            v.idUbicacionDestino = cursor.getInt(cursor.getColumnIndexOrThrow("idUbicacionDestino"));
            v.nombreUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("nombreUbicacionDestino"));
            v.direccionUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("direccionUbicacionDestino"));
            v.latitudUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("latitudUbicacionDestino"));
            v.longitudUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("longitudUbicacionDestino"));
            v.referenciaInternaUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("referenciaInternaUbicacionDestino"));
            v.codigoClienteUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("codigoClienteUbicacionDestino"));
            v.departamentoUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("departamentoUbicacionDestino"));
            v.nombreDepartamentoUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("nombreDepartamentoUbicacionDestino"));
            v.nombreMunicipioUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("nombreMunicipioUbicacionDestino"));
            v.telefonoUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("telefonoUbicacionDestino"));
            v.correoElectronicoUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("correoElectronicoUbicacionDestino"));
            v.idSectorLogisticoUbicacionDestino = cursor.getInt(cursor.getColumnIndexOrThrow("idSectorLogisticoUbicacionDestino"));
            v.nombreSectorLogisticoUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("nombreSectorLogisticoUbicacionDestino"));
            v.fechaRecoleccion = cursor.getString(cursor.getColumnIndexOrThrow("fechaRecoleccion"));
            v.fechaEntrega = cursor.getString(cursor.getColumnIndexOrThrow("fechaEntrega"));
            v.valorDocumento = cursor.getString(cursor.getColumnIndexOrThrow("valorDocumento"));
            v.referencia = cursor.getString(cursor.getColumnIndexOrThrow("referencia"));
            v.observaciones = cursor.getString(cursor.getColumnIndexOrThrow("observaciones"));
            v.idTrasladoLogistica = cursor.getInt(cursor.getColumnIndexOrThrow("idTrasladoLogistica"));
            v.bultos = cursor.getInt(cursor.getColumnIndexOrThrow("bultos"));
            v.idSectorLogisticoTraslado = cursor.getInt(cursor.getColumnIndexOrThrow("idSectorLogisticoTraslado"));
            v.nombreSectorLogisticoTraslado = cursor.getString(cursor.getColumnIndexOrThrow("nombreSectorLogisticoTraslado"));
            v.horarioRecoleccionTraslado = cursor.getString(cursor.getColumnIndexOrThrow("horarioRecoleccionTraslado"));
            v.idHorarioTraslado = cursor.getInt(cursor.getColumnIndexOrThrow("idHorarioTraslado"));
            v.nombreHorarioTraslado = cursor.getString(cursor.getColumnIndexOrThrow("nombreHorarioTraslado"));
            v.horarioEntregaTraslado = cursor.getInt(cursor.getColumnIndexOrThrow("horarioEntregaTraslado"));
            v.direccionEntregaTraslado = cursor.getString(cursor.getColumnIndexOrThrow("direccionEntregaTraslado"));
            v.departamentoTraslado = cursor.getString(cursor.getColumnIndexOrThrow("departamentoTraslado"));
            v.nombreDepartamentoTraslado = cursor.getString(cursor.getColumnIndexOrThrow("nombreDepartamentoTraslado"));
            v.municipioTraslado = cursor.getString(cursor.getColumnIndexOrThrow("municipioTraslado"));
            v.nombreMunicipioTraslado = cursor.getString(cursor.getColumnIndexOrThrow("nombreMunicipioTraslado"));
            v.zonaTraslado = cursor.getString(cursor.getColumnIndexOrThrow("zonaTraslado"));
            v.entregarATraslado = cursor.getString(cursor.getColumnIndexOrThrow("entregarATraslado"));
            v.recibidoPorTraslado = cursor.getString(cursor.getColumnIndexOrThrow("recibidoPorTraslado"));
            v.latitudEntregaTraslado = cursor.getString(cursor.getColumnIndexOrThrow("latitudEntregaTraslado"));
            v.longitudEntregaTraslado = cursor.getString(cursor.getColumnIndexOrThrow("longitudEntregaTraslado"));
            v.observacionesEntregaTraslado = cursor.getString(cursor.getColumnIndexOrThrow("observacionesEntregaTraslado"));
            v.idMotivoDeRechazoTraslado = cursor.getInt(cursor.getColumnIndexOrThrow("idMotivoDeRechazoTraslado"));
            v.imagenDeRecibidoTraslado = cursor.getString(cursor.getColumnIndexOrThrow("imagenDeRecibidoTraslado"));
            v.imagenDeEntregadoTraslado = cursor.getString(cursor.getColumnIndexOrThrow("imagenDeEntregadoTraslado"));
            v.fechaDeEntregaTraslado = cursor.getString(cursor.getColumnIndexOrThrow("fechaDeEntregaTraslado"));

            lst.add(v);
        }
        cursor.close();
        return lst;
    }


    public ArrayList<HojaRutaModel> select(int IdHojaDeRuta, int IdTraslado) {
        ArrayList<HojaRutaModel> lst = new ArrayList<>();
        Cursor cursor = db.query("TblHojaRuta", null, "idHojaDeRuta = ? AND idTraslado",new String[]{String.valueOf(IdHojaDeRuta),String.valueOf(IdTraslado)} , null, null, null);
       // cursor.moveToFirst();
        while (cursor.moveToNext()) {
            HojaRutaModel v = new HojaRutaModel();
            v.Id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            v.idHojaDeRuta = cursor.getInt(cursor.getColumnIndexOrThrow("idHojaDeRuta"));
            v.hojaDeRutaEstado = cursor.getInt(cursor.getColumnIndexOrThrow("hojaDeRutaEstado"));
            v.hojaDeRutaNombreEstado = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaNombreEstado"));
            v.fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"));
            v.idSectorLogistico = cursor.getInt(cursor.getColumnIndexOrThrow("idSectorLogistico"));
            v.nombreSectorLogistico = cursor.getString(cursor.getColumnIndexOrThrow("nombreSectorLogistico"));
            v.idVehiculo = cursor.getInt(cursor.getColumnIndexOrThrow("idVehiculo"));
            v.nombreVehiculo = cursor.getString(cursor.getColumnIndexOrThrow("nombreVehiculo"));
            v.placaVehiculo = cursor.getString(cursor.getColumnIndexOrThrow("placaVehiculo"));
            v.idPiloto = cursor.getInt(cursor.getColumnIndexOrThrow("idPiloto"));
            v.nombrePiloto = cursor.getString(cursor.getColumnIndexOrThrow("nombrePiloto"));
            v.idAuxiliar = cursor.getInt(cursor.getColumnIndexOrThrow("idAuxiliar"));
            v.nombreAuxiliar = cursor.getString(cursor.getColumnIndexOrThrow("nombreAuxiliar"));
            v.hojaDeRutaVale = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaVale"));
            v.hojaDeRutaOtrosGastos = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaOtrosGastos"));
            v.hojaDeRutaFechaHoraSalida = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaFechaHoraSalida"));
            v.hojaDeRutaFechaHoraRegreso = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaFechaHoraRegreso"));
            v.hojaDeRutaKmInicial = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaKmInicial"));
            v.hojaDeRutaKmFinal = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaKmFinal"));
            v.hojaDeRutaValeCombustible = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaValeCombustible"));
            v.hojaDeRutaGalones = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaGalones"));
            v.hojaDeRutaLatitudInicial = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaLatitudInicial"));
            v.hojaDeRutaLongitudInicial = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaLongitudInicial"));
            v.hojaDeRutaLatitudFinal = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaLatitudFinal"));
            v.hojaDeRutaLongitudFinal = cursor.getString(cursor.getColumnIndexOrThrow("hojaDeRutaLongitudFinal"));
            v.idTraslado = cursor.getInt(cursor.getColumnIndexOrThrow("idTraslado"));
            v.idSociedad = cursor.getString(cursor.getColumnIndexOrThrow("idSociedad"));
            v.idTipoMovimiento = cursor.getInt(cursor.getColumnIndexOrThrow("idTipoMovimiento"));
            v.identificador = cursor.getString(cursor.getColumnIndexOrThrow("identificador"));
            v.idEstado = cursor.getInt(cursor.getColumnIndexOrThrow("idEstado"));
            v.idUbicacionOrigen = cursor.getInt(cursor.getColumnIndexOrThrow("idUbicacionOrigen"));
            v.nombreUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("nombreUbicacionOrigen"));
            v.direccionUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("direccionUbicacionOrigen"));
            v.latitudUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("latitudUbicacionOrigen"));
            v.longitudUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("longitudUbicacionOrigen"));
            v.referenciaInternaUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("referenciaInternaUbicacionOrigen"));
            v.codigoClienteUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("codigoClienteUbicacionOrigen"));
            v.departamentoUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("departamentoUbicacionOrigen"));
            v.nombreDepartamentoUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("nombreDepartamentoUbicacionOrigen"));
            v.nombreMunicipioUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("nombreMunicipioUbicacionOrigen"));
            v.telefonoUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("telefonoUbicacionOrigen"));
            v.correoElectronicoUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("correoElectronicoUbicacionOrigen"));
            v.idSectorLogisticoUbicacionOrigen = cursor.getInt(cursor.getColumnIndexOrThrow("idSectorLogisticoUbicacionOrigen"));
            v.nombreSectorLogisticoUbicacionOrigen = cursor.getString(cursor.getColumnIndexOrThrow("nombreSectorLogisticoUbicacionOrigen"));
            v.idUbicacionDestino = cursor.getInt(cursor.getColumnIndexOrThrow("idUbicacionDestino"));
            v.nombreUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("nombreUbicacionDestino"));
            v.direccionUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("direccionUbicacionDestino"));
            v.latitudUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("latitudUbicacionDestino"));
            v.longitudUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("longitudUbicacionDestino"));
            v.referenciaInternaUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("referenciaInternaUbicacionDestino"));
            v.codigoClienteUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("codigoClienteUbicacionDestino"));
            v.departamentoUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("departamentoUbicacionDestino"));
            v.nombreDepartamentoUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("nombreDepartamentoUbicacionDestino"));
            v.nombreMunicipioUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("nombreMunicipioUbicacionDestino"));
            v.telefonoUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("telefonoUbicacionDestino"));
            v.correoElectronicoUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("correoElectronicoUbicacionDestino"));
            v.idSectorLogisticoUbicacionDestino = cursor.getInt(cursor.getColumnIndexOrThrow("idSectorLogisticoUbicacionDestino"));
            v.nombreSectorLogisticoUbicacionDestino = cursor.getString(cursor.getColumnIndexOrThrow("nombreSectorLogisticoUbicacionDestino"));
            v.fechaRecoleccion = cursor.getString(cursor.getColumnIndexOrThrow("fechaRecoleccion"));
            v.fechaEntrega = cursor.getString(cursor.getColumnIndexOrThrow("fechaEntrega"));
            v.valorDocumento = cursor.getString(cursor.getColumnIndexOrThrow("valorDocumento"));
            v.referencia = cursor.getString(cursor.getColumnIndexOrThrow("referencia"));
            v.observaciones = cursor.getString(cursor.getColumnIndexOrThrow("observaciones"));
            v.idTrasladoLogistica = cursor.getInt(cursor.getColumnIndexOrThrow("idTrasladoLogistica"));
            v.bultos = cursor.getInt(cursor.getColumnIndexOrThrow("bultos"));
            v.idSectorLogisticoTraslado = cursor.getInt(cursor.getColumnIndexOrThrow("idSectorLogisticoTraslado"));
            v.nombreSectorLogisticoTraslado = cursor.getString(cursor.getColumnIndexOrThrow("nombreSectorLogisticoTraslado"));
            v.horarioRecoleccionTraslado = cursor.getString(cursor.getColumnIndexOrThrow("horarioRecoleccionTraslado"));
            v.idHorarioTraslado = cursor.getInt(cursor.getColumnIndexOrThrow("idHorarioTraslado"));
            v.nombreHorarioTraslado = cursor.getString(cursor.getColumnIndexOrThrow("nombreHorarioTraslado"));
            v.horarioEntregaTraslado = cursor.getInt(cursor.getColumnIndexOrThrow("horarioEntregaTraslado"));
            v.direccionEntregaTraslado = cursor.getString(cursor.getColumnIndexOrThrow("direccionEntregaTraslado"));
            v.departamentoTraslado = cursor.getString(cursor.getColumnIndexOrThrow("departamentoTraslado"));
            v.nombreDepartamentoTraslado = cursor.getString(cursor.getColumnIndexOrThrow("nombreDepartamentoTraslado"));
            v.municipioTraslado = cursor.getString(cursor.getColumnIndexOrThrow("municipioTraslado"));
            v.nombreMunicipioTraslado = cursor.getString(cursor.getColumnIndexOrThrow("nombreMunicipioTraslado"));
            v.zonaTraslado = cursor.getString(cursor.getColumnIndexOrThrow("zonaTraslado"));
            v.entregarATraslado = cursor.getString(cursor.getColumnIndexOrThrow("entregarATraslado"));
            v.recibidoPorTraslado = cursor.getString(cursor.getColumnIndexOrThrow("recibidoPorTraslado"));
            v.latitudEntregaTraslado = cursor.getString(cursor.getColumnIndexOrThrow("latitudEntregaTraslado"));
            v.longitudEntregaTraslado = cursor.getString(cursor.getColumnIndexOrThrow("longitudEntregaTraslado"));
            v.observacionesEntregaTraslado = cursor.getString(cursor.getColumnIndexOrThrow("observacionesEntregaTraslado"));
            v.idMotivoDeRechazoTraslado = cursor.getInt(cursor.getColumnIndexOrThrow("idMotivoDeRechazoTraslado"));
            v.imagenDeRecibidoTraslado = cursor.getString(cursor.getColumnIndexOrThrow("imagenDeRecibidoTraslado"));
            v.imagenDeEntregadoTraslado = cursor.getString(cursor.getColumnIndexOrThrow("imagenDeEntregadoTraslado"));
            v.fechaDeEntregaTraslado = cursor.getString(cursor.getColumnIndexOrThrow("fechaDeEntregaTraslado"));

            lst.add(v);
        }
        cursor.close();
        return lst;
    }

}
