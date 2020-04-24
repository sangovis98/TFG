package com.example.tfg.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.R;
import com.example.tfg.interfaces.OnItemListener;
import com.example.tfg.modelo.EntrenoSemana;

import java.util.ArrayList;

public class AdapterEntrenosSemanales extends RecyclerView.Adapter<AdapterEntrenosSemanales.ViewHolderEntrenoDieta>{

    private ArrayList<EntrenoSemana> listItems;
    private OnItemListener onEntrenoSemanalListener;

    public AdapterEntrenosSemanales(ArrayList<EntrenoSemana> listItems, OnItemListener onEntrenoSemanalListener) {
        this.listItems = listItems;
        this.onEntrenoSemanalListener = onEntrenoSemanalListener;
    }

    @NonNull
    @Override
    public ViewHolderEntrenoDieta onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entreno, parent, false);

        return new AdapterEntrenosSemanales.ViewHolderEntrenoDieta(view, onEntrenoSemanalListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEntrenoDieta holder, int position) {
        holder.nKalEntreno.setText(String.valueOf(listItems.get(position).getGastoCalorico()));
        holder.nEntreno.setText(listItems.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolderEntrenoDieta extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nKalEntreno, nEntreno;

        public ViewHolderEntrenoDieta(@NonNull View itemView, OnItemListener onEntrenoSemanalListener) {
            super(itemView);
            nKalEntreno = itemView.findViewById(R.id.nKcalEntreno);
            nEntreno = itemView.findViewById(R.id.nEntreno);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onEntrenoSemanalListener.onItemClick(getAdapterPosition());
        }
    }
}
