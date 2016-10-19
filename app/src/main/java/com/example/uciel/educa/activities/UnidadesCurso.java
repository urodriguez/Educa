package com.example.uciel.educa.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.uciel.educa.R;
import com.example.uciel.educa.domain.Curso;
import com.example.uciel.educa.domain.SingletonUserLogin;
import com.example.uciel.educa.network.RQSingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;

public class UnidadesCurso extends AppCompatActivity {

    private Bundle extras;

    private SingletonUserLogin userLoginData;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private Curso curso;

    private LinearLayout llUnidades;

    private final String URL_CURSOS_DISP = "http://educa-mnforlenza.rhcloud.com/api/curso/listar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unidades_curso);

        extras = getIntent().getExtras();

        userLoginData = SingletonUserLogin.getInstance();

        setToolbar(); // Setear Toolbar como action bar

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        initializeData();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        TextView userName = (TextView)navigationView.getHeaderView(0).findViewById(R.id.username);
        userName.setText(userLoginData.getUserName());

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
                                Intent home = new Intent(UnidadesCurso.this,Home.class);
                                startActivity(home);
                                break;
                            case "Mis Cursos":
                                Intent misCursos = new Intent(UnidadesCurso.this,MisCursos.class);
                                startActivity(misCursos);
                                break;
                            case "Mis Diplomas":
                                Intent misDiplomas = new Intent(UnidadesCurso.this,MisDiplomas.class);
                                startActivity(misDiplomas);
                                break;
                        }

                        drawerLayout.closeDrawers(); // Cerrar drawer
                        return true;
                    }
                }
        );
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

    private void initializeData(){
        // Instantiate the RequestQueue.
        String url = URL_CURSOS_DISP;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseHomeResponse(response);
                        mostrarTitulo();
                        mostrarUnidades();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        android.util.Log.d("MSG", "ERROR RESPONSE");
                    }
                }
        );

        RQSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void parseHomeResponse(String response){
        Gson g = new Gson();

        Type collectionType = new TypeToken<Collection<Curso>>(){}.getType();
        Collection<Curso> cusos = g.fromJson(response, collectionType);

        android.util.Log.d("MSG", "ID_U= " +  extras.getInt("ID"));
        for(Curso c: cusos){
            if(extras.getInt("ID") == c.getId()){
                android.util.Log.d("MSG", "CURSO ENCONTRADO");
                curso = c;
            }
        }
    }


    private void mostrarTitulo() {
        TextView tvNombreCurso = (TextView) findViewById(R.id.cursoActual);
        tvNombreCurso.setText(curso.getNombre());
    }

    private void mostrarUnidades() {
        llUnidades = (LinearLayout) findViewById(R.id.linearScrollUnidades);

        android.util.Log.d("MSG", "CANT UNID= " + curso.getCantDeUnidades());

        for (int i = 0; i < curso.getCantDeUnidades(); i++){
            CardView cv = new CardView(this);
            RelativeLayout rl = new RelativeLayout(this);
            TextView tv = new TextView(this);

            RelativeLayout.LayoutParams paramsdos = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 124);
            cv.setLayoutParams(paramsdos); //causes layout update

            final int textViewUnidadID = i;
            tv.setId(textViewUnidadID);
            tv.setText("  " + curso.getTituloUnidadNum(i));
            tv.setTypeface(null, Typeface.BOLD);
            tv.setTextSize(18);

            rl.addView(tv);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)tv.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_VERTICAL);

            tv.setLayoutParams(params); //causes layout update

            cv.addView(rl);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent cursosIntent = new Intent(UnidadesCurso.this,ContenidoUnidad.class);
                    cursosIntent.putExtra("ID_CURSO", curso.getId());

                    android.util.Log.i("INFO", "ID UNIDAD: " + textViewUnidadID + 1);
                    TextView textView = (TextView) findViewById(textViewUnidadID);
                    cursosIntent.putExtra("UNIDAD", textView.getText());
                    cursosIntent.putExtra("ID_UNIDAD", textViewUnidadID + 1);

                    startActivity(cursosIntent);

                    /*TextView textView = (TextView) findViewById(finalI);
                    CharSequence text = textView.getText();
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                    toast.show();*/
                }
            });

            llUnidades.addView(cv);

            TextView txtEspacio = new TextView(this);
            txtEspacio.setText(" ");
            txtEspacio.setTextSize(3);

            llUnidades.addView(txtEspacio);
        }





    }
}
