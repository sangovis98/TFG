package com.example.tfg.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.R;
import com.example.tfg.interfaces.OnItemListener;
import com.example.tfg.modelo.DiaDieta;
import com.example.tfg.modelo.Producto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterDiaDieta extends RecyclerView.Adapter<AdapterDiaDieta.ViewHolderDiaDieta>{

    private ArrayList<DiaDieta> listItems;
    private OnItemListener onDiaDietaListener;
    private double total;

    public AdapterDiaDieta(ArrayList<DiaDieta> listItems, OnItemListener onDiaDietaListener) {
        this.listItems = listItems;
        this.onDiaDietaListener = onDiaDietaListener;
    }

    @NonNull
    @Override
    public AdapterDiaDieta.ViewHolderDiaDieta onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dieta_dia, parent, false);

        return new AdapterDiaDieta.ViewHolderDiaDieta(view, onDiaDietaListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDiaDieta.ViewHolderDiaDieta holder, int position) {
        total = listItems.get(position).getProteinas() + listItems.get(position).getHidratos() + listItems.get(position).getGrasas();
        holder.txtNombreDiaDieta.setText(listItems.get(position).getnDia());
        holder.txtProteinasDiaDieta.setText(String.valueOf(listItems.get(position).getProteinas()));
        holder.txtHidratosDiaDieta.setText(String.valueOf(listItems.get(position).getHidratos()));
        holder.txtGrasasDiaDieta.setText(String.valueOf(listItems.get(position).getGrasas()));
        
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolderDiaDieta extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtNombreDiaDieta;
        TextView txtProteinasDiaDieta;
        TextView txtHidratosDiaDieta;
        TextView txtGrasasDiaDieta;
        TextView txtItemDiaDietaCalorias;
        ProgressBar pbProteinas;
        ProgressBar pbHidratos;
        ProgressBar pbGrasas;

        public ViewHolderDiaDieta(@NonNull View itemView, OnItemListener onDiaDietaListener) {
            super(itemView);
            txtNombreDiaDieta = itemView.findViewById(R.id.txtItemDiaDieta);
            txtProteinasDiaDieta = itemView.findViewById(R.id.txtItemDiaDietaProteinas);
            txtHidratosDiaDieta = itemView.findViewById(R.id.txtItemDiaDietaHidratos);
            txtGrasasDiaDieta = itemView.findViewById(R.id.txtItemDiaDietaGrasas);
            txtItemDiaDietaCalorias = itemView.findViewById(R.id.txtItemDiaDietaCalorias);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onDiaDietaListener.onItemClick(getAdapterPosition());
        }
    }
}
