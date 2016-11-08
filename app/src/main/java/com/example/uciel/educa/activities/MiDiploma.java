package com.example.uciel.educa.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.uciel.educa.R;
import com.example.uciel.educa.domain.SingletonUserLogin;

public class MiDiploma extends AppCompatActivity {
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_diploma);

        extras = getIntent().getExtras();

        String nombreCurso = extras.getString("NOMBRE_CURSO");
        String fechaInicio = extras.getString("FECHA_INICIO");

        TextView tvNombreCurso = (TextView) findViewById(R.id.nombre_curso);
        TextView tvNombreAlumno = (TextView) findViewById(R.id.nombre_alumno);
        TextView tvFechaInicio = (TextView) findViewById(R.id.fecha_inicio);

        tvNombreCurso.setText(nombreCurso);
        tvNombreAlumno.setText(SingletonUserLogin.getInstance().getUserName());
        tvFechaInicio.setText(fechaInicio);
    }
}
