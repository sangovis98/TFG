package com.example.tfg;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.adapters.AdapterProductos;
import com.example.tfg.interfaces.OnItemListener;
import com.example.tfg.modelo.DiaDieta;
import com.example.tfg.modelo.Producto;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
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

    @Override
    protected void onStart() {
        super.onStart();
        obtenerDiaDieta();
    }

    public void initRecylerView(){
        recyclerView = findViewById(R.id.listaProductos);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapterProductos = new AdapterProductos(productos, this, ListaProductosActivity.this, diaDieta);
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
    public void onItemClick(final int position) {

        if (productosDiaDieta == null){
            n = 0;
        }else {
            n = productosDiaDieta.size();
        }

        final EditText editText = new EditText(getApplicationContext());
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(editText);
        layout.setPadding(60, 0, 60, 0);

        AlertDialog.Builder dialog = new AlertDialog.Builder(ListaProductosActivity.this);
        dialog.setView(layout);
        dialog.setTitle("Gramos del alimento");
        dialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editText.getText().toString().equals("") || Integer.parseInt(editText.getText().toString()) == 0) {
                    Toast.makeText(getApplicationContext(), "Introduzca un valor válido", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    p = new Producto(n + 1, listaFiltrada.get(position).getNombre(), listaFiltrada.get(position).getProteinas100(), listaFiltrada.get(position).getHidratos100(), listaFiltrada.get(position).getGrasas100(), listaFiltrada.get(position).getImg(), Double.parseDouble(editText.getText().toString()));
                    //Añade este producto dentro del arraylist productos en la base de datos
                    Map<String, Object> map = new HashMap<>();
                    map.put("productos", FieldValue.arrayUnion(p));

                    db.collection("usuarios")
                            .document(firebaseUser.getUid())
                            .collection("diasDietas")
                            .document(diaDieta.getnDia())
                            .set(map, SetOptions.merge());

                    db.collection("usuarios")
                            .document(firebaseUser.getUid())
                            .collection("diasDietas")
                            .document(diaDieta.getnDia())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Intent i = new Intent(getApplicationContext(), DiaDietaActivity.class);
                                    productosDiaDieta = documentSnapshot.toObject(DiaDieta.class).getProductos();
                                    i.putExtra("dieta", diaDieta);
                                    i.putExtra("dietas", productosDiaDieta);
                                    startActivity(i);
                                }
                            });
                    dialog.dismiss();
                }
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
}
