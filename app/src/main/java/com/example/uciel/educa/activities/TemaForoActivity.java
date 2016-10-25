package com.example.uciel.educa.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.example.uciel.educa.R;
import com.example.uciel.educa.domain.SingletonUserLogin;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TemaForoActivity extends AppCompatActivity {
    private Bundle extras;
    private SingletonUserLogin userLoginData;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ArrayList<String> mensajesForo = new ArrayList<>();
    private LinearLayout llMensajesForo;
    private ScrollView scroll_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tema_foro);

        extras = getIntent().getExtras();

        userLoginData = SingletonUserLogin.getInstance();

        setToolbar(); // Setear Toolbar como action bar

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        cargarTemas();
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
                                Intent home = new Intent(TemaForoActivity.this,Home.class);
                                startActivity(home);
                                break;
                            case "Mis Cursos":
                                Intent foro = new Intent(TemaForoActivity.this,MisCursos.class);
                                startActivity(foro);
                                break;
                            case "Mis Diplomas":
                                Intent misDiplomas = new Intent(TemaForoActivity.this, MisDiplomas.class);
                                startActivity(misDiplomas);
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    private void cargarTemas() {
        for(int i = 0; i < 25; i++){
            mensajesForo.add("mensaje "+i);
        }



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



        for(int i = 0; i < mensajesForo.size(); i++){
            TextView tvMensajeForo = new TextView(this);
            tvMensajeForo.setText(mensajesForo.get(i));
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
                    TextView tvMensajeForo = new TextView(TemaForoActivity.this);
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
