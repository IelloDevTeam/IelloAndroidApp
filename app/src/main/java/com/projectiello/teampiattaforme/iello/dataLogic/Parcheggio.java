package com.projectiello.teampiattaforme.iello.dataLogic;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by riccardomaldini on 25/09/17.
 * Classe che rappresenta un singolo parcheggio.
 */

public class Parcheggio {

    // indirizzo del parcheggio, calcolato tramite reverse geocoding
    private  String mIndirizzo;

    // distanza del parcheggio dalla posizione dell'utente, o dalla posizione scelta
    private int mDistanza;

    // cordinate del parcheggio
    private LatLng mCoordinate;


    /**
     * Costruttore per l'assegnamento del parcheggio da un oggetto JSON, utilizzato durante il
     * download dei parcheggi tramite IelloApi.
     * @throws JSONException dovuta alla conversione dell'oggetto JSON in dati del parcheggio
     */
    public Parcheggio(JSONObject jParcheggio) throws JSONException {
        double lat = jParcheggio.getDouble("latitudine");
        double lng = jParcheggio.getDouble("longitudine");

        mCoordinate = new LatLng(lat, lng);

        // todo imposta indirizzo tramite reverse geocoding, distanza tramite conversione
        mDistanza = 100;
        mIndirizzo = "Via del Tutto Eccezionale";
    }


    public LatLng getCoordinate() {
        return mCoordinate;
    }

    public String getDistanzaUI() {
        return mDistanza + " m";
    }

    public String getIndirizzoUI() {
        return mIndirizzo;
    }
}
