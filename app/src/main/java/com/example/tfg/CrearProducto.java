package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.modelo.DiaDieta;
import com.example.tfg.modelo.Producto;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CrearProducto extends AppCompatActivity {

    private EditText etNombreProducto;
    private EditText etProteinas;
    private EditText etHidratos;
    private EditText etGrasas;
    private Button btnAddProducto;
    private Producto p;
    private FirebaseFirestore db;
    private int nProductos;
    private DiaDieta diaDieta;
    private ArrayList<Producto> productosDieta;
    private Intent i;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_producto);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        nProductos = getIntent().getExtras().getInt("productos");
        diaDieta = getIntent().getParcelableExtra("diaDieta");
        productosDieta = getIntent().getParcelableArrayListExtra("productosDieta");

        etNombreProducto = findViewById(R.id.etNombreProducto);
        etProteinas = findViewById(R.id.etProteinas);
        etHidratos = findViewById(R.id.etHidratos);
        etGrasas = findViewById(R.id.etHidratos);
        btnAddProducto = findViewById(R.id.btnAddProducto);

        //Creamos alimento

        //Creamos y añadimos alimento a dieta

        btnAddProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), DiaDietaActivity.class);
                p = new Producto(nProductos + 1 ,etNombreProducto.getText().toString(), Double.parseDouble(etProteinas.getText().toString()), Double.parseDouble(etHidratos.getText().toString()), Double.parseDouble(etGrasas.getText().toString()), "");

                //Añadimos a lista general de productos
                db.collection("productos").add(p);

                //Añadimos el producto a nuestra dieta
                p = new Producto(productosDieta.size()+1, etNombreProducto.getText().toString(), Double.parseDouble(etProteinas.getText().toString()), Double.parseDouble(etHidratos.getText().toString()), Double.parseDouble(etGrasas.getText().toString()), "");
                Map<String, Object> map = new HashMap<>();
                map.put("productos", FieldValue.arrayUnion(p));
                db.collection("usuarios")
                        .document(firebaseUser.getUid())
                        .collection("diasDietas")
                        .document(diaDieta.getnDia())
                        .set(map, SetOptions.merge());

                //Recogemos el alimento de la base de datos y pasamos por el bundle
                db.collection("usuarios")
                        .document(firebaseUser.getUid())
                        .collection("diasDietas")
                        .document(diaDieta.getnDia())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                //Obtenemos la lista de prductos dentro de la dieta
                                productosDieta = documentSnapshot.toObject(DiaDieta.class).getProductos();
                                i.putExtra("dietas", productosDieta);
                                i.putExtra("dieta", diaDieta);
                                startActivity(i);
                                finish();
                            }
                        });
            }
        });
    }
}
