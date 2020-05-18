package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    private Usuario u;

    EditText eTUsuario;
    EditText eTPass;

    String username;
    String pass;
    int num = 0;

    FirebaseUser user;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        eTUsuario = findViewById(R.id.eTusuario);
        eTPass = findViewById(R.id.eTPass);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();

                //Si el usuario no tiene cuenta
                if (user != null) {
                    if (!user.isEmailVerified()) {
                        Toast.makeText(getApplicationContext(), "Verifique la cuenta en su correo electrónico", Toast.LENGTH_LONG).show();
                    } else if (user.isEmailVerified()) {
                        db.collection("usuarios")
                                .document(user.getUid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        u = task.getResult().toObject(Usuario.class);

                                        db.collection("usuarios").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                u = documentSnapshot.toObject(Usuario.class);

                                                if (u.isPrimeraVez()) {
                                                    startActivity(new Intent(getApplicationContext(), TabbedInicioActivity.class));
                                                } else {
                                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                    i.putExtra("us", u);
                                                    startActivity(i);
                                                }
                                            }
                                        });


                                    }
                                });
                    }
                }
            }
        };

        final ImageView fondoLogin1 = findViewById(R.id.loginImg1);
        final ImageView fondoLogin2 = findViewById(R.id.loginImg2);
        final ImageView fondoLogin3 = findViewById(R.id.loginImg3);
        TextView registrate = findViewById(R.id.txtRegistrate);

        final Timer timer = new Timer();
        //Set the schedule function
        timer.scheduleAtFixedRate(new TimerTask() {
                                      @Override
                                      public void run() {
                                          if (num == 0) {
                                              fondoLogin3.animate().alpha(0).setDuration(1000);
                                              fondoLogin1.animate().alpha(1).setDuration(1000);
                                              num = 1;
                                          } else if (num == 1) {
                                              fondoLogin1.animate().alpha(0).setDuration(1000);
                                              fondoLogin2.animate().alpha(1).setDuration(1000);
                                              num = 2;
                                          } else {
                                              fondoLogin2.animate().alpha(0).setDuration(1000);
                                              fondoLogin3.animate().alpha(1).setDuration(1000);
                                              num = 0;
                                          }
                                      }
                                  },
                0, 8000);   // 1000 Millisecond  = 1 second

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
                //Cancelamos la transición de imagenes del slider
                timer.cancel();
            }
        });

        registrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), RegistroActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if ((authStateListener) != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    public void login(View view) {
        username = eTUsuario.getText().toString();
        pass = eTPass.getText().toString();

        if (username.equals("") || pass.equals("")) {
            Toast.makeText(getApplicationContext(), "Falta alguno de los campos", Toast.LENGTH_LONG).show();
        } else {
            //Manejamos que algo haya salido mal
            firebaseAuth.signInWithEmailAndPassword(username, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Hubo un error al introducir sus datos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
