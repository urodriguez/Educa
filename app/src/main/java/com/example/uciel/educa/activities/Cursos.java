package com.example.uciel.educa.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.uciel.educa.R;
import com.example.uciel.educa.adapters.RVAdapter;
import com.example.uciel.educa.domain.Curso;
import com.example.uciel.educa.network.RQSingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Cursos extends AppCompatActivity implements SearchView.OnQueryTextListener {
    SearchView searchView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private List<Curso> cursos;
    private RecyclerView rv;
    private TextView tvCategoriaActual;
    private String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        setToolbar(); // Setear Toolbar como action bar

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }


        Intent intent = getIntent();//obtengo el Intent de otra activity
        Bundle extras = intent.getExtras();//obtengo los extras de ese Intent
        this.userName = "Anonimo";//getIntent().getExtras().getString("USER");
        tvCategoriaActual = (TextView) findViewById(R.id.categoriaActual);
        tvCategoriaActual.setText(extras.getString("CATEGORIA"));

        this.cargarFiltroYBusqueda();

        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();
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

        userName.setText(this.userName);

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
                            Intent homeIntent = new Intent(Cursos.this,Home.class);
                            startActivity(homeIntent);
                        }

                        drawerLayout.closeDrawers(); // Cerrar drawer
                        return true;
                    }
                }
        );
    }


    private void initializeData(){
        cursos = new ArrayList<>();
        String idCategoria = getIntent().getExtras().getString("ID");
        String url ="http://educa-mnforlenza.rhcloud.com/api/curso/listar/"+idCategoria;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("[]")){
                            Context context = getApplicationContext();
                            CharSequence text = "Aun no hay cursos disponibles para esta categoria";
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        } else {
                            parseHomeResponse(response);
                            initializeAdapter();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        android.util.Log.d("DEBUG", "That didn't work!");
                    }
                }
        );
        //queue.add(stringRequest);
        RQSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(cursos, "VERTICAL", this.userName, this);
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
                RVAdapter adapter = new RVAdapter(cursos, "VERTICAL", userName, Cursos.this);
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

    public void parseHomeResponse(String response){
        Gson g = new Gson();

        Type collectionType = new TypeToken<Collection<Curso>>(){}.getType();
        Collection<Curso> cusos = g.fromJson(response, collectionType);


        HashMap<String, String> hm = new HashMap<String,String>();
        for(Curso c: cusos){
            cursos.add(c);
            android.util.Log.d("CATEGORIASCURSO", "NOMBRE :" + c.getNombre() + "Docente: " + c.getNombreDocente());
        }

    }
}
