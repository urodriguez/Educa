package com.example.uciel.educa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

public class Cursos extends AppCompatActivity implements SearchView.OnQueryTextListener {
    SearchView searchView;
    ListView listView;
    DrawerLayout drawerLayout;

    private List<Curso> cursos;
    private RecyclerView rv;
    private TextView tvCategoriaActual;

    final String[] opciones = {"Cursos disponibles", "Mis cursos", "Diplomas"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        //this.cargarMenuLateral();

        tvCategoriaActual = (TextView) findViewById(R.id.categoriaActual);
        tvCategoriaActual.setText("Estas en: ");

        this.cargarFiltroYBusqueda();

        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();
    }

    private void initializeData(){
        cursos = new ArrayList<>();
        cursos.add(new Curso("Angular", "Jose", R.drawable.angular));
        cursos.add(new Curso("JavaScript", "Carlos", R.drawable.cursojs));
        cursos.add(new Curso("Matematica", "Juan", R.drawable.matematica));
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(cursos, "VERTICAL");
        rv.setAdapter(adapter);
    }


    private void cargarFiltroYBusqueda(){
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        // User pressed the search button
        android.util.Log.d("INFO", "Buscando: " + query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        return false;
    }
}
