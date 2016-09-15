package com.example.uciel.educa.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.uciel.educa.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;

import java.util.Arrays;
import java.util.HashMap;

public class Log extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener{

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private String fbAccessToken;

    GoogleApiClient mGoogleApiClient;

    private SliderLayout mDemoSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_log);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
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

        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email", "user_birthday"));
        if(AccessToken.getCurrentAccessToken() != null){
            LoginManager.getInstance().logOut();
        }
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        loginResult.getAccessToken().getUserId(),
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                try {
                                    Intent homeIntent = new Intent(Log.this,Home.class);
                                    homeIntent.putExtra("USER", response.getJSONObject().getString("name"));
                                    startActivity(homeIntent);
                                    //nameUser[0] = response.getJSONObject().getString("name");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ).executeAsync();


/*                android.util.Log.d(TAG,
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                        );*/


            }

            @Override
            public void onCancel() {
                android.util.Log.d(TAG,"Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                android.util.Log.d(TAG,"Login attempt failed.");
            }
        });

        this.cargarSliderLayout();
    }

    private void cargarSliderLayout() {
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Descubre el conocimiento",R.drawable.conocimiento);
        file_maps.put("Descarga material",R.drawable.material);
        file_maps.put("Rinde el examen",R.drawable.examen);
        file_maps.put("Recibe el diploma", R.drawable.diploma);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(3000);
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);//para FACE

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    // [END onActivityResult]


    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        android.util.Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            android.util.Log.d(TAG, "account Name: " + acct.getDisplayName());
            android.util.Log.d(TAG, "account Email: " + acct.getEmail());

            Intent homeIntent = new Intent(Log.this,Home.class);
            homeIntent.putExtra("USER", acct.getDisplayName());
            startActivity(homeIntent);
        } else {
            android.util.Log.d(TAG, "ERROR AL INICIAR SESION");
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        android.util.Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

}
