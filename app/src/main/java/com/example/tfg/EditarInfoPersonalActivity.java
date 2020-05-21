package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.modelo.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class EditarInfoPersonalActivity extends AppCompatActivity {

    private Spinner spinnerSexo, spinnerActividad;
    private EditText etNombrePerfil, etPerfilEdad, etPerfilAltura;
    private Button btnEnviarPerfil;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Usuario usuario;

    private double tmb, peso, objetivo, actividad, normocalorica, totalKcal;
    private String sexo, progreso;
    private int altura, edad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_editar_info_personal);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        etPerfilEdad = findViewById(R.id.etPerfilEdad);
        etPerfilAltura = findViewById(R.id.etPerfilAltura);

        spinnerSexo = findViewById(R.id.spinnerSexo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.sexo, android.R.layout.simple_spinner_dropdown_item);
        spinnerSexo.setAdapter(adapter);

        spinnerActividad = findViewById(R.id.spinnerActividad);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.actividad, android.R.layout.simple_spinner_dropdown_item);
        spinnerActividad.setAdapter(adapter2);

        etNombrePerfil = findViewById(R.id.etNombrePerfil);

        db.collection("usuarios").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usuario = documentSnapshot.toObject(Usuario.class);

                //Llenar campos
                etPerfilEdad.setText(String.valueOf(usuario.getEdad()));
                etNombrePerfil.setText(usuario.getNombre());
                etPerfilAltura.setText(String.valueOf(usuario.getAltura()));
                if (usuario.getActividad().equals("Ligera (1-3 día/semana)")) {
                    spinnerActividad.setSelection(0);
                } else if (usuario.getActividad().equals("Moderada (3-5 día/semana)")) {
                    spinnerActividad.setSelection(1);
                } else if (usuario.getActividad().equals("Alta (6-7 día/semana)")) {
                    spinnerActividad.setSelection(2);
                } else {
                    spinnerActividad.setSelection(3);
                }

                if (usuario.getSexo().equals("Hombre")) {
                    spinnerSexo.setSelection(0);
                } else if (usuario.getSexo().equals("Mujer")) {
                    spinnerSexo.setSelection(1);
                } else {
                    spinnerSexo.setSelection(2);
                }

                btnEnviarPerfil = findViewById(R.id.btnEnviarPerfil);
                btnEnviarPerfil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llamarUsuario();
                        Intent i = new Intent(EditarInfoPersonalActivity.this, MainActivity.class);
                        i.putExtra("fragment", "3");
                        startActivity(i);
                    }
                });
            }
        });
    }

    public void llamarUsuario() {
        db.collection("usuarios")
                .document(firebaseUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        usuario = documentSnapshot.toObject(Usuario.class);

                        calculoTmb();
                        calculoNormocalorica();
                        calculoTotalKcal();
                        usuario.setSexo(spinnerSexo.getSelectedItem().toString());
                        usuario.setActividad(spinnerActividad.getSelectedItem().toString());
                        usuario.setNombre(etNombrePerfil.getText().toString());
                        usuario.setAltura(Integer.parseInt(etPerfilAltura.getText().toString()));
                        usuario.setEdad(Integer.parseInt(etPerfilEdad.getText().toString()));
                        usuario.setTotalKcal(totalKcal);

                        establecerUsuario();
                    }
                });


    }

    public void establecerUsuario() {
        db.collection("usuarios")
                .document(firebaseUser.getUid())
                .set(usuario, SetOptions.merge());
    }

    public void calculoTmb() {
        if (usuario.getSexo().equals("Hombre") || usuario.getSexo().equals("Otro")) {
            tmb = 10 * usuario.getPeso() + 6.25 * usuario.getAltura() - 5 * usuario.getEdad() + 5;
        } else {
            tmb = 10 * usuario.getPeso() + 6.25 - 5 * usuario.getEdad() - 161;
        }
    }

    public void calculoNormocalorica() {
        if (spinnerActividad.getSelectedItem().toString().equals("Ninguna")) {
            actividad = 1.375;
        } else if (spinnerActividad.getSelectedItem().toString().equals("Ligera (1-3 día/semana)")) {
            actividad = 1.55;
        } else if (spinnerActividad.getSelectedItem().toString().equals("Moderada (3-5 día/semana)")) {
            actividad = 1.725;
        } else if (spinnerActividad.getSelectedItem().toString().equals("Alta (6-7 día/semana)")) {
            actividad = 1.9;
        }

        normocalorica = tmb * actividad;
    }

    public void calculoTotalKcal() {
        if (usuario.getPorcentaje().equals("-10% (Bajada lenta asegurando masa muscular)")) {
            totalKcal = normocalorica - (normocalorica * 0.1);
        } else if (usuario.getPorcentaje().equals("-20% (Bajada media con riesgo mínima de pérdida de masa muscular)")) {
            totalKcal = normocalorica - (normocalorica * 0.2);
        } else if (usuario.getPorcentaje().equals("-30% (Bajada rápida pero con alto riesgo de pérdida de masa muscular)")) {
            totalKcal = normocalorica - (normocalorica * 0.3);
        } else if (usuario.getPorcentaje().equals("+10% (Subida lenta si tienes tendencia a engordar)")) {
            totalKcal = normocalorica + (normocalorica * 0.1);
        } else if (usuario.getPorcentaje().equals("+15% (Subida moderada)")) {
            totalKcal = normocalorica + (normocalorica * 0.15);
        } else if (usuario.getPorcentaje().equals("+20% (Subida rápida si te cuesta coger peso)")) {
            totalKcal = normocalorica + (normocalorica * 0.2);
        } else {
            Toast.makeText(getApplicationContext(), "Debes escoger un porcentaje", Toast.LENGTH_SHORT).show();
        }
    }
}
