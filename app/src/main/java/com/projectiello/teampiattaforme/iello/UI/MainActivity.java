package com.projectiello.teampiattaforme.iello.UI;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.projectiello.teampiattaforme.iello.Manifest;
import com.projectiello.teampiattaforme.iello.R;
import com.projectiello.teampiattaforme.iello.apiConnection.AsyncDownloadParcheggi;
import com.projectiello.teampiattaforme.iello.dataLogic.ElencoParcheggi;
import com.projectiello.teampiattaforme.iello.dataLogic.Parcheggio;
import com.projectiello.teampiattaforme.iello.utilities.HelperGeolocalizzazione;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * Created by riccardomaldini on 25/09/17.
 * Activity principale.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    // riferimento alla search bar
    private MaterialSearchView mSearchView;

    // riferimento alla mappa
    private GoogleMap mGoogleMap;

    // classe per la gestione della geolocalizzazione
    private HelperGeolocalizzazione mHelpLocalization;



    // lista dei marker nella mappa
    private List<Marker> mListMarker = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mHelpLocalization = new HelperGeolocalizzazione(this);

        // inizializza il fab. Un click nel fab determina la posizione dell'utente, sposta la mappa
        // su tale posizione, inizializza la mappa e mostra i risultati della ricerca.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo ottieni la posizione

                mHelpLocalization.avviaRicercaPosizione();


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // inizializzazione searchView
        // imposta la barra di ricerca
        mSearchView = (MaterialSearchView) findViewById(R.id.search_view);
        mSearchView.setVoiceSearch(true);
        //mArraySquadre = ElencoPartite.getInstance().getArrayPartite();
        //mSearchView.setSuggestions(mArraySquadre);
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // todo trova con google le coordinate dell'indirizzo

                AsyncDownloadParcheggi adp = new AsyncDownloadParcheggi(MainActivity.this, 43.724283, 12.635698);
                adp.execute();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Do some magic
                return false;
            }
        });


        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
            mHelpLocalization.avviaRicercaPosizione();
        }
    }




    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        LatLng urbino = new LatLng(43.724283, 12.635698);
        Marker marker = googleMap.addMarker(new MarkerOptions().position(urbino).title("Marker in Sydney"));
        mListMarker.add(marker);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(urbino, 16.0f));
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            // gestione ricerca vocale
            /*
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }
            return;
            */
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(searchItem);

        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_impostazioni) {

        } else if (id == R.id.nav_segnalazione) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void posizionaCamera(double lat, double lon) {
        // riposiziona la camera
        LatLng actualPosition = new LatLng(lat, lon);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(actualPosition, 16.0f));
    }


    // metodo per concludere la ricerca dei parcheggi. Posiziona la mappa, la popola e inizializza
    // il fragment parcheggi
    public void popolaParcheggi() {

        // rimuovi tutti i marker
        for(Marker m : mListMarker) {
            m.remove();
        }
        mListMarker.clear();

        // aggiungi un marker per ogni posizione
        for (Parcheggio p : ElencoParcheggi.getInstance().getListParcheggi()) {
            LatLng posParcheggio = new LatLng(p.getLat(), p.getLong());
            Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(posParcheggio).title(p.getIndirizzoUI()));
            mListMarker.add(marker);
        }

        // crea fragment parcheggi
        ParcheggiFragment.newInstance(this);
    }

}
