package com.example.tfg;

import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.tfg.fragments.TabbedFragment1;
import com.example.tfg.fragments.TabbedFragment2;
import com.example.tfg.fragments.TabbedFragment3;
import com.example.tfg.modelo.Usuario;
import com.example.tfg.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class TabbedInicioActivity extends AppCompatActivity implements TabbedFragment1.OnFragmentInteractionListener,
        TabbedFragment2.OnFragmentInteractionListener,
        TabbedFragment3.OnFragmentInteractionListener {

    ViewPager viewPager;
    private LinearLayout linearLayout;
    private TextView[] puntosSlide;

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private Usuario usuario;
    private EditText etPesoPrimeraVez, etAlturaPrimeraVez, etObjetivoPrimeraVez, etEdadPrimeraVez;
    private Spinner sSexoPrimeraVez, sActividadDiariaPrimeraVez;
    private Button btnPrimeraVez;
    private TextView primeraVez;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_inicio);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        linearLayout = findViewById(R.id.llintro);
        agregarIndicadorPuntos(0);
        viewPager.addOnPageChangeListener(viewListener);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        linearLayout = findViewById(R.id.llintro);
        agregarIndicadorPuntos(0);
    }

    private void agregarIndicadorPuntos(int pos) {
        puntosSlide = new TextView[3];
        linearLayout.removeAllViews();

        for (int i = 0; i < puntosSlide.length; i++) {
            puntosSlide[i] = new TextView(this);
            puntosSlide[i].setText(Html.fromHtml("&#8226;"));
            puntosSlide[i].setTextSize(35);
            puntosSlide[i].setTextColor(getResources().getColor(R.color.orangeTransparent));
            linearLayout.addView(puntosSlide[i]);
        }

        if (puntosSlide.length > 0) {
            puntosSlide[pos].setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            agregarIndicadorPuntos(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void OnFragmentInteractionListener(Uri uri) {

    }
}