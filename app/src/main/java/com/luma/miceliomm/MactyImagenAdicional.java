package com.luma.miceliomm;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.luma.miceliomm.adapter.ImagenAdicionalAdapter;
import com.luma.miceliomm.adapter.VehiculoMaestroAdapter;
import com.luma.miceliomm.controller.ImagenAdicionalController;
import com.luma.miceliomm.controller.VehiculoController;
import com.luma.miceliomm.customs.FunctionCustoms;
import com.luma.miceliomm.customs.UnsafeOkHttpClient;
import com.luma.miceliomm.model.ImagenAdicionalModel;
import com.luma.miceliomm.model.TrasladoLogisticoRecoleccionModel;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class MactyImagenAdicional extends AppCompatActivity implements ImagenAdicionalAdapter.OnItemClickListener {

    private Intent intent;
    private FunctionCustoms util;
    private int idHojaDeRuta, idTraslado, idTrasladoLogistico;

    private ArrayList<ImagenAdicionalModel> arrayList;
    private ImagenAdicionalController controller;
    private ImagenAdicionalAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.macty_imagen_adicional);
        findViewsById();
        actions();
        getParametros();
    }

    private void findViewsById() {
        util = new FunctionCustoms();
        ((TextView) findViewById(R.id.lblTituloNavBar)).setText("Imagenes De Traslados");

        //RecyclerView
        ((RecyclerView) findViewById(R.id.grdDatos)).setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ((RecyclerView) findViewById(R.id.grdDatos)).setLayoutManager(llm);
    }

    private void actions() {

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

        ((SwipeRefreshLayout) findViewById(R.id.swipeRefresh)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buscar();
            }
        });
    }

    private void getParametros(){
        Bundle parametros = this.getIntent().getExtras();
        if(parametros !=null){
            idHojaDeRuta = parametros.getInt("idHojaRuta");
            idTraslado = parametros.getInt("idTraslado" );
            idTrasladoLogistico = parametros.getInt("idTrasladoLogistica");
            buscar();
        }else{
            util.mensaje("No se obtuvo id de la hoja de ruta correctamente",this).show();
        }
    }

    private void buscar(){
        arrayList = new ArrayList<>();
        adapter = new ImagenAdicionalAdapter(arrayList, this, this);
        controller = new ImagenAdicionalController(this
                , ((RecyclerView) findViewById(R.id.grdDatos))
                ,  ((LinearLayout) findViewById(R.id.emptyView))
                , ((SwipeRefreshLayout) findViewById(R.id.swipeRefresh))
                , adapter
        );

        controller.getDatos(idTrasladoLogistico);
        ((TextView) findViewById(R.id.lblFechaActualizacion)).setText(util.getFechaHoraActual());
    }

    @Override
    public void onClick(ImagenAdicionalAdapter.ItemAdapterViewHolder holder, int position) {

    }

    @Override
    public void onClickImagen(ImagenAdicionalAdapter.ItemAdapterViewHolder holder, int position) {
            dialogViewImage(adapter.info.get(position).archivoImagen);
    }

    // <editor-fold defaultstate="collapsed" desc="zoom img">
    private AlertDialog alertDialogZoomImg;
    private Picasso picasso;

    private void dialogViewImage(String url) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        final LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.content_view_imagen, null);
        this.dialogViewImageFindId(dialogLayout, url);
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

    private void dialogViewImageFindId(View view,String imageUrl){
        // Configurar Picasso con cliente personalizado
        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(client))
                .build();

        Log.d("PICASSO_DEBUG", "Intentando cargar: " + imageUrl);

        picasso.load(imageUrl)
                .placeholder(R.drawable.cargando)
                .error(R.drawable.no_camara)
                .fit()
                .centerCrop()
                .into(((ImageView) view.findViewById(R.id.imgvwZoom)), new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("PICASSO_DEBUG", "✓ Imagen cargada: " + imageUrl);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("PICASSO_DEBUG", "✗ Error cargando: " + imageUrl, e);
                        e.printStackTrace();
                    }
                });
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