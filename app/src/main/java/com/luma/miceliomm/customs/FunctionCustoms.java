package com.luma.miceliomm.customs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;

public class FunctionCustoms {

    GlobalCustoms var= GlobalCustoms.getInstance();

    public String getIdDispositivo(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getFechaHoraActual() {
        Date date = new Date();
        DateFormat houFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String FechaHora = houFormat.format(date);
        return FechaHora;
    }

    public String getFechaHoraActualJson() {
        Date date = new Date();
        DateFormat houFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String FechaHora = houFormat.format(date);
        return FechaHora;
    }

    public String getFechaActual() {
        Date date = new Date();
        DateFormat houFormat = new SimpleDateFormat("dd/MM/yyyy");
        String FechaHora = houFormat.format(date);
        return FechaHora;
    }

    public String getFechaActualFormat() {
        Date date = new Date();
        DateFormat houFormat = new SimpleDateFormat("yyyy-MM-dd");
        String FechaHora = houFormat.format(date);
        return FechaHora;
    }

    public String getHoraActual() {
        Date date = new Date();
        DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        String Hora = hourFormat.format(date);
        return Hora;
    }

    public String getBitacoraFechaHoraActual() {
        Date date = new Date();
        DateFormat houFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
        String FechaHora = houFormat.format(date);
        return FechaHora;
    }

    public String getBitacoraFechaActual() {
        Date date = new Date();
        DateFormat houFormat = new SimpleDateFormat("ddMMyyyy");
        String FechaHora = houFormat.format(date);
        return FechaHora;
    }

    public String getHoraDeFecha(String Fecha) {
        return Fecha.trim().substring(11, Fecha.length() - 3);
    }

    public String getCodigo(String xDescripcion, String xSeparado) {
        String[] cadena = xDescripcion.split(xSeparado);
        return cadena[0];
    }

    public String getDescripcion(String xDescripcion, String xSeparado) {
        String[] cadena = xDescripcion.split(xSeparado);
        return cadena[1];
    }

    public String getVersion(Context context) {
        int currentVersionCode = 0;
        String currentVersionName = "";
        try {

            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            currentVersionCode = packageInfo.versionCode;
            currentVersionName = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AutoUpdate", "Ha habido un error con el packete :S", e);
        }
        return "Version: " + Integer.toString(currentVersionCode) + "." + currentVersionName;
    }



    public String getVersionDB(Context context){

        DbHandler  dbHandler = new DbHandler(context, null, null, 1);

        return "Version De B.D.: " + dbHandler.getDbVersion();
    }


    public AlertDialog mensaje(String Mensaje, Activity Macty) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Macty);
        AlertDialog alerta;
        builder.setCancelable(false);

