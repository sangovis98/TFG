package com.example.tfg.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.DiaDietaActivity;
import com.example.tfg.R;
import com.example.tfg.adapters.AdapterDiaDieta;
import com.example.tfg.interfaces.OnItemListener;
import com.example.tfg.modelo.DiaDieta;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;


public class ListaDietasFragment extends Fragment implements OnItemListener {
    private RecyclerView recyclerView;
    private ImageView imageView;
    private ArrayList<DiaDieta> dietasDias;
    private FirebaseFirestore db;
    private DiaDieta diaDieta;
    private AdapterDiaDieta adapterDiaDieta;
    private String nombreDialog;

    LinearLayoutManager linearLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lista_dietas, container, false);

        db = FirebaseFirestore.getInstance();

        recyclerView = root.findViewById(R.id.listaEntrenosSemanales);

        llenarDiaDietas();

        //Crear nuevo DiaDieta
        imageView = root.findViewById(R.id.imgAddEntrenoSemanal);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Nombre del dia Dieta");

                final EditText editText = new EditText(getActivity());
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                dialog.setView(editText);

                dialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nombreDialog = editText.getText().toString();
                        //Comprobueba que no hay una dieta con el mismo nombre
                        for (DiaDieta diaDieta : dietasDias){
                            if (nombreDialog.equals(diaDieta.getnDia())){
                                Toast.makeText(getContext(), "Ya existe una dieta con ese nombre", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                        //Creamos dieta
                        diaDieta = new DiaDieta(dietasDias.size()+1, nombreDialog, 0, 0, 0, 0, null);
                        db.collection("usuarios")
                                .document("KVNZAq8vBvgCB4pP4iJv")
                                .collection("diasDietas")
                                .document(nombreDialog)
                                .set(diaDieta, SetOptions.merge());

                        llenarDiaDietas();
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

    public void initRecylerView(){
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        if (dietasDias == null){
            Toast.makeText(getContext(), "No tienes dietas", Toast.LENGTH_SHORT).show();
        }else {
            adapterDiaDieta = new AdapterDiaDieta(dietasDias, this);
            recyclerView.setAdapter(adapterDiaDieta);
        }
    }

    /**
     * Llena el arraylist de diaDietas y llama a la funcion initrecyclerview
     */
    public void llenarDiaDietas(){
        dietasDias = new ArrayList<>();
        db.collection("usuarios")
                .document("KVNZAq8vBvgCB4pP4iJv")
                .collection("diasDietas")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot ds : queryDocumentSnapshots){
                            diaDieta = ds.toObject(DiaDieta.class);
                            dietasDias.add(diaDieta);
                        }
                        initRecylerView();
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(getActivity(), DiaDietaActivity.class);
        i.putExtra("dieta", dietasDias.get(position));
        i.putParcelableArrayListExtra("dietas", dietasDias.get(position).getProductos());
        startActivity(i);
    }
}
