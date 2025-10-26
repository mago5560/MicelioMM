package com.luma.miceliomm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
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

import com.luma.miceliomm.adapter.EstadoMaestroAdapter;
import com.luma.miceliomm.adapter.MotivoMaestroAdapter;
import com.luma.miceliomm.adapter.PilotoMaestroAdapter;
import com.luma.miceliomm.adapter.SectorMaestroAdapter;
import com.luma.miceliomm.adapter.VehiculoMaestroAdapter;
import com.luma.miceliomm.controller.EstadoController;
import com.luma.miceliomm.controller.MotivoController;
import com.luma.miceliomm.controller.PilotoController;
import com.luma.miceliomm.controller.SectorController;
import com.luma.miceliomm.controller.UsuarioController;
import com.luma.miceliomm.controller.VehiculoController;
import com.luma.miceliomm.customs.FunctionCustoms;
import com.luma.miceliomm.model.EstadoModel;
import com.luma.miceliomm.model.MotivoModel;
import com.luma.miceliomm.model.PilotoModel;
import com.luma.miceliomm.model.SectorModel;
import com.luma.miceliomm.model.VehiculoModel;

import java.util.ArrayList;

public class MactyMaestros extends AppCompatActivity implements
        VehiculoMaestroAdapter.OnItemClickListener,
        EstadoMaestroAdapter.OnItemClickListener,
        PilotoMaestroAdapter.OnItemClickListener,
        MotivoMaestroAdapter.OnItemClickListener,
        SectorMaestroAdapter.OnItemClickListener {
    private Intent intent;
    private FunctionCustoms util;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.macty_maestros);

        findViewsById();
        actions();


    }

    private void findViewsById() {
        util = new FunctionCustoms();
        ((TextView) findViewById(R.id.lblTituloNavBar)).setText("Maestros");
        sectorController = new SectorController(this);
        motivoController = new MotivoController(this);
        vehiculoController = new VehiculoController(this);
        estadoController = new EstadoController(this);
        pilotoController = new PilotoController(this);


        //Fill Totales
        ((TextView) findViewById(R.id.lblCVTotalSector)).setText("Total: " + sectorController.totalRegistros());
        ((TextView) findViewById(R.id.lblCVTotalMotivo)).setText("Total: " + motivoController.totalRegistros());
        ((TextView) findViewById(R.id.lblCVTotalVehiculos)).setText("Total: " + vehiculoController.totalRegistros());
        ((TextView) findViewById(R.id.lblCVTotalEstados)).setText("Total: " + estadoController.totalRegistros());
        ((TextView) findViewById(R.id.lblCVTotalPilotos)).setText("Total: " + pilotoController.totalRegistros());
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


        ((ImageView) findViewById(R.id.imgvwDescargarVehiculos)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descargarVehiculos();
            }
        });
        ((ImageView) findViewById(R.id.imgvwVerVehiculos)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogVehiculo();
            }
        });


        ((ImageView) findViewById(R.id.imgvwDescargarEstados)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descargarEstado();
            }
        });
        ((ImageView) findViewById(R.id.imgvwVerEstados)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEstado();
            }
        });



        ((ImageView) findViewById(R.id.imgvwDescargarPilotos)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descargarPiloto();
            }
        });
        ((ImageView) findViewById(R.id.imgvwVerPilotos)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPiloto();
            }
        });


        ((ImageView) findViewById(R.id.imgvwDescargarMotivos)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descargarMotivo();
            }
        });
        ((ImageView) findViewById(R.id.imgvwVerMotivos)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMotivo();
            }
        });


        ((ImageView) findViewById(R.id.imgvwDescargarSector)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descargarSector();
            }
        });
        ((ImageView) findViewById(R.id.imgvwVerSector)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSector();
            }
        });

    }

    // <editor-fold defaultstate="collapsed" desc="Maestro Vehiculos">
    private void descargarVehiculos() {
        vehiculoController = new VehiculoController(this);
        vehiculoController.getVehiculos(((TextView) findViewById(R.id.lblCVTotalVehiculos)));

    }

    private VehiculoController vehiculoController;

    private AlertDialog alertDialogVehiculoMaestro;
    private VehiculoMaestroAdapter vehiculoMaestroAdapter;
    private ArrayList<VehiculoModel> vehiculoModelArrayList;

    private void dialogVehiculo() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        final LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.content_datos_maestros, null);
        dialogVehiculoFindViewsByIds(dialogLayout);
        dialogVehiculoActions(dialogLayout);
        buscarVehiculo(dialogLayout);
        builder.setTitle("Registros Existentes");

        builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setView(dialogLayout);
        alertDialogVehiculoMaestro = builder.show();
    }


    private void dialogVehiculoFindViewsByIds(View v) {
        //RecyclerView
        ((RecyclerView) v.findViewById(R.id.grdDatos)).setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ((RecyclerView) v.findViewById(R.id.grdDatos)).setLayoutManager(llm);
        ((TextView) v.findViewById(R.id.lblTituloNavBar)).setText("");
    }

    private void dialogVehiculoActions(View v) {
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
                vehiculoMaestroAdapter.getFilter().filter(s);
                return false;
            }
        });

        ((SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buscarVehiculo(v);
            }
        });
    }

    @Override
    public void onClick(VehiculoMaestroAdapter.ItemAdapterViewHolder holder, int position) {

    }

    private void buscarVehiculo(View v) {
        vehiculoModelArrayList = new ArrayList<>();
        vehiculoMaestroAdapter = new VehiculoMaestroAdapter(vehiculoModelArrayList, this, this);
        vehiculoController = new VehiculoController(this
                , ((RecyclerView) v.findViewById(R.id.grdDatos))
                , vehiculoMaestroAdapter
                , ((SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh))
                , ((LinearLayout) v.findViewById(R.id.emptyView))
        );

        vehiculoController.buscar();
        ((TextView) v.findViewById(R.id.lblFechaActualizacion)).setText(util.getFechaHoraActual());
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Maestro Estado">
    private EstadoController estadoController;

    private void descargarEstado() {
        estadoController = new EstadoController(this);
        estadoController.getDatos(((TextView) findViewById(R.id.lblCVTotalEstados)));
    }

    AlertDialog alertDialogEstadoMaestro;
    EstadoMaestroAdapter estadoMaestroAdapter;
    ArrayList<EstadoModel> estadoModelArrayList;

    private void dialogEstado() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        final LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.content_datos_maestros, null);
        dialogEstadoFindViewsByIds(dialogLayout);
        dialogEstadoActions(dialogLayout);
        buscarEstado(dialogLayout);
        builder.setTitle("Registros Existentes");

        builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setView(dialogLayout);
        alertDialogEstadoMaestro = builder.show();
    }


    private void dialogEstadoFindViewsByIds(View v) {
        //RecyclerView
        ((RecyclerView) v.findViewById(R.id.grdDatos)).setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ((RecyclerView) v.findViewById(R.id.grdDatos)).setLayoutManager(llm);
        ((TextView) v.findViewById(R.id.lblTituloNavBar)).setText("");
    }

    private void dialogEstadoActions(View v) {
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
                estadoMaestroAdapter.getFilter().filter(s);
                return false;
            }
        });

        ((SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buscarEstado(v);
            }
        });
    }

    @Override
    public void onClick(EstadoMaestroAdapter.ItemAdapterViewHolder holder, int position) {

    }

    private void buscarEstado(View v) {
        estadoModelArrayList = new ArrayList<>();
        estadoMaestroAdapter = new EstadoMaestroAdapter(estadoModelArrayList, this, this);
        estadoController = new EstadoController(this
                , ((RecyclerView) v.findViewById(R.id.grdDatos))
                , estadoMaestroAdapter
                , ((SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh))
                , ((LinearLayout) v.findViewById(R.id.emptyView))
        );

        estadoController.buscar();
        ((TextView) v.findViewById(R.id.lblFechaActualizacion)).setText(util.getFechaHoraActual());
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Maestro Pilotos">
    private PilotoController pilotoController;

    private void descargarPiloto() {
        pilotoController = new PilotoController(this);
        pilotoController.getDatos(((TextView) findViewById(R.id.lblCVTotalPilotos)));
    }

    AlertDialog alertDialogPilotoMaestro;
    PilotoMaestroAdapter pilotoMaestroAdapter;
    ArrayList<PilotoModel> pilotoModelArrayList;

    private void dialogPiloto() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        final LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.content_datos_maestros, null);
        dialogPilotoFindViewsByIds(dialogLayout);
        dialogPilotoActions(dialogLayout);
        buscarPiloto(dialogLayout);
        builder.setTitle("Registros Existentes");

        builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setView(dialogLayout);
        alertDialogPilotoMaestro = builder.show();
    }


    private void dialogPilotoFindViewsByIds(View v) {
        //RecyclerView
        ((RecyclerView) v.findViewById(R.id.grdDatos)).setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ((RecyclerView) v.findViewById(R.id.grdDatos)).setLayoutManager(llm);
        ((TextView) v.findViewById(R.id.lblTituloNavBar)).setText("");
    }

    private void dialogPilotoActions(View v) {
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
                pilotoMaestroAdapter.getFilter().filter(s);
                return false;
            }
        });

        ((SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buscarPiloto(v);
            }
        });
    }

    @Override
    public void onClick(PilotoMaestroAdapter.ItemAdapterViewHolder holder, int position) {

    }

    private void buscarPiloto(View v) {
        pilotoModelArrayList = new ArrayList<>();
        pilotoMaestroAdapter = new PilotoMaestroAdapter(pilotoModelArrayList, this, this);
        pilotoController = new PilotoController(this
                , ((RecyclerView) v.findViewById(R.id.grdDatos))
                , pilotoMaestroAdapter
                , ((SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh))
                , ((LinearLayout) v.findViewById(R.id.emptyView))
        );

        pilotoController.buscar();
        ((TextView) v.findViewById(R.id.lblFechaActualizacion)).setText(util.getFechaHoraActual());
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Maestro Motivos">
    private MotivoController motivoController;

    private void descargarMotivo() {
        motivoController = new MotivoController(this);
        motivoController.getDatos(((TextView) findViewById(R.id.lblCVTotalMotivo)));
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

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Maestro Sector">
    private SectorController sectorController;

    private void descargarSector() {
        sectorController = new SectorController(this);
        sectorController.getDatos(((TextView) findViewById(R.id.lblCVTotalSector)));

    }

    AlertDialog alertDialogSectorMaestro;
    SectorMaestroAdapter sectorMaestroAdapter;
    ArrayList<SectorModel> sectorModelArrayList;

    private void dialogSector() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        final LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.content_datos_maestros, null);
        dialogSectorFindViewsByIds(dialogLayout);
        dialogSectorActions(dialogLayout);
        buscarSector(dialogLayout);
        builder.setTitle("Registros Existentes");

        builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setView(dialogLayout);
        alertDialogSectorMaestro = builder.show();
    }


    private void dialogSectorFindViewsByIds(View v) {
        //RecyclerView
        ((RecyclerView) v.findViewById(R.id.grdDatos)).setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ((RecyclerView) v.findViewById(R.id.grdDatos)).setLayoutManager(llm);
        ((TextView) v.findViewById(R.id.lblTituloNavBar)).setText("");
    }

    private void dialogSectorActions(View v) {
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
                sectorMaestroAdapter.getFilter().filter(s);
                return false;
            }
        });

        ((SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buscarSector(v);
            }
        });
    }

    @Override
    public void onClick(SectorMaestroAdapter.ItemAdapterViewHolder holder, int position) {

    }

    private void buscarSector(View v) {
        sectorModelArrayList = new ArrayList<>();
        sectorMaestroAdapter = new SectorMaestroAdapter(sectorModelArrayList, this, this);
        sectorController = new SectorController(this
                , ((RecyclerView) v.findViewById(R.id.grdDatos))
                , sectorMaestroAdapter
                , ((SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh))
                , ((LinearLayout) v.findViewById(R.id.emptyView))
        );

        sectorController.buscar();
        ((TextView) v.findViewById(R.id.lblFechaActualizacion)).setText(util.getFechaHoraActual());
    }

    // </editor-fold>
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

    // <editor-fold defaultstate="collapsed" desc="Menu, Opciones y regresar a principal">

    private void goHome() {
        intent = new Intent().setClass(this, MactyPrincipal.class);
        startActivity(intent);
        finish();
    }

    // </editor-fold>

}