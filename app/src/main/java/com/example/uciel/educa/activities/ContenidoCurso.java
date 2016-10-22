package com.example.uciel.educa.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.uciel.educa.R;
import com.example.uciel.educa.adapters.VPAdapterContCurso;
import com.example.uciel.educa.domain.Curso;
import com.example.uciel.educa.domain.SingletonUserLogin;
import com.example.uciel.educa.network.RQSingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

public class ContenidoCurso extends AppCompatActivity {

    private Bundle extras;

    private SingletonUserLogin userLoginData;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private Toolbar toolbar;

    private TabLayout tabs;
    private ViewPager viewPager;
    private VPAdapterContCurso vpaContCurso;

    private Curso curso;
    private ArrayList<String> mensajesForo = new ArrayList<>();

    private LinearLayout llUnidades, llMensajesForo;


    private final String URL_CURSOS_DISP = "http://educa-mnforlenza.rhcloud.com/api/curso/listar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenido_curso);

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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(extras.getString("NOMBRE_CURSO"));
        toolbar.setTitleTextColor(Color.WHITE);

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
                        parseUnidadesResponse(response);
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

        //todo
        //url = URL_FORO + curso.getId();
        stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseForoResponse(response);
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

    public void parseUnidadesResponse(String response){
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

    public void parseForoResponse(String response){
        android.util.Log.d("MSG", response.toString());
        //TODO cargar los mensajes del foro
        setTabs();
    }

    private void setTabs() {
        tabs = (TabLayout) findViewById(R.id.tabs);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        vpaContCurso = new VPAdapterContCurso(getSupportFragmentManager());
        viewPager.setAdapter(vpaContCurso);

        final TabLayout.Tab unidades = tabs.newTab();
        final TabLayout.Tab foro = tabs.newTab();

        unidades.setText("UNIDADES");
        foro.setText("FORO");

        tabs.addTab(unidades, 0);
        tabs.addTab(foro, 1);

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
                                cargarUnidades();
                                break;

                            case 1:
                                cargarForo();
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

        viewPager.setCurrentItem(0);
        cargarUnidades();



        /*final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                viewPager.setCurrentItem(0);
                cargarMaterial();
            }
        };
        handler.postDelayed(r, 500);*/

    }

    private void cargarUnidades() {
        llUnidades = (LinearLayout) viewPager.findViewById(R.id.linearScrollUnidades);
        llUnidades.removeAllViews();

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
                    Intent cursosIntent = new Intent(ContenidoCurso.this,ContenidoUnidad.class);
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

    private void cargarForo() {
        mensajesForo.add("hola");
        mensajesForo.add("hola, que buen curso!");

        llMensajesForo = (LinearLayout) viewPager.findViewById(R.id.linearScrollForo);
        llMensajesForo.removeAllViews();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels - 208 - 100 - 80;

        android.util.Log.d("MSG", "HS= " + height);

        ScrollView scroll_view = (ScrollView) viewPager.findViewById(R.id.scrollForo);
        /*scroll_view.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, height));*/
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) scroll_view.getLayoutParams();
        //layoutParams.topMargin = 208;
        layoutParams.height = height;
        scroll_view.setLayoutParams(layoutParams);

        for(int i = 0; i < mensajesForo.size(); i++){
            TextView tvMensajeForo = new TextView(this);
            tvMensajeForo.setText(mensajesForo.get(i));
            llMensajesForo.addView(tvMensajeForo);

            // Agrego un divisor
            llMensajesForo.addView(crearDivisor(LinearLayout.LayoutParams.MATCH_PARENT, 1, 10, 15, 10, 15, Color.LTGRAY));
        }

        FloatingActionButton fab = (FloatingActionButton) viewPager.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etMensajeIngresado = (EditText) viewPager.findViewById(R.id.editTextForo);

                TextView tvMensajeForo = new TextView(ContenidoCurso.this);
                tvMensajeForo.setText(etMensajeIngresado.getText().toString());
                llMensajesForo.addView(tvMensajeForo);

                // Agrego un divisor
                llMensajesForo.addView(crearDivisor(LinearLayout.LayoutParams.MATCH_PARENT, 1, 10, 15, 10, 15, Color.LTGRAY));
            }
        });




    }

    private ImageView crearDivisor(int ancho, int alto, int margenI, int margenTop, int margenD, int margenBottom, int c) {
        ImageView divisor = new ImageView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ancho, alto);
        lp.setMargins(margenI, margenTop, margenD, margenBottom);
        divisor.setLayoutParams(lp);
        divisor.setBackgroundColor(c);

        return divisor;
    }
}
