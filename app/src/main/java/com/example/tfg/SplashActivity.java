package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.modelo.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1000;
    private ImageView fondoSplash;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        fondoSplash = findViewById(R.id.fondoSplash);
        if (firebaseUser != null) {
            db.collection("usuarios").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                Intent i;

                @Override
                public void onSuccess(final DocumentSnapshot documentSnapshot) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (firebaseUser == null) {
                                i = new Intent(getApplicationContext(), LoginActivity.class);
                            } else {
                                if (documentSnapshot.toObject(Usuario.class).isPrimeraVez()) {
                                    i = new Intent(getApplicationContext(), TabbedInicioActivity.class);
                                } else {
                                    i = new Intent(getApplicationContext(), MainActivity.class);
                                }
                            }
                            startActivity(i);
                            finish();
                        }
                    }, SPLASH_TIME_OUT);
                }
            });
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }
}
