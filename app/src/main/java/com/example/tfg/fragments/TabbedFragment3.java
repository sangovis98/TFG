package com.example.tfg.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.tfg.MainActivity;
import com.example.tfg.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabbedFragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabbedFragment3 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button btnPrimeraVez;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private Spinner sActividadPrimeraVez;
    //Variables de cálculo fitness
    private double tmb, peso, objetivo, actividad, normocalorica, totalKcal;
    private String sexo, progreso;
    private int altura, edad;

    EditText etPesoPrimeraVez, etAlturaPrimeraVez, etEdadPrimeraVez, etObjetivoPrimeraVez;
    Spinner sSexoPrimeraVez, sProgreso;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabbedFragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static TabbedFragment3 newInstance(String param1, String param2) {
        TabbedFragment3 fragment = new TabbedFragment3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TabbedFragment3() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_tabbed3, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        sActividadPrimeraVez = root.findViewById(R.id.sActividadPrimeraVez);
        final ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(), R.array.actividad, android.R.layout.simple_spinner_dropdown_item);
        sActividadPrimeraVez.setAdapter(adapter3);

        etPesoPrimeraVez = getActivity().findViewById(R.id.etPesoPrimeraVez);
        etAlturaPrimeraVez = getActivity().findViewById(R.id.etAlturaPrimeraVez);
        etEdadPrimeraVez = getActivity().findViewById(R.id.etEdadPrimeraVez);
        etObjetivoPrimeraVez = getActivity().findViewById(R.id.etObjetivoPrimeraVez);
        sSexoPrimeraVez = getActivity().findViewById(R.id.sSexoPrimeraVez);
        sProgreso = getActivity().findViewById(R.id.sProgres);

        btnPrimeraVez = root.findViewById(R.id.btnPrimeraVez);
        btnPrimeraVez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etPesoPrimeraVez.getText().toString().trim().equals("") &&
                        !etAlturaPrimeraVez.getText().toString().trim().equals("") &&
                        !etEdadPrimeraVez.getText().toString().trim().equals("") &&
                        !etObjetivoPrimeraVez.getText().toString().trim().equals("")) {

                    //Relleno de variables
                    peso = Double.parseDouble(etPesoPrimeraVez.getText().toString().trim());
                    altura = Integer.parseInt(etAlturaPrimeraVez.getText().toString().trim());
                    edad = Integer.parseInt(etEdadPrimeraVez.getText().toString().trim());
                    objetivo = Double.parseDouble(etObjetivoPrimeraVez.getText().toString().trim());
                    sexo = sSexoPrimeraVez.getSelectedItem().toString();
                    progreso = sProgreso.getSelectedItem().toString();

                    Map<String, Object> update = new HashMap<>();
                    update.put("peso", peso);
                    update.put("altura", altura);
                    update.put("edad", edad);
                    update.put("objetivo", objetivo);
                    update.put("porcentaje", progreso);
                    update.put("actividad", sActividadPrimeraVez.getSelectedItem().toString());
                    update.put("sexo", sexo);
                    update.put("primeraVez", false);

                    calculoTmb();

                    calculoNormocalorica();

                    calculoTotalKcal();

                    update.put("totalKcal", (int) totalKcal);

                    double pG, gG, hG, pKcal, gKcal, hKcal;

                    pG = 2 * Integer.parseInt(etPesoPrimeraVez.getText().toString());
                    pKcal = pG * 4;
                    gG = 1 * Integer.parseInt(etPesoPrimeraVez.getText().toString());
                    gKcal = gG * 9;
                    hKcal = totalKcal - (pKcal + gKcal);
                    hG = hKcal / 4;

                    update.put("consumoProteinas", 2);
                    update.put("consumoHidratos", 0);
                    update.put("consumoGrasas", 1);
                    update.put("totalProteinas", pG);
                    update.put("totalHidratos", hG);
                    update.put("totalGrasas", gG);

                    db.collection("usuarios").document(firebaseUser.getUid()).set(update, SetOptions.merge());
                    startActivity(new Intent(getContext(), MainActivity.class));
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "Introduzca todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    public void calculoTmb() {
        if (sexo.equals("Hombre") || sexo.equals("Otro")) {
            tmb = 10 * peso + 6.25 * altura - 5 * edad + 5;
        } else {
            tmb = 10 * peso + 6.25 - 5 * edad - 161;
        }
    }

    public void calculoNormocalorica() {
        if (sActividadPrimeraVez.getSelectedItem().toString().equals("Ninguna")) {
            actividad = 1.375;
        } else if (sActividadPrimeraVez.getSelectedItem().toString().equals("Ligera (1-3 día/semana)")) {
            actividad = 1.55;
        } else if (sActividadPrimeraVez.getSelectedItem().toString().equals("Moderada (3-5 día/semana)")) {
            actividad = 1.725;
        } else if (sActividadPrimeraVez.getSelectedItem().toString().equals("Alta (6-7 día/semana)")) {
            actividad = 1.9;
        }

        normocalorica = tmb * actividad;
    }

    public void calculoTotalKcal() {
        if (progreso.equals("-10% (Bajada lenta asegurando masa muscular)")) {
            totalKcal = normocalorica - (normocalorica * 0.1);
        } else if (progreso.equals("-20% (Bajada media con riesgo mínima de pérdida de masa muscular)")) {
            totalKcal = normocalorica - (normocalorica * 0.2);
        } else if (progreso.equals("-30% (Bajada rápida pero con alto riesgo de pérdida de masa muscular)")) {
            totalKcal = normocalorica - (normocalorica * 0.3);
        } else if (progreso.equals("+10% (Subida lenta si tienes tendencia a engordar)")) {
            totalKcal = normocalorica + (normocalorica * 0.1);
        } else if (progreso.equals("+15% (Subida moderada)")) {
            totalKcal = normocalorica + (normocalorica * 0.15);
        } else if (progreso.equals("+20% (Subida rápida si te cuesta coger peso)")) {
            totalKcal = normocalorica + (normocalorica * 0.2);
        } else {
            Toast.makeText(getContext(), "Debes escoger un porcentaje", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnFragmentInteractionListener {
        void OnFragmentInteractionListener(Uri uri);
    }
}
