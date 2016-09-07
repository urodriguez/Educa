package com.example.uciel.educa;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements android.widget.SearchView.OnQueryTextListener{

    android.widget.SearchView searchView;
    private List<Curso> cursos;
    private RecyclerView rv;

    LinearLayout llCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.cargarFiltroYBusqueda();

        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();

        llCategorias = (LinearLayout) findViewById(R.id.LLcategorias);
        mostrarCategorias();
    }

    private void mostrarCategorias() {
        for(int i = 0; i < 10; i++){
            ImageView img = new ImageView(this);
            img.setImageResource(R.drawable.desarrollo);
            img.setLayoutParams(new LinearLayout.LayoutParams(150,150));
            llCategorias.addView(img);
        }
    }

    private void initializeData(){
        cursos = new ArrayList<>();
        cursos.add(new Curso("Angular", "Jose", R.drawable.angular));
        cursos.add(new Curso("JavaScript", "Carlos", R.drawable.cursojs));
        cursos.add(new Curso("Matematica", "Juan", R.drawable.matematica));
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(cursos, "HORIZONTAL");
        rv.setAdapter(adapter);
    }

    private void cargarFiltroYBusqueda(){
        searchView = (android.widget.SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
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
