package com.luma.miceliomm;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.OnBackPressedCallback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.luma.miceliomm.adapter.MotivoMaestroAdapter;
import com.luma.miceliomm.controller.MotivoController;
import com.luma.miceliomm.controller.TrasladoLogisticaController;
import com.luma.miceliomm.customs.FunctionCustoms;
import com.luma.miceliomm.model.HojaRutaDetalleModel;
import com.luma.miceliomm.model.MotivoModel;

import com.luma.miceliomm.model.TrasladoLogisticoRecoleccionModel;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;

import java.util.List;

import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MactyRecoleccionTraslado extends AppCompatActivity implements MotivoMaestroAdapter.OnItemClickListener{

    private Intent intent;
    private FunctionCustoms util;

    // Constantes para requests
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;

    // Datos recibidos
    private int idHojaDeRuta, idTraslado, idTrasladoLogistico, escalaCD=0;

    private TrasladoLogisticaController trasladoLogisticaController;
    private TrasladoLogisticoRecoleccionModel trasladoLogisticoRecoleccionModel;

    private HojaRutaDetalleModel hojaRutaDetalleModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.macty_recoleccion_traslado);

        findViewsById();
        actions();
        getParametros();


    }
    private void findViewsById(){
        util = new FunctionCustoms();
        ((TextView) findViewById(R.id.lblTituloNavBar)).setText("Recoleccion de Traslado");
        trasladoLogisticaController = new TrasladoLogisticaController(this);
        trasladoLogisticoRecoleccionModel = new TrasladoLogisticoRecoleccionModel();
        hojaRutaDetalleModel = new HojaRutaDetalleModel();
    }




    private void actions(){

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                goHome();
            }
        });

        ((ImageView) findViewById(R.id.imgvwRetroceder)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });

        ((Button) findViewById(R.id.btnTomarFoto)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCamara();
            }
        });

        ((Button) findViewById(R.id.btnSeleccionarImagen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarImagen();
            }
        });

        ((Button) findViewById(R.id.btnGrabar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(escalaCD == 1){
                        mensajeEscalaCD().show();
                }else{
                    grabar();
                }

            }
        });


        ((ImageView) findViewById(R.id.ivArchivoImagen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogViewImage(trasladoLogisticoRecoleccionModel);
            }
        });

        ((TextView) findViewById(R.id.lblIrMapaTraslado)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                openLocationInMap(Double.valueOf(hojaRutaDetalleModel.latitudEntregaTraslado),
                        Double.valueOf(hojaRutaDetalleModel.longitudEntregaTraslado),
                        String.valueOf(hojaRutaDetalleModel.referencia));
                 */

                alertDialogMaps(hojaRutaDetalleModel.latitudEntregaTraslado,
                        hojaRutaDetalleModel.longitudEntregaTraslado, hojaRutaDetalleModel.referencia);
            }
        });

        ((TextView) findViewById(R.id.lbltelefonoUbicacionDestino)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarLlamada( ((TextView) view.findViewById(R.id.lbltelefonoUbicacionDestino)).getText().toString() );
            }
        });

        ((Button) findViewById(R.id.btnNoRecolectar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialogNoRecoleccion();
            }
        });

        ((ToggleButton) findViewById(R.id.toggleEscalaCD)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    escalaCD = 1;
                }else{
                    escalaCD = 0;}
            }
        });
    }

    private void getParametros(){
        Bundle parametros = this.getIntent().getExtras();
        if(parametros !=null){
            idHojaDeRuta = parametros.getInt("idHojaRuta");
            trasladoLogisticoRecoleccionModel.idHojaDeRuta = idHojaDeRuta;
            idTraslado = parametros.getInt("idTraslado" );
            trasladoLogisticoRecoleccionModel.idTraslado = idTraslado;
            idTrasladoLogistico = parametros.getInt("idTrasladoLogistica");
            trasladoLogisticoRecoleccionModel.idTrasladoLogistico = idTrasladoLogistico;
            fillDatos();
        }else{
            util.mensaje("No se obtuvo id de la hoja de ruta correctamente",this).show();
        }
    }

    private void fillDatos(){
        hojaRutaDetalleModel =  trasladoLogisticaController.selectHojaRutaDetalleId(idHojaDeRuta,idTraslado,idTrasladoLogistico);

        ((TextView) findViewById(R.id.lblReferencia)) .setText( hojaRutaDetalleModel.referencia);
        ((TextView) findViewById(R.id.lblObservaciones)) .setText(hojaRutaDetalleModel.observaciones);

        ((TextView) findViewById(R.id.lblHorarioEntregaTraslado)) .setText(String.valueOf(hojaRutaDetalleModel.horarioEntregaTraslado));
        ((TextView) findViewById(R.id.lblNombreHorarioTrasaldo)) .setText(hojaRutaDetalleModel.nombreHorarioTraslado);
        ((TextView) findViewById(R.id.lblDireccionEntregaTraslado)) .setText(hojaRutaDetalleModel.direccionEntregaTraslado);
        ((TextView) findViewById(R.id.lblZonaTraslado)) .setText(hojaRutaDetalleModel.zonaTraslado);

        ((TextView) findViewById(R.id.lblNombreDepartamentoTraslado)) .setText(hojaRutaDetalleModel.nombreDepartamentoTraslado);
        ((TextView) findViewById(R.id.lblNombreMunicipioTraslado)) .setText(hojaRutaDetalleModel.nombreMunicipioTraslado);
        ((TextView) findViewById(R.id.lblEntregarATraslado)) .setText(hojaRutaDetalleModel.entregarATraslado);
        ((TextView) findViewById(R.id.lblRecibidoPorTraslado)) .setText(hojaRutaDetalleModel.recibidoPorTraslado);
        ((TextView) findViewById(R.id.lblObservacionesEntregaTraslado)) .setText(hojaRutaDetalleModel.observacionesEntregaTraslado);
        ((TextView) findViewById(R.id.lblTotalBultos)) .setText(String.valueOf(hojaRutaDetalleModel.totalBultos));


        ((TextView) findViewById(R.id.lblnombreUbicacionOrigen)) .setText( hojaRutaDetalleModel.nombreUbicacionOrigen);
        ((TextView) findViewById(R.id.lblnombreUbicacionDestino)) .setText( hojaRutaDetalleModel.nombreUbicacionDestino);
        ((TextView) findViewById(R.id.lblnombreTipoMovimiento)) .setText( hojaRutaDetalleModel.nombreTipoMovimiento);
        ((TextView) findViewById(R.id.lbltelefonoUbicacionDestino)) .setText( hojaRutaDetalleModel.telefonoUbicacionDestino);

    }

    private void grabar(){
        if (validarCampos()){
            trasladoLogisticoRecoleccionModel.observaciones = ((EditText) findViewById(R.id.txtObservaciones)).getText().toString();
            trasladoLogisticoRecoleccionModel.escalaCD = this.escalaCD;
            trasladoLogisticoRecoleccionModel.tipoDocumento = ((EditText) findViewById(R.id.txtTipoDocumentoRecepcion)).getText().toString();
            trasladoLogisticoRecoleccionModel.idEstado = 4;
            trasladoLogisticoRecoleccionModel.fechaHoraRecoleccion = util.getFechaHoraActual();
            trasladoLogisticaController.setRecoleccionTrasladoLogistico(trasladoLogisticoRecoleccionModel);

        }
    }

    public android.app.AlertDialog mensajeEscalaCD() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        android.app.AlertDialog alerta;
        builder.setCancelable(false);

        builder.setTitle("Mensaje del Sistema");
        builder.setMessage("Selecciono operacion con Escala en C.D. esta seguro?");

        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                grabar();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alerta = builder.create();
        return alerta;
    }

    private boolean validarCampos(){
        if (trasladoLogisticoRecoleccionModel.rutaImagen.isEmpty()){
            util.mensaje("La imagen es requerida por el sistema",this).show();
            return false;
        }
        return true;
    }

    private void iniciarLlamada(String numeroTelefono){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + numeroTelefono));

        // Verificar que haya una app de teléfono disponible
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            util.mensaje("No hay apliccion de telefono disponible", this).show();
        }
    }


    // <editor-fold defaultstate="collapsed" desc="zoom img">
    private AlertDialog alertDialogZoomImg;
    private void dialogViewImage(TrasladoLogisticoRecoleccionModel _cls) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        final LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.content_view_imagen, null);
        this.dialogViewImageFindId(dialogLayout, _cls);
        builder.setView(dialogLayout);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dialogInterface.dismiss();
                alertDialogZoomImg.dismiss();
            }
        });

        alertDialogZoomImg = builder.show();
    }

    private void dialogViewImageFindId(View view,TrasladoLogisticoRecoleccionModel _cls){
        if (!_cls.rutaImagen.isEmpty()) {
            Bitmap bMap = BitmapFactory.decodeFile(_cls.rutaImagen);
            ((ImageView) view.findViewById(R.id.imgvwZoom)).setImageBitmap(bMap);
        }else{
            ((ImageView) view.findViewById(R.id.imgvwZoom)).setImageResource(R.drawable.no_camara);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="tomarFoto, seleccionarImagen">

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }


    private String picturePath = "", nombreImagen = "";
    private Uri uriSaveImage = null;
    private File fileImage = null;
    private void  getCamara() {
        try {

            picturePath = util.getPath();
            nombreImagen = util.getNombreImagen() ;
            if (picturePath != null) {
                picturePath = picturePath + nombreImagen;
                fileImage = new File(picturePath);

                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    String authority = this.getPackageName() + ".fileprovider";
                    uriSaveImage = FileProvider.getUriForFile(this, authority, fileImage);
                } else {
                    uriSaveImage = Uri.fromFile(fileImage);
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSaveImage);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            } else {
                util.mensaje("No se puede obtener la ruta para grabar la imagen, verifique accesos", this).show();
            }


        } catch (Exception ex) {
            ex.printStackTrace();
            util.mensajeError(ex.getMessage() + "(getCamara)",this).show();
        }
    }

    private void fillImagen(){
        if (!trasladoLogisticoRecoleccionModel.rutaImagen.isEmpty()) {
            Bitmap bMap = BitmapFactory.decodeFile(trasladoLogisticoRecoleccionModel.rutaImagen);
            ((ImageView) findViewById(R.id.ivArchivoImagen)).setImageBitmap(bMap);
            ((ImageView) findViewById(R.id.ivArchivoImagen)).setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.ivNoArchivoImagen)).setVisibility(View.GONE);
            //((ImageView) findViewById(R.id.ivArchivoImagen)) .setImageURI(Uri.parse(model.rutaImagen));
        }
    }

    // </editor-fold>


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                MediaScannerConnection.scanFile(this,
                        new String[]{picturePath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });
                trasladoLogisticoRecoleccionModel.rutaImagen = picturePath ;
                comprimirImagen();
                fillImagen();
            } else if (requestCode == REQUEST_PICK_IMAGE && data != null) {
                Uri selectedImage = data.getData();
                comprimirImagen(selectedImage);
                ((ImageView) findViewById(R.id.ivArchivoImagen)).setImageURI(selectedImage);
                ((ImageView) findViewById(R.id.ivArchivoImagen)).setVisibility(View.VISIBLE);
                ((ImageView) findViewById(R.id.ivNoArchivoImagen)).setVisibility(View.GONE);
                //trasladoLogisticaModel.rutaImagen = selectedImage.toString();
            }
        }
    }


    private void comprimirImagen() {
        Bitmap bMap = BitmapFactory.decodeFile(trasladoLogisticoRecoleccionModel.rutaImagen);
        OutputStream fileOutStream = null;
        try {
            fileOutStream = new FileOutputStream(fileImage);
            bMap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutStream);
            fileOutStream.flush();
            fileOutStream.close();
            trasladoLogisticoRecoleccionModel.fileImagen = fileImage;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("FileNot", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("IOExeption", e.getMessage());
        }
    }

    private void comprimirImagen(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            File outputDir = getCacheDir(); // puedes usar getExternalFilesDir si prefieres
            File outputFile = File.createTempFile("temp_image", ".jpg", outputDir);

            FileOutputStream fos = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();

            // Actualiza tu modelo con la ruta física
            trasladoLogisticoRecoleccionModel.rutaImagen =outputFile.getAbsolutePath();
            trasladoLogisticoRecoleccionModel.nombreImagen = outputFile.getName();
            trasladoLogisticoRecoleccionModel.fileImagen = outputFile;
            Log.d("Imagen", "Imagen comprimida en: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Imagen", "Error al comprimir imagen: " + e.getMessage());
        }
    }



    // <editor-fold defaultstate="collapsed" desc="Implementacion de mapa">
    private void openLocationInMap(double latitude, double longitude, String label) {
        try {
            // URI para Google Maps
            String uri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude;
            if (label != null && !label.isEmpty()) {
                uri += " (" + Uri.encode(label) + ")";
            }

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

            // Verificar si hay aplicaciones disponibles
            PackageManager packageManager = getPackageManager();
            List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);

            if (activities.size() > 0) {
                // Crear chooser para que el usuario elija la app
                String title = "Seleccionar aplicación de mapas";
                Intent chooser = Intent.createChooser(mapIntent, title);

                // Verificar que el chooser puede ser resuelto
                if (mapIntent.resolveActivity(packageManager) != null) {
                    startActivity(chooser);
                }
            } else {
                // No hay aplicaciones de mapas instaladas
                Toast.makeText(this, "No se encontraron aplicaciones de mapas", Toast.LENGTH_LONG).show();
            }

        } catch (ActivityNotFoundException e) {
            // Fallback: abrir en navegador web
            String url = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al abrir la ubicación", Toast.LENGTH_SHORT).show();
        }
    }


    private void alertDialogMaps(String Latitud, String Longitud, String Referencia) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final CharSequence[] items = new CharSequence[3];

        items[0] = "Waze";
        items[1] = "Google Maps";
        items[2] = "Otros";

        builder.setTitle("Seleccionar Opcion");
        builder.setCancelable(true);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        irWaze(Latitud, Longitud);
                        break;
                    case 1:
                        irGoogleMaps(Latitud + "," + Longitud, String.valueOf(Referencia));
                        break;
                    case 2:
                        irMapa(Latitud + "," + Longitud, String.valueOf(Referencia));
                        break;
                }
            }
        });

        builder.show();
    }

    private void irWaze(String Latitud, String Longitud) {
        try {
            // Launch Waze to look for Hawaii:
            //String url = "https://waze.com/ul?ll="+ Destino +"&z=10";
            String url = "waze://?ll=" + Latitud + ", " + Longitud + "&navigate=yes";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // If Waze is not installed, open it in Google Play:
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
            startActivity(intent);
        }
    }

    private void irGoogleMaps(String Destino, String Referencia) {
        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Destino + "(Referencia: " + Referencia + ")?z=12");
        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");
        // Attempt to start an activity that can handle the Intent
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            try {
                goMapRuta(Destino);
            } catch (ActivityNotFoundException innerEx) {
                util.msgToast("Instalar la aplicaion de maps", this);
            }
        }
    }

    private void goMapRuta(String destiny) {
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + destiny));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    private void irMapa(String Destino, String Referencia) {
        Uri uri = Uri.parse("geo:" + Destino + "?z=16&q=" + Destino + "(" + Referencia + ")");
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
    // </editor-fold>



    // <editor-fold defaultstate="collapsed" desc="Dialog No Recoleccion">
    private  View viewDialogLayoutRechazo;
    private AlertDialog alertDialogNoRecoleccion;
    private void dialogNoRecoleccion( ) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        final LayoutInflater inflater = getLayoutInflater();
        viewDialogLayoutRechazo = inflater.inflate(R.layout.content_view_no_recoleccion, null);
        this.dialogNoRecoleccionFindById( );
        this.dialogNoRecoleccionActions( );
        builder.setView(viewDialogLayoutRechazo );

        alertDialogNoRecoleccion = builder.show();
    }


    private void dialogNoRecoleccionFindById( ){



    }
    private void dialogNoRecoleccionActions( ){
        ((Button) viewDialogLayoutRechazo.findViewById(R.id.btnCancelarNoRecoleccion)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                alertDialogNoRecoleccion.dismiss();
            }
        });

        ((Button) viewDialogLayoutRechazo.findViewById(R.id.btnAceptarNoRecoleccion)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                trasladoLogisticoRecoleccionModel.idEstado = 7;
                trasladoLogisticoRecoleccionModel.fechaHoraRecoleccion = util.getFechaHoraActual();
                trasladoLogisticaController.setRecoleccionTrasladoLogisticoEstado(trasladoLogisticoRecoleccionModel);
            }
        });

        ((EditText) viewDialogLayoutRechazo.findViewById(R.id.txtMotivoRechazo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMotivo();
            }
        });
    }
    // </editor-fold>



    // <editor-fold defaultstate="collapsed" desc="Maestro Motivos">
    private MotivoController motivoController;

    private void descargarMotivo() {
        motivoController = new MotivoController(this);
        //motivoController.getDatos(  );
    }

    AlertDialog alertDialogMotivoMaestro;
    MotivoMaestroAdapter motivoMaestroAdapter;
    ArrayList<MotivoModel> motivoModelArrayList;

    private void dialogMotivo() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        final LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.content_datos_maestros, null);
        dialogMotivoFindViewsByIds(dialogLayout);
        dialogMotivoActions(dialogLayout);
        buscarMotivo(dialogLayout);
        builder.setTitle("Registros Existentes");

        builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton("Limpiar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) viewDialogLayoutRechazo.findViewById(R.id.txtMotivoRechazo)).setText("");
                trasladoLogisticoRecoleccionModel.motivoNoRecoleccion = "";
                dialogInterface.dismiss();
            }
        });

        builder.setView(dialogLayout);
        alertDialogMotivoMaestro = builder.show();
    }


    private void dialogMotivoFindViewsByIds(View v) {
        //RecyclerView
        ((RecyclerView) v.findViewById(R.id.grdDatos)).setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ((RecyclerView) v.findViewById(R.id.grdDatos)).setLayoutManager(llm);
        ((TextView) v.findViewById(R.id.lblTituloNavBar)).setText("");
    }

    private void dialogMotivoActions(View v) {
        ((ImageView) v.findViewById(R.id.imgvwAbrirBuscador)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vv) {
                visibleFilter(v, true);
            }
        });

        ((ImageView) v.findViewById(R.id.imgvwCerrarBuscador)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vv) {
                visibleFilter(v, false);
            }
        });

        ((SearchView) v.findViewById(R.id.txtBuscador)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                motivoMaestroAdapter.getFilter().filter(s);
                return false;
            }
        });

        ((SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buscarMotivo(v);
            }
        });
    }

    @Override
    public void onClick(MotivoMaestroAdapter.ItemAdapterViewHolder holder, int position) {
        ((EditText) viewDialogLayoutRechazo.findViewById(R.id.txtMotivoRechazo)).setText(motivoMaestroAdapter.info.get(position).Descripcion);
        trasladoLogisticoRecoleccionModel.motivoNoRecoleccion = motivoMaestroAdapter.info.get(position).Descripcion;
        trasladoLogisticoRecoleccionModel.idMotivoDeRechazoTraslado = String.valueOf(motivoMaestroAdapter.info.get(position).IdMotivoDeRechazo);
        alertDialogMotivoMaestro.dismiss();
    }

    private void buscarMotivo(View v) {
        motivoModelArrayList = new ArrayList<>();
        motivoMaestroAdapter = new MotivoMaestroAdapter(motivoModelArrayList, this, this);
        motivoController = new MotivoController(this
                , ((RecyclerView) v.findViewById(R.id.grdDatos))
                , motivoMaestroAdapter
                , ((SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh))
                , ((LinearLayout) v.findViewById(R.id.emptyView))
        );

        motivoController.buscar();
        ((TextView) v.findViewById(R.id.lblFechaActualizacion)).setText(util.getFechaHoraActual());
    }

    private void visibleFilter(View v, boolean visible) {
        if (visible) {
            ((LinearLayout) v.findViewById(R.id.llyFilter)).setVisibility(View.VISIBLE);
            ((SearchView) v.findViewById(R.id.txtBuscador)).requestFocus();
            ((ImageView) v.findViewById(R.id.imgvwAbrirBuscador)).setVisibility(View.GONE);
        } else {
            ((LinearLayout) v.findViewById(R.id.llyFilter)).setVisibility(View.GONE);
            ((SearchView) v.findViewById(R.id.txtBuscador)).setQuery("", false);
            ((ImageView) v.findViewById(R.id.imgvwAbrirBuscador)).setVisibility(View.VISIBLE);
        }
    }


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Menu, Opciones y regresar a principal">

    private void goHome() {
        intent = new Intent().setClass(this, MactyHojaRutaDetalle.class);
        intent.putExtra("idHojaRuta",idHojaDeRuta);
        startActivity(intent);
        finish();
    }

    // </editor-fold>

}