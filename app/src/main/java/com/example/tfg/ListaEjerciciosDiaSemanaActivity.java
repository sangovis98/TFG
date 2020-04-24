package com.example.tfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfg.adapters.AdapterDiaDieta;
import com.example.tfg.adapters.AdapterEjercicios;
import com.example.tfg.interfaces.OnItemListener;
import com.example.tfg.modelo.DiaDieta;
import com.example.tfg.modelo.Ejercicio;
import com.example.tfg.modelo.EntrenoSemana;
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

public class ListaEjerciciosDiaSemanaActivity extends AppCompatActivity implements OnItemListener {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private ArrayList<Ejercicio> ejsSegunDia;
    private AdapterEjercicios adapterEjercicios;
    private EntrenoSemana entrenoSemana;
    private String nDia;
    private ArrayList<Ejercicio> ejercicios;
    private TextView txtNDia;
    private ImageView ivAddEjercicioDia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ejercicios_dia_semana);

        db = FirebaseFirestore.getInstance();

        obtenerBundle();

        ivAddEjercicioDia = findViewById(R.id.ivAddEjercicioDia);
        ivAddEjercicioDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListaEjerciciosActivity.class);
                i.putExtra("nDia", nDia);
                i.putExtra("entrenoSemanal", entrenoSemana);
                startActivity(i);
                finish();
            }
        });

        txtNDia = findViewById(R.id.txtNDia);
        txtNDia.setText(nDia);

        llenarEjerciciosDia();
    }

    public void initRecylerView(){
        recyclerView = findViewById(R.id.listaEjerciciosDia);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setHasFixedSize(true);
        if (ejsSegunDia == null){
            Toast.makeText(getApplicationContext(), "No tienes ejercicios en este día", Toast.LENGTH_SHORT).show();
        }else {
            adapterEjercicios = new AdapterEjercicios(ejsSegunDia, this);
            recyclerView.setAdapter(adapterEjercicios);
        }
    }

    /**
     * Llena el arraylist de diaDietas y llama a la funcion initrecyclerview
     */
    public void llenarEjerciciosDia(){
        ejercicios = new ArrayList<>();
        ejsSegunDia = new ArrayList<>();
        db.collection("usuarios")
                .document("KVNZAq8vBvgCB4pP4iJv")
                .collection("entrenosSemanales")
                .document(entrenoSemana.getNombre())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ejercicios = documentSnapshot.toObject(EntrenoSemana.class).getEjEntrenoSemanal();
                        if (ejercicios != null){
                            for (Ejercicio e : ejercicios){
                                if (e.getnDia().equals(nDia)){
                                    ejsSegunDia.add(e);
                                }
                            }
                            initRecylerView();
                        }
                    }
                });
    }

    @Override
    public void onItemClick(final int position) {
        android.app.AlertDialog.Builder dialog = new AlertDialog.Builder(ListaEjerciciosDiaSemanaActivity.this);
        dialog.setTitle("¿Cuánto vas a ejercitar?");

        LinearLayout layout = new LinearLayout(ListaEjerciciosDiaSemanaActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText repeticiones = new EditText(getApplicationContext());
        repeticiones.setInputType(InputType.TYPE_CLASS_TEXT);
        repeticiones.setHint("Repeticiones");
        layout.addView(repeticiones);
        final EditText series = new EditText(getApplicationContext());
        series.setInputType(InputType.TYPE_CLASS_TEXT);
        series.setHint("Series");
        layout.addView(series);

        dialog.setView(layout);

        dialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Ejercicio seleccionado
                Ejercicio e = new Ejercicio(ejercicios.get(position).getId(), ejercicios.get(position).getNombre(), ejercicios.get(position).getRepeticiones(), ejercicios.get(position).getSeries(), ejercicios.get(position).getGrupo(), ejercicios.get(position).getnDia());

                for (Ejercicio ej : ejercicios){
                    if (ej.getId() == e.getId()){
                        ej.setSeries(Integer.parseInt(series.getText().toString()));
                        ej.setRepeticiones(Integer.parseInt(repeticiones.getText().toString()));
                    }
                }

                Map<String, Object> map = new HashMap<>();
                map.put("ejEntrenoSemanal", ejercicios);

                //Lectura de base de datos
                db.collection("usuarios")
                        .document("KVNZAq8vBvgCB4pP4iJv")
                        .collection("entrenosSemanales")
                        .document(entrenoSemana.getNombre())
                        .set(map, SetOptions.merge());

                entrenoSemana.setEjEntrenoSemanal(ejercicios);

                llenarEjerciciosDia();
                dialog.dismiss();
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

    public void obtenerBundle(){
        nDia = getIntent().getStringExtra("nDia");
        entrenoSemana = getIntent().getParcelableExtra("entrenoSemanal");
    }
}
