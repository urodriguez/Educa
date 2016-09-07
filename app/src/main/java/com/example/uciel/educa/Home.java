package com.example.uciel.educa;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements android.widget.SearchView.OnQueryTextListener{

    android.widget.SearchView searchView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private List<Curso> cursos;
    private RecyclerView rv;

    LinearLayout llCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setToolbar(); // Setear Toolbar como action bar

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

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

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            getMenuInflater().inflate(R.menu.nav_menu, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Marcar item presionado
                        menuItem.setChecked(true);
                        // Crear nuevo fragmento
                        String title = menuItem.getTitle().toString();
                        //selectItem(title);
                        android.util.Log.d("INFO", "ITEM SELECCIONADO: " + title);

                        if(title.equals("Home")){
                            drawerLayout.closeDrawers(); // Cerrar drawer
                        }

                        //drawerLayout.closeDrawers(); // Cerrar drawer
                        return true;
                    }
                }
        );
    }

    private void mostrarCategorias() {
        ImageView imgP = (ImageView) findViewById(R.id.programacion);
        ImageView imgG = (ImageView) findViewById(R.id.gastronomia);
        ImageView imgI = (ImageView) findViewById(R.id.idiomas);
        ImageView imgGe = (ImageView) findViewById(R.id.gestion);
        ImageView imgE = (ImageView) findViewById(R.id.exactas);

        if (imgP != null && imgG != null && imgI != null && imgGe != null && imgE != null) {
            imgP.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent cursosIntent = new Intent(Home.this,Cursos.class);
                    cursosIntent.putExtra("CATEGORIA", "Programación");
                    startActivity(cursosIntent);
                }
            });

            imgG.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent cursosIntent = new Intent(Home.this,Cursos.class);
                    cursosIntent.putExtra("CATEGORIA", "Gastronomía");
                    startActivity(cursosIntent);
                }
            });

            imgI.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent cursosIntent = new Intent(Home.this,Cursos.class);
                    cursosIntent.putExtra("CATEGORIA", "Idiomas");
                    startActivity(cursosIntent);
                }
            });

            imgGe.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent cursosIntent = new Intent(Home.this,Cursos.class);
                    cursosIntent.putExtra("CATEGORIA", "Gestión");
                    startActivity(cursosIntent);
                }
            });

            imgE.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent cursosIntent = new Intent(Home.this,Cursos.class);
                    cursosIntent.putExtra("CATEGORIA", "Exactas");
                    startActivity(cursosIntent);
                }
            });
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
