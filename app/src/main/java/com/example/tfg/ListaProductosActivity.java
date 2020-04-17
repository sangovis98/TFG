package com.example.tfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.tfg.adapters.AdapterProductos;
import com.example.tfg.interfaces.OnItemListener;
import com.example.tfg.modelo.DiaDieta;
import com.example.tfg.modelo.Producto;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaProductosActivity extends AppCompatActivity implements OnItemListener {

    private RecyclerView recyclerView;
    private ArrayList<Producto> productos;
    private Producto p;
    private LinearLayoutManager linearLayoutManager;
    private AdapterProductos adapterProductos;
    private FirebaseFirestore db;
    private DiaDieta diaDieta;
    private ImageView ivBuscaProducto;
    private ArrayList<Producto> productosDiaDieta;
    private ArrayList<Producto> listaFiltrada;
    private int n;
    private ImageView btnCrearProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.listaProductos);

        obtenerDiaDieta();

        productos = new ArrayList<>();
        db.collection("productos")
                .orderBy("nombre")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots){
                            p = qds.toObject(Producto.class);
                            productos.add(p);
                        }
                        initRecylerView();
                        listaFiltrada = new ArrayList<>(productos);
                    }
                });

        //Busqueda de productos
        EditText editText = findViewById(R.id.etBusqueda);
        //Búsqueda de alimentos
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        btnCrearProducto = findViewById(R.id.btnCrearProducto);
        btnCrearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CrearProducto.class);
                i.putExtra("productos", productos.size());
                i.putExtra("productosDieta", productosDiaDieta);
                i.putExtra("diaDieta", diaDieta);
                startActivity(i);
            }
        });

    }

    public void initRecylerView(){
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapterProductos = new AdapterProductos(productos, this);
        recyclerView.setAdapter(adapterProductos);
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

    public void filter(String txt){
        listaFiltrada = new ArrayList<>();

        for (Producto item : productos){
            if (item.getNombre().toLowerCase().contains(txt.toLowerCase())){
                listaFiltrada.add(item);
            }
        }
        adapterProductos.filtraLista(listaFiltrada);
    }

    @Override
    public void onItemClick(int position) {

        n = productosDiaDieta.size();
        p = new Producto(n+1, listaFiltrada.get(position).getNombre(), listaFiltrada.get(position).getProteinas(), listaFiltrada.get(position).getHidratos(), listaFiltrada.get(position).getGrasas());

        Map<String, Object> map = new HashMap<>();
        map.put("productos", FieldValue.arrayUnion(p));

        db.collection("usuarios")
                .document("KVNZAq8vBvgCB4pP4iJv")
                .collection("diasDietas")
                .document(diaDieta.getnDia()).set(map, SetOptions.merge());

        Intent i = new Intent(getApplicationContext(), DiaDietaActivity.class);
        i.putExtra("dieta", diaDieta);
        i.putExtra("dietas", productosDiaDieta);
        startActivity(i);
    }

}
