package com.example.uciel.educa;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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

import com.example.uciel.educa.domain.Curso;
import com.example.uciel.educa.network.HttpHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements android.widget.SearchView.OnQueryTextListener{

    android.widget.SearchView searchView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private List<Curso> cursos;
    private RecyclerView rv;

    String userName = "Anonimo";

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
        TextView userName = (TextView)navigationView.getHeaderView(0).findViewById(R.id.username);
        //userName.setText(getIntent().getExtras().getString("USER"));
        userName.setText("Anonimo");

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


        //userName = getIntent().getExtras().getString("USER");

        if (imgP != null && imgG != null && imgI != null && imgGe != null && imgE != null) {
            imgP.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent cursosIntent = new Intent(Home.this,Cursos.class);
                    cursosIntent.putExtra("CATEGORIA", "Programación");
                    cursosIntent.putExtra("USER",userName);
                    startActivity(cursosIntent);
                }
            });

            imgG.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent cursosIntent = new Intent(Home.this,Cursos.class);
                    cursosIntent.putExtra("CATEGORIA", "Gastronomía");
                    cursosIntent.putExtra("USER",userName);
                    startActivity(cursosIntent);
                }
            });

            imgI.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent cursosIntent = new Intent(Home.this,Cursos.class);
                    cursosIntent.putExtra("CATEGORIA", "Idiomas");
                    cursosIntent.putExtra("USER",userName);
                    startActivity(cursosIntent);
                }
            });

            imgGe.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent cursosIntent = new Intent(Home.this,Cursos.class);
                    cursosIntent.putExtra("CATEGORIA", "Gestión");
                    cursosIntent.putExtra("USER",userName);
                    startActivity(cursosIntent);
                }
            });

            imgE.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent cursosIntent = new Intent(Home.this,Cursos.class);
                    cursosIntent.putExtra("CATEGORIA", "Exactas");
                    cursosIntent.putExtra("USER",userName);
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

        String stringUrl = "http://educa-mnforlenza.rhcloud.com/api/curso/ultimos-cursos";
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadHomeTask().execute(stringUrl);
        } else {
            // display error
        }

    }


    private class DownloadHomeTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            HttpHandler httpHandler = new HttpHandler();
            httpHandler.makeServiceCall(urls[0]);
            return null;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //TODO: parse json
            //textView.setText(result);
        }
    }


    private void initializeAdapter(){
        //RVAdapter adapter = new RVAdapter(cursos, "HORIZONTAL",getIntent().getExtras().getString("USER"));
        RVAdapter adapter = new RVAdapter(cursos, "HORIZONTAL","anonimo");
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
