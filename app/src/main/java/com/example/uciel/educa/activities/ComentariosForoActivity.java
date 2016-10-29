package com.example.uciel.educa.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.uciel.educa.R;
import com.example.uciel.educa.domain.ProgressBarHandler;
import com.example.uciel.educa.domain.SingletonUserLogin;
import com.example.uciel.educa.network.RQSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ComentariosForoActivity extends AppCompatActivity {
    private Bundle extras;
    private SingletonUserLogin userLoginData;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ProgressBarHandler progressBH;
    private ArrayList<String> comentariosForo = new ArrayList<>();
    private LinearLayout llMensajesForo;
    private ScrollView scroll_view;

    private final String URL_COMENTARIOS = "http://educa-mnforlenza.rhcloud.com/api/comentario/listar/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios_foro);

        extras = getIntent().getExtras();

        userLoginData = SingletonUserLogin.getInstance();

        setToolbar(); // Setear Toolbar como action bar

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        progressBH = new ProgressBarHandler(this, (RelativeLayout) findViewById(R.id.rlComentarios));
        progressBH.show();

        initializeData();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(extras.getString("TITULO_TEMA"));
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
                                Intent home = new Intent(ComentariosForoActivity.this,Home.class);
                                startActivity(home);
                                break;
                            case "Mis Cursos":
                                Intent foro = new Intent(ComentariosForoActivity.this,MisCursos.class);
                                startActivity(foro);
                                break;
                            case "Mis Diplomas":
                                Intent misDiplomas = new Intent(ComentariosForoActivity.this, MisDiplomas.class);
                                startActivity(misDiplomas);
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    private void initializeData(){
        // Instantiate the RequestQueue.
        String url = URL_COMENTARIOS + extras.getInt("ID_TEMA");
        android.util.Log.d("MSG", "URL_COM= " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseComentariosResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        android.util.Log.d("MSG", "ERROR RESPONSE");
                        CharSequence text = "Error al cargar comentarios. ¡Reintente nuevamente!";
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
        );
        RQSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void parseComentariosResponse(String response){
        JSONArray jsonarray;
        try {
            jsonarray = new JSONArray(response);
            for (int i = 0; i < jsonarray.length() ; i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String nYa = jsonobject.getJSONObject("usuario").getString("nombre") + " " + jsonobject.getJSONObject("usuario").getString("apellido");
                comentariosForo.add(nYa + " dijo: \n" + jsonobject.getString("descripcion"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        };

        progressBH.hide();
        cargarTemas();
    }

    private void cargarTemas() {
        llMensajesForo = (LinearLayout) findViewById(R.id.linearScrollForo);
        llMensajesForo.removeAllViews();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels - 56 - 100 - 170;

        android.util.Log.d("MSG", "HS= " + height);

        scroll_view = (ScrollView) findViewById(R.id.scrollForo);
        scroll_view.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, height));
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) scroll_view.getLayoutParams();
        //layoutParams.topMargin = 208;
        layoutParams.height = height;
        scroll_view.setLayoutParams(layoutParams);



        for(int i = 0; i < comentariosForo.size(); i++){
            TextView tvMensajeForo = new TextView(this);
            tvMensajeForo.setText(comentariosForo.get(i));
            llMensajesForo.addView(tvMensajeForo);

            // Agrego un divisor
            llMensajesForo.addView(crearDivisor(LinearLayout.LayoutParams.MATCH_PARENT, 1, 10, 15, 10, 15, Color.LTGRAY));
        }

        scroll_view.post(new Runnable() {
            @Override
            public void run() {
                scroll_view.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etMensajeIngresado = (EditText) findViewById(R.id.editTextForo);
                if(etMensajeIngresado.getText().toString().length() < 500){
                    TextView tvMensajeForo = new TextView(ComentariosForoActivity.this);
                    tvMensajeForo.setText(etMensajeIngresado.getText().toString());
                    llMensajesForo.addView(tvMensajeForo);

                    // Agrego un divisor
                    llMensajesForo.addView(crearDivisor(LinearLayout.LayoutParams.MATCH_PARENT, 1, 10, 15, 10, 15, Color.LTGRAY));

                    scroll_view.post(new Runnable() {
                        @Override
                        public void run() {
                            scroll_view.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                } else {
                    String mensaje = "¡Excediste en número máximo permitido de caracteres!";
                    Toast toast = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT);
                    toast.show();
                }
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
