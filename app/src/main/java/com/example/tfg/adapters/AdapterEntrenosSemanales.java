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
import com.example.tfg.modelo.EntrenoSemana;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterEntrenosSemanales extends RecyclerView.Adapter<AdapterEntrenosSemanales.ViewHolderEntrenoDieta>{

    private ArrayList<EntrenoSemana> listItems;
    private OnItemListener onEntrenoSemanalListener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

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
    public void onBindViewHolder(@NonNull final ViewHolderEntrenoDieta holder, final int position) {
        holder.nEntreno.setText(listItems.get(position).getNombre());

        int pierna = 0, pecho = 0, espalda = 0;
        //Obtengo los grupos musculares de cada ejercicio
        if (listItems.get(position).getEjEntrenoSemanal() != null) {
            for (Ejercicio e : listItems.get(position).getEjEntrenoSemanal()) {
                if (e.getGrupo().equals("Piernas")) {
                    pierna++;
                } else if (e.getGrupo().equals("Espalda")) {
                    espalda++;
                } else {
                    pecho++;
                }
            }
        }

        holder.txtItemEntrenoGrupoPecho.setText("Pecho " + pecho);
        holder.txtItemEntrenoGrupoPiernas.setText("Espalda " + espalda);
        holder.txtItemEntrenoGrupoEspalda.setText("Piernas " + pierna);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolderEntrenoDieta extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nEntreno, txtItemEntrenoGrupoPecho, txtItemEntrenoGrupoPiernas, txtItemEntrenoGrupoEspalda;

        public ViewHolderEntrenoDieta(@NonNull View itemView, OnItemListener onEntrenoSemanalListener) {
            super(itemView);
            nEntreno = itemView.findViewById(R.id.nEntreno);
            txtItemEntrenoGrupoPecho = itemView.findViewById(R.id.txtItemEntrenoGrupoPecho);
            txtItemEntrenoGrupoEspalda = itemView.findViewById(R.id.txtItemEntrenoGrupoEspalda);
            txtItemEntrenoGrupoPiernas = itemView.findViewById(R.id.txtItemEntrenoGrupoPiernas);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onEntrenoSemanalListener.onItemClick(getAdapterPosition());
        }
    }
}
