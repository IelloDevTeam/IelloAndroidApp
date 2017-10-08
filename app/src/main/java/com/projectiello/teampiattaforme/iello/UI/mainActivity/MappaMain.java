package com.projectiello.teampiattaforme.iello.UI.mainActivity;

import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projectiello.teampiattaforme.iello.R;
import com.projectiello.teampiattaforme.iello.dataLogic.ElencoParcheggi;
import com.projectiello.teampiattaforme.iello.dataLogic.Parcheggio;
import com.projectiello.teampiattaforme.iello.utilities.MappaGoogle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by riccardomaldini on 08/10/17.
 * Mappa utilizzata nella MainActivity. Di questa mappa in particolare va gestito il posizionamento
 * dei markers al download dei parcheggi da Iello API.
 */

public class MappaMain extends MappaGoogle {

    // lista dei markers presenti nella mappa
    private List<Marker> mListMarker = new ArrayList<>();

    // riferimento all'activity generatrice
    private MainActivity mMainActivity;


    /**
     * Costruttore con in quale inizializzare la mappa. In particolare, dalla mappa main vanno
     * impostate alcune proprietà specifiche.
     */
    MappaMain(MainActivity mainActivity) {
        super(mainActivity);
        mMainActivity = mainActivity;
    }


    /**
     * Imposta un marker per ogni parcheggio, e se disponibile, per la posizione attuale
     */
    public void settaMarkers() {
        // rimuovi tutti i markers
        for(Marker m : mListMarker)
            m.remove();
        mListMarker.clear();

        // aggiungi un marker per ogni posizione
        for (Parcheggio p : ElencoParcheggi.getInstance().getListParcheggi()) {
            LatLng coordParcheggio = p.getCoordinate();

            Marker marker = getMappaGoogle().addMarker(new MarkerOptions()
                    .position(coordParcheggio)
                    .title(p.getIndirizzoUI())
                    .icon(BitmapDescriptorFactory.defaultMarker(45)));

            mListMarker.add(marker);
        }

        // aggiungi un marker nella posizione dell'utente
        if(ElencoParcheggi.getInstance().getCoordAttuali() != COORD_INIZIALI) {
            LatLng coordUsr = ElencoParcheggi.getInstance().getCoordAttuali();

            // trova l'indirizzo corrispondente alla posizione dell'utente tramite
            // Google Geocoding API
            String indirizzo;
            Geocoder geocoder = new Geocoder(mMainActivity, Locale.getDefault());
            try {
                List<Address> addresses
                        = geocoder.getFromLocation(coordUsr.latitude, coordUsr.longitude, 1);
                indirizzo = mMainActivity.getString(R.string.tua_posizione) + ", " + addresses.get(0).getAddressLine(0);
            } catch (IOException e) {
                e.printStackTrace();
                indirizzo = mMainActivity.getString(R.string.tua_posizione);
            }

            // posizionamento del marker, impostato con l'indirizzo dell'utente come titolo
            Marker marker = getMappaGoogle().addMarker(new MarkerOptions()
                    .position(ElencoParcheggi.getInstance().getCoordAttuali())
                    .title(indirizzo));
            mListMarker.add(marker);
        }
    }
}
