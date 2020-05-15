package com.example.tfg.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.R;
import com.example.tfg.interfaces.OnItemListener;
import com.example.tfg.modelo.Ejercicio;

import java.util.ArrayList;

public class AdapterEjerciciosNoSeries extends RecyclerView.Adapter<AdapterEjerciciosNoSeries.ViewHolderEjerciciosNoSeries> {

    private ArrayList<Ejercicio> listItems;
    private OnItemListener onEjercicioListener;

    public AdapterEjerciciosNoSeries(ArrayList<Ejercicio> listItems, OnItemListener onEjercicioListener) {
        this.listItems = listItems;
        this.onEjercicioListener = onEjercicioListener;
    }

    @NonNull
    @Override
    public AdapterEjerciciosNoSeries.ViewHolderEjerciciosNoSeries onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ejercicio_no_series, parent, false);

        return new AdapterEjerciciosNoSeries.ViewHolderEjerciciosNoSeries(view, onEjercicioListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEjerciciosNoSeries holder, int position) {
        holder.txtItemEjercicioNS.setText(listItems.get(position).getNombre());
        holder.txtItemGrupoNS.setText(listItems.get(position).getGrupo());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolderEjerciciosNoSeries extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtItemEjercicioNS, txtItemGrupoNS;

        public ViewHolderEjerciciosNoSeries(@NonNull View itemView, OnItemListener onEjercicioListener) {
            super(itemView);
            txtItemEjercicioNS = itemView.findViewById(R.id.txtItemEjercicioNS);
            txtItemGrupoNS = itemView.findViewById(R.id.txtItemGrupoNS);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onEjercicioListener.onItemClick(getAdapterPosition());
        }
    }

    public void filtraLista(ArrayList<Ejercicio> listaFiltrada) {
        listItems = listaFiltrada;
        notifyDataSetChanged();
    }
}
