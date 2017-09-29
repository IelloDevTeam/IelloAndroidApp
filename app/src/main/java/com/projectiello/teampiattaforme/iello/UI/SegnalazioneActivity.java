package com.projectiello.teampiattaforme.iello.UI;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.projectiello.teampiattaforme.iello.R;
import com.projectiello.teampiattaforme.iello.ricercaParcheggi.HelperRete;
import com.projectiello.teampiattaforme.iello.utilities.HelperPreferences;
import com.projectiello.teampiattaforme.iello.utilities.MappaPrincipale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by riccardomaldini on 29/09/17.
 * Activity dalla quale si può inviare una segnalazione di un posteggio individuato da un utente.
 */

public class SegnalazioneActivity extends AppCompatActivity {

    // riferimento alla mappa dell'activity
    private GoogleMap mGoogleMap;

    // riferimento alla progressbar
    private FrameLayout mProgBar;

    // riferimento agli altri elementi
    private EditText mEditIndirizzo;

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



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;

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
        FloatingActionButton fabInvia = (FloatingActionButton) findViewById(R.id.fabInvia);


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


        fabInvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo logica caricamento dati firebase
            }
        });
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
