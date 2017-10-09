package com.projectiello.teampiattaforme.iello.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.projectiello.teampiattaforme.iello.R;


import static android.content.ContentValues.TAG;

/**
 * Created by riccardomaldini on 08/10/17.
 * Classe base per la gestione di una mappa in una data activity. Tale classe verrà poi sovrascritta
 * per creare classi specifiche per la gestione delle mappe.
 */

public class MappaGoogle implements OnMapReadyCallback {
    // riferimento alla mappa caricata tramite Google Maps API
    private GoogleMap mGoogleMap;

    // contesto corrente, utilizzato in varie occorrenze
    private Context mContext;

    // coordinate iniziali della mappa, per convenzione sopra Urbino
    public static LatLng COORD_INIZIALI = new LatLng(43.7262568, 12.6365634);


    /**
     * Costruttore con in quale inizializzare la mappa. Verrà sovrascritto dalle classi figlie per
     * impostare alcune proprietà più specifiche
     */
    public MappaGoogle(AppCompatActivity c) {
        SupportMapFragment mapFragment = (SupportMapFragment) c.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mContext = c;
    }


    /**
     * Metodo invocato per modificare lo stile della mappa
     */
    public void aggiornaStile() {
        // imposta stile mappa
        int stileMappa = HelperPreferences.getStileMappa(mContext);
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                    mContext, stileMappa));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }


    /**
     * Quando la mappa è disponibile, viene impostato lo stile di questa e centrata la mappa sopra
     * Urbino di default.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        // imposta stile mappa
        int stileMappa = HelperPreferences.getStileMappa(mContext);
        try {
            boolean success = mGoogleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            mContext, stileMappa));

            if (!success)
                Log.e(TAG, "Style parsing failed.");

        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(COORD_INIZIALI, 15.0f));
    }


    /**
     * Riposiziona la camera con un'animazione
     */
    public void muoviCamera(LatLng coordinate) {
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(coordinate, 16);
        mGoogleMap.animateCamera(location);
    }


    protected GoogleMap getMappaGoogle() {
        return mGoogleMap;
    }
}
