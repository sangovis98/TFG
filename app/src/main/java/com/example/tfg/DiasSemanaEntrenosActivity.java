package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.tfg.modelo.Ejercicio;
import com.example.tfg.modelo.EntrenoSemana;
import com.example.tfg.modelo.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DiasSemanaEntrenosActivity extends AppCompatActivity {

    private EntrenoSemana entrenoSemana;
    private ArrayList<Ejercicio> ejerciciosSemana;
    private FirebaseFirestore db;
    private CardView l,m,x,j,v,s,d;
    private TextView grupoL, grupoM, grupoX, grupoJ, grupoV, grupoS, grupoD, ndietaDia;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ImageView ivDelEntreno, ivUseEntreno;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dias_semana_entrenos);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        entrenoSemana = getIntent().getParcelableExtra("entrenoSemanal");

        ndietaDia = findViewById(R.id.ndietaDia);
        ndietaDia.setText(entrenoSemana.getNombre());

        ivDelEntreno = findViewById(R.id.ivDelEntreno);
        ivUseEntreno = findViewById(R.id.ivUseEntreno);

        Picasso.get().load(R.drawable.correct).into(ivUseEntreno);
        db.collection("usuarios").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.toObject(Usuario.class).getEntrenoSemanaEnUso().equals(entrenoSemana.getNombre())) {
                    Picasso.get().load(R.drawable.less).into(ivUseEntreno);
                } else {
                    Picasso.get().load(R.drawable.correct).into(ivUseEntreno);
                }
            }
        });

        //Eliminar entreno
        ivDelEntreno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("usuarios").document(firebaseUser.getUid()).collection("entrenosSemanales").document(entrenoSemana.getNombre()).delete();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        ivUseEntreno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("usuarios").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> map = new HashMap<>();

                        if (documentSnapshot.toObject(Usuario.class).getEntrenoSemanaEnUso().equals("")) {
                            map.put("entrenoSemanaEnUso", entrenoSemana.getNombre());
                            Picasso.get().load(R.drawable.less).into(ivUseEntreno);
                        } else {
                            map.put("entrenoSemanaEnUso", "");
                            db.collection("usuarios").document(firebaseUser.getUid()).set(map, SetOptions.merge());
                            Picasso.get().load(R.drawable.correct).into(ivUseEntreno);
                        }
                        db.collection("usuarios").document(firebaseUser.getUid()).set(map, SetOptions.merge());
                        Toast.makeText(getApplicationContext(), entrenoSemana.getNombre() + " ahora es tu entreno en uso", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        l = findViewById(R.id.l);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListaEjerciciosDiaSemanaActivity.class);
                i.putExtra("nDia", "Lunes");
                i.putExtra("entrenoSemanal", entrenoSemana);
                startActivity(i);
            }
        });

        m = findViewById(R.id.m);
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListaEjerciciosDiaSemanaActivity.class);
                i.putExtra("nDia", "Martes");
                i.putExtra("entrenoSemanal", entrenoSemana);
                startActivity(i);
            }
        });

        x = findViewById(R.id.x);
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListaEjerciciosDiaSemanaActivity.class);
                i.putExtra("nDia", "Miercoles");
                i.putExtra("entrenoSemanal", entrenoSemana);

                startActivity(i);
            }
        });

        j = findViewById(R.id.j);
        j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListaEjerciciosDiaSemanaActivity.class);
                i.putExtra("nDia", "Jueves");
                i.putExtra("entrenoSemanal", entrenoSemana);

                startActivity(i);
            }
        });

        v = findViewById(R.id.v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListaEjerciciosDiaSemanaActivity.class);
                i.putExtra("nDia", "Viernes");
                i.putExtra("entrenoSemanal", entrenoSemana);

                startActivity(i);
            }
        });

        s = findViewById(R.id.s);
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListaEjerciciosDiaSemanaActivity.class);
                i.putExtra("nDia", "Sabado");
                i.putExtra("entrenoSemanal", entrenoSemana);
                startActivity(i);
            }
        });

        d = findViewById(R.id.d);
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListaEjerciciosDiaSemanaActivity.class);
                i.putExtra("nDia", "Domingo");
                i.putExtra("entrenoSemanal", entrenoSemana);
                startActivity(i);
            }
        });
    }
}
