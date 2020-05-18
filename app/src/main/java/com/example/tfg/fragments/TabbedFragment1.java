package com.example.tfg.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.tfg.R;
import com.example.tfg.modelo.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabbedFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabbedFragment1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TextView primeraVez;
    private EditText etPesoPrimeraVez, etAlturaPrimeraVez, etEdadPrimeraVez;
    private Spinner sSexoPrimeraVez;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TabbedFragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabbedFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static TabbedFragment1 newInstance(String param1, String param2) {
        TabbedFragment1 fragment = new TabbedFragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_tabbed1, container, false);
        db.collection("usuarios").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Usuario usuario = documentSnapshot.toObject(Usuario.class);

                primeraVez = root.findViewById(R.id.txtPrimeraVez);
                primeraVez.setText("Bienvenido " + usuario.getNombre() + "!");

                etPesoPrimeraVez = root.findViewById(R.id.etPesoPrimeraVez);

                etAlturaPrimeraVez = root.findViewById(R.id.etAlturaPrimeraVez);

                etEdadPrimeraVez = root.findViewById(R.id.etEdadPrimeraVez);

                sSexoPrimeraVez = root.findViewById(R.id.sSexoPrimeraVez);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.sexo, android.R.layout.simple_spinner_dropdown_item);
                sSexoPrimeraVez.setAdapter(adapter);
            }
        });

        return root;
    }

    public interface OnFragmentInteractionListener {
        void OnFragmentInteractionListener(Uri uri);
    }
}
