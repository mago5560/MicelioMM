package com.luma.miceliomm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.luma.miceliomm.R;
import com.luma.miceliomm.model.MotivoModel;

import java.util.ArrayList;
import java.util.List;

public class MotivoMaestroAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    public List<MotivoModel> info;
    public List<MotivoModel> aux;
    private final Context context;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;

    private static OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        public void onClick(ItemAdapterViewHolder holder, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public MotivoMaestroAdapter(List<MotivoModel> info, Context context,  OnItemClickListener mItemClickListener) {
        this.info = info;
        this.aux = new ArrayList<>(info);
        this.context = context;
        this.mItemClickListener = mItemClickListener;
    }

    public void setInfo(List<MotivoModel> info) {
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
        View tarjeta = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_maestro, parent, false);
        return new ItemAdapterViewHolder(tarjeta);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemAdapterViewHolder) {
            final MotivoModel cls = info.get(position);
            ItemAdapterViewHolder holderItem = (ItemAdapterViewHolder) holder;
            holderItem.lblCVIdMaestro.setText(String.valueOf(cls.IdMotivoDeRechazo));
            holderItem.lblCVTitulo.setText("Descripcion");
            holderItem.lblCVTituloDescripcion.setText(cls.Descripcion);
            holderItem.lblCVSubTitulo.setVisibility(View.GONE);
            holderItem.lblCVSubTituloDescripcion.setVisibility(View.GONE);

        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            footerHolder.footerText.setText("No se encontraron mas registros...");
        }
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
        public TextView lblCVIdMaestro,lblCVTitulo, lblCVTituloDescripcion,lblCVSubTitulo,lblCVSubTituloDescripcion;
        public CardView cv;


        public ItemAdapterViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            lblCVIdMaestro = itemView.findViewById(R.id.lblCVIdMaestro);
            lblCVTitulo = itemView.findViewById(R.id.lblCVTitulo);
            lblCVTituloDescripcion = itemView.findViewById(R.id.lblCVTituloDescripcion);
            lblCVSubTitulo = itemView.findViewById(R.id.lblCVSubTitulo);
            lblCVSubTituloDescripcion = itemView.findViewById(R.id.lblCVSubTituloDescripcion);
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
            List<MotivoModel> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(aux);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (MotivoModel item : aux) {
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
