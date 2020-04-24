package com.example.tfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tfg.adapters.AdapterEjercicios;
import com.example.tfg.interfaces.OnItemListener;
import com.example.tfg.modelo.Ejercicio;
import com.example.tfg.modelo.EntrenoSemana;
import com.example.tfg.modelo.Producto;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaEjerciciosActivity extends AppCompatActivity implements OnItemListener {

    private String nDia;
    private EntrenoSemana entrenoSemana;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private AdapterEjercicios adapterEjercicios;
    private ArrayList<Ejercicio> ejercicios;
    private Ejercicio e;
    private ImageView ivAddEG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ejercicios);

        db = FirebaseFirestore.getInstance();

        obtenerBundle();

        ivAddEG = findViewById(R.id.ivAddEG);
        ivAddEG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DocumentarEjercicioActivity.class);
                i.putExtra("nDia", nDia);
                i.putExtra("entrenoSemanal", entrenoSemana);
                startActivity(i);
            }
        });

        llenarEjerciciosDia();
    }

    public void initRecylerView(){
        recyclerView = findViewById(R.id.listaGeneralEjercicios);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setHasFixedSize(true);
        if (ejercicios == null){
            Toast.makeText(getApplicationContext(), "No tienes ejercicios en este día", Toast.LENGTH_SHORT).show();
        }else {
            adapterEjercicios = new AdapterEjercicios(ejercicios, this);
            recyclerView.setAdapter(adapterEjercicios);
        }
    }

    public void llenarEjerciciosDia(){
        ejercicios = new ArrayList<>();
        db.collection("ejercicios")
                .orderBy("nombre")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot qds : queryDocumentSnapshots){
                            e = qds.toObject(Ejercicio.class);
                            ejercicios.add(e);
                        }
                        initRecylerView();
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        int n;
        if (entrenoSemana.getEjEntrenoSemanal() == null){
            n = 0;
        }else {
            n = entrenoSemana.getEjEntrenoSemanal().size();
        }

        e = new Ejercicio(n+1, ejercicios.get(position).getNombre(), ejercicios.get(position).getRepeticiones(), ejercicios.get(position).getSeries(), ejercicios.get(position).getGrupo(), nDia);

        Map<String, Object> map = new HashMap<>();
        map.put("ejEntrenoSemanal", FieldValue.arrayUnion(e));

        db.collection("usuarios")
                .document("KVNZAq8vBvgCB4pP4iJv")
                .collection("entrenosSemanales")
                .document(entrenoSemana.getNombre())
                .set(map , SetOptions.merge());

        Intent i = new Intent(getApplicationContext(), ListaEjerciciosDiaSemanaActivity.class);
        i.putExtra("nDia", nDia);
        i.putExtra("entrenoSemanal", entrenoSemana);
        startActivity(i);
        finish();
    }

    public void obtenerBundle(){
        nDia = getIntent().getStringExtra("nDia");
        entrenoSemana = getIntent().getParcelableExtra("entrenoSemanal");
    }

    @Override
    protected void onStart() {
        super.onStart();
        db.collection("usuarios")
                .document("KVNZAq8vBvgCB4pP4iJv")
                .collection("entrenosSemanales")
                .document(entrenoSemana.getNombre())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        entrenoSemana.setEjEntrenoSemanal(documentSnapshot.toObject(EntrenoSemana.class).getEjEntrenoSemanal());
                    }
                });
    }
}

