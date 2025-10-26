package com.luma.miceliomm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.luma.miceliomm.R;
import com.luma.miceliomm.model.HojaRutaDetalleModel;
import com.luma.miceliomm.model.HojaRutaResumenModel;

import java.util.ArrayList;
import java.util.List;

public class HojaRutaDetalleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    public List<HojaRutaDetalleModel> info;
    public List<HojaRutaDetalleModel> aux;
    private final Context context;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;

    private static OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        public void onClick(ItemAdapterViewHolder holder, int position);
        public void onClickTrasladoLogistico(ItemAdapterViewHolder holder, int position);

        public void onClickAgregarImagenPaquete(ItemAdapterViewHolder holder, int position);
        public void onClickIniciarPaquete(ItemAdapterViewHolder holder, int position);
        public void onClickRechazoTrasladoLogistico(ItemAdapterViewHolder holder,int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public HojaRutaDetalleAdapter(List<HojaRutaDetalleModel> info, Context context,  OnItemClickListener mItemClickListener) {
        this.info = info;
        this.aux = new ArrayList<>(info);
        this.context = context;
        this.mItemClickListener = mItemClickListener;
    }

    public void setInfo(List<HojaRutaDetalleModel> info) {
        this.info = info;
        this.aux = new ArrayList<>(info);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            //Inflating footer view
            View tarjeta = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view_footer, parent, false);
            return new FooterViewHolder(tarjeta);
        }
        View tarjeta = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paquete, parent, false);
        return new ItemAdapterViewHolder(tarjeta);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemAdapterViewHolder) {
            final HojaRutaDetalleModel cls = info.get(position);
            ItemAdapterViewHolder holderItem = (ItemAdapterViewHolder) holder;
            holderItem.lblCVIdTrasladoLogistica.setText(String.valueOf(cls.idTrasladoLogistica));
            holderItem.lblCVobservaciones.setText(cls.observaciones);
            holderItem.lblCVReferencia.setText(String.valueOf(cls.referencia));
            holderItem.lblCVDireccionEntregaTraslado.setText(cls.direccionEntregaTraslado);

            holderItem.lblCVnombreEstado.setText(cls.nombreEstado);
            holderItem.lblCVTotalBultos.setText(String.valueOf(cls.totalBultos));

            if (cls.idEstado <= 3){
                    holderItem.lblIniciarPaquete.setVisibility(View.VISIBLE);
                    holderItem.llyTrasladoLogistico.setVisibility(View.GONE);
            }else if (cls.idEstado  == 4 || cls.idEstado  == 5 && cls.hojaDeRutaEstado == 5){
                holderItem.lblIniciarPaquete.setVisibility(View.GONE);
                holderItem.llyTrasladoLogistico.setVisibility(View.VISIBLE);
            }




            //ConfiguracionDeColor
            configurarEstado(holderItem.lblCVnombreEstado,cls.idEstado, holderItem.llyCVColores);

        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            footerHolder.footerText.setText("No se encontraron mas registros...");
        }
    }

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

        textView.setBackgroundColor(ContextCompat.getColor(context, colorRes));
        textView.setTextColor( ContextCompat.getColor(context, colorText));
        linearLayout.setBackgroundColor(ContextCompat.getColor(context,colorRes));
    }

    //Si se utiliza esta fucion tomar en cuenta que se agrega size +1 por el footer.
    @Override
    public int getItemCount() {
        if (info.size() == 0) {
            return 0;
        } else
            return info.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == info.size()) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    public class ItemAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView lblCVIdTrasladoLogistica,lblCVobservaciones, lblCVReferencia,lblCVDireccionEntregaTraslado,lblCVTotalBultos,lblCVnombreEstado;
        private TextView lblIrARechazarTraslado,lblIrAEntregarTraslado,lblAgregarImagen,lblIniciarPaquete;
        private LinearLayout llyCVColores,llyTrasladoLogistico;
        private CardView cv;


        public ItemAdapterViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            lblCVIdTrasladoLogistica = itemView.findViewById(R.id.lblCVIdTrasladoLogistica);
            lblCVobservaciones = itemView.findViewById(R.id.lblCVobservaciones);
            lblCVReferencia = itemView.findViewById(R.id.lblCVReferencia);
            lblCVDireccionEntregaTraslado = itemView.findViewById(R.id.lblCVDireccionEntregaTraslado);
            lblCVTotalBultos = itemView.findViewById(R.id.lblCVTotalBultos);
            lblCVnombreEstado = itemView.findViewById(R.id.lblCVnombreEstado);
            lblIrARechazarTraslado = itemView.findViewById(R.id.lblIrARechazarTraslado);
            lblIrAEntregarTraslado = itemView.findViewById(R.id.lblIrAEntregarTraslado);
            lblAgregarImagen = itemView.findViewById(R.id.lblAgregarImagen);

            lblIniciarPaquete = itemView.findViewById(R.id.lblIniciarPaquete);
            llyCVColores = itemView.findViewById(R.id.llyCVColores);
            llyTrasladoLogistico = itemView.findViewById(R.id.llyTrasladoLogistico);

            cv.setOnClickListener(this);
            lblIrAEntregarTraslado.setOnClickListener(this);
            lblIrARechazarTraslado.setOnClickListener(this);
            lblIniciarPaquete.setOnClickListener(this);
            lblAgregarImagen.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                if (v == cv) {
                    mItemClickListener.onClick(this, getLayoutPosition());
                }else if (v == lblIrAEntregarTraslado){
                    mItemClickListener.onClickTrasladoLogistico(this,getLayoutPosition());
                }else if (v == lblIrARechazarTraslado){
                    mItemClickListener.onClickRechazoTrasladoLogistico(this,getLayoutPosition());
                }else if (v == lblAgregarImagen){
                    mItemClickListener.onClickAgregarImagenPaquete(this,getLayoutPosition());
                }else if (v == lblIniciarPaquete){
                    mItemClickListener.onClickIniciarPaquete(this,getLayoutPosition());
                }
            }
        }

    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView footerText;

        public FooterViewHolder(View view) {
            super(view);
            footerText = (TextView) view.findViewById(R.id.footer_text);
        }
    }




    // <editor-fold defaultstate="collapsed" desc="(FILTER GRD)">
    @Override
    public Filter getFilter() {
        return getBuscar;
    }

    private Filter getBuscar = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<HojaRutaDetalleModel> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(aux);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (HojaRutaDetalleModel item : aux) {
                    if (item.toString().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            info.clear();
            info.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    // </editor-fold>
}
