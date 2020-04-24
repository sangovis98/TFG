package com.example.tfg.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.R;
import com.example.tfg.interfaces.OnItemListener;
import com.example.tfg.modelo.DiaDieta;
import com.example.tfg.modelo.Ejercicio;

import java.util.ArrayList;

public class AdapterEjercicios extends RecyclerView.Adapter<AdapterEjercicios.ViewHolderEjercicios> {

    private ArrayList<Ejercicio> listItems;
    private OnItemListener onEjercicioListener;

    public AdapterEjercicios(ArrayList<Ejercicio> listItems, OnItemListener onEjercicioListener) {
        this.listItems = listItems;
        this.onEjercicioListener = onEjercicioListener;
    }

    @NonNull
    @Override
    public AdapterEjercicios.ViewHolderEjercicios onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ejercicio, parent, false);

        return new AdapterEjercicios.ViewHolderEjercicios(view, onEjercicioListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEjercicios holder, int position) {
        holder.txtItemEjercicio.setText(listItems.get(position).getNombre());
        holder.txtItemRepeticiones.setText(String.valueOf(listItems.get(position).getRepeticiones()));
        holder.txtItemSeries.setText(String.valueOf(listItems.get(position).getSeries()));
        holder.txtItemGrupo.setText(listItems.get(position).getGrupo());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolderEjercicios extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtItemEjercicio, txtItemGrupo, txtItemSeries, txtItemRepeticiones;

        public ViewHolderEjercicios(@NonNull View itemView, OnItemListener onEjercicioListener) {
            super(itemView);
            txtItemEjercicio = itemView.findViewById(R.id.txtItemEjercicio);
            txtItemGrupo = itemView.findViewById(R.id.txtItemGrupo);
            txtItemSeries = itemView.findViewById(R.id.txtItemSeries);
            txtItemRepeticiones = itemView.findViewById(R.id.txtItemRepeticiones);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onEjercicioListener.onItemClick(getAdapterPosition());
        }
    }
}
