package com.example.uciel.educa.activities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.uciel.educa.R;
import com.example.uciel.educa.adapters.RVAdapter;
import com.example.uciel.educa.domain.Curso;

import java.util.ArrayList;
import java.util.List;

public class MisCursos extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private SearchView searchView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private List<Curso> cursos;
    private RecyclerView rv;

    private String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_cursos);

        setToolbar(); // Setear Toolbar como action bar

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        this.cargarFiltroYBusqueda();

        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
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
        TextView userName = (TextView)navigationView.getHeaderView(0).findViewById(R.id.username);
        //userName.setText(getIntent().getExtras().getString("USER"));
        userName.setText("Anonimo");

        navigationView.getMenu().getItem(1).setChecked(true);//mis cursos = item 1
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

                        switch (title) {
                            case "Home":
                                Intent home = new Intent(MisCursos.this,Home.class);
                                startActivity(home);
                                break;
                            case "Mis Cursos":
                                drawerLayout.closeDrawers(); // Cerrar drawer
                                break;
                            case "Mis Diplomas":
                                Intent misDiplomas = new Intent(MisCursos.this,MisDiplomas.class);
                                startActivity(misDiplomas);
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    private void initializeAdapter(){
        //RVAdapter adapter = new RVAdapter(cursos, "HORIZONTAL",getIntent().getExtras().getString("USER"));
        RVAdapter adapter = new RVAdapter(cursos, "VERTICAL","anonimo", this);
        rv.setAdapter(adapter);
    }

    private void cargarFiltroYBusqueda(){
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);

        searchView.setOnCloseListener(new SearchView.OnCloseListener(){
            @Override
            public boolean onClose() {
                //Al cerrar el search reestablezco los cursos por si el alumno se
                //arrepiente y decide cancelar la busqueda
                RVAdapter adapter = new RVAdapter(cursos, "VERTICAL", userName, MisCursos.this);
                rv.setAdapter(adapter);
                return false;
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // User pressed the search button
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        List<Curso> cursosFiltrados = new ArrayList<>();

        for (Curso c: cursos){
            if(c.getNombre().toLowerCase().contains(newText.toLowerCase())){
                cursosFiltrados.add(c);
            }
        }

        RVAdapter adapter = new RVAdapter(cursosFiltrados, "VERTICAL", this.userName, this);
        rv.setAdapter(adapter);
        return false;
    }

}
