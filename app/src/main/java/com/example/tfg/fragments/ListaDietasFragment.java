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
import android.widget.TextView;
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
import com.example.tfg.modelo.Producto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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

        recyclerView = root.findViewById(R.id.listaDietasDias);


/*
        ArrayList<Producto> productos = new ArrayList<>();
        productos.add(new Producto(1,"Pera", 12.3, 23.4, 23.4));
        productos.add(new Producto(2,"Manzana", 30.3, 32.4, 34.4));
        productos.add(new Producto(3,"Kiwi", 4.3, 2.3, 3.2));
        productos.add(new Producto(4,"Leche", 1.1, 3.3, 34.4));
        productos.add(new Producto(5,"Nuez", 2.2, 3.3, 3.3));

        diaDieta = new DiaDieta("dietachuli", 3600, 120.2, 600.3, 90.4, productos);

        db.collection("usuarios").document("KVNZAq8vBvgCB4pP4iJv").collection("diasDietas").document(diaDieta.getnDia()).set(diaDieta);

*/
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

        //Crear nuevo DiaDieta
        imageView = root.findViewById(R.id.imgAddDiaDieta);
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
        adapterDiaDieta = new AdapterDiaDieta(dietasDias, this);
        recyclerView.setAdapter(adapterDiaDieta);
    }

    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(getActivity(), DiaDietaActivity.class);
        i.putExtra("dieta", dietasDias.get(position));
        i.putParcelableArrayListExtra("dietas", dietasDias.get(position).getProductos());
        startActivity(i);
    }
}
