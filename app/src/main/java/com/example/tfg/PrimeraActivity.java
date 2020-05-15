package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.modelo.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class PrimeraActivity extends AppCompatActivity {

    TextView primeraVez, etPesoPrimeraVez, etAlturaPrimeraVez, etObjetivoPrimeraVez, etEdadPrimeraVez;
    Spinner sSexoPrimeraVez, sActividadDiariaPrimeraVez;
    Button btnPrimeraVez;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primera);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        primeraVez = findViewById(R.id.txtPrimeraVez);
        db.collection("usuarios").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usuario = documentSnapshot.toObject(Usuario.class);

                primeraVez.setText("Bienvenido " + usuario.getNombre() + "!");

                etPesoPrimeraVez = findViewById(R.id.etPesoPrimeraVez);

                etAlturaPrimeraVez = findViewById(R.id.etAlturaPrimeraVez);

                etObjetivoPrimeraVez = findViewById(R.id.etObjetivoPrimeraVez);

                sSexoPrimeraVez = findViewById(R.id.sSexoPrimeraVez);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.sexo, android.R.layout.simple_spinner_dropdown_item);
                sSexoPrimeraVez.setAdapter(adapter);

                sActividadDiariaPrimeraVez = findViewById(R.id.sActividadPrimeraVez);
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.actividad, android.R.layout.simple_spinner_dropdown_item);
                sActividadDiariaPrimeraVez.setAdapter(adapter1);

                etEdadPrimeraVez = findViewById(R.id.etEdadPrimeraVez);

                btnPrimeraVez = findViewById(R.id.btnPrimeraVez);
                btnPrimeraVez.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!etPesoPrimeraVez.getText().toString().trim().equals("") &&
                                !etAlturaPrimeraVez.getText().toString().trim().equals("") &&
                                !etEdadPrimeraVez.getText().toString().trim().equals("") &&
                                !etObjetivoPrimeraVez.getText().toString().trim().equals("")) {
                            Map<String, Object> update = new HashMap<>();
                            update.put("peso", Double.parseDouble(etPesoPrimeraVez.getText().toString()));
                            update.put("altura", Integer.parseInt(etAlturaPrimeraVez.getText().toString()));
                            update.put("objetivo", Integer.parseInt(etObjetivoPrimeraVez.getText().toString()));
                            update.put("sexo", sSexoPrimeraVez.getSelectedItem().toString());
                            update.put("edad", Integer.parseInt(etEdadPrimeraVez.getText().toString()));
                            update.put("actividad", sActividadDiariaPrimeraVez.getSelectedItem().toString());
                            update.put("primeraVez", false);
                            db.collection("usuarios").document(firebaseUser.getUid()).set(update, SetOptions.merge());
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Introduzca todos los campos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
