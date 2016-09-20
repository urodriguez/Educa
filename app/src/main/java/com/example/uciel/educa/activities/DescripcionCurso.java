package com.example.uciel.educa.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.*;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.uciel.educa.R;
import com.example.uciel.educa.adapters.ViewPagerAdapter;

public class DescripcionCurso extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TabLayout tabs;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    private TextView tvNombre,tvEstado,tvProfesor,tvDescripcion,tvComentarios;

    private RatingBar ratingBar;

    private LinearLayout llComentarios, llUnidades, llSesiones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_curso);

        setToolbar(); // Setear Toolbar como action bar

        setTabs();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }



    }

    private void setTabs() {
        tabs = (TabLayout) findViewById(R.id.tabs);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        final TabLayout.Tab detalle = tabs.newTab();
        final TabLayout.Tab unidades = tabs.newTab();
        final TabLayout.Tab sesiones = tabs.newTab();

        detalle.setText("DETALLE");
        unidades.setText("UNIDADES");
        sesiones.setText("SESIONES");

        tabs.addTab(detalle, 0);
        tabs.addTab(unidades, 1);
        tabs.addTab(sesiones, 2);

        tabs.setTabTextColors(ContextCompat.getColorStateList(this, R.color.white));
        tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.black_overlay));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        tabs.setOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {

                        /*OBS: NO REEMPLAZAR LOS 'IF' POR UN 'SWITCH' ==> NO ANDA PARA TABS*/

                        if(tab.getPosition() == 0){
                            viewPager.setCurrentItem(0);
                            cargarTextosDescriptivos();

                            ratingBar = (RatingBar)viewPager.findViewById(R.id.ratingBar);
                            ratingBar.setRating(getIntent().getExtras().getFloat("VALORACION"));

                            cargarComentarios();
                        } else if (tab.getPosition() == 1) {
                            Log.d("tag","CARGANDO UNIDADES");
                            viewPager.setCurrentItem(1);
                            cargarUnidades();
                        } else {
                            Log.d("tag","CARGANDO SESIONES");
                            viewPager.setCurrentItem(2);
                            cargarSesiones();
                        }

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }
                }
        );

        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                viewPager.setCurrentItem(0);

                cargarTextosDescriptivos();

                ratingBar = (RatingBar)viewPager.findViewById(R.id.ratingBar);
                ratingBar.setRating(getIntent().getExtras().getFloat("VALORACION"));

                cargarComentarios();
            }
        };
        handler.postDelayed(r, 500);

    }

    private void cargarTextosDescriptivos() {
        tvNombre = (TextView) viewPager.findViewById(R.id.textView2);
        tvEstado = (TextView) viewPager.findViewById(R.id.textView3);
        tvProfesor = (TextView) viewPager.findViewById(R.id.textView4);
        tvDescripcion = (TextView) viewPager.findViewById(R.id.textView5);

        tvNombre.setText("Nombre: " + getIntent().getExtras().getString("NOMBRE"));
        tvEstado.setText("Estado: " + getIntent().getExtras().getString("ESTADO"));
        tvProfesor.setText("Profesor: " + getIntent().getExtras().getString("PROFESOR"));
        tvDescripcion.setText("Descripción: " + getIntent().getExtras().getString("DESCRIPCION"));
    }


    private void cargarComentarios() {
        llComentarios = (LinearLayout) viewPager.findViewById(R.id.llComentarios);

        for(int i = 0; i < 150; i++){
            TextView txt = new TextView(this);
            txt.setText("SOY UN COMENTARIO");
            llComentarios.addView(txt);

            /* INICIO: Creo un divisor para cada comentario*/
            ImageView divider = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
            lp.setMargins(10, 10, 10, 10);
            divider.setLayoutParams(lp);
            divider.setBackgroundColor(Color.GRAY);

            // Agrego el divisor
            llComentarios.addView(divider);
            /* FIN: Divisor agregado para ese comentario*/
        }
    }

    private void cargarUnidades() {
        llUnidades = (LinearLayout) viewPager.findViewById(R.id.llUnidades);

        for(int i = 0; i < 150; i++){
            TextView txt = new TextView(this);
            txt.setText("SOY UNA UNIDAD");
            llUnidades.addView(txt);

            /* INICIO: Creo un divisor para cada comentario*/
            ImageView divider = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
            lp.setMargins(10, 10, 10, 10);
            divider.setLayoutParams(lp);
            divider.setBackgroundColor(Color.GRAY);

            // Agrego el divisor
            llUnidades.addView(divider);
            /* FIN: Divisor agregado para ese comentario*/
        }
    }

    private void cargarSesiones() {
        llSesiones = (LinearLayout) viewPager.findViewById(R.id.sesiones_linear);

        for(int i = 0; i < 3; i++){
            LinearLayout llH = new LinearLayout(this);
            llH.setOrientation(LinearLayout.HORIZONTAL);

            TextView txt = new TextView(this);
            txt.setText("S1");
            llH.addView(txt);

            Button button = new Button(this);
            button.setText("INSCRIBIRSE");
            llH.addView(button);

            llSesiones.addView(llH);

            /* INICIO: Creo un divisor para cada comentario*/
            ImageView divider = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
            lp.setMargins(10, 10, 10, 10);
            divider.setLayoutParams(lp);
            divider.setBackgroundColor(Color.GRAY);

            // Agrego el divisor
            llSesiones.addView(divider);
            /* FIN: Divisor agregado para ese comentario*/


        }
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

                        switch (title) {
                            case "Home":
                                Intent home = new Intent(DescripcionCurso.this,Home.class);
                                startActivity(home);
                                break;
                            case "Mis Cursos":
                                Intent misCursos = new Intent(DescripcionCurso.this,MisCursos.class);
                                startActivity(misCursos);
                                break;
                            case "Mis Diplomas":
                                Intent misDiplomas = new Intent(DescripcionCurso.this,MisDiplomas.class);
                                startActivity(misDiplomas);
                                break;
                        }

                        drawerLayout.closeDrawers(); // Cerrar drawer
                        return true;
                    }
                }
        );
    }

}
