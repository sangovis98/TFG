package com.example.tfg.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        double proteinas = 0, hidratos = 0, grasas = 0;

        if (listItems.get(position).getProductos() != null){
            for (Producto p : listItems.get(position).getProductos()){
                proteinas += p.getProteinas();
                hidratos += p.getHidratos();
                grasas += p.getGrasas();
            }
        }

        holder.txtNombreDiaDieta.setText(listItems.get(position).getnDia());
        holder.txtProteinasDiaDieta.setText(String.format("%.2f", proteinas));
        holder.txtHidratosDiaDieta.setText(String.format("%.2f", hidratos));
        holder.txtGrasasDiaDieta.setText(String.format("%.2f", grasas));
        holder.pbProteinas.setProgress((int) proteinas);
        holder.pbHidratos.setProgress((int) hidratos);
        holder.pbGrasas.setProgress((int) grasas);
        holder.txtItemDiaDietaCalorias.setText(String.valueOf((int) (proteinas + hidratos + grasas)));
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
            pbProteinas = itemView.findViewById(R.id.pbProteinas);
            pbHidratos = itemView.findViewById(R.id.pbHidratos);
            pbGrasas = itemView.findViewById(R.id.pbGrasas);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onDiaDietaListener.onItemClick(getAdapterPosition());
        }
    }
}
