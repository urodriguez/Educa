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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class Home extends AppCompatActivity implements android.widget.SearchView.OnQueryTextListener{

    android.widget.SearchView searchView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private List<Curso> cursos;
    private RecyclerView rv;

    private String userName = "";

    LinearLayout llCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setToolbar(); // Setear Toolbar como action bar

        userName = getIntent().getExtras().getString("USER");
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
        final TextView userName = (TextView)navigationView.getHeaderView(0).findViewById(R.id.username);
        userName.setText(this.userName);

        navigationView.getMenu().getItem(0).setChecked(true);//home = item 0
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
                                drawerLayout.closeDrawers(); // Cerrar drawer
                                break;
                            case "Mis Cursos":
                                Intent misCursos = new Intent(Home.this,MisCursos.class);
                                misCursos.putExtra("USER", Home.this.userName);
                                startActivity(misCursos);
                                break;
                            case "Mis Diplomas":
                                Intent misDiplomas = new Intent(Home.this,MisDiplomas.class);
                                misDiplomas.putExtra("USER", Home.this.userName);
                                startActivity(misDiplomas);
                                break;
                        }
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


        //userName = getIntent().getExtras().getString("USER");

        if (imgP != null && imgG != null && imgI != null && imgGe != null && imgE != null) {
            imgP.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent cursosIntent = new Intent(Home.this,Cursos.class);
                    cursosIntent.putExtra("CATEGORIA", "Programación");
                    cursosIntent.putExtra("USER",userName);
                    cursosIntent.putExtra("ID","1");
                    startActivity(cursosIntent);
                }
            });

            imgG.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent cursosIntent = new Intent(Home.this,Cursos.class);
                    cursosIntent.putExtra("CATEGORIA", "Gastronomía");
                    cursosIntent.putExtra("USER",userName);
                    cursosIntent.putExtra("ID","2");
                    startActivity(cursosIntent);
                }
            });

            imgI.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent cursosIntent = new Intent(Home.this,Cursos.class);
                    cursosIntent.putExtra("CATEGORIA", "Idiomas");
                    cursosIntent.putExtra("USER",userName);
                    cursosIntent.putExtra("ID","6");
                    startActivity(cursosIntent);
                }
            });

            imgGe.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent cursosIntent = new Intent(Home.this,Cursos.class);
                    cursosIntent.putExtra("CATEGORIA", "Gestión");
                    cursosIntent.putExtra("USER",userName);
                    cursosIntent.putExtra("ID","4");
                    startActivity(cursosIntent);
                }
            });

            imgE.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent cursosIntent = new Intent(Home.this,Cursos.class);
                    cursosIntent.putExtra("CATEGORIA", "Exactas");
                    cursosIntent.putExtra("USER",userName);
                    cursosIntent.putExtra("ID","5");
                    startActivity(cursosIntent);
                }
            });
        }
    }

    private void initializeData(){
        cursos = new ArrayList<>();
        // Instantiate the RequestQueue.
        //RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://educa-mnforlenza.rhcloud.com/api/curso/ultimos-cursos";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        android.util.Log.i("INFO", "Response is: "+ response.substring(0,10));
                        parseHomeResponse(response);
                        initializeAdapter();
                    }
                },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                android.util.Log.d("DEBUG", "That didn't work!");
            }
        }
        );


        /*JsonObjectRequest jsObjRequest = new JsonObjectRequest
        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                String res = response.toString();
                android.util.Log.i("INFO", "Res is: "+ res.substring(0,10));
                parseHomeResponse(res);
                initializeAdapter();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                android.util.Log.e("Volley", "Res is: "+ error.getMessage());
            }
        });*/

        //queue.add(stringRequest);
        RQSingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(cursos, "HORIZONTAL",userName, this);
        rv.setAdapter(adapter);
    }

    private void cargarFiltroYBusqueda(){
        searchView = (android.widget.SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // User pressed the search button
        Intent cursosBuscadoIntent = new Intent(Home.this,CursosBuscados.class);
        cursosBuscadoIntent.putExtra("BUSQUEDA", query);
        cursosBuscadoIntent.putExtra("USER", this.userName);
        startActivity(cursosBuscadoIntent);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        return false;
    }

    public void parseHomeResponse(String response){
        Gson g = new Gson();

        Type collectionType = new TypeToken<Collection<Curso>>(){}.getType();
        Collection<Curso> cusos = g.fromJson(response, collectionType);


        HashMap<String, String> hm = new HashMap<String,String>();
        for(Curso c: cusos){
            cursos.add(c);
            android.util.Log.d("CURSO", "NOMBRE :" + c.getNombre() + "Docente: " + c.getNombreDocente());
        }

    }
}
