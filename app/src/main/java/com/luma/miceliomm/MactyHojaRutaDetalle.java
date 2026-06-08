package com.luma.miceliomm;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
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
    private String idEstado = "3";

    private HojaDeRutaController hojaDeRutaController;
    private HojaRutaDetalleAdapter hojaRutaDetalleAdapter;
    private ArrayList<HojaRutaDetalleModel> hojaRutaDetalleModelArrayList;
    private CardView selectedCard;

    private ArrayList<String> Filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.macty_hoja_ruta_detalle);
        findViewsById();
        actions();
        estadosCard();
        getParametros();
    }

    private void findViewsById(){
        util = new FunctionCustoms();
        Filter = new ArrayList<>();

        ((TextView) findViewById(R.id.lblTituloNavBar)).setText("Hoja De Ruta Detalle");

        //RecyclerView
        ((RecyclerView) findViewById(R.id.grdDatos)).setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ((RecyclerView) findViewById(R.id.grdDatos)).setLayoutManager(llm);

        ((CardView) findViewById(R.id.cvProgramado)).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_select_card)));
        selectedCard = ((CardView) findViewById(R.id.cvProgramado));

        //asignacion adapter
        hojaRutaDetalleModelArrayList = new ArrayList<>();
        hojaRutaDetalleAdapter = new HojaRutaDetalleAdapter(hojaRutaDetalleModelArrayList, this, this);
        hojaDeRutaController = new HojaDeRutaController(this
                , ((RecyclerView) findViewById(R.id.grdDatos))
                , ((LinearLayout) findViewById(R.id.emptyView))
                , hojaRutaDetalleAdapter
                , ((SwipeRefreshLayout) findViewById(R.id.swipeRefresh))
        );

        hojaDeRutaController.setControllerGrd(  ((TextView) findViewById(R.id.lblFechaActualizacion)),
                ((TextView) findViewById(R.id.lblTotalRegistros)) );
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

        ((ImageView) findViewById(R.id.imgvwAbrirBuscador)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vv) {
                visibleFilter( true);
            }
        });

        ((ImageView) findViewById(R.id.imgvwCerrarBuscador)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vv) {
                visibleFilter( false);
            }
        });

        ((SearchView) findViewById(R.id.txtBuscador)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                s = s.trim();

                if (!s.isEmpty()) {
                    addChipToGroup(s);
                    ((SearchView)  findViewById(R.id.txtBuscador)).setQuery("", false);
                }

                return true; // consume el evento

                //return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if( s.length() > 1 && s.charAt(s.length()-1)== 32){
                    addChipToGroup(s.substring(0,s.length()-1));
                    ((SearchView)  findViewById(R.id.txtBuscador)).setQuery("", false);
                }
                //productoAdapter.getFilter().filter(s);
                return false;
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
        ///hojaDeRutaController = new HojaDeRutaController(this);
        HojaRutaResumenModel v =  hojaDeRutaController.selectHojaRutaId(idHojaRuta);
        ((TextView) findViewById(R.id.lblCVidHojaDeRuta))  .setText(String.valueOf(v.idHoraRuta));
        ((TextView) findViewById(R.id.lblCVnombreSectorLogistico)) .setText(v.nombreSectorLogistico);
        ((TextView) findViewById(R.id.lblCVnombrePiloto)) .setText(v.nombrePiloto);
        ((TextView) findViewById(R.id.lblCVfecha)) .setText(v.fecha);
        ((TextView) findViewById(R.id.lblCVTotalBultos)) .setText(String.valueOf(v.totalBultos));
        ((TextView) findViewById(R.id.lblCVnombreEstado)).setText(v.hojaRutaNombreEstado);

        //((TextView) findViewById(R.id.lblCVtelefonoUbicacionDestino)).setText(v.telefonoUbicacionDestino);

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

        ((TextView) findViewById(R.id.lblProgramado)).setText(String.valueOf(
                hojaDeRutaController.selectCountTrasladoLogisticoEstado(String.valueOf(idHojaRuta),"3")));
        ((TextView) findViewById(R.id.lblRuta)).setText(String.valueOf(
                hojaDeRutaController.selectCountTrasladoLogisticoEstado(String.valueOf(idHojaRuta),"4,5")));
        ((TextView) findViewById(R.id.lblEntregado)).setText(String.valueOf(
                hojaDeRutaController.selectCountTrasladoLogisticoEstado(String.valueOf(idHojaRuta),"6,8,9")));
        ((TextView) findViewById(R.id.lblDevuelto)).setText(
                String.valueOf(hojaDeRutaController.selectCountTrasladoLogisticoEstado(String.valueOf(idHojaRuta),"7")));

        buscarDetalle();
    }

    private void estadosCard(){
        ((CardView) findViewById(R.id.cvProgramado)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. desmarcar anterior
                if (selectedCard != null) selectedCard.setBackgroundTintList(null);
                // 2. marcar el pulsado
                idEstado = "3";
                CardView card = (CardView) view;
                card.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_select_card)));
                selectedCard = card;
                buscarDetalle();
            }
        });

        ((CardView) findViewById(R.id.cvRuta)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. desmarcar anterior
                if (selectedCard != null) selectedCard.setBackgroundTintList(null);
                // 2. marcar el pulsado
                idEstado = "4,5";
                CardView card = (CardView) view;
                card.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_select_card)));
                selectedCard = card;
                buscarDetalle();
            }
        });

        ((CardView) findViewById(R.id.cvEntregado)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. desmarcar anterior
                if (selectedCard != null) selectedCard.setBackgroundTintList(null);
                // 2. marcar el pulsado
                idEstado = "6,8,9";
                CardView card = (CardView) view;
                card.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_select_card)));
                selectedCard = card;
                buscarDetalle();
            }
        });

        ((CardView) findViewById(R.id.cvDevuelto)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. desmarcar anterior
                if (selectedCard != null) selectedCard.setBackgroundTintList(null);
                // 2. marcar el pulsado
                idEstado = "7";
                CardView card = (CardView) view;
                card.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_select_card)));
                selectedCard = card;
                buscarDetalle();
            }
        });



    }

    private void visibleFilter(  boolean visible) {
        if (visible) {
            ((LinearLayout) findViewById(R.id.llyFilter)).setVisibility(View.VISIBLE);
            ((SearchView) findViewById(R.id.txtBuscador)).requestFocus();
            ((ImageView) findViewById(R.id.imgvwAbrirBuscador)).setVisibility(View.GONE);
        } else {
            ((LinearLayout) findViewById(R.id.llyFilter)).setVisibility(View.GONE);
            ((SearchView) findViewById(R.id.txtBuscador)).setQuery("", false);
            ((ImageView) findViewById(R.id.imgvwAbrirBuscador)).setVisibility(View.VISIBLE);
        }
    }
    private void addChipToGroup(String filtro) {
        Chip chip = new Chip(this);
        chip.setText(filtro);
        Filter.add(filtro);
        chip.setChipBackgroundColorResource(R.color.color_primary);
        chip.setCloseIconVisible(true);
        chip.setCheckable(false);
        chip.setClickable(true);
        chip.setTextColor(getResources().getColor(R.color.white));
        chip.setChipIconResource(R.drawable.filter_icon);
        chip.setChipIconTintResource(R.color.white);

        ((ChipGroup) findViewById(R.id.chip_group)).addView(chip);
        buscarDetalle();
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ChipGroup) findViewById(R.id.chip_group)).removeView(((Chip) view));
                Filter.remove(((Chip) view).getText());
                buscarDetalle();
            }
        });

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

        hojaDeRutaController.buscarDetalle(idHojaRuta,idEstado,Filter);
        //((TextView) findViewById(R.id.lblFechaActualizacion)).setText(util.getFechaHoraActual());
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

    @Override
    public void onClickTelefono(HojaRutaDetalleAdapter.ItemAdapterViewHolder holder, int position) {
        iniciarLlamada(hojaRutaDetalleAdapter.info.get(position).telefonoUbicacionDestino);
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

    //Rechazo Logistico = 1
    //Aceptacion Logistico = 0
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

    //Inicio de recoleccion paquete
    private void recolectarTraslado(int idHojaDeRuta, int idTraslado, int idTrasladoLogistica){
       // if ( !hojaDeRutaController.existsHojaEnRuta() ) {
        intent = new Intent().setClass(this, MactyRecoleccionTraslado.class);
        intent.putExtra("idHojaRuta",idHojaDeRuta);
        intent.putExtra("idTraslado",idTraslado);
        intent.putExtra("idTrasladoLogistica",idTrasladoLogistica);
        startActivity(intent);
        finish();
        /*}else{
            util.mensaje("Existe Hoja de Traslados pendientes en finalizar, favor de verificar.",this).show();
       }*/
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
       // if (!hojaDeRutaController.existsTrasladoLogisticoNoRecolectado(idHojaRuta)){
            intent = new Intent().setClass(this, MactyActualizarHojaRuta.class);
            intent.putExtra("idHojaRuta",idHojaRuta);
            intent.putExtra("iniciar",1);
            intent.putExtra("finalizar",0);
            startActivity(intent);
            finish();
       // }else{
       //     util.mensaje("Aun hay traslados sin recolectar, para iniciar la ruta, verifique",this).show();
       // }

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
            util.mensaje("Aun hay traslados sin entregar para finalizar la Hoja de Ruta, verifique",this).show();}
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
        //intent = new Intent().setClass(this, MactyHojaRuta.class);
        intent = new Intent().setClass(this, MactyPrincipal.class);
        startActivity(intent);
        finish();
    }

    // </editor-fold>
}