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

public class AdapterEntrenoSeleccionado extends RecyclerView.Adapter<AdapterEntrenoSeleccionado.ViewHolderEjercicios> {

    private ArrayList<Ejercicio> listItems;
    private OnItemListener onEjercicioListener;

    public AdapterEntrenoSeleccionado(ArrayList<Ejercicio> listItems, OnItemListener onEjercicioListener) {
        this.listItems = listItems;
        this.onEjercicioListener = onEjercicioListener;
    }

    @NonNull
    @Override
    public AdapterEntrenoSeleccionado.ViewHolderEjercicios onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ejercicio_entreno_seleccionado, parent, false);

        return new AdapterEntrenoSeleccionado.ViewHolderEjercicios(view, onEjercicioListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEjercicios holder, int position) {
        holder.txtItemNombreEntrenoS.setText(listItems.get(position).getNombre());
        holder.txtItemSeriesS.setText(String.valueOf(listItems.get(position).getSeries()));
        holder.txtItemRepeticionesS.setText(String.valueOf(listItems.get(position).getRepeticiones()));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolderEjercicios extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtItemNombreEntrenoS, txtItemSeriesS, txtItemRepeticionesS;

        public ViewHolderEjercicios(@NonNull View itemView, OnItemListener onEjercicioListener) {
            super(itemView);
            txtItemNombreEntrenoS = itemView.findViewById(R.id.txtItemNombreEntrenoS);
            txtItemRepeticionesS = itemView.findViewById(R.id.txtItemRepeticionesS);
            txtItemSeriesS = itemView.findViewById(R.id.txtItemSeriesS);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onEjercicioListener.onItemClick(getAdapterPosition());
        }
    }
}
