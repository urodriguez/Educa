package com.example.uciel.educa;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;

public class DescripcionCurso extends AppCompatActivity {

    Button btnInscribirse;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private TextView tvNombre,tvEstado,tvProfesor,tvDescripcion,tvComentarios;

    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_curso);

        setToolbar(); // Setear Toolbar como action bar

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        this.cargarTextosDescriptivos();

        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ratingBar.setRating(getIntent().getExtras().getFloat("VALORACION"));

        btnInscribirse = (Button) findViewById(R.id.buttonInscribirse);
        btnInscribirse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.util.Log.d("MSG: ", "TE HAS INSCRIPTO");
            }
        });

    }

    private void cargarTextosDescriptivos() {
        tvNombre = (TextView) findViewById(R.id.textView2);
        tvEstado = (TextView) findViewById(R.id.textView3);
        tvProfesor = (TextView) findViewById(R.id.textView4);
        tvDescripcion = (TextView) findViewById(R.id.textView5);
        tvComentarios = (TextView) findViewById(R.id.textView7);

        tvNombre.setText("Nombre: " + getIntent().getExtras().getString("NOMBRE"));
        tvEstado.setText("Estado: " + getIntent().getExtras().getString("ESTADO"));
        tvProfesor.setText("Profesor: " + getIntent().getExtras().getString("PROFESOR"));
        tvDescripcion.setText("Descripción: " + getIntent().getExtras().getString("DESCRIPCION"));
        tvComentarios.setText("Comentarios: \n" + "* Muy buen curso" + "\n" + "* Me sirvio mucho" );//TODO por ahora hardcodeado
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
        userName.setText(getIntent().getExtras().getString("USER"));

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
                            Intent homeIntent = new Intent(DescripcionCurso.this,Home.class);
                            startActivity(homeIntent);
                        }

                        drawerLayout.closeDrawers(); // Cerrar drawer
                        return true;
                    }
                }
        );
    }

}
