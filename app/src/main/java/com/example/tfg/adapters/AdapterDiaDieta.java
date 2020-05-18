package com.example.tfg.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.MainActivity;
import com.example.tfg.R;
import com.example.tfg.interfaces.OnItemListener;
import com.example.tfg.modelo.DiaDieta;
import com.example.tfg.modelo.Producto;
import com.example.tfg.modelo.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

/**
 * Esta clase adapter permite llenar el recyler view correspondiente  a las listas de dietas diarias
 */
public class AdapterDiaDieta extends RecyclerView.Adapter<AdapterDiaDieta.ViewHolderDiaDieta> {

    private ArrayList<DiaDieta> listItems;
    private OnItemListener onDiaDietaListener;
    private Usuario u;
    private Context context;
    private String nombre;

    public AdapterDiaDieta(ArrayList<DiaDieta> listItems, OnItemListener onDiaDietaListener, Usuario u, Context context) {
        this.listItems = listItems;
        this.onDiaDietaListener = onDiaDietaListener;
        this.u = u;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterDiaDieta.ViewHolderDiaDieta onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dieta_dia, parent, false);

        return new AdapterDiaDieta.ViewHolderDiaDieta(view, onDiaDietaListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterDiaDieta.ViewHolderDiaDieta holder, final int position) {

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        double proteinas = 0, hidratos = 0, grasas = 0;

        if (listItems.get(position).getProductos() != null) {
            for (Producto p : listItems.get(position).getProductos()) {
                //Cambio el valor nutricional por cada 100 gramos a el total de cada macro por el peso total del alimento
                proteinas += p.getProteinas100() / 100 * p.getGramos(); //Total deproteínas
                hidratos += p.getHidratos100() / 100 * p.getGramos(); //Total de hidratos
                grasas += p.getGrasas100() / 100 * p.getGramos(); //Total de grasas
            }
        }

        final double finalProteinas = proteinas;
        final double finalHidratos = hidratos;
        final double finalGrasas = grasas;
        db.collection("usuarios").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                u = documentSnapshot.toObject(Usuario.class);

                holder.pbProteinas.setMax((int) u.getTotalProteinas());
                holder.pbProteinas.setProgress((int) finalProteinas);
                holder.pbHidratos.setMax((int) u.getTotalHidratos());
                holder.pbHidratos.setProgress((int) finalHidratos);
                holder.pbGrasas.setMax((int) u.getTotalGrasas());
                holder.pbGrasas.setProgress((int) finalGrasas);

                if (holder.pbProteinas.getProgress() > holder.pbProteinas.getMax() * 0.9) {
                    holder.pbProteinas.getProgressDrawable().setColorFilter(
                            Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
                } else if (holder.pbProteinas.getProgress() < holder.pbProteinas.getMax() && holder.pbProteinas.getProgress() > holder.pbProteinas.getMax() * 0.7) {
                    holder.pbProteinas.getProgressDrawable().setColorFilter(
                            Color.rgb(255, 170, 0), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    holder.pbProteinas.getProgressDrawable().setColorFilter(
                            Color.rgb(3, 169, 244), android.graphics.PorterDuff.Mode.SRC_IN);
                }

                if (holder.pbHidratos.getProgress() > holder.pbHidratos.getMax() * 0.9) {
                    holder.pbHidratos.getProgressDrawable().setColorFilter(
                            Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
                } else if (holder.pbHidratos.getProgress() < holder.pbHidratos.getMax() && holder.pbHidratos.getProgress() > holder.pbHidratos.getMax() * 0.7) {
                    holder.pbHidratos.getProgressDrawable().setColorFilter(
                            Color.rgb(255, 170, 0), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    holder.pbHidratos.getProgressDrawable().setColorFilter(
                            Color.rgb(3, 169, 244), android.graphics.PorterDuff.Mode.SRC_IN);
                }

                if (holder.pbGrasas.getProgress() > holder.pbGrasas.getMax() * 0.9) {
                    holder.pbGrasas.getProgressDrawable().setColorFilter(
                            Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
                } else if (holder.pbGrasas.getProgress() < holder.pbGrasas.getMax() && holder.pbGrasas.getProgress() > holder.pbGrasas.getMax() * 0.7) {
                    holder.pbGrasas.getProgressDrawable().setColorFilter(
                            Color.rgb(255, 170, 0), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    holder.pbGrasas.getProgressDrawable().setColorFilter(
                            Color.rgb(3, 169, 244), android.graphics.PorterDuff.Mode.SRC_IN);
                }
            }
        });

        holder.txtNombreDiaDieta.setText(listItems.get(position).getnDia());
        holder.txtProteinasDiaDieta.setText(String.format("%.2f", proteinas));
        holder.txtHidratosDiaDieta.setText(String.format("%.2f", hidratos));
        holder.txtGrasasDiaDieta.setText(String.format("%.2f", grasas));
        holder.txtItemDiaDietaCalorias.setText((int) (proteinas + hidratos + grasas) + " Kcal");

        //Editar Dieta
        holder.ivItemDiaDietaEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Editar el nombre");

                final EditText editText = new EditText(context);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                dialog.setView(editText);

                dialog.setPositiveButton("EDITAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nombre = editText.getText().toString();

                        db.collection("usuarios").document(firebaseUser.getUid()).collection("diasDietas").document(listItems.get(position).getnDia()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                DiaDieta diaDieta = documentSnapshot.toObject(DiaDieta.class);

                                //Editar, es necesario eliminar y volver a crear el dia dieta pues no se puede cambiar el nombre del id del documento (permanece con el id de su creacón)
                                diaDieta.setnDia(nombre);
                                db.collection("usuarios").document(firebaseUser.getUid()).collection("diasDietas").document(listItems.get(position).getnDia()).delete();
                                db.collection("usuarios").document(firebaseUser.getUid()).collection("diasDietas").document(nombre).set(diaDieta, SetOptions.merge());
                                ((MainActivity) context).finish();
                                ((MainActivity) context).overridePendingTransition(0, 0);
                                context.startActivity(new Intent(context, MainActivity.class));
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

    public class ViewHolderDiaDieta extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtNombreDiaDieta;
        TextView txtProteinasDiaDieta;
        TextView txtHidratosDiaDieta;
        TextView txtGrasasDiaDieta;
        TextView txtItemDiaDietaCalorias;
        ProgressBar pbProteinas;
        ProgressBar pbHidratos;
        ProgressBar pbGrasas;
        ImageView ivItemDiaDietaEditar;

        public ViewHolderDiaDieta(@NonNull View itemView, OnItemListener onDiaDietaListener) {
            super(itemView);
            txtNombreDiaDieta = itemView.findViewById(R.id.txtItemNombreEntrenoS);
            txtProteinasDiaDieta = itemView.findViewById(R.id.txtItemDiaDietaProteinas);
            txtHidratosDiaDieta = itemView.findViewById(R.id.txtItemDiaDietaHidratos);
            txtGrasasDiaDieta = itemView.findViewById(R.id.txtItemDiaDietaGrasas);
            txtItemDiaDietaCalorias = itemView.findViewById(R.id.txtItemDiaDietaCalorias);
            pbProteinas = itemView.findViewById(R.id.pbProteinas);
            pbHidratos = itemView.findViewById(R.id.pbHidratos);
            pbGrasas = itemView.findViewById(R.id.pbGrasas);
            ivItemDiaDietaEditar = itemView.findViewById(R.id.sItemDiaDietaEditar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onDiaDietaListener.onItemClick(getAdapterPosition());
        }
    }
}
