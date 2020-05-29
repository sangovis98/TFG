package com.example.tfg.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tfg.DiasSemanaEntrenosActivity;
import com.example.tfg.R;
import com.example.tfg.adapters.AdapterEntrenosSemanales;
import com.example.tfg.interfaces.OnItemListener;
import com.example.tfg.modelo.EntrenoSemana;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

/**
 * Clase contenedora del fragment entrenos
 */

public class ListaEntrenosFragment extends Fragment implements OnItemListener {

    private FirebaseFirestore db;
    private ImageView imgAddEntrenoSemanal;
    private ArrayList<EntrenoSemana> entrenosSemanales;
    private EntrenoSemana entrenoSemana;
    private RecyclerView recyclerView;
    private AdapterEntrenosSemanales adapterEntrenosSemanales;
    private String nombreDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private LottieAnimationView lottieAnimationView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lista_entrenos, container, false);

        lottieAnimationView = root.findViewById(R.id.loadingEntrenos);
        lottieAnimationView.playAnimation();
        recyclerView = root.findViewById(R.id.listaEntrenosSemanales);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        llenarEntrenoSemanal();

        imgAddEntrenoSemanal = root.findViewById(R.id.imgAddEntrenoSemanal);
        imgAddEntrenoSemanal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Nombre del entreno semanal");

                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setPadding(60, 0, 60, 0);
                final int max = 15;
                final TextView tv = new TextView(getContext());
                tv.setText("0/" + max);
                final EditText editText = new EditText(getActivity());
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max)});
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.toString().length() <= max) {
                            tv.setText(s.toString().length() + "/" + max);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                linearLayout.addView(editText);
                linearLayout.addView(tv);
                dialog.setView(linearLayout);

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
                                .document(firebaseUser.getUid())
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

    /**
     * Hace una consulta a la base de datos y llena la lista de entrenos semanales
     */
    public void llenarEntrenoSemanal(){
        entrenosSemanales = new ArrayList<>();
        db.collection("usuarios")
                .document(firebaseUser.getUid())
                .collection("entrenosSemanales")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot ds : queryDocumentSnapshots){
                            entrenoSemana = ds.toObject(EntrenoSemana.class);
                            entrenosSemanales.add(entrenoSemana);
                        }
                        lottieAnimationView.cancelAnimation();
                        lottieAnimationView.setVisibility(View.GONE);
                        initRecylerView();
                    }
                });
    }

    /**
     * Añade todas las dietas al recycler view mediante el adapter
     */
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

    /**
     * Nos permite acceder al elemento clicado
     *
     * @param position posición del elemento dentro de el adapter
     */
    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(getContext(), DiasSemanaEntrenosActivity.class);
        i.putExtra("entrenoSemanal", entrenosSemanales.get(position));//Entreno semanal en el cual hago click
        startActivity(i);
    }
}
