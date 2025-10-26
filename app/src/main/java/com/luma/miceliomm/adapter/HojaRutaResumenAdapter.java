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
import com.luma.miceliomm.model.HojaRutaResumenModel;
import java.util.ArrayList;
import java.util.List;

public class HojaRutaResumenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    public List<HojaRutaResumenModel> info;
    public List<HojaRutaResumenModel> aux;
    private final Context context;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;

    private static OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        public void onClick(ItemAdapterViewHolder holder, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public HojaRutaResumenAdapter(List<HojaRutaResumenModel> info, Context context,  OnItemClickListener mItemClickListener) {
        this.info = info;
        this.aux = new ArrayList<>(info);
        this.context = context;
        this.mItemClickListener = mItemClickListener;
    }

    public void setInfo(List<HojaRutaResumenModel> info) {
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
        View tarjeta = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hoja_ruta_resumen, parent, false);
        return new ItemAdapterViewHolder(tarjeta);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemAdapterViewHolder) {
            final HojaRutaResumenModel cls = info.get(position);
            ItemAdapterViewHolder holderItem = (ItemAdapterViewHolder) holder;
            holderItem.lblCVidHojaDeRuta.setText(String.valueOf(cls.idHoraRuta));
            holderItem.lblCVnombreSectorLogistico.setText(cls.nombreSectorLogistico);
            holderItem.lblCVnombrePiloto.setText(cls.nombrePiloto);
            holderItem.lblCVfecha.setText(cls.fecha);
            holderItem.lblCVTotalBultos.setText(String.valueOf(cls.totalBultos));

            holderItem.lblCVnombreEstado.setText(cls.hojaRutaNombreEstado);

            //ConfiguracionDeColor
            configurarEstado(holderItem.lblCVnombreEstado,cls.hojaDeRutaEstado, holderItem.llyCVColores);
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
        private TextView lblCVidHojaDeRuta,lblCVnombreSectorLogistico, lblCVnombrePiloto,lblCVfecha,lblCVTotalBultos;
        private CardView cv;

        private TextView lblCVnombreEstado;
        private LinearLayout llyCVColores;

        public ItemAdapterViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            lblCVidHojaDeRuta = itemView.findViewById(R.id.lblCVidHojaDeRuta);
            lblCVnombreSectorLogistico = itemView.findViewById(R.id.lblCVnombreSectorLogistico);
            lblCVnombrePiloto = itemView.findViewById(R.id.lblCVnombrePiloto);
            lblCVfecha = itemView.findViewById(R.id.lblCVfecha);
            lblCVTotalBultos = itemView.findViewById(R.id.lblCVTotalBultos);

            lblCVnombreEstado = itemView.findViewById(R.id.lblCVnombreEstado);
            llyCVColores = itemView.findViewById(R.id.llyCVColores);
            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                if (v == cv) {
                    mItemClickListener.onClick(this, getLayoutPosition());
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
            List<HojaRutaResumenModel> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(aux);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (HojaRutaResumenModel item : aux) {
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
