package com.example.uciel.educa.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.uciel.educa.R;

import java.io.InputStream;
import java.util.Locale;

public class VideoActivity extends AppCompatActivity {

    private VideoView myVideoView;
    private ProgressDialog progressDialog;
    private int position = 0;
    private MediaController mediaControls;
    private Toolbar toolbar;

    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        extras = getIntent().getExtras();

        //set the media controller buttons
        if (mediaControls == null) {
            mediaControls = new MediaController(VideoActivity.this);
        }

        this.setToolbar();
        //initialize the VideoView
        myVideoView = (VideoView) findViewById(R.id.video_view);
        inicializarVideo();
    }

    private void inicializarVideo() {
        // create a progress bar while the video file is loading
        progressDialog = new ProgressDialog(VideoActivity.this);
        // set a title for the progress bar
        progressDialog.setTitle(extras.getString("UNIDAD"));
        // set a message for the progress bar
        progressDialog.setMessage("Loading...");
        //set the progress bar not cancelable on users' touch
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                ProgressDialog progress = (ProgressDialog)dialog;
                finish();
            }
        });
        // show the progress bar
        progressDialog.show();



        try {
            //set the media controller in the VideoView
            myVideoView.setMediaController(mediaControls);

            //set the uri of the video to be played
            //myVideoView.setVideoURI(Uri.parse("http://res.cloudinary.com/nhuallpa/video/upload/v1476072479/video_h7ihf2.mp4"));

            int idCurso = extras.getInt("ID_CURSO");
            int idUnidad = extras.getInt("ID_UNIDAD");
            String urlVideo = "http://educa-mnforlenza.rhcloud.com/api/unidad/"+idUnidad+"/"+idCurso+"/video";
            Log.i("VideoActivity", "Cargando video " + urlVideo);
            myVideoView.setVideoURI(Uri.parse(urlVideo));

            InputStream subtitule = getResources().getAssets().open("discurso.vtt");
            myVideoView.addSubtitleSource(subtitule, MediaFormat.createSubtitleFormat("text/vtt", Locale.ENGLISH.getLanguage()));


        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        myVideoView.requestFocus();

        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                // close the progress bar and play the video
                progressDialog.dismiss();
                //if we have a position on savedInstanceState, the video playback should start from here
                myVideoView.seekTo(position);
                if (position == 0) {
                    myVideoView.start();
                } else {
                    //if we come from a resumed activity, video playback will be paused
                    myVideoView.pause();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //we use onSaveInstanceState in order to store the video playback position for orientation change
        savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
        myVideoView.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //we use onRestoreInstanceState in order to play the video playback from the stored position
        position = savedInstanceState.getInt("Position");
        myVideoView.seekTo(position);

    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarVideo);
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
