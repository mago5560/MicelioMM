package com.luma.miceliomm.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luma.miceliomm.MactyHojaRuta;
import com.luma.miceliomm.MactyHojaRutaDetalle;
import com.luma.miceliomm.R;
import com.luma.miceliomm.adapter.HojaRutaResumenAdapter;
import com.luma.miceliomm.controller.HojaDeRutaController;
import com.luma.miceliomm.customs.FiltroHojaRutaIntance;
import com.luma.miceliomm.customs.FunctionCustoms;
import com.luma.miceliomm.model.HojaRutaResumenModel;

import java.util.ArrayList;

public class FragmPrincipal extends Fragment implements HojaRutaResumenAdapter.OnItemClickListener {

    private View view;
    private NavController navController;
    private Intent intent;
    private FunctionCustoms util;
    private HojaDeRutaController hojaDeRutaController;
    private HojaRutaResumenAdapter hojaRutaResumenAdapter;
    private ArrayList<HojaRutaResumenModel> hojaRutaResumenModelArrayList;

    private FiltroHojaRutaIntance filtroHojaRutaIntance = FiltroHojaRutaIntance.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragm_principal, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        findViewsById();
        actions();
        buscar();
    }

    private void findViewsById(){
        util = new FunctionCustoms();

        if(!filtroHojaRutaIntance.getFechaInicial().isEmpty()){
            ((TextView)  view.findViewById(R.id.lblFechaInicialBusqueda)).setText(filtroHojaRutaIntance.getFechaInicial());
            ((TextView)  view.findViewById(R.id.lblFechaFinalBusqueda)).setText(filtroHojaRutaIntance.getFechaFinal());
        }else {
            ((TextView) view.findViewById(R.id.lblFechaInicialBusqueda)).setText(util.getFechaActual());
            ((TextView) view.findViewById(R.id.lblFechaFinalBusqueda)).setText(util.getFechaActual());
        }


        //RecyclerView
        ((RecyclerView) view.findViewById(R.id.grdDatos)).setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ((RecyclerView) view.findViewById(R.id.grdDatos)).setLayoutManager(llm);
    }

    private void actions(){

        ((SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buscar();
            }
        });

        ((Button) view.findViewById(R.id.btnBuscar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscar();
            }
        });

        ((TextView)  view.findViewById(R.id.lblFechaInicialBusqueda)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                util.getFechaDialog(getContext(),((TextView) view));
            }
        });

        ((TextView)  view.findViewById(R.id.lblFechaFinalBusqueda)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                util.getFechaDialog(getContext(),((TextView) view));
            }
        });

    }

    private void buscar(){
        filtroHojaRutaIntance.setFechaInicial(((TextView) this.view.findViewById(R.id.lblFechaInicialBusqueda)).getText().toString() );
        filtroHojaRutaIntance.setFechaFinal(((TextView) this.view.findViewById(R.id.lblFechaFinalBusqueda)).getText().toString());

        hojaRutaResumenModelArrayList = new ArrayList<>();
        hojaRutaResumenAdapter = new HojaRutaResumenAdapter(hojaRutaResumenModelArrayList, getContext(), this);
        hojaDeRutaController = new HojaDeRutaController(getContext()
                , ((RecyclerView) view.findViewById(R.id.grdDatos))
                , ((LinearLayout) view.findViewById(R.id.emptyView))
                , hojaRutaResumenAdapter
                , ((SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh))
        );

        hojaDeRutaController.buscar(util.formatDateDB ( ((TextView) view.findViewById(R.id.lblFechaInicialBusqueda)).getText().toString() )
                ,util.formatDateDB ( ((TextView) view.findViewById(R.id.lblFechaFinalBusqueda)).getText().toString() ));
        ((TextView) view.findViewById(R.id.lblFechaActualizacion)).setText(util.getFechaHoraActual());
    }


    @Override
    public void onClick(HojaRutaResumenAdapter.ItemAdapterViewHolder holder, int position) {
        hojaDeRutaDetalle(hojaRutaResumenAdapter.info.get(position).idHoraRuta);
    }

    private void hojaDeRutaDetalle(int idHojaDeRuta){
        intent = new Intent().setClass(getContext(), MactyHojaRutaDetalle.class);
        intent.putExtra("idHojaRuta",idHojaDeRuta);
        startActivity(intent);
        getActivity().finish();
    }



}