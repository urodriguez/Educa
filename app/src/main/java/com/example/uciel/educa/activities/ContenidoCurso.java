package com.example.uciel.educa.activities;

import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.*;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.example.uciel.educa.adapters.VPAdapterContCurso;
import com.example.uciel.educa.domain.Curso;
import com.example.uciel.educa.domain.ProgressBarHandler;
import com.example.uciel.educa.domain.SingletonUserLogin;
import com.example.uciel.educa.network.RQSingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

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
    private int idSesionActual;
    private ArrayList<Pair<String,String>> temasForo = new ArrayList<>();
    private int idForoActual;
    private boolean foroModerado;

    private ProgressBarHandler progressBH;

    private LinearLayout llUnidades, llMensajesForo;

    private AlertDialog.Builder alertDialogBuilder;

    private int cantDeTemas = 0;

    private final String URL_CURSOS_DISP = "http://educa-mnforlenza.rhcloud.com/api/curso/listar";
    private final String URL_MIS_SESIONES = "http://educa-mnforlenza.rhcloud.com/api/usuario/mis-sesiones/";
    private final String URL_FORO = "http://educa-mnforlenza.rhcloud.com/api/foro/";
    private final String URL_CREAR_TEMA = "http://educa-mnforlenza.rhcloud.com/api/tema/crear";

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
        progressBH = new ProgressBarHandler(this, (RelativeLayout) findViewById(R.id.rlContUnidad));
        progressBH.show();

        // Instantiate the RequestQueue.
        String url;

        url = URL_CURSOS_DISP;
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
                        CharSequence text = "Error al cargar unidades. Reintente nuevamente!";
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                        toast.show();
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
        JSONArray jsonarray;
        try {
            jsonarray = new JSONArray(response);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                if(jsonobject.getInt("idCurso") == curso.getId()){
                    idSesionActual = jsonobject.getInt("numero");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = URL_FORO + "/" + idSesionActual + "/" + curso.getId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
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
                        CharSequence text = "Error al cargar foro. Reintente nuevamente!";
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
        );
        RQSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void parseForoResponse(String response){
        android.util.Log.d("MSG", response.toString());
        JSONArray jsonarray;
        try {
            JSONObject jResponse = new JSONObject(response);
            idForoActual = jResponse.getInt("id");
            foroModerado = jResponse.getBoolean("moderado");
            jsonarray = new JSONArray(jResponse.getString("temas"));
            for (int i = jsonarray.length() - 1; i >= 0 ; i--) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                android.util.Log.d("MSG", "T= " + jsonobject.getString("titulo") + " D= " + jsonobject.getString("descripcion"));
                temasForo.add(new Pair<>(jsonobject.getString("titulo"), jsonobject.getString("descripcion")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < temasForo.size() ; i++) {
            android.util.Log.d("MSG", "Ti= " + temasForo.get(i).first + " De= " + temasForo.get(i).second);
        }

        progressBH.hide();
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

            final int textViewUnidadID = i+100;
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

                    TextView textView = (TextView) findViewById(textViewUnidadID);
                    cursosIntent.putExtra("UNIDAD", textView.getText());
                    cursosIntent.putExtra("ID_UNIDAD", (textViewUnidadID - 100) + 1);//Transforma a ID de unidad

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
        llMensajesForo = (LinearLayout) viewPager.findViewById(R.id.linearScrollTemaForo);
        llMensajesForo.removeAllViews();

        for (int i = 0; i < temasForo.size(); i++){
            cargarTemas(temasForo.get(i).first, temasForo.get(i).second);
        }

        FloatingActionButton fab = (FloatingActionButton) viewPager.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                alertDialogBuilder = new AlertDialog.Builder(ContenidoCurso.this);
                alertDialogBuilder.setTitle("Tema nuevo");

                LayoutInflater inflater = getLayoutInflater();

                final View dialogView = inflater.inflate(R.layout.dialog_tema_foro, null);
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                alertDialogBuilder.setView(dialogView);

                // set dialog message
                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    private String mensaje;
                    public void onClick(DialogInterface dialog, int id) {
                        EditText tvT = (EditText) dialogView.findViewById(R.id.titulo);
                        EditText tvD = (EditText) dialogView.findViewById(R.id.descripcion);
                        if(tvT.getText().toString().length() < 45 && tvD.getText().toString().length() < 200){
                            registrarTema(tvT.getText().toString(), tvD.getText().toString());
                            if(!foroModerado){
                                //si NO es moderado ademas de hacer un post, tambien lo muestra
                                cargarTemas(tvT.getText().toString(), tvD.getText().toString());
                            } else
                                mensaje = "¡El tema aparecerá luego de ser aprobado!";
                                Toast toast = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT);
                                toast.show();
                        } else {
                            CharSequence text = "¡Limite de caracteres excedido!";
                            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    }
                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });

    }

    private void registrarTema(String titulo, String descripcion) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        //JSONObject to send
        JSONObject dataToSend = new JSONObject();
        try {
            dataToSend.put("idUsuario", userLoginData.getUserID());
            dataToSend.put("idForo", idForoActual);
            dataToSend.put("titulo", titulo);
            dataToSend.put("descripcion", descripcion);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        android.util.Log.d("MSG", dataToSend.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, URL_CREAR_TEMA, dataToSend,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        android.util.Log.d("MSG", "SUCCESS Response");
                        android.util.Log.d("MSG", response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        android.util.Log.d("MSG", "ERROR Response");
                        CharSequence text = "Error al crear tema ¡Reintente nuevamente!";
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
        );

        // Add the request to the RequestQueue.
        queue.add(jsonObjReq);
    }

    private void cargarTemas(String titulo, String descripcion){
        CardView cv = new CardView(this);
        RelativeLayout rl = new RelativeLayout(this);

        RelativeLayout.LayoutParams paramsdos = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 100);
        cv.setLayoutParams(paramsdos); //causes layout update

        TextView tv = new TextView(this);
        final int textViewUnidadID = cantDeTemas++;
        tv.setId(textViewUnidadID);
        tv.setText(titulo);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextSize(24);
        tv.setPadding(4,4,4,4);
        rl.addView(tv);

        TextView tvd = new TextView(this);
        tvd.setText(descripcion);
        tvd.setTypeface(null, Typeface.ITALIC);
        tvd.setTextSize(18);
        tvd.setPadding(4,4,4,4);
        rl.addView(tvd);


        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)tv.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        tv.setLayoutParams(params); //causes layout update

        RelativeLayout.LayoutParams paramsD = (RelativeLayout.LayoutParams)tvd.getLayoutParams();
        paramsD.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        tvd.setLayoutParams(paramsD); //causes layout update

        cv.addView(rl);

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent temaForoIntent = new Intent(ContenidoCurso.this,ComentariosForoActivity.class);
                temaForoIntent.putExtra("FORO_MODERADO", foroModerado);
                temaForoIntent.putExtra("ID_TEMA", textViewUnidadID);
                TextView textView = (TextView) findViewById(textViewUnidadID);
                temaForoIntent.putExtra("TITULO_TEMA", textView.getText());

                startActivity(temaForoIntent);

                    /*TextView textView = (TextView) findViewById(finalI);
                    CharSequence text = textView.getText();
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                    toast.show();*/
            }
        });

        llMensajesForo.addView(cv);

        TextView txtEspacio = new TextView(this);
        txtEspacio.setText(" ");
        txtEspacio.setTextSize(3);

        llMensajesForo.addView(txtEspacio);

    }
}
