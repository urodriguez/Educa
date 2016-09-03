package com.example.uciel.educa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.*;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

public class Cursos extends AppCompatActivity implements SearchView.OnQueryTextListener {
    LinearLayout linearLayoutCursos;
    SearchView searchView;
    Button btnFiltrar;

    final CharSequence categorias[] = new CharSequence[] {"Nombre", "Categoria"};
    String filtrandoPor = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);

        btnFiltrar = (Button) findViewById(R.id.buttonFiltrar);
        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Cursos.this);
                builder.setTitle("Selecciona un filtro");
                builder.setItems(categorias, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        searchView.setQueryHint("Buscar por " + categorias[which]);
                        filtrandoPor = categorias[which].toString();
                    }
                });
                builder.show();
            }
        });

        linearLayoutCursos = (LinearLayout) findViewById(R.id.LLcursos);

        this.mostrarCursos();
    }

    private void mostrarCursos() {
        TextView tva = new TextView(this);
        tva.setText("CURSO 1");
        linearLayoutCursos.addView(tva);

        TextView tvs = new TextView(this);
        tvs.setText("CURSO 2");
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

        for(int i = 5; i < 150; i++){
            TextView txt = new TextView(this);
            txt.setText("CURSO " + String.valueOf(i));
            linearLayoutCursos.addView(txt);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // User pressed the search button
        android.util.Log.d("INFO", "Buscando: " + query + " - Filtro: " + filtrandoPor);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        return false;
    }
}
