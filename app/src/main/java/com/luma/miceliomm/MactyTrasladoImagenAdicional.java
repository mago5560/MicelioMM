package com.luma.miceliomm;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.luma.miceliomm.controller.LocationController;
import com.luma.miceliomm.controller.TrasladoImagenAdicionalController;
import com.luma.miceliomm.controller.TrasladoLogisticaController;
import com.luma.miceliomm.customs.FunctionCustoms;
import com.luma.miceliomm.model.HojaRutaDetalleModel;
import com.luma.miceliomm.model.TrasladoImagenAdicionalModel;
import com.luma.miceliomm.model.TrasladoLogisticaModel;
import com.luma.miceliomm.model.TrasladoLogisticoRecoleccionModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MactyTrasladoImagenAdicional extends AppCompatActivity
        implements LocationController.LocationUpdateListener{

    private Intent intent;
    private FunctionCustoms util;
    // Constantes para requests
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 1001;

    // Datos recibidos
    private int idHojaDeRuta, idTraslado, idTrasladoLogistico;

    private TrasladoImagenAdicionalModel model;
    private TrasladoImagenAdicionalController controller;
    private TrasladoLogisticaController trasladoLogisticaController;

    private LocationController locationController;
    private Location currentLocation;

    private String Latitud ="0", Longitud="0";

    private HojaRutaDetalleModel hojaRutaDetalleModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.macty_traslado_imagen_adicional);

        findViewsById();
        actions();
        getParametros();

        // Manejar permisos y ubicación
        handleLocationPermissions();

    }

    private void findViewsById(){
        util = new FunctionCustoms();
        ((TextView) findViewById(R.id.lblTituloNavBar)).setText("Traslado Imagen Adicional");
        controller = new TrasladoImagenAdicionalController(this);
        trasladoLogisticaController = new TrasladoLogisticaController(this);
        model = new TrasladoImagenAdicionalModel();
        hojaRutaDetalleModel = new HojaRutaDetalleModel();

        // Inicializar controlador de ubicación
        locationController = new LocationController(this);
        locationController.addLocationListener(this);

        //Ejecucion cuando no existe el algun objeto necesitado en sqlite solo por TI
        //controller.alterObject();
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
                //tomarFoto();
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
                grabar();
            }
        });


        ((ImageView) findViewById(R.id.ivArchivoImagen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogViewImage(model);
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

    }

    private void getParametros(){
        Bundle parametros = this.getIntent().getExtras();
        if(parametros !=null){
            idHojaDeRuta = parametros.getInt("idHojaRuta");
            model.idHojaDeRuta = idHojaDeRuta;
            idTraslado = parametros.getInt("idTraslado" );
            model.idTraslado = idTraslado;
            idTrasladoLogistico = parametros.getInt("idTrasladoLogistica");
            model.idTrasladoLogistica = idTrasladoLogistico;
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
    }


    private void grabar(){
        if (validarCampos()){
            model.fecha = util.getFechaHoraActual();
            model.latitud = Latitud;
            model.longitud = Longitud;
            controller.setTrasladoImagenAdicional(model);

        }
    }

    private boolean validarCampos(){
        if (model.rutaImagen.isEmpty()){
            util.mensaje("La imagen es requerida por el sistema",this).show();
            return false;
        }
        return true;
    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }




    // <editor-fold defaultstate="collapsed" desc="zoom img">
    private AlertDialog alertDialogZoomImg;
    private void dialogViewImage(TrasladoImagenAdicionalModel _cls) {
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

    private void dialogViewImageFindId(View view,TrasladoImagenAdicionalModel _cls){
        if (!_cls.rutaImagen.isEmpty()) {
            Bitmap bMap = BitmapFactory.decodeFile(_cls.rutaImagen);
            ((ImageView) view.findViewById(R.id.imgvwZoom)).setImageBitmap(bMap);
        }else{
            ((ImageView) view.findViewById(R.id.imgvwZoom)).setImageResource(R.drawable.no_camara);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="tomarFoto, seleccionarImagen">
    /* TODO: INVESTIGAR EL PROCESO QUE NO FUNCIONA

       private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "mm_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        model.rutaImagen = image.getAbsolutePath();
        model.imagen = image.getName();
        return image;
    }

    private File photoFile= null;

    private void tomarFoto() {
        try {
            // Verificar permisos de cámara primero
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA_PERMISSION);
                return;
            }

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            getPackageName() +  ".fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                    // Otorgar permisos de lectura temporal a la app de cámara
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        } catch (Exception e) {
            util.msgToast("Error al tomar foto: " + e.getMessage(), this);
            Log.e("CameraError", "Error: " + e.getMessage(), e);
        }
    }
     */

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
        if (!model.rutaImagen.isEmpty()) {
            Bitmap bMap = BitmapFactory.decodeFile(model.rutaImagen);
            ((ImageView) findViewById(R.id.ivArchivoImagen)).setImageBitmap(bMap);
            //((ImageView) findViewById(R.id.ivArchivoImagen)) .setImageURI(Uri.parse(model.rutaImagen));
        }
    }

    // </editor-fold>


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                /*if (model.rutaImagen != null) {
                    comprimirImagen();
                    ((ImageView) findViewById(R.id.ivArchivoImagen)) .setImageURI(Uri.parse(model.rutaImagen));
                }
                 */

                MediaScannerConnection.scanFile(this,
                        new String[]{picturePath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });
                model.rutaImagen = picturePath ;
                comprimirImagen();
                fillImagen();
            } else if (requestCode == REQUEST_PICK_IMAGE && data != null) {
                Uri selectedImage = data.getData();
                comprimirImagen(selectedImage);
                ((ImageView) findViewById(R.id.ivArchivoImagen)).setImageURI(selectedImage);
            }
        }
    }


    private void comprimirImagen() {
        Bitmap bMap = BitmapFactory.decodeFile(model.rutaImagen);
        OutputStream fileOutStream = null;
        try {
            fileOutStream = new FileOutputStream(fileImage);
            bMap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutStream);
            fileOutStream.flush();
            fileOutStream.close();
            model.fileImagen = fileImage;
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
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();

            // Actualiza tu modelo con la ruta física
            model.rutaImagen =outputFile.getAbsolutePath();
            model.imagen = outputFile.getName();
            Log.d("Imagen", "Imagen comprimida en: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Imagen", "Error al comprimir imagen: " + e.getMessage());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        /*if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, intentar tomar foto nuevamente
                new Handler().postDelayed(this::tomarFoto, 500);
            } else {
                util.msgToast("Permiso de cámara denegado",this);
            }
        }
        */

        if (locationController.onRequestPermissionsResult(requestCode, grantResults)) {
            startLocationUpdates();
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


    // <editor-fold defaultstate="collapsed" desc="Menu, Opciones y regresar a principal">

    private void goHome() {
        intent = new Intent().setClass(this, MactyHojaRutaDetalle.class);
        intent.putExtra("idHojaRuta",idHojaDeRuta);
        startActivity(intent);
        finish();
    }

    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Manejo de permisos y ubicacion">
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationController != null) {
            locationController.stopLocationUpdates();
            locationController.removeAllListeners();
        }
    }
    @Override
    public void onLocationUpdated(Location location) {
        currentLocation = location;
        Latitud = String.valueOf(location.getLatitude());
        Longitud =  String.valueOf(location.getLongitude());
    }

    @Override
    public void onLocationError(String error) {
        ((TextView) findViewById(R.id.tvUbicacion)) .setText("Error: " + error);
        util.msgToast(error,this);
    }

    @Override
    public void onPermissionsDenied() {
        ((TextView) findViewById(R.id.tvUbicacion)) .setText("Permisos denegados - No se puede obtener ubicación");
        util.msgToast("Los permisos de ubicación son necesarios",this);
    }

    private void handleLocationPermissions() {
        if (locationController.hasLocationPermissions()) {
            startLocationUpdates();
        } else {
            locationController.requestLocationPermissions(this);
        }
    }

    private void startLocationUpdates() {
        if (locationController.isGPSEnabled()) {
            locationController.startLocationUpdates();
        } else {
            ((TextView) findViewById(R.id.tvUbicacion)) .setText("GPS deshabilitado - Active el GPS");
            util.msgToast("Active el GPS para obtener ubicación precisa",this);
        }
    }

    // </editor-fold>

}