package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.modelo.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class EditarDietaActivity extends AppCompatActivity {

    private SeekBar skProteinas, skGrasas;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DocumentReference dr;
    private Usuario u;
    private TextView txtDietaP, txtDietaG;
    private Button btnDieta;
    private double peso, consumoProteinas, consumoGrasas;
    private double totalKcal;
    private TextView txtDietaPPrueba, txtDietaHPrueba, txtDietaGPrueba;
    private double pG, gG, hG, pKcal, gKcal, hKcal;
    private double proteinas, grasas;
    private double totalProteinas, totalHidratos, totalGrasas;
    private ProgressBar pbItemDietaP, pbItemDietaH, pbItemDietaG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_editar_dieta);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        txtDietaP = findViewById(R.id.txtDietaP);
        txtDietaG = findViewById(R.id.txtDietaG);
        txtDietaPPrueba = findViewById(R.id.txtDietaPPrueba);
        txtDietaHPrueba = findViewById(R.id.txtDietaHPrueba);
        txtDietaGPrueba = findViewById(R.id.txtDietaGPrueba);
        pbItemDietaP = findViewById(R.id.pbItemEditarDietaP);
        pbItemDietaH = findViewById(R.id.pbItemEditarDietaH);
        pbItemDietaG = findViewById(R.id.pbItemEditarDietaG);

        dr = db.collection("usuarios").document(firebaseUser.getUid());

        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                u = documentSnapshot.toObject(Usuario.class);

                peso = u.getPeso();
                proteinas = u.getConsumoProteinas();
                grasas = u.getConsumoGrasas();
                totalKcal = u.getTotalKcal();

                double actividad;
                if (u.getActividad().equals("Ninguna")) {
                    actividad = 1.2;
                } else if (u.getActividad().equals("Ligera (1-3 día/semana)")) {
                    actividad = 1.4;
                } else if (u.getActividad().equals("Moderada (3-5 día/semana)")) {
                    actividad = 1.6;
                } else if (u.getActividad().equals("Alta (6-7 día/semana)")) {
                    actividad = 1.8;
                } else {
                    actividad = 2;
                }
                //Operación simple para cálculo calórico
                double kcal = u.getPeso() * 22 * actividad;

                //Operación de Kcal dependiendo del porcentaje elegido
                if (u.getPorcentaje().equals("-10% (Bajada lenta asegurando masa muscular)")) {
                    totalKcal = kcal - (kcal * 0.1);
                } else if (u.getPorcentaje().equals("-20% (Bajada media con riesgo mínima de pérdida de masa muscular)")) {
                    totalKcal = kcal - (kcal * 0.2);
                } else if (u.getPorcentaje().equals("-30% (Bajada rápida pero con alto riesgo de pérdida de masa muscular)")) {
                    totalKcal = kcal - (kcal * 0.3);
                } else if (u.getPorcentaje().equals("+10% (Subida lenta si tienes tendencia a engordar)")) {
                    totalKcal = kcal + (kcal * 0.1);
                } else if (u.getPorcentaje().equals("+15% (Subida moderada)")) {
                    totalKcal = kcal + (kcal * 0.15);
                } else if (u.getPorcentaje().equals("+20% (Subida rápida si te cuesta coger peso)")) {
                    totalKcal = kcal + (kcal * 0.2);
                } else {
                    Toast.makeText(getApplicationContext(), "Debes escoger un porcentaje", Toast.LENGTH_SHORT).show();
                }

                u.setTotalKcal(totalKcal);

                operacionMuestra();

                //Establece el máximo de los progress bar
                pbItemDietaP.setMax((int) (2.5 * peso));
                pbItemDietaH.setMax((int) (totalKcal - ((2.5 * peso) + (1.5 * peso))));
                pbItemDietaG.setMax((int) (1.5 * peso));

                txtDietaP.setText(String.valueOf(proteinas));
                txtDietaG.setText(String.valueOf(grasas));
                txtDietaPPrueba.setText(String.valueOf(Math.round(pG * 100) / 100.f));
                txtDietaHPrueba.setText(String.valueOf(Math.round(hG * 100) / 100.f));
                txtDietaGPrueba.setText(String.valueOf(Math.round(gG * 100) / 100.f));
                pbItemDietaP.setProgress((int) pG);
                pbItemDietaG.setProgress((int) gG);
                pbItemDietaH.setProgress((int) hG);
            }
        });

        skProteinas = findViewById(R.id.skProteinas);
        pbItemDietaP.setProgress((int) pG);
        skProteinas.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Proteinas
                if (progress == 0) {
                    proteinas = 1.8;
                } else if (progress == 1) {
                    proteinas = 1.9;
                } else if (progress == 2) {
                    proteinas = 2;
                } else if (progress == 3) {
                    proteinas = 2.1;
                } else if (progress == 4) {
                    proteinas = 2.2;
                } else if (progress == 5) {
                    proteinas = 2.3;
                } else if (progress == 6) {
                    proteinas = 2.4;
                } else {
                    proteinas = 2.5;
                }
                operacionMuestra();
                txtDietaP.setText(String.valueOf(proteinas));
                pbItemDietaP.setProgress((int) (pG));
                pbItemDietaH.setProgress((int) (hG));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        skGrasas = findViewById(R.id.skGrasas);
        skGrasas.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Grasas
                if (progress == 0) {
                    grasas = 0.5;
                } else if (progress == 1) {
                    grasas = 0.6;
                } else if (progress == 2) {
                    grasas = 0.7;
                } else if (progress == 3) {
                    grasas = 0.8;
                } else if (progress == 4) {
                    grasas = 0.9;
                } else if (progress == 5) {
                    grasas = 1;
                } else if (progress == 6) {
                    grasas = 1.1;
                } else if (progress == 7) {
                    grasas = 1.2;
                } else if (progress == 8) {
                    grasas = 1.3;
                } else if (progress == 9) {
                    grasas = 1.4;
                } else {
                    grasas = 1.5;
                }
                operacionMuestra();
                txtDietaG.setText(String.valueOf(grasas));
                pbItemDietaG.setProgress((int) (gG));
                pbItemDietaH.setProgress((int) (hG));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        btnDieta = findViewById(R.id.btnDieta);
        btnDieta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dr.set(u, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.putExtra("fragment", "3");
                        startActivity(new Intent(i));
                    }
                });
            }
        });
    }

    public void operacionMuestra() {
        //Cálculo de macros en Kcal
        pG = proteinas * peso;
        pKcal = pG * 4;
        gG = grasas * peso;
        gKcal = gG * 9;
        hKcal = totalKcal - (pKcal + gKcal);
        hG = hKcal / 4;

        txtDietaPPrueba.setText(String.valueOf(Math.round(pG * 100) / 100.f));
        txtDietaHPrueba.setText(String.valueOf(Math.round(hG * 100) / 100.f));
        txtDietaGPrueba.setText(String.valueOf(Math.round(gG * 100) / 100.f));
        u.setConsumoProteinas(proteinas);
        u.setConsumoGrasas(grasas);
        u.setTotalProteinas(pG);
        u.setTotalHidratos(hG);
        u.setTotalGrasas(gG);
    }
}
