package com.example.tfg;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.modelo.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EditarObjetivoActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private Usuario usuario;
    private EditText etPesoObjetivo, etPesoActual;
    private Button btnObjetivo, btnFInicio, btnFFinal;
    private Date date;
    private Spinner sPorcentaje;
    private DocumentReference dr;
    private ArrayAdapter<CharSequence> adapter;
    private double actividad;
    private double tmb, kcal, totalKcal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_editar_objetivo);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        etPesoObjetivo = findViewById(R.id.etPesoObjetivo);
        etPesoActual = findViewById(R.id.etPesoActual);
        btnFInicio = findViewById(R.id.btnFInicio);
        btnFFinal = findViewById(R.id.btnFFinal);
        sPorcentaje = findViewById(R.id.sPorcentaje);

        String pattern = "dd-MMM-yyyy";
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        dr = db.collection("usuarios").document(firebaseUser.getUid());

        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usuario = documentSnapshot.toObject(Usuario.class);

                if (usuario.getfInicio() == null) {
                    btnFInicio.setText(String.valueOf(LocalDate.now()));
                } else {
                    btnFInicio.setText(simpleDateFormat.format(usuario.getfInicio()));
                }

                if (usuario.getfFinal() == null) {
                    btnFFinal.setText(String.valueOf(LocalDate.now()));
                } else {
                    btnFFinal.setText(simpleDateFormat.format(usuario.getfFinal()));
                }

                etPesoObjetivo.setText(String.valueOf(usuario.getObjetivo()));
                etPesoActual.setText(String.valueOf(usuario.getPeso()));

                comprobarPesos();
                if (usuario.getPorcentaje().equals("-10% (Bajada lenta asegurando masa muscular)")) {
                    sPorcentaje.setSelection(0);
                } else if (usuario.getPorcentaje().equals("-20% (Bajada media con riesgo mínima de pérdida de masa muscular)")) {
                    sPorcentaje.setSelection(1);
                } else if (usuario.getPorcentaje().equals("-30% (Bajada rápida pero con alto riesgo de pérdida de masa muscular)")) {
                    sPorcentaje.setSelection(2);
                } else if (usuario.getPorcentaje().equals("+10% (Subida lenta si tienes tendencia a engordar)")) {
                    sPorcentaje.setSelection(0);
                } else if (usuario.getPorcentaje().equals("+15% (Subida moderada)")) {
                    sPorcentaje.setSelection(1);
                } else if (usuario.getPorcentaje().equals("+20% (Subida rápida si te cuesta coger peso)")) {
                    sPorcentaje.setSelection(2);
                }

                etPesoActual.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        comprobarPesos();
                    }
                });

                etPesoObjetivo.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        comprobarPesos();
                    }
                });


            }
        });


        btnFInicio.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditarObjetivoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date = new GregorianCalendar(year, month, dayOfMonth).getTime();
                        usuario.setfInicio(date);

                        dr.set(usuario, SetOptions.merge());

                        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                usuario = documentSnapshot.toObject(Usuario.class);
                                btnFInicio.setText(simpleDateFormat.format(usuario.getfInicio()));
                            }
                        });
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btnFFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditarObjetivoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date = new GregorianCalendar(year, month, dayOfMonth).getTime();

                        usuario.setfFinal(date);

                        dr.set(usuario, SetOptions.merge());

                        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                usuario = documentSnapshot.toObject(Usuario.class);
                                btnFFinal.setText(simpleDateFormat.format(usuario.getfFinal()));
                            }
                        });
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();


            }
        });

        btnObjetivo = findViewById(R.id.btnObjetivo);
        btnObjetivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        usuario = documentSnapshot.toObject(Usuario.class);
                        usuario.setObjetivo(Double.parseDouble(etPesoObjetivo.getText().toString()));
                        usuario.setPeso(Double.parseDouble(etPesoActual.getText().toString()));
                        usuario.setPorcentaje(sPorcentaje.getSelectedItem().toString());

                        //Determina actividad
                        if (usuario.getActividad().equals("Ligera (1-3 día/semana)")) {
                            actividad = 1.375;
                        } else if (usuario.getActividad().equals("Moderada (3-5 día/semana)")) {
                            actividad = 1.55;
                        } else if (usuario.getActividad().equals("Alta (6-7 día/semana)")) {
                            actividad = 1.725;
                        } else {
                            actividad = 1.9;
                        }

                        //Operación simple para Tasa metabólica basal calórico
                        if (usuario.getSexo().equals("Hombre")) {
                            tmb = (10 * usuario.getPeso()) + 6.25 * usuario.getAltura() - (5 * usuario.getEdad() + 5);
                        } else {
                            tmb = (10 * usuario.getPeso()) + 6.25 * usuario.getAltura() - (5 * usuario.getEdad() - 161);
                        }

                        //Kcal
                        kcal = tmb * actividad;

                        totalKcal = 0;
                        //Operación de Kcal dependiendo del porcentaje elegido
                        if (usuario.getPorcentaje().equals("-10% (Bajada lenta asegurando masa muscular)")) {
                            totalKcal = kcal - (kcal * 0.1);
                        } else if (usuario.getPorcentaje().equals("-20% (Bajada media con riesgo mínima de pérdida de masa muscular)")) {
                            totalKcal = kcal - (kcal * 0.2);
                        } else if (usuario.getPorcentaje().equals("-30% (Bajada rápida pero con alto riesgo de pérdida de masa muscular)")) {
                            totalKcal = kcal - (kcal * 0.3);
                        } else if (usuario.getPorcentaje().equals("+10% (Subida lenta si tienes tendencia a engordar)")) {
                            totalKcal = kcal + (kcal * 0.1);
                        } else if (usuario.getPorcentaje().equals("+15% (Subida moderada)")) {
                            totalKcal = kcal + (kcal * 0.15);
                        } else if (usuario.getPorcentaje().equals("+20% (Subida rápida si te cuesta coger peso)")) {
                            totalKcal = kcal + (kcal * 0.2);
                        } else {
                            Toast.makeText(getApplicationContext(), "Debes escoger un porcentaje", Toast.LENGTH_SHORT).show();
                        }

                        //Cálculo de macros en Kcal
                        double pG = usuario.getConsumoProteinas() * usuario.getPeso();
                        double pKcal = pG * 4;
                        double gG = usuario.getConsumoGrasas() * usuario.getPeso();
                        double gKcal = gG * 9;
                        double hKcal = totalKcal - (pKcal + gKcal);
                        double hG = hKcal / 4;

                        usuario.setTotalProteinas(pG);
                        usuario.setTotalHidratos(hG);
                        usuario.setTotalGrasas(gG);
                        usuario.setTotalKcal(totalKcal);

                        dr.set(usuario, SetOptions.merge());
                        Intent i = new Intent(EditarObjetivoActivity.this, MainActivity.class);
                        i.putExtra("fragment", "3");
                        startActivity(i);
                    }
                });
            }
        });
    }

    /**
     * Determina si el peso no está establecido, y lo inicializa con valor 0, si el peso objetivo es mayor al actual, determinará que el usuario quiere subir de peso,
     * mientras que si el peso objetivo es menor al actual, determinará que el usurio quiere bajar de peso
     */
    public void comprobarPesos() {

        if (etPesoActual.getText().toString().matches("")) {
            etPesoActual.setText("0");
        }
        if (etPesoObjetivo.getText().toString().matches("")) {
            etPesoObjetivo.setText("0");
        }

        if (Double.parseDouble(etPesoActual.getText().toString()) == 0 || Double.parseDouble(etPesoObjetivo.getText().toString()) == 0) {
            adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.porcentajeVacio, android.R.layout.simple_spinner_dropdown_item);
        } else {
            if (Double.parseDouble(etPesoActual.getText().toString()) > Double.parseDouble(etPesoObjetivo.getText().toString())) {
                adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.porcentajeBajar, android.R.layout.simple_spinner_dropdown_item);
            } else {
                adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.porcentajeSubir, android.R.layout.simple_spinner_dropdown_item);
            }
        }
        sPorcentaje.setAdapter(adapter);
    }
}
