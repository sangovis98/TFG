package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.tfg.adapters.AdapterProductos;
import com.example.tfg.interfaces.OnItemListener;
import com.example.tfg.modelo.DiaDieta;
import com.example.tfg.modelo.Producto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

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
    private ImageView imageView;

    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dia_dieta);

        db = FirebaseFirestore.getInstance();

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        obtenerDiaDieta();
        initRecylerView();
    }

    public void initRecylerView(){
        recyclerView = findViewById(R.id.listaProductos);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapterProductos = new AdapterProductos(productosDiaDieta, this);
        recyclerView.setAdapter(adapterProductos);
    }

    @Override
    public void onItemClick(int position) {

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
}


