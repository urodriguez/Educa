package com.example.uciel.educa.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.uciel.educa.R;
import com.example.uciel.educa.adapters.VPAdapterContUnidad;
import com.example.uciel.educa.domain.Choice;
import com.example.uciel.educa.domain.Curso;
import com.example.uciel.educa.domain.ItemDeExamen;
import com.example.uciel.educa.domain.Pregunta;
import com.example.uciel.educa.domain.Respuesta;
import com.example.uciel.educa.domain.SingletonUserLogin;
import com.example.uciel.educa.network.RQSingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ContenidoUnidad extends AppCompatActivity {
    private SingletonUserLogin userLoginData;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TabLayout tabs;
    private ViewPager viewPager;
    private VPAdapterContUnidad vpaContUnidad;

    private LinearLayout llMaterial, llPracticas, llExamenPresentacion, llExamenItems;

    private Bundle extras;

    private List<ItemDeExamen> itemsExamen = new ArrayList<>();

    private final String URL_CONSULTAR_EXAMEN = "http://educa-mnforlenza.rhcloud.com/api/consultarExamen/";
    private final String URL_PREG_UNIDAD = "http://educa-mnforlenza.rhcloud.com/api/unidad/";

    private boolean examFail = false;
    private String estadoExamen;
    private Button btnComenzar;
    private int cantDePregAprobadas = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenido_unidad);

        extras = getIntent().getExtras();

        setToolbar(); // Setear Toolbar como action bar

        userLoginData = SingletonUserLogin.getInstance();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        setTabs();
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(extras.getString("UNIDAD"));
        toolbar.setTitleTextColor(Color.WHITE);

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
                                Intent home = new Intent(ContenidoUnidad.this,Home.class);
                                startActivity(home);
                                break;
                            case "Mis Cursos":
                                Intent misCursos = new Intent(ContenidoUnidad.this,MisCursos.class);
                                startActivity(misCursos);
                                break;
                            case "Mis Diplomas":
                                Intent misDiplomas = new Intent(ContenidoUnidad.this,MisDiplomas.class);
                                startActivity(misDiplomas);
                                break;
                        }

                        drawerLayout.closeDrawers(); // Cerrar drawer
                        return true;
                    }
                }
        );
    }

    private void setTabs() {
        tabs = (TabLayout) findViewById(R.id.tabs);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        vpaContUnidad = new VPAdapterContUnidad(getSupportFragmentManager());
        viewPager.setAdapter(vpaContUnidad);

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
                                initializeExamData();

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

        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                viewPager.setCurrentItem(0);
                cargarMaterial();
            }
        };
        handler.postDelayed(r, 1000);

    }

    private void initializeExamData(){
        // Instantiate the RequestQueue.
        String url = URL_CONSULTAR_EXAMEN + extras.getInt("ID_CURSO")+ "/" + extras.getInt("ID_UNIDAD") + "/" +  userLoginData.getUserID();
        android.util.Log.d("MSG", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        examFail = false;
                        parseStateExamResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        android.util.Log.d("MSG", "ERROR RESPONSE");
                        examFail = true;

                    }
                }
        );
        RQSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void parseStateExamResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            estadoExamen = jsonObject.getString("estado");
            if(estadoExamen.equals("APROBADO")){
                cantDePregAprobadas = jsonObject.getInt("cantDePregAprobadas");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!examFail){
            // Instantiate the RequestQueue.
            String url = URL_PREG_UNIDAD + extras.getInt("ID_UNIDAD") + "/" + extras.getInt("ID_CURSO") + "/examen";
            android.util.Log.d("MSG", url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            examFail = false;
                            parseExamResponse(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            android.util.Log.d("MSG", "ERROR RESPONSE");
                            examFail = true;

                        }
                    }
            );
            RQSingleton.getInstance(this).addToRequestQueue(stringRequest);
        }
    }

    public void parseExamResponse(String response){
        android.util.Log.d("MSG", response.toString());
        try {
            JSONObject jsonExamen = new JSONObject(response);
            JSONArray jsonArray = jsonExamen.getJSONArray("preguntas");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonItemExamen = jsonArray.getJSONObject(i);
                if(jsonItemExamen.getBoolean("multipleChoice") == true){
                    Choice itemChoice = new Choice(jsonItemExamen.getJSONObject("id").getInt("idPregunta"),
                            jsonItemExamen.getString("enunciado"),
                            jsonItemExamen.getJSONArray("opciones"),
                            this);
                    itemsExamen.add(itemChoice);
                } else{
                    Pregunta preg = new Pregunta(jsonItemExamen.getJSONObject("id").getInt("idPregunta"),
                            jsonItemExamen.getString("enunciado"),
                            jsonItemExamen.getString("respuesta"),
                            this);
                    itemsExamen.add(preg);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        cargarExamen();
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
                Intent videoIntent = new Intent(ContenidoUnidad.this,VideoActivity.class);
                videoIntent.putExtra("ID_CURSO", extras.getInt("ID_CURSO"));
                videoIntent.putExtra("UNIDAD", extras.getString("UNIDAD"));
                videoIntent.putExtra("ID_UNIDAD", extras.getInt("ID_UNIDAD"));
                android.util.Log.i("INFO", "ID UNIDAD RECIBIDA: " + extras.getInt("ID_UNIDAD"));
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
                Intent materialIntent = new Intent(ContenidoUnidad.this,MaterialActivity.class);
                materialIntent.putExtra("ID_CURSO", extras.getInt("ID_CURSO"));
                materialIntent.putExtra("UNIDAD", extras.getString("UNIDAD"));
                materialIntent.putExtra("ID_UNIDAD", extras.getInt("ID_UNIDAD"));
                android.util.Log.i("INFO", "ID UNIDAD RECIBIDA: " + extras.getInt("ID_UNIDAD"));
                startActivity(materialIntent);
            }
        });


        llMaterial.addView(rlMaterial);
        llMaterial.addView(rlText);

        // Agrego un divisor
        llMaterial.addView(crearDivisor(LinearLayout.LayoutParams.MATCH_PARENT, 1, 10, 15, 10, 15, Color.LTGRAY));
    }

    private void cargarPracticas() {

    }

    private void cargarExamen() {
        llExamenPresentacion = (LinearLayout) viewPager.findViewById(R.id.llExamenPresentacion);

        TextView txtExamen = (TextView) viewPager.findViewById(R.id.textViewExamTitulo);
        txtExamen.setText("Examen - Unidad " + extras.getInt("ID_UNIDAD"));

        TextView txtDuracion = (TextView) viewPager.findViewById(R.id.textViewExamDuracion);
        txtDuracion.setText("Duración estimida: " + "X" + " min");

        cargarEstadoExamen();

        btnComenzar = (Button) viewPager.findViewById(R.id.buttonComenzar);

        btnComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(examFail == false){
                    llExamenPresentacion.setVisibility(View.GONE);

                    llExamenItems = (LinearLayout) viewPager.findViewById(R.id.llExamenItems);
                    llExamenItems.setVisibility(View.VISIBLE);

                    for (int i = 0; i < itemsExamen.size(); i++){
                        TextView txtEnunciado = new TextView(ContenidoUnidad.this);
                        txtEnunciado.setText(itemsExamen.get(i).getEnunciado());
                        txtEnunciado.setTextSize(18);
                        txtEnunciado.setTypeface(null, Typeface.BOLD);
                        llExamenItems.addView(txtEnunciado);

                        llExamenItems.addView(itemsExamen.get(i).getCompletable());

                        // Agrego un divisor
                        llExamenItems.addView(crearDivisor(LinearLayout.LayoutParams.MATCH_PARENT, 1, 10, 15, 10, 15, Color.LTGRAY));
                    }

                    Button btnCorregir = new Button(ContenidoUnidad.this);
                    btnCorregir.setText(R.string.boton_comenzar_corregir);
                    llExamenItems.addView(btnCorregir);

                    btnCorregir.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        boolean estaAprobado = true;
                        for (int i = 0; i < itemsExamen.size(); i++){
                            if(itemsExamen.get(i).itemAprobado() == false){
                                estaAprobado = false;
                            } else {
                                cantDePregAprobadas++;
                            }
                        }
                        android.util.Log.d("MSG", "RESULTADO= " + estaAprobado);
                        android.util.Log.d("MSG", "#APROB= " + cantDePregAprobadas);
                        llExamenItems.setVisibility(View.GONE);
                        llExamenItems.removeAllViews();

                        //TODO hacer un POST con el estado del examen
                        initializeExamData();

                        llExamenPresentacion.setVisibility(View.VISIBLE);
                        }
                    });
                } else{
                    CharSequence text = "Error al cargar examen. Reintente nuevamente!";
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
    }

    private void cargarEstadoExamen(){
        Button btnEstado = (Button) viewPager.findViewById(R.id.buttonEstado);
        if(estadoExamen.equals("PENDIENTE")){
            btnEstado.setText("PENDIENTE");
            btnEstado.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
        } else{
            float porcentaje = ((float)cantDePregAprobadas/itemsExamen.size())*100;
            BigDecimal bd = new BigDecimal(Float.toString(porcentaje));
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            android.util.Log.d("MSG", "PORCENTAJE= " + bd);
            if(porcentaje > 60){
                btnEstado.setText("APROBADO " + bd + "%" + "\n" +
                        "Acertadas/Total = " + cantDePregAprobadas + "/" + itemsExamen.size() + "\n");
                btnEstado.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
                btnComenzar.setEnabled(false);
            } else {
                //TODO enviarlo a Mis Cursos pues desaprobo y ya no puede ver el contenido
                CharSequence text = "Lamentablemente has desaprobado el examen. " +
                        "Puedes volver a intentarlo cuando comience la proxima sesión";
                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                toast.show();

                startActivity(new Intent(ContenidoUnidad.this, MisCursos.class));
                /*btnEstado.setText("DESAPROBADO " + bd + "%" + "\n" +
                        "Acertadas/Total = " + cantDePregAprobadas + "/" + itemsExamen.size() + "\n");
                btnEstado.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                btnComenzar.setEnabled(false);*/
            }
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
}
