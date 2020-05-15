package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.adapters.AdapterEjerciciosNoSeries;
import com.example.tfg.interfaces.OnItemListener;
import com.example.tfg.modelo.Ejercicio;
import com.example.tfg.modelo.EntrenoSemana;
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

public class ListaEjerciciosActivity extends AppCompatActivity implements OnItemListener {

    private String nDia;
    private EntrenoSemana entrenoSemana;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private AdapterEjerciciosNoSeries adapterEjercicios;
    private ArrayList<Ejercicio> ejercicios;
    private ArrayList<Ejercicio> listaFiltrada;
    private Ejercicio e;
    private ImageView ivAddEG;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ejercicios);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        nDia = getIntent().getStringExtra("nDia");
        entrenoSemana = getIntent().getParcelableExtra("entrenoSemanal");

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

        EditText editText = findViewById(R.id.etBusquedaEjercicios);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    public void initRecylerView(){
        recyclerView = findViewById(R.id.listaGeneralEjercicios);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        recyclerView.setHasFixedSize(true);
        if (ejercicios == null){
            Toast.makeText(getApplicationContext(), "No tienes ejercicios en este día", Toast.LENGTH_SHORT).show();
        }else {
            adapterEjercicios = new AdapterEjerciciosNoSeries(ejercicios, this);
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
                        listaFiltrada = new ArrayList<>(ejercicios);
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
                .document(firebaseAuth.getUid())
                .collection("entrenosSemanales")
                .document(entrenoSemana.getNombre())
                .set(map , SetOptions.merge());

        Intent i = new Intent(getApplicationContext(), ListaEjerciciosDiaSemanaActivity.class);
        i.putExtra("nDia", nDia);
        i.putExtra("entrenoSemanal", entrenoSemana);
        startActivity(i);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        db.collection("usuarios")
                .document(firebaseAuth.getUid())
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

    public void filter(String txt) {
        listaFiltrada = new ArrayList<>();

        for (Ejercicio item : ejercicios) {
            if (item.getNombre().toLowerCase().contains(txt.toLowerCase())) {
                listaFiltrada.add(item);
            }
        }
        adapterEjercicios.filtraLista(listaFiltrada);
    }
}

