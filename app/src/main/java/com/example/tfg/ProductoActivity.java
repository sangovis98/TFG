package com.example.tfg;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.modelo.Producto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProductoActivity extends AppCompatActivity {

    private Producto p;
    private Producto producto;
    private FirebaseFirestore db;

    private TextView txtProductoNombre;
    private ImageView ivProductoAdd;
    private String momentoDia;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        p = getIntent().getParcelableExtra("producto");
        momentoDia = getIntent().getStringExtra("momentoDia");

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        txtProductoNombre = findViewById(R.id.txtProductoNombre);
        txtProductoNombre.setText(p.getNombre());

        ivProductoAdd = findViewById(R.id.btnProductoAdd);
        ivProductoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                map.put("nombre", p.getNombre());
                map.put("momentoDia", momentoDia);
                map.put("proteinas", 23.2);
                map.put("hidratos", 23.2);
                map.put("grasas", 3.3);

                db.collection("usuarios").document(firebaseUser.getUid()).collection("productosDieta").add(map);
            }
        });
    }
}
