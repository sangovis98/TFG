package com.example.tfg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.adapters.AdapterProductos;
import com.example.tfg.interfaces.OnItemListener;
import com.example.tfg.modelo.DiaDieta;
import com.example.tfg.modelo.Producto;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DiaDietaActivity extends AppCompatActivity implements OnItemListener {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private AdapterProductos adapterProductos;
    private Producto p;
    private ArrayList<Producto> productosDiaDieta;
    private EditText etBuscaProducto;
    private String momentoDia;
    private DiaDieta diaDieta;
    private ArrayList<DiaDieta> dietasDias;
    private ImageView imageView, ivDelDieta;
    private TextView txtNombreDiaDieta;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dia_dieta);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        txtNombreDiaDieta = findViewById(R.id.txtNombreDiaDieta);

        imageView = findViewById(R.id.ivAddProductoDieta);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListaProductosActivity.class);
                i.putExtra("dieta", diaDieta);
                i.putExtra("dietas", productosDiaDieta);
                startActivity(i);
            }
        });
        ivDelDieta = findViewById(R.id.ivDelDieta);

    }

    @Override
    protected void onStart() {
        super.onStart();
        obtenerDiaDieta();
        initRecylerView();
        txtNombreDiaDieta.setText(diaDieta.getnDia());

        ivDelDieta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder dialog = new AlertDialog.Builder(DiaDietaActivity.this);
                dialog.setTitle("¿Eliminar permanentemente este día?");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("usuarios").document(firebaseUser.getUid()).collection("diasDietas").document(diaDieta.getnDia()).delete();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

    public void initRecylerView(){
        recyclerView = findViewById(R.id.listaProductos);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        if (productosDiaDieta == null){
            Snackbar.make(recyclerView, "Su dieta no tiene productos", Snackbar.LENGTH_SHORT).show();
        }else {
            adapterProductos = new AdapterProductos(productosDiaDieta, this, DiaDietaActivity.this, diaDieta);
            recyclerView.setAdapter(adapterProductos);
        }
    }

    @Override
    public void onItemClick(final int position) {

        final EditText et = new EditText(getApplicationContext());
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        linearLayout.addView(et);

        AlertDialog.Builder dialog = new AlertDialog.Builder(DiaDietaActivity.this);
        dialog.setView(linearLayout);
        dialog.setTitle("Editar peso del alimento");
        dialog.setPositiveButton("EDITAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (et.getText().toString().equals("") || Integer.parseInt(et.getText().toString()) == 0) {
                    Toast.makeText(getApplicationContext(), "Introduzca un valor válido", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    db.collection("usuarios").document(firebaseUser.getUid()).collection("diasDietas").document(diaDieta.getnDia()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ArrayList<Producto> dd = documentSnapshot.toObject(DiaDieta.class).getProductos();

                            for (Producto p : dd) {
                                if (p.getNombre().equals(productosDiaDieta.get(position).getNombre())) {
                                    p.setGramos(Integer.parseInt(et.getText().toString()));
                                }
                            }

                            Map<String, Object> upd = new HashMap<>();
                            upd.put("productos", dd);
                            db.collection("usuarios").document(firebaseUser.getUid()).collection("diasDietas").document(diaDieta.getnDia()).set(upd, SetOptions.merge());
                            //Recargo la activity
                            Intent i = new Intent(getApplicationContext(), DiaDietaActivity.class);
                            productosDiaDieta.get(position).setGramos(Integer.parseInt(et.getText().toString()));
                            i.putExtra("dieta", diaDieta);
                            i.putExtra("dietas", productosDiaDieta);
                            startActivity(i);
                            overridePendingTransition(0, 0);
                        }
                    });
                }
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    /**
     * Obtiene por intent el objeto DiaDieta y el arraylist de productos que contiene por separado,
     * ya que parcelable extra no obtiene el objeto con el arraylist incluido
     * @return
     */
    public void obtenerDiaDieta(){
        diaDieta = getIntent().getParcelableExtra("dieta");
        productosDiaDieta = getIntent().getParcelableArrayListExtra("dietas");
        diaDieta.setProductos(productosDiaDieta);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}


