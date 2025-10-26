package com.luma.miceliomm.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.luma.miceliomm.R;
import com.luma.miceliomm.customs.UnsafeOkHttpClient;
import com.luma.miceliomm.model.ImagenAdicionalModel;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class ImagenAdicionalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>   {

    public List<ImagenAdicionalModel> info;
    public List<ImagenAdicionalModel> aux;
    private final Context context;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;

    private Picasso picasso;

    private static OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        public void onClick(ItemAdapterViewHolder holder, int position);
        public void onClickImagen(ItemAdapterViewHolder holder, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public ImagenAdicionalAdapter(List<ImagenAdicionalModel> info, Context context,  OnItemClickListener mItemClickListener) {
        this.info = info;
        this.aux = new ArrayList<>(info);
        this.context = context;
        this.mItemClickListener = mItemClickListener;

        // Configurar Picasso con cliente personalizado
        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(client))
                .build();
    }

    public void setInfo(List<ImagenAdicionalModel> info) {
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
        View tarjeta = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imagen_adicional, parent, false);
        return new ItemAdapterViewHolder(tarjeta);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemAdapterViewHolder) {
            final ImagenAdicionalModel cls = info.get(position);
            ItemAdapterViewHolder holderItem = (ItemAdapterViewHolder) holder;
            // Cargar imagen con Picasso
            String imageUrl = cls.archivoImagen;
            Log.d("PICASSO_DEBUG", "Intentando cargar: " + imageUrl);

            picasso.load(imageUrl)
                    .placeholder(R.drawable.cargando)
                    .error(R.drawable.no_camara)
                    .fit()
                    .centerCrop()
                    .into(holderItem.imgvwAdicional, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("PICASSO_DEBUG", "✓ Imagen cargada: " + imageUrl);
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("PICASSO_DEBUG", "✗ Error cargando: " + imageUrl, e);
                            e.printStackTrace();
                        }
                    });


           /*
           Picasso.get()
                    .load(cls.archivoImagen)
                    .placeholder(R.drawable.cargando) // imagen placeholder
                    .error(R.drawable.no_camara) // imagen de error
                    .fit()
                    .centerCrop()
                    .into(holderItem.imgvwAdicional);

            Glide.with(context)
                    .load(cls.archivoImagen)
                    .placeholder(R.drawable.cargando)
                    .error(R.drawable.no_camara)
                    .centerCrop()
                    .into(holderItem.imgvwAdicional);
            */



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
        private ImageView imgvwAdicional;
        private CardView cv;


        public ItemAdapterViewHolder(View itemView) {
            super(itemView);
            imgvwAdicional = itemView.findViewById(R.id.imgvwAdicional);
            cv = itemView.findViewById(R.id.cv);

            imgvwAdicional.setOnClickListener(this);
            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                if (v == cv) {
                    mItemClickListener.onClick(this, getLayoutPosition());
                }else if (v == imgvwAdicional){
                    mItemClickListener.onClickImagen(this,getLayoutPosition());
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

}
