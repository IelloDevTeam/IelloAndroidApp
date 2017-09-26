package com.projectiello.teampiattaforme.iello.utilities;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projectiello.teampiattaforme.iello.UI.MainActivity;
import com.projectiello.teampiattaforme.iello.dataLogic.ElencoParcheggi;
import com.projectiello.teampiattaforme.iello.dataLogic.Parcheggio;
import com.projectiello.teampiattaforme.iello.ricercaParcheggi.DownloadParcheggi;

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

    public LatLng mCoordIniziali = new LatLng(43.724283, 12.635698);


    public void inizializzaMappa(GoogleMap gmap) {
        mGoogleMap = gmap;
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCoordIniziali, 15.0f));
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
    public void settaMarkers() {
        // rimuovi tutti i marker
        for(Marker m : mListMarker) {
            m.remove();
        }
        mListMarker.clear();

        // aggiungi un marker per ogni posizione
        for (Parcheggio p : ElencoParcheggi.getInstance().getListParcheggi()) {
            LatLng posParcheggio = p.getCoordinate();
            Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(posParcheggio).title(p.getIndirizzoUI()));
            mListMarker.add(marker);
        }


        if(ElencoParcheggi.getInstance().getCoordAttuali() != mCoordIniziali) {
            // todo imposta un marker per la posizione attuale
        }
    }
}
