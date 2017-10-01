package com.projectiello.teampiattaforme.iello.UI;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.projectiello.teampiattaforme.iello.R;
import com.projectiello.teampiattaforme.iello.dataLogic.Segnalazione;
import com.projectiello.teampiattaforme.iello.ricercaParcheggi.HelperRete;
import com.projectiello.teampiattaforme.iello.utilities.HelperPreferences;
import com.projectiello.teampiattaforme.iello.utilities.MappaPrincipale;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by riccardomaldini on 29/09/17.
 * Activity dalla quale si può inviare una segnalazione di un posteggio individuato da un utente.
 */

public class SegnalazioneActivity extends AppCompatActivity implements GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    // riferimento alla mappa dell'activity
    private GoogleMap mGoogleMap;

    // riferimento alla progressbar
    private FrameLayout mProgBar;

    // riferimento agli altri elementi
    private EditText mEditIndirizzo;

    private FloatingActionButton mFabInvia;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    /* Utilizzo firebase per ora, perchè supporta l'autenticazione
       e le API nostre non ancora e sarebbero poco sicure
     */
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;

    // Tag JSON per caricamento su FIREBASE
    private static final String TAG_SEGNALAZIONI = "segnalazioni";

    // Marker attualmente impostato sulla mappa.
    private Marker mMarkerLocation;

    private boolean mLogged;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segnalazione);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.segnala_posteggio);
        }

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                mGoogleMap.setOnMapLongClickListener(SegnalazioneActivity.this);
                mGoogleMap.setOnMarkerClickListener(SegnalazioneActivity.this);
                // imposta stile mappa
                int stileMappa = HelperPreferences.getStileMappa(SegnalazioneActivity.this);
                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = mGoogleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    SegnalazioneActivity.this, stileMappa));

                    if (!success) {
                        Log.e(TAG, "Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MappaPrincipale.COORD_INIZIALI, 15.0f));
            }
        });

        mProgBar = (FrameLayout) findViewById(R.id.clippedProgressBar);
        mEditIndirizzo = (EditText) findViewById(R.id.editIndirizzo);
        FloatingActionButton fabSearch = (FloatingActionButton) findViewById(R.id.fabSearch);
        mFabInvia = (FloatingActionButton) findViewById(R.id.fabInvia);


        mEditIndirizzo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    AsyncRicercaRapidaIndirizzo rri = new AsyncRicercaRapidaIndirizzo();
                    rri.execute();
                    return true;
                }
                return false;
            }
        });

        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncRicercaRapidaIndirizzo rri = new AsyncRicercaRapidaIndirizzo();
                rri.execute();
            }
        });

        mFirebaseAuth.signInWithEmailAndPassword("piattaforme@gmail.com", "piattaforme101")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            Toast.makeText(SegnalazioneActivity.this, "Connesso al DB Firebase! L'app è pronta a inviare posizioni.",
                                    Toast.LENGTH_LONG).show();

                            mLogged = true;
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SegnalazioneActivity.this, "Non sei connesso al Database. Non pioi effettuare operazioni.",
                                    Toast.LENGTH_LONG).show();
                            mLogged = false;
                        }
                        hideProgressBar();
                    }
                });

        mFabInvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // TODO: logica caricamento dati firebase
            sendLocationToFirebase(mMarkerLocation.getPosition());
            setSelectedMarker(null);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if(currentUser == null) {
            //_multiFabButton.hide();
        }
    }

    private void sendLocationToFirebase(LatLng location)
    {
        if (location != null) {

            if (mFirebaseAuth.getCurrentUser() != null) {
                Segnalazione segnalazione = new Segnalazione(
                    location.latitude,
                    location.longitude,
                    mFirebaseAuth.getCurrentUser().getUid()
                );

                mFirebaseDatabase.getReference("/" + TAG_SEGNALAZIONI)
                        .push()
                        .setValue(segnalazione);

                Toast.makeText(SegnalazioneActivity.this,
                        "Segnalazione Inviata",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    /*
     * Metodi per accedere alla progressBar, utilizzata come schermata di caricamento durante la
     * ricerca dei parcheggi.
     */
    public void showProgressBar() {
        mProgBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        mProgBar.setVisibility(View.GONE);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mGoogleMap.clear();
        mGoogleMap.addMarker(new MarkerOptions().draggable(false).position(latLng));
        setSelectedMarker(null);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Selezione del marker, cambia colore in giallo
        // e rendo visibile il FAB per l'invio della posizione
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        setSelectedMarker(marker);
        return false;
    }

    private void setSelectedMarker(Marker marker)
    {
        if(mLogged)
        {
            if(mMarkerLocation != null)
                mMarkerLocation.remove();
            mMarkerLocation = marker;
            if(marker != null)
                mFabInvia.show();
            else
                mFabInvia.hide();
        }
        else
            Snackbar.make(findViewById(R.id.coordinator),
                    "Devi aver effettuato il login, assicurati di avere una connessione internet attiva",
                    Toast.LENGTH_SHORT).show();
    }


    /**
     * AsyncTask strettamente collegato all'activity, quindi definito qua dentro. Sposta la mappa
     * a seconda dell'indirizzo selezionato dall'utente.
     */
    private class AsyncRicercaRapidaIndirizzo extends AsyncTask<Void, JSONObject, JSONObject> {
        private String mQuery;

        @Override
        protected void onPreExecute() {
            showProgressBar();
            mQuery = mEditIndirizzo.getText().toString();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            String queryFormattata = mQuery.replaceAll(" ", "+" + "");
            String url = "https://maps.google.com/maps/api/geocode/json" +
                    "?address=" + queryFormattata + "&key=" + getString(R.string.google_geoc_key);

            return HelperRete.volleySyncRequest(SegnalazioneActivity.this, url);
        }


        @Override
        protected void onPostExecute(JSONObject response) {

            try {
                Log.i("jsonresp", response.toString());

                double lng = ((JSONArray) response.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

                double lat = ((JSONArray) response.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");

                LatLng coordRicerca = new LatLng(lat, lng);

                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(coordRicerca, 16);
                mGoogleMap.animateCamera(location);


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(SegnalazioneActivity.this,
                        R.string.indirizzo_non_riconosciuto, Toast.LENGTH_SHORT).show();
            }

            hideProgressBar();
        }
    }
}
