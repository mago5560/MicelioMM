package com.luma.miceliomm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.luma.miceliomm.adapter.HojaRutaResumenAdapter;
import com.luma.miceliomm.controller.HojaDeRutaController;
import com.luma.miceliomm.customs.FunctionCustoms;
import com.luma.miceliomm.model.HojaRutaResumenModel;

import java.util.ArrayList;

public class MactyHojaRuta extends AppCompatActivity implements HojaRutaResumenAdapter.OnItemClickListener {

    private  Intent intent;
    private FunctionCustoms util;

    private HojaDeRutaController hojaDeRutaController;
    private HojaRutaResumenAdapter hojaRutaResumenAdapter;
   private ArrayList<HojaRutaResumenModel> hojaRutaResumenModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.macty_hoja_ruta);

        findViewsById();
        actions();
        buscar();
    }

    private void findViewsById(){
        util = new FunctionCustoms();
        ((TextView) findViewById(R.id.lblTituloNavBar)).setText("Hoja De Ruta");

        /*
        if(!filtroHojaRutaIntance.getFechaInicial().isEmpty()){
            ((TextView)  findViewById(R.id.lblFechaInicialBusqueda)).setText(filtroHojaRutaIntance.getFechaInicial());
            ((TextView)  findViewById(R.id.lblFechaFinalBusqueda)).setText(filtroHojaRutaIntance.getFechaFinal());
            ((Chip)  findViewById(R.id.chipTransferidos)).setChecked(filtroHojaRutaIntance.isTransferido());
            ((Chip)  findViewById(R.id.chipSinTransferir)).setChecked(filtroHojaRutaIntance.isSinTransferir());
        }else {
            ((TextView) findViewById(R.id.lblFechaInicialBusqueda)).setText(util.getFechaActual());
            ((TextView) findViewById(R.id.lblFechaFinalBusqueda)).setText(util.getFechaActual());
        }


         */

        ((TextView) findViewById(R.id.lblFechaInicialBusqueda)).setText(util.getFechaActual());
        ((TextView) findViewById(R.id.lblFechaFinalBusqueda)).setText(util.getFechaActual());


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
                buscar();
            }
        });

        ((Button) findViewById(R.id.btnBuscar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscar();
            }
        });

        ((TextView)  findViewById(R.id.lblFechaInicialBusqueda)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                util.getFechaDialog(MactyHojaRuta.this,((TextView) view));
            }
        });

        ((TextView)  findViewById(R.id.lblFechaFinalBusqueda)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                util.getFechaDialog(MactyHojaRuta.this,((TextView) view));
            }
        });

    }

    private void buscar(){
        hojaRutaResumenModelArrayList = new ArrayList<>();
        hojaRutaResumenAdapter = new HojaRutaResumenAdapter(hojaRutaResumenModelArrayList, this, this);
        hojaDeRutaController = new HojaDeRutaController(this
                , ((RecyclerView) findViewById(R.id.grdDatos))
                , ((LinearLayout) findViewById(R.id.emptyView))
                , hojaRutaResumenAdapter
                , ((SwipeRefreshLayout) findViewById(R.id.swipeRefresh))
        );

        hojaDeRutaController.buscar(util.formatDateDB ( ((TextView) findViewById(R.id.lblFechaInicialBusqueda)).getText().toString() )
                ,util.formatDateDB ( ((TextView) findViewById(R.id.lblFechaFinalBusqueda)).getText().toString() ));
        ((TextView) findViewById(R.id.lblFechaActualizacion)).setText(util.getFechaHoraActual());
    }

    @Override
    public void onClick(HojaRutaResumenAdapter.ItemAdapterViewHolder holder, int position) {
        hojaDeRutaDetalle(hojaRutaResumenAdapter.info.get(position).idHoraRuta);
    }

    private void hojaDeRutaDetalle(int idHojaDeRuta){
        intent = new Intent().setClass(this, MactyHojaRutaDetalle.class);
        intent.putExtra("idHojaRuta",idHojaDeRuta);
        startActivity(intent);
        finish();
    }

    // <editor-fold defaultstate="collapsed" desc="Menu, Opciones y regresar a principal">
    private void goHome() {
        intent = new Intent().setClass(this, MactyPrincipal.class);
        startActivity(intent);
        finish();
    }
    // </editor-fold>

}