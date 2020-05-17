package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

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

public class DocumentarEjercicioActivity extends AppCompatActivity {


    private String nDia;
    private Spinner sGrupoMuscular;
    private FirebaseFirestore db;
    private Ejercicio e;
    private ArrayList<Ejercicio> ejercicios;
    private EditText etNombreEjercicio;
    private Button btnDocumentarEjercicio;
    private EntrenoSemana entrenoSemana;
    private ArrayList<Ejercicio> ejsDieta;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private int de = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentar_ejercicio);

        nDia = getIntent().getStringExtra("nDia");
        entrenoSemana = getIntent().getParcelableExtra("entrenoSemanal");

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        sGrupoMuscular = findViewById(R.id.sGrupoMuscular);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gm, android.R.layout.simple_spinner_item);
        sGrupoMuscular.setAdapter(adapter);

        etNombreEjercicio = findViewById(R.id.etNombreEjercicio);

        ejercicios = new ArrayList<>();
        btnDocumentarEjercicio = findViewById(R.id.btnDocumentarEjercicio);
        btnDocumentarEjercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ejercicios != null) {
                    de = ejercicios.size();
                }
                //Contabiliza número de ejercicios
                db.collection("ejercicios")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot qds : queryDocumentSnapshots){
                                    e = qds.toObject(Ejercicio.class);
                                    ejercicios.add(e);
                                }
                                e = new Ejercicio(de + 1, etNombreEjercicio.getText().toString(), 0, 0, sGrupoMuscular.getSelectedItem().toString(), "");

                                //Añade a la collección general de ejercicios
                                db.collection("ejercicios").add(e);
                            }
                        });

                ejercicios = new ArrayList<>();
                ejsDieta = new ArrayList<>();
                db.collection("usuarios")
                        .document(firebaseUser.getUid())
                        .collection("entrenosSemanales")
                        .document(entrenoSemana.getNombre())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                EntrenoSemana es = documentSnapshot.toObject(EntrenoSemana.class);

                                for (Ejercicio eje : es.getEjEntrenoSemanal()) {
                                    ejsDieta.add(eje);
                                }
                                Ejercicio ejer = new Ejercicio(ejsDieta.size() + 1, etNombreEjercicio.getText().toString(), 0, 0, sGrupoMuscular.getSelectedItem().toString(), nDia);

                                Map<String, Object> map = new HashMap<>();
                                map.put("ejEntrenoSemanal", FieldValue.arrayUnion(ejer));

                                //Añade a la colección del usuario
                                db.collection("usuarios")
                                        .document(firebaseUser.getUid())
                                        .collection("entrenosSemanales")
                                        .document(entrenoSemana.getNombre())
                                        .set(map, SetOptions.merge());

                                Intent i = new Intent(getApplicationContext(), ListaEjerciciosDiaSemanaActivity.class);
                                i.putExtra("entrenoSemanal", entrenoSemana);
                                i.putExtra("nDia", nDia);
                                startActivity(i);
                                finish();
                            }
                        });
            }
        });
    }
}
