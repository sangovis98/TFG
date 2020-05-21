package com.example.tfg.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.tfg.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabbedFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabbedFragment2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText etObjetivoPrimeraVez;
    private Spinner sProgreso;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TabbedFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabbedFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static TabbedFragment2 newInstance(String param1, String param2) {
        TabbedFragment2 fragment = new TabbedFragment2();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tabbed2, container, false);

        sProgreso = root.findViewById(R.id.sProgres);

        etObjetivoPrimeraVez = root.findViewById(R.id.etObjetivoPrimeraVez);
        etObjetivoPrimeraVez.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                EditText etPeso = getActivity().findViewById(R.id.etPesoPrimeraVez);

                if (!etObjetivoPrimeraVez.getText().toString().trim().equals("") && !etPeso.getText().toString().trim().equals("")) {
                    if (Double.parseDouble(etPeso.getText().toString()) > Double.parseDouble(s.toString())) {
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.porcentajeBajar, android.R.layout.simple_spinner_dropdown_item);
                        sProgreso.setAdapter(adapter);
                    } else if (Double.parseDouble(etPeso.getText().toString()) < Double.parseDouble(etObjetivoPrimeraVez.getText().toString())) {
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.porcentajeSubir, android.R.layout.simple_spinner_dropdown_item);
                        sProgreso.setAdapter(adapter);
                    }
                }
            }
        });

        return root;
    }

    public interface OnFragmentInteractionListener {
        void OnFragmentInteractionListener(Uri uri);
    }
}
