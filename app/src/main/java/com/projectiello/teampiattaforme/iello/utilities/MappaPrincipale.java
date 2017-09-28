package com.projectiello.teampiattaforme.iello.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
import com.projectiello.teampiattaforme.iello.R;
import com.projectiello.teampiattaforme.iello.UI.MainActivity;
import com.projectiello.teampiattaforme.iello.dataLogic.ElencoParcheggi;
import com.projectiello.teampiattaforme.iello.dataLogic.Parcheggio;
import com.projectiello.teampiattaforme.iello.ricercaParcheggi.DownloadParcheggi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by riccardomaldini on 26/09/17.
 * Singleton che permette di gestire esternamente la mappa della main.
 */

public class MappaPrincipale {

    private static final MappaPrincipale ourInstance = new MappaPrincipale();

    public static MappaPrincipale getInstance() {
        return ourInstance;
    }

    private MappaPrincipale() {}

    // riferimento alla mappa
    private GoogleMap mGoogleMap;

    // lista dei marker nella mappa
    private List<Marker> mListMarker = new ArrayList<>();

    // coordinate iniziale impostate su urbino
    public static LatLng COORD_INIZIALI = new LatLng(43.7262568, 12.6365634);


    /**
     * Richiede a GooglePlayServices il download della mappa; quindi la pone nel fragment map, e
     * appena la mappa è disponibile la inizializza
     */
    public void inizializzaMappa(final AppCompatActivity activity) {
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        ((MainActivity) activity).showProgressBar();

        SupportMapFragment mapFragment = (SupportMapFragment) activity.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;

                // imposta stile mappa
                int stileMappa = HelperPreferences.getStileMappa(activity);
                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = mGoogleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    activity, stileMappa));

                    if (!success) {
                        Log.e(TAG, "Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(COORD_INIZIALI, 15.0f));

                // avvia una ricerca preliminare cercando i posteggi nelle vicinanze di urbino
                DownloadParcheggi dp = new DownloadParcheggi((MainActivity) activity, COORD_INIZIALI, true);
                dp.execute();
            }
        });
    }

    /**
     * Metodo invocato per modificare lo stile della mappa
     */
    public void aggiornaStile(Context c) {
        // imposta stile mappa
        int stileMappa = HelperPreferences.getStileMappa(c);
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mGoogleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            c, stileMappa));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }


    /**
     * Riposiziona la camera con un'animazione
     */
    public void muoviCamera(LatLng coordinate) {
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(coordinate, 16);
        mGoogleMap.animateCamera(location);
    }


    /**
     * Imposta un marker per ogni parcheggio, e se disponibile per la posizione attuale
     */
    public void settaMarkers(Context c) {
        // rimuovi tutti i marker
        for(Marker m : mListMarker) {
            m.remove();
        }
        mListMarker.clear();

        // aggiungi un marker per ogni posizione
        for (Parcheggio p : ElencoParcheggi.getInstance().getListParcheggi()) {
            LatLng posParcheggio = p.getCoordinate();
            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(posParcheggio)
                    .title(p.getIndirizzoUI())
                    .icon(BitmapDescriptorFactory.defaultMarker(45)));
            mListMarker.add(marker);
        }


        // aggiungi un marker nella posizione dell'utente
        if(ElencoParcheggi.getInstance().getCoordAttuali() != COORD_INIZIALI) {
            LatLng crd = ElencoParcheggi.getInstance().getCoordAttuali();
            String indirizzo = null;

            Geocoder geocoder = new Geocoder(c, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(crd.latitude, crd.longitude, 1);
                indirizzo = c.getString(R.string.tua_posizione) + ", " + addresses.get(0).getAddressLine(0);
            } catch (IOException e) {
                e.printStackTrace();
                indirizzo = c.getString(R.string.tua_posizione);
            }

            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(ElencoParcheggi.getInstance().getCoordAttuali())
                    .title(indirizzo));
            mListMarker.add(marker);
        }
    }
}
