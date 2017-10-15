package com.projectiello.teampiattaforme.iello.UI.mainActivity;

import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projectiello.teampiattaforme.iello.R;
import com.projectiello.teampiattaforme.iello.UI.mainActivity.ricercaParcheggi.AsyncDownloadParcheggi;
import com.projectiello.teampiattaforme.iello.dataLogic.ElencoParcheggi;
import com.projectiello.teampiattaforme.iello.dataLogic.Parcheggio;
import com.projectiello.teampiattaforme.iello.utilities.HelperRete;
import com.projectiello.teampiattaforme.iello.utilities.MappaGoogle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by riccardomaldini on 08/10/17.
 * Mappa utilizzata nella MainActivity. Di questa mappa in particolare va gestito il posizionamento
 * dei markers al download dei parcheggi da Iello API.
 */

public class MappaMain extends MappaGoogle
        implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    // lista dei markers presenti nella mappa
    private List<Marker> mListMarker = new ArrayList<>();
    private Marker mPosizioneUtente;

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        getMappaGoogle().setOnMarkerClickListener(this);
        getMappaGoogle().setOnMapClickListener(this);
        AsyncDownloadParcheggi adp
                = new AsyncDownloadParcheggi(mMainActivity, MappaGoogle.COORD_INIZIALI);
        adp.execute();
    }

    /**
     * Imposta un marker per ogni parcheggio, e se disponibile, per la posizione attuale
     */
    public void settaMarkers() {
        // rimuovi tutti i markers
        for(Marker m : mListMarker)
            m.remove();
        mListMarker.clear();
        if(mPosizioneUtente != null)
            mPosizioneUtente.remove();
        mPosizioneUtente = null;

        // aggiungi un marker per ogni posizione
        for (Parcheggio p : ElencoParcheggi.getInstance().getListParcheggi()) {
            LatLng coordParcheggio = p.getCoordinate();

            Marker marker = getMappaGoogle().addMarker(new MarkerOptions()
                    .position(coordParcheggio)
                    .title(p.getIndirizzoUI())
                    .icon(BitmapDescriptorFactory.defaultMarker(52)));

            mListMarker.add(marker);
        }

        // aggiungi un marker nella posizione dell'utente
        if(ElencoParcheggi.getInstance().getCoordAttuali() != COORD_INIZIALI) {
            LatLng coordUsr = ElencoParcheggi.getInstance().getCoordAttuali();

            // posizionamento del marker, impostato con l'indirizzo dell'utente come titolo
            mPosizioneUtente = getMappaGoogle().addMarker(new MarkerOptions()
                    .position(ElencoParcheggi.getInstance().getCoordAttuali())
                    .title(mMainActivity.getString(R.string.tua_posizione)));
        }
    }


    /**
     * distingue la tipologia del marker passato in ingresso.
     */
    private boolean isParcheggio(Marker markerDaTestare) {

        for(Marker markerDaLista : mListMarker) {
            if (markerDaTestare.equals(markerDaLista))
                return true;
        }

        return false;
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        muoviCamera(marker.getPosition());
        if(isParcheggio(marker)
                && mMainActivity.getFragmentManager().findFragmentByTag("parchFrag") == null)
            ParcheggiFragment.newInstance(mMainActivity, marker.getPosition());
        else {
            ParcheggiFragment.clearFragment(mMainActivity);
            Toast.makeText(mMainActivity, marker.getTitle(), Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        ParcheggiFragment.clearFragment(mMainActivity);
    }
}
