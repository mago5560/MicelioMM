package com.luma.miceliomm.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luma.miceliomm.MactyHojaRuta;
import com.luma.miceliomm.MactyMaestros;
import com.luma.miceliomm.MactyUpdate;
import com.luma.miceliomm.R;
import com.luma.miceliomm.adapter.MenuAdapter;
import com.luma.miceliomm.controller.HojaDeRutaController;
import com.luma.miceliomm.controller.MenuController;
import com.luma.miceliomm.controller.UsuarioController;
import com.luma.miceliomm.customs.FunctionCustoms;
import com.luma.miceliomm.customs.GlobalCustoms;
import com.luma.miceliomm.model.MenuModel;

import java.util.ArrayList;


public class FragmOpciones extends Fragment  implements MenuAdapter.OnItemClickListener{

    private Intent intent;
    private FunctionCustoms util;

    GlobalCustoms vars = GlobalCustoms.getInstance();

    private View view;

    private NavController navController;

    private MenuAdapter menuAdapter;
    private ArrayList<MenuModel> menuClassAList;
    private MenuController menuControler;

    private UsuarioController usuarioControler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragm__opciones, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        findViewsByIds();
        actions();
        getMenu();
    }

    private void findViewsByIds() {
        util = new FunctionCustoms();
        this.usuarioControler = new UsuarioController(getContext());

        ((TextView)  view.findViewById(R.id.lblUsuario)).setText("Usuario: "+usuarioControler.getUsuario().getUsuario());
        //((TextView)  view.findViewById(R.id.lblNombreUsuario)).setText(usuarioControler.getUsuario().getNombre());
        ((TextView) view.findViewById(R.id.lblNombreUsuario)).setVisibility(View.GONE);
        ((TextView)  view.findViewById(R.id.lblVersion)).setText(util.getVersion(getContext()));
        ((TextView)  view.findViewById(R.id.lblVersionDB)).setText(util.getVersionDB(getContext()));
    }



    private void actions() {

    }

    private void getMenu() {
        menuClassAList = new ArrayList<>();
        menuAdapter = new MenuAdapter(menuClassAList, getContext(), this);
        menuControler = new MenuController(((RecyclerView)  view.findViewById(R.id.grdDatos))
                , menuAdapter
                , getContext());
        menuControler.fillOpciones();
    }


    @Override
    public void onClickCV(MenuAdapter.ItemAdapterViewHolder holder, int position) {
        switch (menuAdapter.info.get(position).getId()) {
            case 1:
                hojaDeRuta();
                break;
            case 2:
                maestros();
                break;
            case 3:
                actualizarAplicacion();
                break;
            case 4:
                compartirAplicacion();
                break;
            case 5:
                descargarRuta();
                break;
            case 0:
                usuarioControler.setLogout();
                break;
            default:
                util.mensaje("Opcion en Mantenimiento", getActivity()).show();
        }
    }
    private void actualizarAplicacion() {
        intent = new Intent().setClass(getContext(), MactyUpdate.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void compartirAplicacion() {
        intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, vars.getLINK_APP());
        startActivity(Intent.createChooser(intent, "Compartir " + vars.getNAME_APP()));
    }

    private void maestros(){
        intent = new Intent().setClass(getContext(), MactyMaestros.class);
        startActivity(intent);
        getActivity(). finish();
    }

    private void hojaDeRuta(){
        intent = new Intent().setClass(getContext(), MactyHojaRuta.class);
        startActivity(intent);
        getActivity().finish();
    }
    private void descargarRuta(){
        HojaDeRutaController controller = new HojaDeRutaController(getContext());
        controller.dialogDescargaRuta();
    }


}