package com.luma.miceliomm.customs;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.luma.miceliomm.R;
import com.luma.miceliomm.controller.HojaDeRutaController;

import java.util.ArrayList;

public class FiltroPrincipalCustoms extends BottomSheetDialogFragment {

    private FunctionCustoms util;
    private HojaDeRutaController hojaDeRutaController;

    private FiltroHojaRutaIntance filtroHojaRutaIntance = FiltroHojaRutaIntance.getInstance();

    private View view;

    ArrayList<String> Filter ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.item_filtro_traslado, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
            );
            getDialog().setCancelable(false);
        }

        findViewsById();
        actions();

    }


    public void setDependencias(HojaDeRutaController hojaDeRutaController){
        this.hojaDeRutaController = hojaDeRutaController;
    }

    private void findViewsById(){
        util = new FunctionCustoms();
        Filter = new ArrayList<>();

        if(!filtroHojaRutaIntance.getFechaInicial().isEmpty()){
            ((TextView)  view.findViewById(R.id.txtFechaInicialFiltro)).setText(filtroHojaRutaIntance.getFechaInicial());
            ((TextView)  view.findViewById(R.id.txtFechaFinalFiltro)).setText(filtroHojaRutaIntance.getFechaFinal());
            ((TextView)  view.findViewById(R.id.txtSectorFiltro)).setText(filtroHojaRutaIntance.getSectorLogistico());
        }else {
            ((TextView) view.findViewById(R.id.txtFechaInicialFiltro)).setText(util.getFechaActual());
            ((TextView) view.findViewById(R.id.txtFechaFinalFiltro)).setText(util.getFechaActual());
        }

        for (String child: filtroHojaRutaIntance.getFilter()) {
            addChipToGroup(child, view);
        }

    }

    private void actions () {
        ((ImageView) view.findViewById(R.id.imgvwCerrarSheet)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        ((Button) view.findViewById(R.id.btnLimpiar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiarCampos();
            }
        });
        ((Button) view.findViewById(R.id.btnAplicarFiltro)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aplicarFiltros();
            }
        });

        ((EditText) view.findViewById(R.id.txtFechaInicialFiltro)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                util.getFechaDialog(getContext(),((EditText) view));
            }
        });
        ((EditText) view.findViewById(R.id.txtFechaFinalFiltro)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                util.getFechaDialog(getContext(),((EditText) view));
            }
        });



        ((EditText) view.findViewById(R.id.txtBuscarPor)).setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_SEARCH) {

                String s = v.getText().toString().trim();

                if (!s.isEmpty()) {
                    addChipToGroup(s, view);
                    v.setText("");
                    v.requestFocus();
                }

                return true;
            }

            return false;

        });

    }

    private void limpiarCampos(){
        ((EditText) view.findViewById(R.id.txtFechaInicialFiltro)).setText(util.getFechaActual());
        ((EditText) view.findViewById(R.id.txtFechaFinalFiltro)).setText(util.getFechaActual());
        ((EditText) view.findViewById(R.id.txtSectorFiltro)).setText("");
        Filter = new ArrayList<>();
        ((ChipGroup) view.findViewById(R.id.chip_group)).removeAllViews();
        buscar();
        dismiss();
    }

    private void aplicarFiltros(){
        buscar();
        dismiss();
    }

    private void buscar(){
        saveFilter();

        hojaDeRutaController.buscar(util.formatDateDB ( filtroHojaRutaIntance.getFechaInicial() )
                ,util.formatDateDB ( filtroHojaRutaIntance.getFechaFinal() )
                ,filtroHojaRutaIntance.getSectorLogistico()
                ,filtroHojaRutaIntance.getFilter());
       // ((TextView) view.findViewById(R.id.lblFechaActualizacion)).setText(util.getFechaHoraActual());

    }
    private void saveFilter(){
        filtroHojaRutaIntance.setFechaInicial(((TextView) this.view.findViewById(R.id.txtFechaInicialFiltro)).getText().toString() );
        filtroHojaRutaIntance.setFechaFinal(((TextView) this.view.findViewById(R.id.txtFechaFinalFiltro)).getText().toString());
        filtroHojaRutaIntance.setSectorLogistico(((EditText) view.findViewById(R.id.txtSectorFiltro)).getText().toString());
        filtroHojaRutaIntance.setFilter(Filter);
    }

    private void addChipToGroup(String filtro, View v) {
        Chip chip = new Chip(v.getContext());
        chip.setText(filtro);
        Filter.add(filtro);
        chip.setChipBackgroundColorResource(R.color.color_primary);
        chip.setCloseIconVisible(true);
        chip.setCheckable(false);
        chip.setClickable(true);
        chip.setTextColor(getResources().getColor(R.color.white));
        chip.setChipIconResource(R.drawable.filter_icon);
        chip.setChipIconTintResource(R.color.white);

        ((ChipGroup) v.findViewById(R.id.chip_group)).addView(chip);
        //aplicarFiltros();
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ChipGroup) v.findViewById(R.id.chip_group)).removeView(((Chip) view));
                Filter.remove(((Chip) view).getText());
          //      aplicarFiltros();
            }
        });

    }


}
