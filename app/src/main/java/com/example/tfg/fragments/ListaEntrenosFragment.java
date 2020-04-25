package com.example.tfg.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.DiasSemanaEntrenosActivity;
import com.example.tfg.R;
import com.example.tfg.adapters.AdapterDiaDieta;
import com.example.tfg.adapters.AdapterEntrenosSemanales;
import com.example.tfg.interfaces.OnItemListener;
import com.example.tfg.modelo.DiaDieta;
import com.example.tfg.modelo.Ejercicio;
import com.example.tfg.modelo.EntrenoSemana;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ListaEntrenosFragment extends Fragment implements OnItemListener {

    private FirebaseFirestore db;
    private ImageView imgAddEntrenoSemanal;
    private ArrayList<EntrenoSemana> entrenosSemanales;
    private EntrenoSemana entrenoSemana;
    private RecyclerView recyclerView;
    private AdapterEntrenosSemanales adapterEntrenosSemanales;
    private String nombreDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lista_entrenos, container, false);

        recyclerView = root.findViewById(R.id.listaEntrenosSemanales);

        db = FirebaseFirestore.getInstance();
/*
        EntrenoSemana es = new EntrenoSemana(1, "Entreno", 999, null);
        Ejercicio ej = new Ejercicio(1, "Elevaciones laterales", 15, 3, "Hombros", "Lunes");

        db.collection("usuarios")
                .document("KVNZAq8vBvgCB4pP4iJv")
                .collection("entrenosSemanales")
                .document(es.getNombre())
                .set(es, SetOptions.merge());

        Map<String, Object> map = new HashMap<>();
        map.put("ejEntrenoSemanal", FieldValue.arrayUnion(ej));

        db.collection("usuarios")
                .document("KVNZAq8vBvgCB4pP4iJv")
                .collection("entrenosSemanales")
                .document(es.getNombre())
                .set(map, SetOptions.merge());
*/
        llenarEntrenoSemanal();

        imgAddEntrenoSemanal = root.findViewById(R.id.imgAddEntrenoSemanal);
        imgAddEntrenoSemanal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Nombre del entreno semanal");

                final EditText editText = new EditText(getActivity());
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                dialog.setView(editText);

                dialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nombreDialog = editText.getText().toString();
                        //Comprobueba que no hay un entreno con la misma dieta
                        for (EntrenoSemana es : entrenosSemanales){
                            if (nombreDialog.equals(es.getNombre())){
                                Toast.makeText(getContext(), "Ya existe una dieta con ese nombre", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                        //Creamos entreno semanal
                        entrenoSemana = new EntrenoSemana(entrenosSemanales.size() + 1, nombreDialog, null);
                        db.collection("usuarios")
                                .document("KVNZAq8vBvgCB4pP4iJv")
                                .collection("entrenosSemanales")
                                .document(nombreDialog)
                                .set(entrenoSemana, SetOptions.merge());

                        llenarEntrenoSemanal();
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
        });

        return root;
    }

    public void llenarEntrenoSemanal(){
        entrenosSemanales = new ArrayList<>();
        db.collection("usuarios")
                .document("KVNZAq8vBvgCB4pP4iJv")
                .collection("entrenosSemanales")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot ds : queryDocumentSnapshots){
                            entrenoSemana = ds.toObject(EntrenoSemana.class);
                            entrenosSemanales.add(entrenoSemana);
                        }
                        initRecylerView();
                    }
                });
    }

    public void initRecylerView(){
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setHasFixedSize(true);
        if (entrenosSemanales == null){
            Toast.makeText(getContext(), "No tienes entrenos semanales", Toast.LENGTH_SHORT).show();
        }else {
            adapterEntrenosSemanales = new AdapterEntrenosSemanales(entrenosSemanales, this);
            recyclerView.setAdapter(adapterEntrenosSemanales);
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(getContext(), DiasSemanaEntrenosActivity.class);
        i.putExtra("entrenoSemanal", entrenosSemanales.get(position));//Entreno semanal en el cual hago click
        i.putExtra("ejEntreno", entrenosSemanales.get(position).getEjEntrenoSemanal());//Ejercicios del entreno semanal
        startActivity(i);
    }
}