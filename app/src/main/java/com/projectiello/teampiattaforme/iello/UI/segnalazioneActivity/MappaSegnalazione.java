package com.projectiello.teampiattaforme.iello.UI.segnalazioneActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projectiello.teampiattaforme.iello.utilities.MappaGoogle;

/**
 * Created by riccardomaldini on 08/10/17.
 * Mappa utilizzata in SegnalazioneActivity. Questa deve gestire il posizionamento del marker da
 * parte dell'utente, coordinare il rapporto della mappa con alcuni elementi dell'activity e fornire
 * delle funzionalità collegate alla mappa, accessibili in varie parti dell'applicazione
 */
class MappaSegnalazione extends MappaGoogle
        implements GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    // riferimento all'activity; consente di accedere ad alcuni elementi dell'interfaccia
    private SegnalazioneActivity mSegnalazioneActivity;

    // marker attualmente impostato sulla mappa
    private Marker mMarkerCorrente;


    /**
     * Costruttore con in quale inizializzare la mappa
     */
    MappaSegnalazione(SegnalazioneActivity sAct) {
        super(sAct);
        mSegnalazioneActivity = sAct;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        googleMap.setOnMapClickListener(this);
    }

    /**
     * Rimuove il marker dalla mappa e ne cancella i riferimenti
     */
    private void clearMarker() {
        if(mMarkerCorrente != null)
            mMarkerCorrente.remove();
        mMarkerCorrente = null;
    }


    /**
     * Attiva le funzionalità per interagire con la mappa per la segnalazione, ovvero:
     * rende cliccabile la mappa e rende possibile eliminare eventuali marker
     */
    void attivaFunzioniMappa() {
        getMappaGoogle().setOnMapClickListener(this);
        getMappaGoogle().setOnMarkerClickListener(this);
    }


    /**
     * Permette di accedere al marker per inviarlo al DB Firebase
     */
    LatLng getMarkerPosition() {
        return mMarkerCorrente.getPosition();
    }


    /**
     * Resetta la mappa dopo l'invio della posizione a Firebase, eliminando i marker e nascondendo
     * il tasto di invio
     */
    void resettaInterfacciaMappa() {
        clearMarker();
        mSegnalazioneActivity.hideFabInvia();
    }


    /**
     * Cliccando sulla mappa, viene posizionato un marker alle coordinate del click, e viene
     * mostrato il tasto per inviare la posizione
     */
    @Override
    public void onMapClick(LatLng latLng) {
        clearMarker();
        mMarkerCorrente = getMappaGoogle().addMarker(new MarkerOptions().draggable(false).position(latLng));
        mMarkerCorrente.setIcon(BitmapDescriptorFactory.defaultMarker(52));
        mSegnalazioneActivity.showFabInvia();
    }


    /*
     * return true evita l'animazione predefinita google
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }
}
