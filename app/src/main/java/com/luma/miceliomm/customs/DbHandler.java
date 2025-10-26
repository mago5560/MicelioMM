package com.luma.miceliomm.customs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "miceliomm.sqlite";
    private static final int DATABASE_VERSION = 1;

    public int getDbVersion(){
        return DATABASE_VERSION;
    }

    // Definición de tablas
    private static final String Vehiculos =
            "CREATE TABLE TblVehiculos (" +
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
                    ");";

    private static final String Estados =
            "CREATE TABLE TblEstados (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "idEstado INTEGER NOT NULL, " +
                    "nombre TEXT, " +
                    "descripcion TEXT, " +
                    "fechaCreacion TEXT " +
                    ");";

    private static final String Piloto =
            "CREATE TABLE TblPiloto (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "IdPersonal INTEGER NOT NULL, " +
                    "nombre TEXT, " +
                    "descripcion TEXT, " +
                    "licencia TEXT, " +
                    "activo INTEGER, " +
                    "idVehiculo INTEGER, " +
                    "fechaVencimiento TEXT, " +
                    "fechaCreacion TEXT " +
                    ");";

    private static final String Motivos =
            "CREATE TABLE TblMotivos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "idMotivoDeRechazo INTEGER NOT NULL, " +
                    "descripcion TEXT, " +
                    "fechaCreacion TEXT " +
                    ");";

    private static final String Sector =
            "CREATE TABLE TblSector (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "idSectorLogistico INTEGER NOT NULL, " +
                    "nombre TEXT, " +
                    "descripcion TEXT " +
                    ");";


    private static  final String HojaRuta =
            "CREATE TABLE TblHojaRuta ("+
                    " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " idHojaDeRuta INTEGER NOT NULL, " +
                    " hojaDeRutaEstado INTEGER, " +
                    " hojaDeRutaNombreEstado TEXT,  " +
                    " fecha TEXT,  " +
                    " idSectorLogistico INTEGER, " +
                    " nombreSectorLogistico TEXT,  " +
                    " idVehiculo INTEGER, " +
                    " nombreVehiculo TEXT,  " +
                    " placaVehiculo TEXT,  " +
                    " idPiloto INTEGER, " +
                    " nombrePiloto TEXT,  " +
                    " idAuxiliar INTEGER,  " +
                    " nombreAuxiliar TEXT,  " +
                    " hojaDeRutaVale TEXT,  " +
                    " hojaDeRutaOtrosGastos TEXT,  " +
                    " hojaDeRutaFechaHoraSalida TEXT,  " +
                    " hojaDeRutaFechaHoraRegreso TEXT,  " +
                    " hojaDeRutaKmInicial TEXT,  " +
                    " hojaDeRutaKmFinal TEXT,  " +
                    " hojaDeRutaValeCombustible TEXT,  " +
                    " hojaDeRutaGalones TEXT,  " +
                    " hojaDeRutaLatitudInicial TEXT,  " +
                    " hojaDeRutaLongitudInicial TEXT,  " +
                    " hojaDeRutaLatitudFinal TEXT,  " +
                    " hojaDeRutaLongitudFinal TEXT,  " +
                    " idTraslado INTEGER, " +
                    " idSociedad TEXT,  " +
                    " idTipoMovimiento INTEGER, " +
                    " identificador TEXT,  " +
                    " idEstado INTEGER, " +
                    " idUbicacionOrigen INTEGER, " +
                    " nombreUbicacionOrigen TEXT,  " +
                    " direccionUbicacionOrigen TEXT,  " +
                    " latitudUbicacionOrigen TEXT,  " +
                    " longitudUbicacionOrigen TEXT,  " +
                    " referenciaInternaUbicacionOrigen TEXT,  " +
                    " codigoClienteUbicacionOrigen TEXT,  " +
                    " departamentoUbicacionOrigen TEXT,  " +
                    " nombreDepartamentoUbicacionOrigen TEXT,  " +
                    " nombreMunicipioUbicacionOrigen TEXT,  " +
                    " telefonoUbicacionOrigen TEXT,  " +
                    " correoElectronicoUbicacionOrigen TEXT,  " +
                    " idSectorLogisticoUbicacionOrigen TEXT,  " +
                    " nombreSectorLogisticoUbicacionOrigen TEXT,  " +
                    " idUbicacionDestino INTEGER, " +
                    " nombreUbicacionDestino TEXT,  " +
                    " direccionUbicacionDestino TEXT,  " +
                    " latitudUbicacionDestino TEXT,  " +
                    " longitudUbicacionDestino TEXT,  " +
                    " referenciaInternaUbicacionDestino TEXT,  " +
                    " codigoClienteUbicacionDestino TEXT,  " +
                    " departamentoUbicacionDestino TEXT,  " +
                    " nombreDepartamentoUbicacionDestino TEXT,  " +
                    " nombreMunicipioUbicacionDestino TEXT,  " +
                    " telefonoUbicacionDestino TEXT,  " +
                    " correoElectronicoUbicacionDestino TEXT,  " +
                    " idSectorLogisticoUbicacionDestino TEXT,  " +
                    " nombreSectorLogisticoUbicacionDestino TEXT,  " +
                    " fechaRecoleccion TEXT,  " +
                    " observacionRecoleccion TEXT, "+
                    " fechaEntrega TEXT,  " +
                    " valorDocumento TEXT,  " +
                    " referencia TEXT,  " +
                    " observaciones TEXT,  " +
                    " idTrasladoLogistica INTEGER, " +
                    " bultos INTEGER, " +
                    " idSectorLogisticoTraslado INTEGER, " +
                    " nombreSectorLogisticoTraslado TEXT,  " +
                    " horarioRecoleccionTraslado TEXT,  " +
                    " idHorarioTraslado INTEGER, " +
                    " nombreHorarioTraslado TEXT,  " +
                    " horarioEntregaTraslado TEXT,  " +
                    " direccionEntregaTraslado TEXT,  " +
                    " departamentoTraslado TEXT,  " +
                    " nombreDepartamentoTraslado TEXT,  " +
                    " municipioTraslado TEXT,  " +
                    " nombreMunicipioTraslado TEXT,  " +
                    " zonaTraslado TEXT,  " +
                    " entregarATraslado TEXT,  " +
                    " recibidoPorTraslado TEXT,  " +
                    " latitudEntregaTraslado TEXT,  " +
                    " longitudEntregaTraslado TEXT,  " +
                    " observacionesEntregaTraslado TEXT,  " +
                    " idMotivoDeRechazoTraslado INTEGER,  " +
                    " imagenDeRecibidoTraslado TEXT,  " +
                    " imagenDeEntregadoTraslado TEXT,  " +
                    " fechaDeEntregaTraslado TEXT  " +
                    " );";

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

    public DbHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Vehiculos);
        db.execSQL(Estados);
        db.execSQL(Piloto);
        db.execSQL(Motivos);
        db.execSQL(Sector);
        db.execSQL(HojaRuta);
        db.execSQL(TrasladoImagenAdicional);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Manejo de migraciones
        db.execSQL("DROP TABLE IF EXISTS TblVehiculos");
        db.execSQL("DROP TABLE IF EXISTS TblEstados");
        db.execSQL("DROP TABLE IF EXISTS TblPiloto");
        db.execSQL("DROP TABLE IF EXISTS TblMotivos");
        db.execSQL("DROP TABLE IF EXISTS TblSector");
        db.execSQL("DROP TABLE IF EXISTS TblHojaRuta");
        db.execSQL("DROP TABLE IF EXISTS TblTrasladoImagenAdicional");
        onCreate(db);
    }



}
