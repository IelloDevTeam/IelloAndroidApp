package com.projectiello.teampiattaforme.iello.utilities;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projectiello.teampiattaforme.iello.R;
import com.projectiello.teampiattaforme.iello.dataLogic.ElencoParcheggi;
import com.projectiello.teampiattaforme.iello.dataLogic.Parcheggio;

import java.util.ArrayList;
import java.util.List;

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
    private LatLng mCoordIniziali = new LatLng(43.724283, 12.635698);


    /**
     * Richiede a GooglePlayServices il download della mappa; quindi la pone nel fragment map, e
     * appena la mappa è disponibile la inizializza
     */
    public void inizializzaMappa(AppCompatActivity activity) {
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) activity.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCoordIniziali, 15.0f));
            }
        });
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


        if(ElencoParcheggi.getInstance().getCoordAttuali() != mCoordIniziali) {
            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(ElencoParcheggi.getInstance().getCoordAttuali())
                    .title(c.getString(R.string.tua_posizione)));
            mListMarker.add(marker);
        }
    }
}
