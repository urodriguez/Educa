package com.example.uciel.educa.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import android.util.*;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.uciel.educa.R;
import com.example.uciel.educa.adapters.ViewPagerAdapter;
import com.example.uciel.educa.domain.Critica;
import com.example.uciel.educa.domain.Curso;
import com.example.uciel.educa.domain.Sesion;
import com.example.uciel.educa.domain.SingletonUserLogin;
import com.example.uciel.educa.domain.Unidad;
import com.example.uciel.educa.network.RQSingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DescripcionCurso extends AppCompatActivity {

    private SingletonUserLogin userLoginData;
    private Curso  curso;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TabLayout tabs;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    private NetworkImageView fotoCursoNiv;

    private TextView tvNombre,tvEstado,tvProfesor,tvDescripcion;

    private RatingBar ratingBar;

    private LinearLayout llComentarios, llUnidades, llSesiones;

    private List<Button> botonesInscripcion;

    private Bundle extras;

    private Map<Integer, Integer> mis_IDCURSO_IDSESSION = new HashMap<>();

    private final String IMAGE_ROOT_URL = "http://educa-mnforlenza.rhcloud.com/api/";
    private final String URL_ULT_CURSOS = "http://educa-mnforlenza.rhcloud.com/api/curso/ultimos-cursos";
    private final String URL_CURSOS_ACTUALES = "http://educa-mnforlenza.rhcloud.com/api/curso/listar";
    private final String URL_SUSCRIBIR = "http://educa-mnforlenza.rhcloud.com/api/usuario/suscribir/";
    private final String URL_DESUSCRIBIR = "http://educa-mnforlenza.rhcloud.com/api/usuario/desuscribir/";
    private final String URL_MIS_SESIONES = "http://educa-mnforlenza.rhcloud.com/api/usuario/mis-sesiones/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_curso);

        extras = getIntent().getExtras();

        initializeData();

        setToolbar(); // Setear Toolbar como action bar

        //setTabs();

        userLoginData = SingletonUserLogin.getInstance();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    private void initializeData(){
        // Instantiate the RequestQueue.
        String url;
        if(extras.getBoolean("ES_DE_ULT_CURSOS") == true){
            url = URL_ULT_CURSOS;
        } else {
            url = URL_CURSOS_ACTUALES;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        android.util.Log.i("INFO", "Response is: "+ response.substring(0,10));
                        parseHomeResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        android.util.Log.d("DEBUG", "That didn't work!");
                    }
                }
        );
        RQSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void parseHomeResponse(String response){
        Gson g = new Gson();

        Type collectionType = new TypeToken<Collection<Curso>>(){}.getType();
        Collection<Curso> cursos = g.fromJson(response, collectionType);

        for(Curso c: cursos){
            if(c.getId() == extras.getInt("ID")){
                curso = c;
                android.util.Log.d("TAG", "NOMBRE :" + curso.getNombre());
                android.util.Log.d("TAG", "ID: " + String.valueOf(curso.getId()));
            }

        }

        setTabs();

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
                                cargarDetalle();
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

        /*final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                viewPager.setCurrentItem(0);
                cargarDetalle();
                cargarSesionesInscriptas(URL_MIS_SESIONES + userLoginData.getUserID());
            }
        };
        handler.postDelayed(r, 500);*/

        viewPager.setCurrentItem(0);
        cargarDetalle();
        cargarSesionesInscriptas(URL_MIS_SESIONES + userLoginData.getUserID());

    }

    void cargarDetalle(){
        Log.d("MSG", "CARGANDO DETALLE");
        fotoCursoNiv = (NetworkImageView)viewPager.findViewById(R.id.curso_photo_niv);
        if(fotoCursoNiv == null){
            Log.d("MSG", "ERROR NULL1");
        }
        ImageLoader mImageLoader;
        // Get the NetworkImageView that will display the image.
        mImageLoader = RQSingleton.getInstance(getApplicationContext()).getImageLoader();
        if(mImageLoader == null){
            Log.d("MSG", "ERROR NULL2");
        }
        fotoCursoNiv.setImageUrl(IMAGE_ROOT_URL + curso.getLinkImagen(), mImageLoader);

        cargarTextosDescriptivos();

        ratingBar = (RatingBar)viewPager.findViewById(R.id.ratingBar);
        ratingBar.setRating(curso.getValoracionesPromedio());

        cargarCriticas();
    }

    private void cargarTextosDescriptivos() {
        tvNombre = (TextView) viewPager.findViewById(R.id.textView2);
        tvEstado = (TextView) viewPager.findViewById(R.id.textView3);
        tvProfesor = (TextView) viewPager.findViewById(R.id.textView4);
        tvDescripcion = (TextView) viewPager.findViewById(R.id.textView5);

        tvNombre.setText("Nombre: " + curso.getNombre());
        tvEstado.setText("Estado: " + curso.getEstado());
        tvProfesor.setText("Profesor: " + curso.getNombreCompletoDocente());
        tvDescripcion.setText("Descripción: " + curso.getDescripcion());
    }


    private void cargarCriticas() {
        llComentarios = (LinearLayout) viewPager.findViewById(R.id.llComentarios);

        if(curso.getCantDeCriticas() == 0){
            CharSequence text = "Este curso aún no ha recibido críticas";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();
        }

        llComentarios.addView(crearDivisor(LinearLayout.LayoutParams.MATCH_PARENT, 0, 10, 10, 10, 10, Color.WHITE));

        for(int i = 0; i < curso.getCantDeCriticas(); i++){
            LinearLayout llRB = new LinearLayout(this);
            llRB.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 24));

            RatingBar rBar = new RatingBar(this, null, android.R.attr.ratingBarStyleSmall);
            rBar.setNumStars(5);
            rBar.setRating(curso.getCalificacionCriticaNum(i));
            llRB.addView(rBar);

            llComentarios.addView(llRB);

            TextView txt = new TextView(this);
            txt.setText("Fecha: " + curso.getFechaCriticaNum(i) + "\n" +
                        "Comentario: " + curso.getComentarioCriticaNum(i) + "\n");
            llComentarios.addView(txt);

            // Agrego un divisor
            llComentarios.addView(crearDivisor(LinearLayout.LayoutParams.MATCH_PARENT, 1, 10, 15, 10, 15, Color.LTGRAY));
        }
    }

    private void cargarUnidades() {
        llUnidades = (LinearLayout) viewPager.findViewById(R.id.llUnidades);
        llUnidades.removeAllViews();

        for(int i = 0; i < curso.getCantDeUnidades(); i++){
            TextView txt = new TextView(this);
            txt.setTextSize(18);
            txt.setTypeface(null, Typeface.BOLD);
            txt.setText("Unidad " + String.valueOf(i + 1)+ " - " + curso.getTituloUnidadNum(i));

            Button bc = new Button(this);
            bc.setText(String.valueOf(curso.getDuracionEstUnidadNum(i)) + " hs");
            bc.setTextSize(14);
            bc.setBackgroundResource(R.drawable.round_button);

            RelativeLayout rl = new RelativeLayout(this);
            rl.addView(txt);
            rl.addView(bc);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(88, 88);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.RIGHT_OF, txt.getId());
            bc.setLayoutParams(params); //causes layout update

            llUnidades.addView(rl);

            TextView txtD = new TextView(this);
            txtD.setTextSize(18);
            txtD.setText(curso.getDescripcionUnidadNum(i));
            llUnidades.addView(txtD);

            // Agrego un divisor
            llUnidades.addView(crearDivisor(LinearLayout.LayoutParams.MATCH_PARENT, 1, 10, 15, 10, 15, Color.LTGRAY));
        }
    }

    private void cargarSesiones() {
        llSesiones = (LinearLayout) viewPager.findViewById(R.id.sesiones_linear);
        llSesiones.removeAllViews();

        cargarBotonesDeInscripcion(curso.getSesiones().size());

        for(int i = 0; i < curso.getCantDeSesiones(); i++){
            LinearLayout llH = new LinearLayout(this);
            llH.setOrientation(LinearLayout.HORIZONTAL);

            //ImageView Setup
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.calendario);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(64,LinearLayout.LayoutParams.MATCH_PARENT));
            llH.addView(imageView);

            TextView txt = new TextView(this);
            txt.setTextSize(18);
            txt.setText("Sesión " + String.valueOf(i + 1) + "\n" +
                        "Comienzo: " + curso.getFDSesionNUM(i) + "\n" +
                        "Fin: " + curso.getFHSesionNUM(i) + "\n" +
                        "Inscripciones: " + curso.getFDISesionNUM(i) + "\n");
            llH.addView(txt);

            // Agrego un divisor vertical
            llH.addView(crearDivisor(0, LinearLayout.LayoutParams.WRAP_CONTENT, 24, 8, 24, 8, Color.WHITE));

            llH.addView(botonesInscripcion.get(i));
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams)botonesInscripcion.get(i).getLayoutParams();
            ll.gravity = Gravity.CENTER;
            botonesInscripcion.get(i).setLayoutParams(ll);

            llSesiones.addView(llH);

            // Agrego un divisor horizontal
            llSesiones.addView(crearDivisor(LinearLayout.LayoutParams.MATCH_PARENT, 1, 10, 15, 10, 15, Color.LTGRAY));
        }
    }

    private void cargarBotonesDeInscripcion(final int cantDeSesiones){
        botonesInscripcion = new ArrayList<>();

        //getIDs(URL_MIS_SESIONES + userLoginData.getUserID());
        android.util.Log.i("MSG", "MAP= "+ mis_IDCURSO_IDSESSION.toString());

        if(mis_IDCURSO_IDSESSION.containsKey(curso.getId())){//Entra a un curso ya inscripto
            for(int i = 0; i < cantDeSesiones; i++){
                Button button = new Button(this);
                if ((i+1) == mis_IDCURSO_IDSESSION.get(curso.getId())){
                    button.setText("DESUSCRIBIRSE");
                    button.setBackgroundColor(Color.parseColor("#FF5722"));
                } else {
                    button.setText("NO DISPONIBLE");
                    button.setEnabled(false);
                    button.setBackgroundColor(Color.LTGRAY);
                }
                botonesInscripcion.add(button);
            }
        } else {//Entra a un curso NO inscripto
            for(int i = 0; i < cantDeSesiones; i++){
                Button button = new Button(this);
                button.setText("INSCRIBIRSE");
                button.setBackgroundColor(Color.parseColor("#FF5722"));
                botonesInscripcion.add(button);
            }
        }

        for(int i = 0; i < cantDeSesiones; i++){
            final int finalI = i;
            botonesInscripcion.get(i).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(botonesInscripcion.get(finalI).getText().equals("INSCRIBIRSE")){
                        botonesInscripcion.get(finalI).setText("DESUSCRIBIRSE");
                        inscribirseAsesion(finalI+1);

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
                        desinscribirseAsesion(finalI+1);

                        //Reestablezco los demas botones
                        for (int j = 0; j < cantDeSesiones; j++){
                            if (j != finalI){
                                botonesInscripcion.get(j).setText("INSCRIBIRSE");
                                botonesInscripcion.get(j).setEnabled(true);
                                botonesInscripcion.get(j).setBackgroundColor(Color.parseColor("#FF5722"));
                            }
                        }
                    }
                }
            });
        }
    }

    private void inscribirseAsesion(int idSesion) {
        mis_IDCURSO_IDSESSION.put(curso.getId(), idSesion);

        String url = URL_SUSCRIBIR + userLoginData.getUserID() + "/" + curso.getId() + "/" + idSesion;

        android.util.Log.d("MSG", "URL_I= " + url);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        android.util.Log.d("MSG", "SUCCESS Response");
                        android.util.Log.d("MSG", response.toString());

                        CharSequence text = "¡Felicitaciones! Te has inscripto";
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        android.util.Log.d("MSG", "ERROR Response");
                        CharSequence text = "¡Error al procesar la inscripción! Vuelve a intentarlo";
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                        toast.show();
                    }
        }
        );
        // Add the request to the RequestQueue.
        queue.add(jsonObjReq);
    }

    private void desinscribirseAsesion(int idSesion) {
        mis_IDCURSO_IDSESSION.remove(curso.getId());

        String url = URL_DESUSCRIBIR + userLoginData.getUserID() + "/" + curso.getId() + "/" + idSesion;

        android.util.Log.d("MSG", "URL_D= " + url);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        android.util.Log.d("MSG", "SUCCESS Response");
                        android.util.Log.d("MSG", response.toString());

                        CharSequence text = "Te has desinscripto con exito";
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        android.util.Log.d("MSG", "ERROR Response");
                        CharSequence text = "¡Error al procesar la desinscripción! Vuelve a intentarlo";
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                        toast.show();
                    }
        }
        );
        // Add the request to the RequestQueue.
        queue.add(jsonObjReq);
    }

    private void cargarSesionesInscriptas(String URL){
        android.util.Log.i("MSG", "URL= "+ URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        android.util.Log.i("MSG", "Response is: "+ response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject explrObject = jsonArray.getJSONObject(i);
                                int idCurso = explrObject.getInt("idCurso");
                                int idSesion = explrObject.getInt("numero");
                                android.util.Log.i("MSG", "data: "+ idCurso + "-" + idSesion );
                                mis_IDCURSO_IDSESSION.put(idCurso, idSesion);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        android.util.Log.d("DEBUG", "That didn't work!");
                    }
                }
        );
        RQSingleton.getInstance(this).addToRequestQueue(stringRequest);
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
