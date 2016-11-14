package com.example.uciel.educa.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.*;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.uciel.educa.R;
import com.example.uciel.educa.adapters.RVAdapter;
import com.example.uciel.educa.adapters.RVAdapterMisCursos;
import com.example.uciel.educa.domain.Curso;
import com.example.uciel.educa.domain.ProgressBarHandler;
import com.example.uciel.educa.domain.SesionHandler;
import com.example.uciel.educa.domain.SingletonUserLogin;
import com.example.uciel.educa.network.RQSingleton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MisCursos extends AppCompatActivity implements SearchView.OnQueryTextListener, GoogleApiClient.OnConnectionFailedListener {
    private SearchView searchView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private List<Integer> cursosDesaprobados;
    private List<Curso> cursos;
    private List<SesionHandler> sesionHandlers;
    private RecyclerView rv;

    private ProgressBarHandler progressBarHandler;

    private SingletonUserLogin userLoginData;

    private final String URL_MIS_SESIONES = "http://educa-mnforlenza.rhcloud.com/api/usuario/mis-sesiones/";
    private final String URL_MIS_CURSOS = "http://educa-mnforlenza.rhcloud.com/api/usuario/mis-cursos/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_cursos);

        userLoginData = SingletonUserLogin.getInstance();

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

        progressBarHandler = new ProgressBarHandler(this, (RelativeLayout) findViewById(R.id.rlrv));
        progressBarHandler.show();

        initializeData();
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

    private GoogleApiClient mGoogleApiClient;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            getMenuInflater().inflate(R.menu.nav_menu, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
        userName.setText(userLoginData.getUserName());

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
                                Intent misDiplomas = new Intent(MisCursos.this, MisDiplomas.class);
                                startActivity(misDiplomas);
                                break;
                            case "Cerrar Sesión":
                                if (mGoogleApiClient != null){
                                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                                }
                                startActivity(new Intent(MisCursos.this, Log.class));
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    private void cargarFiltroYBusqueda(){
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);

        searchView.setOnCloseListener(new SearchView.OnCloseListener(){
            @Override
            public boolean onClose() {
                //Al cerrar el search reestablezco los cursos por si el alumno se
                //arrepiente y decide cancelar la busqueda
                RVAdapter adapter = new RVAdapter(cursos, "VERTICAL", userLoginData, MisCursos.this);
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

        RVAdapter adapter = new RVAdapter(cursosFiltrados, "VERTICAL", userLoginData, this);
        rv.setAdapter(adapter);
        return false;
    }

    private void initializeData(){
        String url = URL_MIS_SESIONES + "/" + userLoginData.getUserID();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseSesionesResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        android.util.Log.d("MSG", "ERROR RESPONSE");
                        CharSequence text = "Error al cargar sesiones. Reintente nuevamente!";
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
        );
        RQSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void parseSesionesResponse(String response){
        sesionHandlers = new ArrayList<>();
        cursosDesaprobados = new ArrayList<>();
        JSONArray jsonarray;
        try {
            jsonarray = new JSONArray(response);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                sesionHandlers.add(new SesionHandler(jsonobject.getJSONObject("id").getInt("numero")));
                if(jsonobject.getBoolean("desaprobado")){
                    cursosDesaprobados.add(jsonobject.getJSONObject("id").getInt("idCurso"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initializeCursos();
    }

    private void initializeCursos(){
        cursos = new ArrayList<>();
        String url = URL_MIS_CURSOS + userLoginData.getUserID();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("[]")){
                            Context context = getApplicationContext();
                            CharSequence text = "Aun no te has inscripto a algun curso";
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        } else {
                            parseMisCursosResponse(response);
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
        RQSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void parseMisCursosResponse(String response){
        Gson g = new Gson();

        Type collectionType = new TypeToken<Collection<Curso>>(){}.getType();
        Collection<Curso> cusos = g.fromJson(response, collectionType);

        int i = 0;
        for(Curso c: cusos){
            if(cursosDesaprobados.contains(c.getId())){
                c.desaprobarSesionActual();
            }
            sesionHandlers.get(i++).setFechaInicio(c);
            cursos.add(c);
            android.util.Log.d("CATEGORIASCURSO", "NOMBRE :" + c.getNombre() + "Docente: " + c.getNombreDocente());
        }
    }

    private void initializeAdapter(){
        progressBarHandler.hide();
        RVAdapterMisCursos adapter = new RVAdapterMisCursos(cursos, sesionHandlers,this);
        rv.setAdapter(adapter);
    }



}
