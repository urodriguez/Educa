package com.example.uciel.educa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.*;
import android.view.View;
import android.widget.Button;

public class DescripcionCurso extends AppCompatActivity {

    Button btnInscribirse, btnAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_curso);

        btnInscribirse = (Button) findViewById(R.id.buttonInscribirse);
        btnAtras = (Button) findViewById(R.id.buttonAtras);

        btnInscribirse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.util.Log.d("MSG: ", "TE HAS INSCRIPTO");
            }
        });

        btnAtras.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent cursosIntent = new Intent(DescripcionCurso.this,Cursos.class);
                startActivity(cursosIntent);
            }
        });
    }
}
