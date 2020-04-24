package com.example.tfg.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.R;
import com.example.tfg.interfaces.OnItemListener;
import com.example.tfg.modelo.Producto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterProductos extends RecyclerView.Adapter<AdapterProductos.ViewHolderProductos>{

    private ArrayList<Producto> listItems;
    private OnItemListener onProductoListener;
    private double total;

    public AdapterProductos(ArrayList<Producto> listItems, OnItemListener onProductoListener) {
        this.listItems = listItems;
        this.onProductoListener = onProductoListener;
    }

    @NonNull
    @Override
    public AdapterProductos.ViewHolderProductos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        
        return new ViewHolderProductos(view, onProductoListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProductos.ViewHolderProductos holder, int position) {
        total = listItems.get(position).getProteinas() + listItems.get(position).getHidratos() + listItems.get(position).getGrasas();
        holder.tvNombre.setText(listItems.get(position).getNombre());
        Picasso.get().load(R.drawable.descarga).into(holder.ivProducto);
        holder.pbProteinas.setProgress((int) ((listItems.get(position).getProteinas() * 100) / total));
        holder.pbHidratos.setProgress((int) ((listItems.get(position).getHidratos() * 100) / total));
        holder.pbGrasas.setProgress((int) ((listItems.get(position).getGrasas() * 100) / total));
        holder.txtItemPProteinas.setText(String.valueOf(listItems.get(position).getProteinas()));
        holder.txtItemPHidratos.setText(String.valueOf(listItems.get(position).getHidratos()));
        holder.txtItemPGrasas.setText(String.valueOf(listItems.get(position).getGrasas()));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolderProductos extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvNombre, txtItemPProteinas, txtItemPHidratos, txtItemPGrasas;
        ImageView ivProducto;
        ProgressBar pbProteinas;
        ProgressBar pbHidratos;
        ProgressBar pbGrasas;

        public ViewHolderProductos(@NonNull View itemView, OnItemListener onProductoListener) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.txtItemDiaDieta);
            ivProducto = itemView.findViewById(R.id.ivItemProducto);
            pbProteinas = itemView.findViewById(R.id.pbItemProteinas);
            pbHidratos = itemView.findViewById(R.id.pbItemHidratos);
            pbGrasas = itemView.findViewById(R.id.pbItemGrasas);
            ivProducto = itemView.findViewById(R.id.ivItemProducto);
            txtItemPProteinas = itemView.findViewById(R.id.txtItemPProteinas);
            txtItemPHidratos = itemView.findViewById(R.id.txtItemPHidratos);
            txtItemPGrasas = itemView.findViewById(R.id.txtItemPGrasas);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onProductoListener.onItemClick(getAdapterPosition());
        }
    }

    public void filtraLista(ArrayList<Producto> listaFiltrada){
        listItems = listaFiltrada;
        notifyDataSetChanged();
    }
}
