package com.example.tfg.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.ListaProductosActivity;
import com.example.tfg.R;
import com.example.tfg.interfaces.OnItemListener;
import com.example.tfg.modelo.DiaDieta;
import com.example.tfg.modelo.Producto;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterProductos extends RecyclerView.Adapter<AdapterProductos.ViewHolderProductos>{

    private ArrayList<Producto> listItems;
    private OnItemListener onProductoListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    double pt, ht, hg;
    private Context context;
    private DiaDieta diaDieta;

    public AdapterProductos(ArrayList<Producto> listItems, OnItemListener onProductoListener, Context context, DiaDieta diaDieta) {
        this.listItems = listItems;
        this.onProductoListener = onProductoListener;
        this.context = context;
        this.diaDieta = diaDieta;
    }

    @NonNull
    @Override
    public AdapterProductos.ViewHolderProductos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        
        return new ViewHolderProductos(view, onProductoListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterProductos.ViewHolderProductos holder, final int position) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        holder.tvNombre.setText(listItems.get(position).getNombre());

        holder.txtItemPProteinas.setText(String.valueOf(listItems.get(position).getProteinas()));
        holder.txtItemPHidratos.setText(String.valueOf(listItems.get(position).getHidratos()));
        holder.txtItemPGrasas.setText(String.valueOf(listItems.get(position).getGrasas()));
        if (listItems.get(position).getImg().equals("")){
            Picasso.get().load(R.drawable.descarga).into(holder.ivProducto);
        }else {
            Picasso.get().load(listItems.get(position).getImg()).into(holder.ivProducto);
        }
        holder.ivItemDelProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("¿Eliminar permanentemente este producto?");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cargamos alimentos
                        db.collection("usuarios").document(firebaseUser.getUid()).collection("diasDietas").document(diaDieta.getnDia()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                DiaDieta dd = documentSnapshot.toObject(DiaDieta.class);
                                ArrayList<Producto> dds = diaDieta.getProductos();

                                int pos = 0;
                                for (int i = 0; i < dds.size(); i++) {
                                    if (dds.get(i).getNombre().equals(listItems.get(position).getNombre())) {
                                        pos = i;
                                    }
                                }
                                dds.remove(pos);

                                dd.setProductos(dds);

                                db.collection("usuarios").document(firebaseUser.getUid()).collection("diasDietas").document(diaDieta.getnDia()).set(dd, SetOptions.merge());
                                AdapterProductos.this.notifyDataSetChanged();
                            }
                        });
                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolderProductos extends RecyclerView.ViewHolder implements View.OnClickListener {

        ListaProductosActivity listaProductosActivity;
        TextView tvNombre, txtItemPProteinas, txtItemPHidratos, txtItemPGrasas;
        ImageView ivProducto, ivItemDelProducto;
        ProgressBar pbProteinas;
        ProgressBar pbHidratos;
        ProgressBar pbGrasas;

        public ViewHolderProductos(@NonNull View itemView, OnItemListener onProductoListener) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.txtItemNombreEntrenoS);
            ivProducto = itemView.findViewById(R.id.ivItemProducto);

            txtItemPProteinas = itemView.findViewById(R.id.txtItemPProteinas);
            txtItemPHidratos = itemView.findViewById(R.id.txtItemPHidratos);
            txtItemPGrasas = itemView.findViewById(R.id.txtItemPGrasas);
            ivItemDelProducto = itemView.findViewById(R.id.ivItemDelProducto);
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
