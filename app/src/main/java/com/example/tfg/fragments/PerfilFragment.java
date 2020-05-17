package com.example.tfg.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.tfg.EditarDietaActivity;
import com.example.tfg.EditarInfoPersonalActivity;
import com.example.tfg.EditarObjetivoActivity;
import com.example.tfg.EditarTablaDeEntreno;
import com.example.tfg.R;
import com.example.tfg.modelo.Ejercicio;
import com.example.tfg.modelo.EntrenoSemana;
import com.example.tfg.modelo.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

/**
 * Clase contenedora del fragment perfil
 */

public class PerfilFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ConstraintLayout informacionPersonal, objetivo, dieta, tablaEntreno;
    private TextView entrenoPerfilTE, txtPerfilSexo, txtPerfilEdad, txtPerfilAltura, txtPerfilNombre, txtPerfilActividad, objetivoPesoActual, objtivoPesoFuturo, fInicio, fFinal, dietaCalorias, objetivoProgresion, dietaProteinas, dietaGrasas, dietaHidratos, dietaNombre, entrenoPerfilDias;
    private DocumentReference dr;
    private EntrenoSemana entrenoSemana;
    private Usuario usuario;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        //Inicializamos variables correspondientes a la base de datos y al recycler view
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        String pattern = "dd-MMM-yyyy";
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        final View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        dr = db.collection("usuarios").document(firebaseUser.getUid());
        //Parte de información personal
        txtPerfilSexo = root.findViewById(R.id.txtPerfilSexo);
        txtPerfilNombre = root.findViewById(R.id.txtPerfilNombre);
        txtPerfilActividad = root.findViewById(R.id.txtPerfilActividad);
        txtPerfilEdad = root.findViewById(R.id.txtPerfilEdad);
        txtPerfilAltura = root.findViewById(R.id.txtPerfilAltura);
        objetivoPesoActual = root.findViewById(R.id.objetivoPesoActual);
        objtivoPesoFuturo = root.findViewById(R.id.objetivoPesoFuturo);
        dietaCalorias = root.findViewById(R.id.dietaCalorias);
        fInicio = root.findViewById(R.id.fInicio);
        fFinal = root.findViewById(R.id.fFinal);
        objetivoProgresion = root.findViewById(R.id.objetivoProgresion);
        dietaProteinas = root.findViewById(R.id.dietaProteinas);
        dietaHidratos = root.findViewById(R.id.dietaHidratos);
        dietaGrasas = root.findViewById(R.id.dietaGrasas);
        entrenoPerfilTE = root.findViewById(R.id.entrenoPerfilTE);
        entrenoPerfilDias = root.findViewById(R.id.entrenoPerfilDias);

        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usuario = documentSnapshot.toObject(Usuario.class);
                txtPerfilSexo.setText(usuario.getSexo());
                txtPerfilNombre.setText(String.valueOf(usuario.getNombre()));
                txtPerfilAltura.setText(usuario.getAltura() + " cm");
                txtPerfilEdad.setText(String.valueOf(usuario.getEdad()));
                txtPerfilActividad.setText(usuario.getActividad());
                objetivoPesoActual.setText(usuario.getPeso() + " Kg");
                objtivoPesoFuturo.setText(usuario.getObjetivo() + " Kg");
                objetivoProgresion.setText(usuario.getPorcentaje());
                entrenoPerfilTE.setText(usuario.getEntrenoSemanaEnUso());


                dietaProteinas.setText(String.valueOf(Math.round(usuario.getTotalProteinas() * 100) / 100.f));
                dietaHidratos.setText(String.valueOf(Math.round(usuario.getTotalHidratos() * 100) / 100.f));
                dietaGrasas.setText(String.valueOf(Math.round(usuario.getTotalGrasas() * 100) / 100.f));

                if (usuario.getfInicio() == null) {
                    fInicio.setText("No establecida");
                } else if (usuario.getfFinal() == null) {
                    fFinal.setText("No establecida");
                } else {
                    fInicio.setText(simpleDateFormat.format(usuario.getfInicio()));
                    fFinal.setText(simpleDateFormat.format(usuario.getfFinal()));
                }

                dietaCalorias.setText(Math.round(usuario.getTotalKcal() * 100) / 100.f + " Kcal");


                if (!usuario.getEntrenoSemanaEnUso().equals("")) {
                    db.collection("usuarios").document(firebaseUser.getUid()).collection("entrenosSemanales").document(usuario.getEntrenoSemanaEnUso()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            EntrenoSemana es = documentSnapshot.toObject(EntrenoSemana.class);

                            boolean l = false, m = false, x = false, j = false, v = false, s = false, d = false;
                            for (Ejercicio e : es.getEjEntrenoSemanal()) {
                                if (e.getnDia().equals("Lunes")) {
                                    l = true;
                                } else if (e.getnDia().equals("Martes")) {
                                    m = true;
                                } else if (e.getnDia().equals("Miercoles")) {
                                    x = true;
                                } else if (e.getnDia().equals("Jueves")) {
                                    j = true;
                                } else if (e.getnDia().equals("Viernes")) {
                                    v = true;
                                } else if (e.getnDia().equals("Sabado")) {
                                    s = true;
                                } else if (e.getnDia().equals("Domingo")) {
                                    d = true;
                                }

                                entrenoPerfilDias.setText("Entreno " + compruebaDias(l, m, x, j, v, s, d) + " días/semana");
                            }

                        }
                    });
                } else {
                    entrenoPerfilTE.setText("Elige un entreno");
                    entrenoPerfilDias.setText("Días del entreno");
                }
            }
        });

        informacionPersonal = root.findViewById(R.id.informacionPersonal);
        informacionPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditarInfoPersonalActivity.class));
            }
        });

        objetivo = root.findViewById(R.id.objetivo);
        objetivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditarObjetivoActivity.class));
            }
        });

        dieta = root.findViewById(R.id.dieta);
        dieta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditarDietaActivity.class));
            }
        });

        tablaEntreno = root.findViewById(R.id.tablaEntreno);
        tablaEntreno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usuario.getEntrenoSemanaEnUso().equals("")) {
                    Toast.makeText(getActivity(), "No tienes un entreno en uso", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getContext(), EditarTablaDeEntreno.class));
                }
            }
        });
        return root;
    }

    public int compruebaDias(boolean l, boolean m, boolean x, boolean j, boolean v, boolean s, boolean d) {
        int cont = 0;
        if (l) {
            cont++;
        }
        if (m) {
            cont++;
        }
        if (x) {
            cont++;
        }
        if (j) {
            cont++;
        }
        if (v) {
            cont++;
        }
        if (s) {
            cont++;
        }
        if (d) {
            cont++;
        }
        return cont;
    }
}
