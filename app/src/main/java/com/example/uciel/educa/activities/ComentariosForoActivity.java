package com.example.uciel.educa.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.TypedValue;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.uciel.educa.R;
import com.example.uciel.educa.domain.ComentarioForo;
import com.example.uciel.educa.domain.ProgressBarHandler;
import com.example.uciel.educa.domain.SingletonUserLogin;
import com.example.uciel.educa.network.RQSingleton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ComentariosForoActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private Bundle extras;
    private SingletonUserLogin userLoginData;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ProgressBarHandler progressBH;
    private boolean foroModerado;
    private ArrayList<ComentarioForo> comentariosForo = new ArrayList<>();
    private LinearLayout llMensajesForo;
    private ScrollView scroll_view;
    private CardView cvRespuestaComentario;


    private final String URL_COMENTARIOS = "http://educa-mnforlenza.rhcloud.com/api/comentario/listar/";
    private final String URL_CREAR_COMENTARIO = "http://educa-mnforlenza.rhcloud.com/api/comentario/crear";

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

        foroModerado = extras.getBoolean("FORO_MODERADO");

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
                        android.util.Log.d("MSG", response);
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

    private void parseComentariosResponse(String response){
        JSONArray jsonarray;
        try {
            jsonarray = new JSONArray(response);
            for (int i = 0; i < jsonarray.length() ; i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String nYa = jsonobject.getJSONObject("usuario").getString("nombre");
                long fecha = jsonobject.getLong("fechaCreacion");
                boolean estado = jsonobject.getBoolean("aprobado");
                comentariosForo.add(new ComentarioForo(nYa, fecha, jsonobject.getString("descripcion"), estado));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        };

        progressBH.hide();

        armarLayoutComentarios();

        cargarComentarios();
    }

    private void armarLayoutComentarios(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int heightCardWithPaddingInPX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics());
        int heightToolBarInPX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 68, getResources().getDisplayMetrics());
        int height = displaymetrics.heightPixels - heightToolBarInPX - heightCardWithPaddingInPX - 16;//16 son extras

        android.util.Log.d("MSG", "HS= " + height);
        android.util.Log.d("MSG", "WS= " + displaymetrics.widthPixels);

        scroll_view = (ScrollView) findViewById(R.id.scrollForo);
        scroll_view.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, height));
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) scroll_view.getLayoutParams();

        android.util.Log.d("MSG", "LP_HS= " + layoutParams.height);

        layoutParams.height = height;
        scroll_view.setLayoutParams(layoutParams);

        cvRespuestaComentario = (CardView) findViewById(R.id.cvComentario);
        int widthCV =(int) (0.75 * displaymetrics.widthPixels);

        android.util.Log.d("MSG", "CV_W= " + widthCV);
        RelativeLayout.LayoutParams layoutParamsCV = (RelativeLayout.LayoutParams) cvRespuestaComentario.getLayoutParams();
        layoutParamsCV.width = widthCV;
        cvRespuestaComentario.setLayoutParams(layoutParamsCV);
    }

    private void cargarComentarios() {
        llMensajesForo = (LinearLayout) findViewById(R.id.linearScrollForo);
        llMensajesForo.removeAllViews();

        for(int i = 0; i < comentariosForo.size(); i++){
            if(comentariosForo.get(i).fueAprobado()){
                cargarComentario(comentariosForo.get(i).getAutor(), comentariosForo.get(i).getFecha(),comentariosForo.get(i).getContenido());

                // Agrego un divisor
                llMensajesForo.addView(crearDivisor(LinearLayout.LayoutParams.MATCH_PARENT, 1, 10, 15, 10, 15, Color.LTGRAY));
            }
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
                    registrarComentario(etMensajeIngresado.getText().toString());
                    if(!foroModerado){
                        //si NO es moderado ademas de hacer un post, tambien lo muestra
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        android.util.Log.d("MSG", "fechaCom= " + df.format(Calendar.getInstance().getTime()));
                        cargarComentario(userLoginData.getUserName(), df.format(Calendar.getInstance().getTime()), etMensajeIngresado.getText().toString());

                        // Agrego un divisor
                        llMensajesForo.addView(crearDivisor(LinearLayout.LayoutParams.MATCH_PARENT, 1, 10, 15, 10, 15, Color.LTGRAY));

                        scroll_view.post(new Runnable() {
                            @Override
                            public void run() {
                                scroll_view.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
                    } else{
                        String mensaje = "¡El comentario aparecerá luego de ser aprobado!";
                        Toast toast = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    etMensajeIngresado.setText("");
                } else {
                    String mensaje = "¡Excediste en número máximo permitido de caracteres!";
                    Toast toast = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void cargarComentario(String autor, String fecha, String contenido){
        TextView tvAutorMensajeForo = new TextView(ComentariosForoActivity.this);
        tvAutorMensajeForo.setText(autor + " (" + fecha + ") dijo:");
        tvAutorMensajeForo.setTypeface(null, Typeface.BOLD);
        tvAutorMensajeForo.setTextSize(20);
        llMensajesForo.addView(tvAutorMensajeForo);

        TextView tvContenidoMensajeForo = new TextView(ComentariosForoActivity.this);
        tvContenidoMensajeForo.setText(contenido);
        tvContenidoMensajeForo.setTypeface(null, Typeface.ITALIC);
        tvContenidoMensajeForo.setTextSize(18);
        llMensajesForo.addView(tvContenidoMensajeForo);
    }

    private void registrarComentario(String comentario){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        //JSONObject to send
        JSONObject dataToSend = new JSONObject();
        try {
            dataToSend.put("fechaCreacion", Calendar.getInstance().getTimeInMillis());
            dataToSend.put("idUsuario", userLoginData.getUserID());
            dataToSend.put("idTema", extras.getInt("ID_TEMA"));
            dataToSend.put("descripcion", comentario);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        android.util.Log.d("MSG", dataToSend.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, URL_CREAR_COMENTARIO, dataToSend,
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
                CharSequence text = "Error al crear comentario ¡Reintente nuevamente!";
                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        );

        // Add the request to the RequestQueue.
        queue.add(jsonObjReq);

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
