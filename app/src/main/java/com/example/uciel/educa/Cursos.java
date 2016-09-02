package com.example.uciel.educa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.*;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

public class Cursos extends AppCompatActivity implements SearchView.OnQueryTextListener {
    LinearLayout linearLayoutCursos;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        searchView = (SearchView) findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(this);


        linearLayoutCursos = (LinearLayout) findViewById(R.id.LLcursos);

        TextView tva = new TextView(this);
        tva.setText("CURSO1");
        linearLayoutCursos.addView(tva);

        TextView tvs = new TextView(this);
        tvs.setText("CURSO2");
        linearLayoutCursos.addView(tvs);

        ImageView img = new ImageView (this);
        img.setImageResource(R.drawable.test2);
        linearLayoutCursos.addView(img);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.util.Log.d("INFO", "MOSTRANDO CURSO3");
                Intent descripcionCursoIntent = new Intent(Cursos.this,DescripcionCurso.class);
                startActivity(descripcionCursoIntent);
            }
        });

        ImageView img2 = new ImageView (this);
        img2.setImageResource(R.drawable.test3);
        linearLayoutCursos.addView(img2);
        img2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.util.Log.d("INFO", "MOSTRANDO CURSO4");
                Intent descripcionCursoIntent = new Intent(Cursos.this,DescripcionCurso.class);
                startActivity(descripcionCursoIntent);
            }
        });

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // User pressed the search button
        android.util.Log.d("INFO", query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        return false;
    }
}
