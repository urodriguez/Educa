package com.example.uciel.educa.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.example.uciel.educa.R;
import com.example.uciel.educa.adapters.VPAdapterContCurso;
import com.example.uciel.educa.domain.Curso;
import com.example.uciel.educa.domain.SingletonUserLogin;
import com.example.uciel.educa.network.RQSingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

public class ContenidoCurso extends AppCompatActivity {
    private SingletonUserLogin userLoginData;
    private Curso curso;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TabLayout tabs;
    private ViewPager viewPager;
    private VPAdapterContCurso vpaContCurso;

    private LinearLayout llMaterial, llPracticas, llExamen;

    private Bundle extras;

    private final String URL_CURSOS_DISP = "http://educa-mnforlenza.rhcloud.com/api/curso/listar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenido_curso);

        extras = getIntent().getExtras();

        initializeData();

        setToolbar(); // Setear Toolbar como action bar

        setTabs();

        userLoginData = SingletonUserLogin.getInstance();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    private void initializeData(){
        // Instantiate the RequestQueue.
        String url = URL_CURSOS_DISP;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseHomeResponse(response);
                        //mostrarUnidades();
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

        android.util.Log.d("MSG", "ID_C= " +  extras.getInt("ID"));
        for(Curso c: cusos){
            if(extras.getInt("ID") == c.getId()){
                android.util.Log.d("MSG", "CURSO ENCONTRADO");
                curso = c;
            }
        }
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(extras.getString("UNIDAD"));
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setTabs() {
        tabs = (TabLayout) findViewById(R.id.tabs);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        vpaContCurso = new VPAdapterContCurso(getSupportFragmentManager());
        viewPager.setAdapter(vpaContCurso);

        final TabLayout.Tab material = tabs.newTab();
        final TabLayout.Tab practicas = tabs.newTab();
        final TabLayout.Tab examen = tabs.newTab();

        material.setText("MATERIAL");
        practicas.setText("PRACTICAS");
        examen.setText("EXAMEN");

        tabs.addTab(material, 0);
        tabs.addTab(practicas, 1);
        tabs.addTab(examen, 2);

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
                                cargarMaterial();
                                break;

                            case 1:
                                cargarPracticas();
                                break;

                            case 2:
                                cargarExamenes();
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
                cargarMaterial();
            }
        };
        handler.postDelayed(r, 500);

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
                                Intent home = new Intent(ContenidoCurso.this,Home.class);
                                startActivity(home);
                                break;
                            case "Mis Cursos":
                                Intent misCursos = new Intent(ContenidoCurso.this,MisCursos.class);
                                startActivity(misCursos);
                                break;
                            case "Mis Diplomas":
                                Intent misDiplomas = new Intent(ContenidoCurso.this,MisDiplomas.class);
                                startActivity(misDiplomas);
                                break;
                        }

                        drawerLayout.closeDrawers(); // Cerrar drawer
                        return true;
                    }
                }
        );
    }

    private void cargarMaterial() {
        llMaterial = (LinearLayout) viewPager.findViewById(R.id.llMaterial);
        llMaterial.removeAllViews();

        addVideoItem();
        addMaterialItem();

    }

    private void addVideoItem() {
        ImageView myImage = new ImageView(this);
        myImage.setImageResource(R.drawable.video_thumbnail);


        // cargamos video
        TextView txt = new TextView(this);
        txt.setTextSize(18);
        txt.setTypeface(null, Typeface.BOLD);
        String nombreUnidad = extras.getString("UNIDAD");
        txt.setText(nombreUnidad + "(video)");

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.BELOW, myImage.getId());
        txt.setLayoutParams(params);
        RelativeLayout.LayoutParams paramsImg = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        myImage.setLayoutParams(paramsImg);

        RelativeLayout rlVideo = new RelativeLayout(this);
        rlVideo.addView(myImage);

        RelativeLayout rlText = new RelativeLayout(this);
        rlText.addView(txt);

        rlVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoIntent = new Intent(ContenidoCurso.this,VideoActivity.class);
                videoIntent.putExtra("ID_CURSO", extras.getInt("ID_CURSO"));
                videoIntent.putExtra("UNIDAD", extras.getString("UNIDAD"));
                videoIntent.putExtra("ID_UNIDAD", 1); // CAMBIAR ESTO
                startActivity(videoIntent);
            }
        });


        llMaterial.addView(rlVideo);
        llMaterial.addView(rlText);

        // Agrego un divisor
        llMaterial.addView(crearDivisor(LinearLayout.LayoutParams.MATCH_PARENT, 1, 10, 15, 10, 15, Color.LTGRAY));
    }

    private void addMaterialItem() {
        ImageView myImage = new ImageView(this);
        myImage.setImageResource(R.drawable.block_de_apunte);

        // cargamos video
        TextView txt = new TextView(this);
        txt.setTextSize(18);
        txt.setTypeface(null, Typeface.BOLD);

        String nombreUnidad = extras.getString("UNIDAD");
        txt.setText(nombreUnidad + "(Material)");

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.BELOW, myImage.getId());
        txt.setLayoutParams(params);
        RelativeLayout.LayoutParams paramsImg = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        myImage.setLayoutParams(paramsImg);

        RelativeLayout rlMaterial = new RelativeLayout(this);
        rlMaterial.addView(myImage);

        RelativeLayout rlText = new RelativeLayout(this);
        rlText.addView(txt);

        rlMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent materialIntent = new Intent(ContenidoCurso.this,MaterialActivity.class);
                materialIntent.putExtra("ID_CURSO", extras.getInt("ID_CURSO"));
                materialIntent.putExtra("UNIDAD", extras.getInt("UNIDAD"));
                startActivity(materialIntent);
            }
        });


        llMaterial.addView(rlMaterial);
        llMaterial.addView(rlText);

        // Agrego un divisor
        llMaterial.addView(crearDivisor(LinearLayout.LayoutParams.MATCH_PARENT, 1, 10, 15, 10, 15, Color.LTGRAY));
    }

    private ImageView crearDivisor(int ancho, int alto, int margenI, int margenTop, int margenD, int margenBottom, int c) {
        ImageView divisor = new ImageView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ancho, alto);
        lp.setMargins(margenI, margenTop, margenD, margenBottom);
        divisor.setLayoutParams(lp);
        divisor.setBackgroundColor(c);

        return divisor;
    }

    private void cargarPracticas() {

    }

    private void cargarExamenes() {

    }
}
