package com.luma.miceliomm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.luma.miceliomm.R;
import com.luma.miceliomm.model.MenuModel;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<MenuModel> info;
    private final Context context;


    private static final int TYPE_ITEM = 2;

    private static OnItemClickListener mItemClickListener;


    public interface OnItemClickListener {
        public void onClickCV(ItemAdapterViewHolder holder, int position);
    }


    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public MenuAdapter(ArrayList<MenuModel> info, Context context, OnItemClickListener mItemClickListener) {
        this.info = info;
        this.context = context;
        this.mItemClickListener = mItemClickListener;
    }

    public void setInfo(ArrayList<MenuModel> info) {
        this.info = info;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tarjeta = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new ItemAdapterViewHolder(tarjeta);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemAdapterViewHolder) {
            final MenuModel cls = info.get(position);
            ItemAdapterViewHolder holderItem = (ItemAdapterViewHolder) holder;
            holderItem.name.setText(cls.getNombre());
            holderItem.icon.setImageDrawable(cls.getDrawable());

        }
    }

    @Override
    public int getItemCount() {
        return info.size();
    }
    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    public class ItemAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public CardView cv;
        public ImageView icon;

        public ItemAdapterViewHolder(View itemView) {
            super(itemView);
            cv =  itemView.findViewById(R.id.cv);
            name = itemView.findViewById(R.id.txtNombre);
            icon = itemView.findViewById(R.id.txtIcon);
            cv.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (mItemClickListener != null) {
                if (v == cv) {
                    mItemClickListener.onClickCV(this, getLayoutPosition());
                }
            }

        }

    }
}
