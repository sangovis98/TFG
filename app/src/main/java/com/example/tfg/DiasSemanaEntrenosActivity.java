package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.tfg.modelo.Ejercicio;
import com.example.tfg.modelo.EntrenoSemana;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DiasSemanaEntrenosActivity extends AppCompatActivity {

    private EntrenoSemana entrenoSemana;
    private ArrayList<Ejercicio> ejerciciosSemana;
    private FirebaseFirestore db;
    private CardView l,m,x,j,v,s,d;
    private TextView grupoL, grupoM, grupoX, grupoJ, grupoV, grupoS, grupoD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dias_semana_entrenos);

        db = FirebaseFirestore.getInstance();

        obtenerBundle();

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

    public void obtenerBundle(){
        entrenoSemana = getIntent().getParcelableExtra("entrenoSemanal");
        ejerciciosSemana = getIntent().getParcelableArrayListExtra("ejEntreno");
        entrenoSemana.setEjEntrenoSemanal(ejerciciosSemana);
    }
}
