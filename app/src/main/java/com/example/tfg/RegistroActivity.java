package com.example.tfg;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistroActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    FirebaseFirestore db;

    EditText regEmail;
    EditText regPass;
    EditText regNombre;

    private Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_registro);

        db = FirebaseFirestore.getInstance();

        regEmail = findViewById(R.id.regEmail);
        regPass = findViewById(R.id.regPassword);
        regNombre = findViewById(R.id.regNombre);

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //Toast.makeText(getApplicationContext(), "El usuario fue creado", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getApplicationContext(), "El usuario salió de la sesión", Toast.LENGTH_SHORT).show();
                }
            }
        };

        Button btnReg = findViewById(R.id.btnReg);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (regNombre.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Introduce un nombre de usuario válido", Toast.LENGTH_SHORT).show();
                } else {
                    singUp(v);
                }
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

    public void singUp(View view) {
        String username = regEmail.getText().toString().trim();
        String pass = regPass.getText().toString().trim();

        firebaseAuth.createUserWithEmailAndPassword(username, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Hubo un error en crear usuario", Toast.LENGTH_SHORT).show();
                } else {
                    u = new Usuario(regNombre.getText().toString(), regPass.getText().toString(), regEmail.getText().toString(), "", 0, "", 0, 2.1, 0, 0.9, 0, 0, 0, null, null, null, null, "", "", 0, 0, 0, true);

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    user.sendEmailVerification();

                    db.collection("usuarios").document(user.getUid()).set(u);

                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            }
        });
    }
}
