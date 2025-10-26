package com.luma.miceliomm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.luma.miceliomm.adapter.HojaRutaDetalleAdapter;
import com.luma.miceliomm.adapter.HojaRutaResumenAdapter;
import com.luma.miceliomm.controller.HojaDeRutaController;
import com.luma.miceliomm.customs.FunctionCustoms;
import com.luma.miceliomm.model.HojaRutaDetalleModel;
import com.luma.miceliomm.model.HojaRutaResumenModel;

import java.util.ArrayList;

public class MactyHojaRutaDetalle extends AppCompatActivity implements HojaRutaDetalleAdapter.OnItemClickListener {
    private Intent intent;
    private FunctionCustoms util;
    private int idHojaRuta=0;

    private HojaDeRutaController hojaDeRutaController;
    private HojaRutaDetalleAdapter hojaRutaDetalleAdapter;
    private ArrayList<HojaRutaDetalleModel> hojaRutaDetalleModelArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.macty_hoja_ruta_detalle);
        findViewsById();
        actions();
        getParametros();
    }

    private void findViewsById(){
        util = new FunctionCustoms();
        ((TextView) findViewById(R.id.lblTituloNavBar)).setText("Hoja De Ruta Detalle");

        //RecyclerView
        ((RecyclerView) findViewById(R.id.grdDatos)).setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ((RecyclerView) findViewById(R.id.grdDatos)).setLayoutManager(llm);

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

        ((SwipeRefreshLayout) findViewById(R.id.swipeRefresh)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buscarDetalle();
            }
        });

        ((Button) findViewById(R.id.btnIniciarHojaRuta)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarHojaDeRuta();
            }
        });

        ((Button) findViewById(R.id.btnFinalizarHojaRuta)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalizarHojaDeRuta();
            }
        });

    }

    private void getParametros(){
        Bundle parametros = this.getIntent().getExtras();
        if(parametros !=null){
            idHojaRuta = parametros.getInt("idHojaRuta");
            fillDatos();
        }else{
            util.mensaje("No se obtuvo id de la hoja de ruta correctamente",this).show();
        }
    }

    private void fillDatos(){
        hojaDeRutaController = new HojaDeRutaController(this);
        HojaRutaResumenModel v =  hojaDeRutaController.selectHojaRutaId(idHojaRuta);
        ((TextView) findViewById(R.id.lblCVidHojaDeRuta))  .setText(String.valueOf(v.idHoraRuta));
        ((TextView) findViewById(R.id.lblCVnombreSectorLogistico)) .setText(v.nombreSectorLogistico);
        ((TextView) findViewById(R.id.lblCVnombrePiloto)) .setText(v.nombrePiloto);
        ((TextView) findViewById(R.id.lblCVfecha)) .setText(v.fecha);
        ((TextView) findViewById(R.id.lblCVTotalBultos)) .setText(String.valueOf(v.totalBultos));
        ((TextView) findViewById(R.id.lblCVnombreEstado)).setText(v.hojaRutaNombreEstado);

        //ConfiguracionDeColor
        configurarEstado(((TextView) findViewById(R.id.lblCVnombreEstado)),v.hojaDeRutaEstado, ((LinearLayout) findViewById(R.id.llyCVColores)));

        if (v.hojaDeRutaEstado <= 3){
            ((Button) findViewById(R.id.btnIniciarHojaRuta)).setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.btnFinalizarHojaRuta)).setVisibility(View.GONE);
        } else if (v.hojaDeRutaEstado == 5){
            ((Button) findViewById(R.id.btnIniciarHojaRuta)).setVisibility(View.GONE);
            ((Button) findViewById(R.id.btnFinalizarHojaRuta)).setVisibility(View.VISIBLE);
        }else {
            ((Button) findViewById(R.id.btnIniciarHojaRuta)).setVisibility(View.GONE);
            ((Button) findViewById(R.id.btnFinalizarHojaRuta)).setVisibility(View.GONE);
        }
        buscarDetalle();
    }

    // <editor-fold defaultstate="collapsed" desc="Colocar Color">
    private void configurarEstado(TextView textView , int idEstado , LinearLayout linearLayout) {
        int colorRes;
        int colorText;

        switch (idEstado) {
            case 1:
                colorRes = R.color.color_borrador;
                colorText =R.color.white;
                break;
            case 2:
                colorRes = R.color.color_cerrado;
                colorText =R.color.black;
                break;
            case 3:
                colorRes = R.color.color_listo_recoger;
                colorText =R.color.black;
                break;
            case 4:
                colorRes = R.color.color_programado;
                colorText =R.color.black;
                break;
            case 5:
                colorRes = R.color.color_recibido_piloto;
                colorText =R.color.white;
                break;
            case 6:
                colorRes = R.color.color_ruta;
                colorText =R.color.white;
                break;
            case 7:
                colorRes = R.color.color_entregado;
                colorText =R.color.white;
                break;
            case 8:
                colorRes = R.color.color_re_programado;
                colorText =R.color.black;
                break;
            case 9:
                colorRes = R.color.color_liquidado;
                colorText =R.color.black;
                break;
            default:
                colorRes = R.color.color_borrador;
                colorText =R.color.white;
                break;
        }

        textView.setBackgroundColor(ContextCompat.getColor(this, colorRes));
        textView.setTextColor( ContextCompat.getColor(this, colorText));
        linearLayout.setBackgroundColor(ContextCompat.getColor(this,colorRes));
    }
    // </editor-fold>

    private void buscarDetalle(){
        hojaRutaDetalleModelArrayList = new ArrayList<>();
        hojaRutaDetalleAdapter = new HojaRutaDetalleAdapter(hojaRutaDetalleModelArrayList, this, this);
        hojaDeRutaController = new HojaDeRutaController(this
                , ((RecyclerView) findViewById(R.id.grdDatos))
                , ((LinearLayout) findViewById(R.id.emptyView))
                , hojaRutaDetalleAdapter
                , ((SwipeRefreshLayout) findViewById(R.id.swipeRefresh))
        );

        hojaDeRutaController.buscarDetalle(idHojaRuta);
        ((TextView) findViewById(R.id.lblFechaActualizacion)).setText(util.getFechaHoraActual());
    }

    @Override
    public void onClick(HojaRutaDetalleAdapter.ItemAdapterViewHolder holder, int position) {
            verImagenAdicional(hojaRutaDetalleAdapter.info.get(position).idHoraRuta,
                    hojaRutaDetalleAdapter.info.get(position).idTraslado,
                    hojaRutaDetalleAdapter.info.get(position).idTrasladoLogistica);
    }

    @Override
    public void onClickTrasladoLogistico(HojaRutaDetalleAdapter.ItemAdapterViewHolder holder, int position) {
        trasladoLogistico(hojaRutaDetalleAdapter.info.get(position).idHoraRuta,
                hojaRutaDetalleAdapter.info.get(position).idTraslado,
                hojaRutaDetalleAdapter.info.get(position).idTrasladoLogistica,0);
    }

    @Override
    public void onClickRechazoTrasladoLogistico(HojaRutaDetalleAdapter.ItemAdapterViewHolder holder, int position) {
        trasladoLogistico(hojaRutaDetalleAdapter.info.get(position).idHoraRuta,
                hojaRutaDetalleAdapter.info.get(position).idTraslado,
                hojaRutaDetalleAdapter.info.get(position).idTrasladoLogistica,1);
    }

    @Override
    public void onClickAgregarImagenPaquete(HojaRutaDetalleAdapter.ItemAdapterViewHolder holder, int position) {
        trasladoImagenAdicional(hojaRutaDetalleAdapter.info.get(position).idHoraRuta,
                hojaRutaDetalleAdapter.info.get(position).idTraslado,
                hojaRutaDetalleAdapter.info.get(position).idTrasladoLogistica);
    }

    @Override
    public void onClickIniciarPaquete(HojaRutaDetalleAdapter.ItemAdapterViewHolder holder, int position) {
        recolectarTraslado(hojaRutaDetalleAdapter.info.get(position).idHoraRuta,
                hojaRutaDetalleAdapter.info.get(position).idTraslado,
                hojaRutaDetalleAdapter.info.get(position).idTrasladoLogistica);
    }

    private void trasladoLogistico(int idHojaDeRuta, int idTraslado, int idTrasladoLogistica, int rechazo){
        if (!hojaDeRutaController.existsHojaNoRecolectado(idHojaRuta)){
        intent = new Intent().setClass(this, MactyTrasladoLogistico.class);
        intent.putExtra("idHojaRuta",idHojaDeRuta);
        intent.putExtra("idTraslado",idTraslado);
        intent.putExtra("idTrasladoLogistica",idTrasladoLogistica);
        intent.putExtra("rechazo",rechazo);
        startActivity(intent);
        finish();
        }else{
            util.mensaje("Aun no se ha iniciado la Hoja de Ruta, verifique",this).show();
        }
    }

    private void recolectarTraslado(int idHojaDeRuta, int idTraslado, int idTrasladoLogistica){
        if ( !hojaDeRutaController.existsHojaEnRuta() ) {
        intent = new Intent().setClass(this, MactyRecoleccionTraslado.class);
        intent.putExtra("idHojaRuta",idHojaDeRuta);
        intent.putExtra("idTraslado",idTraslado);
        intent.putExtra("idTrasladoLogistica",idTrasladoLogistica);
        startActivity(intent);
        finish();
        }else{
            util.mensaje("Existe Hoja de Traslados pendientes en finalizar, favor de verificar.",this).show();
        }
    }

    private void trasladoImagenAdicional(int idHojaDeRuta, int idTraslado, int idTrasladoLogistica){
            intent = new Intent().setClass(this, MactyTrasladoImagenAdicional.class);
            intent.putExtra("idHojaRuta",idHojaDeRuta);
            intent.putExtra("idTraslado",idTraslado);
            intent.putExtra("idTrasladoLogistica",idTrasladoLogistica);
            startActivity(intent);
            finish();
    }

    private void iniciarHojaDeRuta(){
        if (!hojaDeRutaController.existsTrasladoLogisticoNoRecolectado(idHojaRuta)){
            intent = new Intent().setClass(this, MactyActualizarHojaRuta.class);
            intent.putExtra("idHojaRuta",idHojaRuta);
            intent.putExtra("iniciar",1);
            intent.putExtra("finalizar",0);
            startActivity(intent);
            finish();
        }else{
            util.mensaje("Aun hay traslados sin recolectar, para iniciar la ruta, verifique",this).show();
        }

    }

    private void finalizarHojaDeRuta(){
        if ( !hojaDeRutaController.existsTrasladoLogisticoEnRuta(idHojaRuta) ) {
            intent = new Intent().setClass(this, MactyActualizarHojaRuta.class);
            intent.putExtra("idHojaRuta", idHojaRuta);
            intent.putExtra("iniciar", 0);
            intent.putExtra("finalizar", 1);
            startActivity(intent);
            finish();
        }else{
            util.mensaje("Aun hay traslados sin entregar para finalizar la Hoja de Ruta, verifique",this).show();
        }
    }

    private void verImagenAdicional(int idHojaDeRuta, int idTraslado, int idTrasladoLogistica){
        intent = new Intent().setClass(this, MactyImagenAdicional.class);
        intent.putExtra("idHojaRuta",idHojaDeRuta);
        intent.putExtra("idTraslado",idTraslado);
        intent.putExtra("idTrasladoLogistica",idTrasladoLogistica);
        startActivity(intent);
        finish();
    }

    // <editor-fold defaultstate="collapsed" desc="Menu, Opciones y regresar a principal">

    private void goHome() {
        intent = new Intent().setClass(this, MactyHojaRuta.class);
        startActivity(intent);
        finish();
    }

    // </editor-fold>
}