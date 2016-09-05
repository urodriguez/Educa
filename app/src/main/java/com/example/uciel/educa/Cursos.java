package com.example.uciel.educa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.*;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class Cursos extends AppCompatActivity implements SearchView.OnQueryTextListener {
    LinearLayout linearLayoutCursos;
    SearchView searchView;
    Button btnFiltrar;
    ListView listView;
    DrawerLayout drawerLayout;

    final String[] opciones = {"Cursos disponibles", "Mis cursos", "Diplomas"};
    final CharSequence categorias[] = new CharSequence[] {"Nombre", "Categoria"};
    String filtrandoPor = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        this.cargarMenuLateral();

        this.cargarFiltroYBusqueda();

        linearLayoutCursos = (LinearLayout) findViewById(R.id.LLcursos);

        this.mostrarCursos();
    }


    private void cargarFiltroYBusqueda(){
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
    }

    private void cargarMenuLateral() {
        listView = (ListView) findViewById(R.id.list_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        listView.setAdapter(new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,
                opciones));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2,
                                    long arg3) {
                if(arg2 == 0){
                    Intent cursosIntent = new Intent(Cursos.this,Cursos.class);
                    startActivity(cursosIntent);
                } else {
                    Toast.makeText(Cursos.this, "Mostrando... " + opciones[arg2],
                            Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawers();
                }

            }
        });
    }


    /*
     * mostramos/ocultamos el menu al precionar el icono de la aplicacion
     * ubicado en la barra XXX
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(listView)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(listView);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void mostrarCursos() {
/*        TextView tva = new TextView(this);
        tva.setText("CURSO 1");
        linearLayoutCursos.addView(tva);

        TextView tvs = new TextView(this);
        tvs.setText("CURSO 2");
        linearLayoutCursos.addView(tvs);*/


        View separador = new View(this);
        separador.setMinimumHeight(1);
        separador.setBackgroundColor(Color.GRAY);
        linearLayoutCursos.addView(separador);

        TextView cat1 = new TextView(this);
        cat1.setText("PROGRAMACION");
        linearLayoutCursos.addView(cat1);

        ImageView img = new ImageView (this);
        img.setImageResource(R.drawable.angular);
        img.setLayoutParams(new LinearLayout.LayoutParams(150,150));

        LinearLayout parent = new LinearLayout(this);
        parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        parent.setOrientation(LinearLayout.VERTICAL);

        //children of parent linearlayout


        LinearLayout layout2 = new LinearLayout(this);
        layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout2.setOrientation(LinearLayout.HORIZONTAL);

        layout2.addView(img);

        LinearLayout layout3 = new LinearLayout(this);
        layout3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout3.setOrientation(LinearLayout.VERTICAL);

        layout2.addView(layout3);

        parent.addView(layout2);

        //children of layout2 LinearLayout

        TextView tv1 = new TextView(this);
        TextView tv2 = new TextView(this);

        tv1.setText("Angular JS");
        tv2.setText("Fontela");

        layout3.addView(tv1);
        layout3.addView(tv2);

        RatingBar ratingBar = new RatingBar(this);
        ratingBar.setNumStars(5);
        ratingBar.setRating(5);
        //ratingBar.setVisibility(View.INVISIBLE);

        layout3.addView(ratingBar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                android.util.Log.d("INFO", String.valueOf(rating));
            }
        });

        linearLayoutCursos.addView(parent);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.util.Log.d("INFO", "MOSTRANDO CURSO3");
                Intent descripcionCursoIntent = new Intent(Cursos.this,DescripcionCurso.class);
                startActivity(descripcionCursoIntent);
            }
        });


        ImageView img2 = new ImageView (this);
        img2.setImageResource(R.drawable.cursojs);
        img2.setLayoutParams(new LinearLayout.LayoutParams(150,150));
        linearLayoutCursos.addView(img2);
        img2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.util.Log.d("INFO", "MOSTRANDO CURSO4");
                Intent descripcionCursoIntent = new Intent(Cursos.this,DescripcionCurso.class);
                startActivity(descripcionCursoIntent);
            }
        });


        View separador2 = new View(this);
        separador2.setMinimumHeight(1);
        separador2.setBackgroundColor(Color.GRAY);
        linearLayoutCursos.addView(separador2);

        TextView cat2 = new TextView(this);
        cat2.setText("EXACTAS");
        linearLayoutCursos.addView(cat2);

        ImageView img3 = new ImageView (this);
        img3.setImageResource(R.drawable.matematica);
        img3.setLayoutParams(new LinearLayout.LayoutParams(150,150));
        linearLayoutCursos.addView(img3);
        img3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.util.Log.d("INFO", "MOSTRANDO CURSO4");
                Intent descripcionCursoIntent = new Intent(Cursos.this,DescripcionCurso.class);
                startActivity(descripcionCursoIntent);
            }
        });

/*        for(int i = 5; i < 100; i++){
            TextView txt = new TextView(this);
            txt.setText("CURSO " + String.valueOf(i));
            linearLayoutCursos.addView(txt);

            View separadorr = new View(this);
            separadorr.setMinimumHeight(1);
            separadorr.setBackgroundColor(Color.GRAY);
            linearLayoutCursos.addView(separadorr);
        }*/
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
