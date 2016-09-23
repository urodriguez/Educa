package com.example.uciel.educa.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.uciel.educa.R;
import com.example.uciel.educa.adapters.ViewPagerAdapter;
import com.example.uciel.educa.domain.Critica;
import com.example.uciel.educa.domain.Sesion;
import com.example.uciel.educa.domain.Unidad;

import java.util.ArrayList;
import java.util.List;

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

    private String userName = "";
    private List<Button> botonesInscripcion;

    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_curso);

        extras = getIntent().getExtras();

        setToolbar(); // Setear Toolbar como action bar

        setTabs();

        userName = extras.getString("USER");
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

                        viewPager.setCurrentItem(tab.getPosition());

                        switch (tab.getPosition()) {
                            case 0:
                                cargarTextosDescriptivos();

                                ratingBar = (RatingBar)viewPager.findViewById(R.id.ratingBar);
                                ratingBar.setRating(extras.getFloat("VALORACION"));

                                cargarComentarios();
                                break;

                            case 1:
                                cargarUnidades();
                                break;

                            case 2:
                                cargarSesiones();
                                break;
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
                ratingBar.setRating(extras.getFloat("VALORACION"));

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

        tvNombre.setText("Nombre: " + extras.getString("NOMBRE"));
        tvEstado.setText("Estado: " + extras.getString("ESTADO"));
        tvProfesor.setText("Profesor: " + extras.getString("PROFESOR"));
        tvDescripcion.setText("Descripción: " + extras.getString("DESCRIPCION"));
    }


    private void cargarComentarios() {
        llComentarios = (LinearLayout) viewPager.findViewById(R.id.llComentarios);

        List<Critica> criticas = new ArrayList<>();
        for (int i = 0; i < extras.getInt("CANT_CRITICAS"); i++){
            criticas.add(new Critica(
                    extras.getString("CRITICA" + String.valueOf(i) + "FECHA"),
                    extras.getFloat("CRITICA" + String.valueOf(i) + "CALIFICACION"),
                    extras.getString("CRITICA" + String.valueOf(i) + "COMENTARIO")
            ));
        }

        llComentarios.addView(crearDivisor(LinearLayout.LayoutParams.MATCH_PARENT, 0, 10, 10, 10, 10, Color.WHITE));

        for(int i = 0; i < criticas.size(); i++){
            LinearLayout llRB = new LinearLayout(this);
            llRB.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 24));

            RatingBar rBar = new RatingBar(this, null, android.R.attr.ratingBarStyleSmall);
            rBar.setNumStars(5);
            rBar.setRating(criticas.get(i).getCalificacion());
            llRB.addView(rBar);

            llComentarios.addView(llRB);

            TextView txt = new TextView(this);
            txt.setText("Fecha: " + criticas.get(i).getCadenaFecha() + "\n" +
                        "Comentario: " + criticas.get(i).getComentario() + "\n");
            llComentarios.addView(txt);

            // Agrego un divisor
            llComentarios.addView(crearDivisor(LinearLayout.LayoutParams.MATCH_PARENT, 1, 10, 15, 10, 15, Color.LTGRAY));
        }
    }

    private void cargarUnidades() {
        llUnidades = (LinearLayout) viewPager.findViewById(R.id.llUnidades);
        llUnidades.removeAllViews();

        List<Unidad> unidades = new ArrayList<>();
        for (int i = 0; i < extras.getInt("CANT_UNIDADES"); i++){
            unidades.add(new Unidad(
                    extras.getString("UNIDAD" + String.valueOf(i) + "TITULO"),
                    extras.getString("UNIDAD" + String.valueOf(i) + "DESCRIPCION"),
                    extras.getInt("UNIDAD" + String.valueOf(i) + "DURACIONESTIMADA")
            ));
        }

        for(int i = 0; i < unidades.size(); i++){
            TextView txt = new TextView(this);
            txt.setTextSize(18);
            txt.setText("Unidad nº: " + String.valueOf(i + 1) + "\n" +
                        "Titulo: " + unidades.get(i).getTitulo() + "\n" +
                        "Descripción: " + unidades.get(i).getDescripcion() + "\n" +
                        "Duración estimada: " + unidades.get(i).getDuracionEstimada() + "\n");
            llUnidades.addView(txt);

            // Agrego un divisor
            llUnidades.addView(crearDivisor(LinearLayout.LayoutParams.MATCH_PARENT, 1, 10, 15, 10, 15, Color.LTGRAY));
        }
    }

    private void cargarSesiones() {
        llSesiones = (LinearLayout) viewPager.findViewById(R.id.sesiones_linear);
        llSesiones.removeAllViews();

        List<Sesion> sesiones = new ArrayList<>();
        for (int i = 0; i < extras.getInt("CANT_SESIONES"); i++){
            sesiones.add(new Sesion(
                    extras.getString("SESION" + String.valueOf(i) + "FECHADESDE"),
                    extras.getString("SESION" + String.valueOf(i) + "FECHAHASTA"),
                    extras.getString("SESION" + String.valueOf(i) + "FECHADESDEINCRIP")
            ));
        }

        cargarBotonesDeIncripcion(sesiones.size());

        for(int i = 0; i < sesiones.size(); i++){
            LinearLayout llH = new LinearLayout(this);
            llH.setOrientation(LinearLayout.HORIZONTAL);

            TextView txt = new TextView(this);
            txt.setTextSize(18);
            txt.setText("Sesión nº: " + String.valueOf(i + 1) + "\n" +
                        "Comienzo: " + sesiones.get(i).getCadenaFechaDesde() + "\n" +
                        "Fin: " + sesiones.get(i).getCadenaFechaHasta() + "\n" +
                        "Inscripciones: " + sesiones.get(i).getCadenaFechaDesdeInscripcion() + "\n");
            llH.addView(txt);

            // Agrego un divisor
            llH.addView(crearDivisor(0, LinearLayout.LayoutParams.MATCH_PARENT, 64, 8, 64, 8, Color.WHITE));

            llH.addView(botonesInscripcion.get(i));
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams)botonesInscripcion.get(i).getLayoutParams();
            ll.gravity = Gravity.CENTER;
            botonesInscripcion.get(i).setLayoutParams(ll);

            llSesiones.addView(llH);

            // Agrego un divisor
            llSesiones.addView(crearDivisor(LinearLayout.LayoutParams.MATCH_PARENT, 1, 10, 15, 10, 15, Color.LTGRAY));
        }
    }

    private void cargarBotonesDeIncripcion(final int cantDeSesiones){
        botonesInscripcion = new ArrayList<>();

        for(int i = 0; i < cantDeSesiones; i++){
            Button button = new Button(this);
            button.setText("INSCRIBIRSE");
            button.setBackgroundColor(Color.GREEN);
            botonesInscripcion.add(button);
        }

        for(int i = 0; i < cantDeSesiones; i++){
            final int finalI = i;
            botonesInscripcion.get(i).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(botonesInscripcion.get(finalI).getText().equals("INSCRIBIRSE")){
                        botonesInscripcion.get(finalI).setText("DESUSCRIBIRSE");

                        //Deshabilito los demas botones
                        for (int j = 0; j < cantDeSesiones; j++){
                            if (j != finalI){
                                botonesInscripcion.get(j).setText("NO DISPONIBLE");
                                botonesInscripcion.get(j).setEnabled(false);
                                botonesInscripcion.get(j).setBackgroundColor(Color.LTGRAY);
                            }
                        }
                    } else {
                        botonesInscripcion.get(finalI).setText("INSCRIBIRSE");

                        //Reestablezco los demas botones
                        for (int j = 0; j < cantDeSesiones; j++){
                            if (j != finalI){
                                botonesInscripcion.get(j).setText("INSCRIBIRSE");
                                botonesInscripcion.get(j).setEnabled(true);
                                botonesInscripcion.get(j).setBackgroundColor(Color.GREEN);
                            }
                        }
                    }
                }
            });
        }
    }


    private ImageView crearDivisor(int ancho, int alto, int margenI, int margenTop, int margenD, int margenBottom, int c) {
        ImageView divisor = new ImageView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ancho, alto);
        lp.setMargins(margenI, margenTop, margenD, margenBottom);
        divisor.setLayoutParams(lp);
        divisor.setBackgroundColor(c);

        return divisor;
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

                        switch (title) {
                            case "Home":
                                Intent home = new Intent(DescripcionCurso.this,Home.class);
                                home.putExtra("USER", DescripcionCurso.this.userName);
                                startActivity(home);
                                break;
                            case "Mis Cursos":
                                Intent misCursos = new Intent(DescripcionCurso.this,MisCursos.class);
                                misCursos.putExtra("USER", DescripcionCurso.this.userName);
                                startActivity(misCursos);
                                break;
                            case "Mis Diplomas":
                                Intent misDiplomas = new Intent(DescripcionCurso.this,MisDiplomas.class);
                                misDiplomas.putExtra("USER", DescripcionCurso.this.userName);
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
