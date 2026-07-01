package com.luma.miceliomm.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.luma.miceliomm.adapter.TipoDocumentoAdapter;
import com.luma.miceliomm.customs.DbHandler;
import com.luma.miceliomm.customs.FunctionCustoms;
import com.luma.miceliomm.customs.GlobalCustoms;
import com.luma.miceliomm.daos.MotivoDao;
import com.luma.miceliomm.daos.TipoDocumentoDao;

import java.util.ArrayList;


public class TipoDocumentoController {

    private Context context;
    private GlobalCustoms var = GlobalCustoms.getInstance();
    private FunctionCustoms util;
    private DbHandler dbHandler;
    private TipoDocumentoDao dao;
    private RecyclerView recyclerView;
    private LinearLayout recyclerViewEmpty;
    private TipoDocumentoAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public TipoDocumentoController(Context context) {
        this.context = context;
        this.util = new FunctionCustoms();
        this.dbHandler = new DbHandler(context, null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        this.dao = new TipoDocumentoDao(db);
    }

    public TipoDocumentoController(Context context, RecyclerView recyclerView, TipoDocumentoAdapter adapter,  SwipeRefreshLayout swipeRefreshLayout, LinearLayout recyclerViewEmpty) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.recyclerViewEmpty = recyclerViewEmpty;
        this.adapter = adapter;
        this.swipeRefreshLayout = swipeRefreshLayout;

        this.util = new FunctionCustoms();
        this.dbHandler = new DbHandler(context, null, null, 1);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        this.dao = new TipoDocumentoDao(db);
    }


    private AsyncBuscar asyncBuscar;

    public void buscar() {
        recyclerView.setScrollingTouchSlop(0);
        asyncBuscar = new AsyncBuscar();
        asyncBuscar.execute();
    }

    private class AsyncBuscar extends AsyncTask<String, Void, ArrayList> {
        private ProgressDialog pDialog;

        @Override
        protected ArrayList doInBackground(String... strings) {
            return dao.select();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(context, "Consultando...", "Espere....", true, true);
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            super.onPostExecute(arrayList);
            viewConsulta(arrayList);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }

    private void viewConsulta(ArrayList arrayList) {
        adapter.setInfo(arrayList);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
        if (arrayList.isEmpty()) {
            //util.msgToast("No se encontraron registros", ((Activity) context));
            recyclerView.setVisibility(View.GONE);
            recyclerViewEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerViewEmpty.setVisibility(View.GONE);
        }
    }
}
