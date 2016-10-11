package com.example.uciel.educa.activities;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.*;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.uciel.educa.R;

public class MaterialActivity extends AppCompatActivity {

    private WebView myWebView;
    private String url;
    private Bundle extras;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);

        myWebView = (WebView) this.findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);

        extras = getIntent().getExtras();
        setToolbar();

        int idCurso = extras.getInt("ID_CURSO");
        int idUnidad = extras.getInt("ID_UNIDAD");

        String urlMaterial = "http://educa-mnforlenza.rhcloud.com/api/unidad/"+idUnidad+"/"+idCurso+"/material";
        android.util.Log.i("MaterialActivity", "Cargando material " + urlMaterial);

        myWebView.loadUrl("https://www.google.com/");

        myWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });
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
}
