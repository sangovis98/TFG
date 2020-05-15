package com.example.tfg;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.adapters.AdapterEntrenoSeleccionado;
import com.example.tfg.interfaces.OnItemListener;
import com.example.tfg.modelo.Ejercicio;
import com.example.tfg.modelo.EntrenoSemana;
import com.example.tfg.modelo.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EditarTablaDeEntreno extends AppCompatActivity implements OnItemListener {

    private RecyclerView recyclerViewL, recyclerViewM, recyclerViewX, recyclerViewJ, recyclerViewV, recyclerViewS, recyclerViewD;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private AdapterEntrenoSeleccionado adapterEjercicios;
    private ArrayList<Ejercicio> ejerciciosEntrenoUso, eLunes, eMartes, eMiercoles, eJueves, eViernes, eSabado, eDomingo;
    private Ejercicio ejercicio;
    private Usuario usuario;
    private TextView editarTablaNombre, txtLunesET, txtMartesET, txtMiercolesET, txtJuevesET, txtViernesET, txtSabadoET, txtDomingoET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_editar_tabla_de_entreno);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        editarTablaNombre = findViewById(R.id.editarTablaNombre);

        recyclerViewL = findViewById(R.id.listaTablaEntrenoSeleccionadaLunes);
        recyclerViewM = findViewById(R.id.listaTablaEntrenoSeleccionadaMartes);
        recyclerViewX = findViewById(R.id.listaTablaEntrenoSeleccionadaMiercoles);
        recyclerViewJ = findViewById(R.id.listaTablaEntrenoSeleccionadaJueves);
        recyclerViewV = findViewById(R.id.listaTablaEntrenoSeleccionadaViernes);
        recyclerViewS = findViewById(R.id.listaTablaEntrenoSeleccionadaSabado);
        recyclerViewD = findViewById(R.id.listaTablaEntrenoSeleccionadaDomingo);

        eLunes = new ArrayList<>();
        eMartes = new ArrayList<>();
        eMiercoles = new ArrayList<>();
        eJueves = new ArrayList<>();
        eViernes = new ArrayList<>();
        eSabado = new ArrayList<>();
        eDomingo = new ArrayList<>();

        txtLunesET = findViewById(R.id.txtLunesET);
        txtLunesET.setVisibility(View.GONE);
        txtMartesET = findViewById(R.id.txtMartesET);
        txtMartesET.setVisibility(View.GONE);
        txtMiercolesET = findViewById(R.id.txtMiercolesET);
        txtMiercolesET.setVisibility(View.GONE);
        txtJuevesET = findViewById(R.id.entrenoPerfilDias);
        txtJuevesET.setVisibility(View.GONE);
        txtViernesET = findViewById(R.id.txtViernesET);
        txtViernesET.setVisibility(View.GONE);
        txtSabadoET = findViewById(R.id.txtSabadoET);
        txtSabadoET.setVisibility(View.GONE);
        txtDomingoET = findViewById(R.id.txtDomingoET);
        txtDomingoET.setVisibility(View.GONE);

        db.collection("usuarios").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usuario = documentSnapshot.toObject(Usuario.class);

                editarTablaNombre.setText(usuario.getEntrenoSemanaEnUso());
                db.collection("usuarios").document(firebaseUser.getUid()).collection("entrenosSemanales").document(usuario.getEntrenoSemanaEnUso()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        EntrenoSemana es = documentSnapshot.toObject(EntrenoSemana.class);
                        if (es.getEjEntrenoSemanal() == null) {
                            Toast.makeText(getApplicationContext(), "No tienes ejercicios en tu entreno seleccionado", Toast.LENGTH_LONG).show();
                        } else {
                            ejerciciosEntrenoUso = new ArrayList<>(es.getEjEntrenoSemanal());

                            for (Ejercicio ej : ejerciciosEntrenoUso) {
                                if (ej.getnDia().equals("Lunes")) {
                                    eLunes.add(ej);
                                    txtLunesET.setVisibility(View.VISIBLE);
                                    initRecylerView(recyclerViewL, eLunes);
                                } else if (ej.getnDia().equals("Martes")) {
                                    eMartes.add(ej);
                                    txtMartesET.setVisibility(View.VISIBLE);
                                    initRecylerView(recyclerViewM, eMartes);
                                } else if (ej.getnDia().equals("Miercoles")) {
                                    eMiercoles.add(ej);
                                    txtMiercolesET.setVisibility(View.VISIBLE);
                                    initRecylerView(recyclerViewX, eMiercoles);
                                } else if (ej.getnDia().equals("Jueves")) {
                                    eJueves.add(ej);
                                    txtJuevesET.setVisibility(View.VISIBLE);
                                    initRecylerView(recyclerViewJ, eJueves);
                                } else if (ej.getnDia().equals("Viernes")) {
                                    eViernes.add(ej);
                                    txtViernesET.setVisibility(View.VISIBLE);
                                    initRecylerView(recyclerViewV, eViernes);
                                } else if (ej.getnDia().equals("Sabado")) {
                                    eSabado.add(ej);
                                    txtSabadoET.setVisibility(View.VISIBLE);
                                    initRecylerView(recyclerViewS, eSabado);
                                } else {
                                    eDomingo.add(ej);
                                    txtDomingoET.setVisibility(View.VISIBLE);
                                    initRecylerView(recyclerViewD, eDomingo);
                                }
                            }

                        }
                    }
                });
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }

    public void initRecylerView(RecyclerView rv, ArrayList<Ejercicio> e) {
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setHasFixedSize(true);
        adapterEjercicios = new AdapterEntrenoSeleccionado(e, this);
        rv.setAdapter(adapterEjercicios);
    }
}