        builder.setTitle("Mensaje del Sistema");
        builder.setMessage(Mensaje);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alerta = builder.create();
        return alerta;
    }

    public AlertDialog mensajeError(String Mensaje, Activity Macty) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Macty);
        AlertDialog alerta;
        builder.setCancelable(false);

        builder.setTitle("Mensaje de Error");
        builder.setMessage("Se detecto un error, favor de reporta a TI.\nError referencia " + Mensaje);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alerta = builder.create();
        return alerta;
    }


    public void msgToast(String Mensaje, Context context) {
        Toast.makeText(context, Mensaje, Toast.LENGTH_LONG).show();
    }

    public void msgSnackBar(String mensaje, Context context) {
        Snackbar.make(((Activity) context).getWindow().getDecorView().getRootView(), mensaje, Snackbar.LENGTH_LONG)
                .show();
    }


    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";

    public void getHoraDialog(Context context, final TextView control) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaF = (hourOfDay < 10) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                String minutoF = (minute < 10) ? String.valueOf(CERO + minute) : String.valueOf(minute);

                control.setText(horaF + DOS_PUNTOS + minutoF + DOS_PUNTOS + "00");
            }
        }, hour, minute, true);
        timePickerDialog.setTitle("Seleccione la Hora");
        timePickerDialog.show();
    }

    public void getHoraDialog(Context context, final EditText control) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaF = (hourOfDay < 10) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                String minutoF = (minute < 10) ? String.valueOf(CERO + minute) : String.valueOf(minute);

                control.setText(horaF + DOS_PUNTOS + minutoF + DOS_PUNTOS + "00");
            }
        }, hour, minute, true);
        timePickerDialog.setTitle("Seleccione la Hora");
        timePickerDialog.show();
    }

    private static final String DIAGONAL = "/";

    public void getFechaDialog(Context context, final TextView control) {
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dataPickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Se coloca +1 debido a que android coloca el mes seleccionado -1
                month += 1;
                String smonth = (month < 10) ? String.valueOf(CERO + month) : String.valueOf(month);
                String sdayOfMonth = (dayOfMonth < 10) ? String.valueOf(CERO + dayOfMonth) : String.valueOf(dayOfMonth);
                control.setText(sdayOfMonth + DIAGONAL + smonth + DIAGONAL + year);
            }
        }, mYear, mMonth, mDay);
        dataPickerDialog.setTitle("Seleccione la Fecha");
        dataPickerDialog.show();
    }

    public void getFechaDialog(Context context, final EditText control) {
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dataPickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Se coloca +1 debido a que android coloca el mes seleccionado -1 y tambien el dia para colocarle el 0 a los numeros < 10
                month += 1;
                String smonth = (month < 10) ? String.valueOf(CERO + month) : String.valueOf(month);
                String sdayOfMonth = (dayOfMonth < 10) ? String.valueOf(CERO + dayOfMonth) : String.valueOf(dayOfMonth);
                control.setText(sdayOfMonth + DIAGONAL + smonth + DIAGONAL + year);
            }
        }, mYear, mMonth, mDay);
        dataPickerDialog.setTitle("Seleccione la Fecha");
        dataPickerDialog.show();
    }

    public boolean validarCampoVacio(EditText editText) {
        String Cadena = editText.getText().toString();
        if (TextUtils.isEmpty(Cadena)) {
            editText.setError("Campo requerido");
            editText.requestFocus();
            return true;
        } else {
            editText.setError(null);
        }
        return false;
    }

    public String setCeros(int Valor, int TotalCeros) {
        Formatter fmt = new Formatter();
        fmt.format("%0" + TotalCeros + "d", Valor);
        return fmt.toString();

    }

    public String formatDosDecimales(Double valor){
        DecimalFormat form = new DecimalFormat();
        form.setMaximumFractionDigits(2);
        return form.format(valor);
    }
    public Integer getDiferenciaFecha(Date fechaInicial, Date fechaFinal, String DatePart) {

        int Resultado = 0;
        double calculoDif;
        int calucloDias;
        long diferencia = fechaFinal.getTime() - fechaInicial.getTime();

        switch (DatePart) {
            case "D":
                calculoDif = (diferencia / 86400000);
                Resultado = (int) calculoDif;
                break;
            case "H":
                calculoDif = Double.valueOf(diferencia) / 3600000;
                Resultado = Integer.parseInt(new DecimalFormat("####.####").format(calculoDif));
                break;
        }
        return Resultado;
    }

    public Date convertToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }


    public String formatDateWS(String dateString) {
        String d = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
            DateFormat dateFormatnew = new SimpleDateFormat("yyyy-MM-dd");
            d = dateFormatnew.format(convertedDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return d;
    }

    public String formatDateTimeWS(String dateString) {
        String d = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
            DateFormat dateFormatnew = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            d = dateFormatnew.format(convertedDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return d;
    }

    public String formatTimeWS(String dateString) {
        String d = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
            DateFormat dateFormatnew = new SimpleDateFormat("HH:mm:ss");
            d = dateFormatnew.format(convertedDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return d;
    }

    public String formatDate(String dateString) {
        String d = "";
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
            DateFormat dateFormatnew = new SimpleDateFormat("dd/MM/yyyy");
            d = dateFormatnew.format(convertedDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return d;
    }

    public String formatTime(String dateString) {
        String d = "";
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
            DateFormat dateFormatnew = new SimpleDateFormat("HH:mm:ss");
            d = dateFormatnew.format(convertedDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return d;
    }

    public String formatDateTime(String dateString) {
        String d = "";
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
            DateFormat dateFormatnew = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            d = dateFormatnew.format(convertedDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return d;
    }

    public String formatDateDB(String dateString){
        String d = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
            DateFormat dateFormatnew = new SimpleDateFormat("yyyy-MM-dd");
            d = dateFormatnew.format(convertedDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return d;
    }

    public String formatDateTimeDB(String dateString){
        String d = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
            DateFormat dateFormatnew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            d = dateFormatnew.format(convertedDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return d;
    }

    public String formatTimeDB(String dateString) {
        String d = "";
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
            DateFormat dateFormatnew = new SimpleDateFormat("HH:mm:ss");
            d = dateFormatnew.format(convertedDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String DbH[] = d.split(":");
        if(DbH.length <= 1){
            d="";
        }else {
            d = DbH[0] + DbH[1];
        }
        return d;
    }

    public String formatDateJSon(String dateString) {
        String d = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
            DateFormat dateFormatnew = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            d = dateFormatnew.format(convertedDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return d;
    }

    public String formatDateTimeJSon(String dateString) {
        String d = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
            DateFormat dateFormatnew = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            d = dateFormatnew.format(convertedDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return d;
    }


    public String getFechaHoraActualFormat() {
        Date date = new Date();
        DateFormat houFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String FechaHora = houFormat.format(date);
        return FechaHora;
    }

    public String getCorrelativoFecha(String Fecha) {
        String codigo = "";
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = new Date(dateFormat.parse(Fecha).getTime());
            DateFormat houFormat = new SimpleDateFormat("yyMMdd");
            codigo = houFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return codigo;
    }

    /*
    //OLD
    public Uri getUri(String Path, Activity macty) {
        File directorioImagen = new File(Path);
        Uri uri = null;
        if (directorioImagen.exists()) {
            uri = Uri.fromFile(directorioImagen);
            if (Build.VERSION.SDK_INT >= 24) {
                uri = FileProvider.getUriForFile(macty, com.luma.miceliomm.BuildConfig.APPLICATION_ID + ".fileprovider",
                        directorioImagen);
            }
        }

        return uri;
    }

     */

    public Uri getUri(String path, Activity macty) {
        File directorioImagen = new File(path);
        Uri uri = null;

        if (directorioImagen.exists()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // Usamos el packageName dinámicamente
                String authority = macty.getPackageName() + ".fileprovider";
                uri = FileProvider.getUriForFile(macty, authority, directorioImagen);
            } else {
                uri = Uri.fromFile(directorioImagen);
            }
        }

        return uri;
    }

    public String getPath() {
        String picturePath = "";
        File file = new File(Environment.getExternalStorageDirectory(), "DCIM" + var.getPATH_FILE_APP());
        boolean isDirectoryCreated = file.exists();
        if (!isDirectoryCreated) {
            isDirectoryCreated = file.mkdirs();
        }
        if (isDirectoryCreated) {
            picturePath = Environment.getExternalStorageDirectory() + File.separator + "DCIM" + var.getPATH_FILE_APP() + File.separator;
        }
        return picturePath;
    }


    public File getFile(Uri imageUri, Activity activity) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.ImageColumns.ORIENTATION};
            cursor = activity
                    .managedQuery(imageUri, proj, null, null, null);
            int file_ColumnIndex = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int orientation_ColumnIndex = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);
            if (cursor.moveToFirst()) {
                String orientation = cursor
                        .getString(orientation_ColumnIndex);
                return new File(cursor.getString(file_ColumnIndex));
            }
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getPath(Uri uri,Activity activity) {
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor =  activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Images.Media._ID + " = ? ",
                new String[]{document_id},
                null);
        cursor.moveToFirst();
        @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    public String formatUrlEncode(String dato){
        String resultado = null;
        try {
            resultado = URLEncoder.encode(dato ,  String.valueOf(StandardCharsets.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public String getNombreImagen(){
        Long timestamp = System.currentTimeMillis() / 1000;
        String imageName = timestamp.toString() + ".jpg";
        return imageName;
    }

    public String removerTildes(String cadena) {
        return cadena.replace("Á", "A")
                .replace("É", "E")
                .replace("Í", "I")
                .replace("Ó", "O")
                .replace("Ú", "U")
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u");
    }
}
