package com.example.uciel.educa.activities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.uciel.educa.R;

public class MaterialActivity extends AppCompatActivity {

    private WebView myWebView;
    private String urlMaterial;
    private Bundle extras;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);

        extras = getIntent().getExtras();
        int idCurso = extras.getInt("ID_CURSO");
        int idUnidad = extras.getInt("ID_UNIDAD");

        setToolbar();

        myWebView = (WebView) this.findViewById(R.id.webView);

        myWebView.getSettings().setLoadsImagesAutomatically(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        myWebView.getSettings().setBuiltInZoomControls(true);


        urlMaterial = "http://educa-mnforlenza.rhcloud.com/api/unidad/"+idUnidad+"/"+idCurso+"/material";
        android.util.Log.d("MSG", "URL= " + urlMaterial);


        myWebView.loadUrl(urlMaterial);

        myWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
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
            ab.setHomeAsUpIndicator(R.drawable.ic_ab_back_holo_dark_am);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }
}
