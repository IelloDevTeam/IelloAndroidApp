package com.projectiello.teampiattaforme.iello.UI.segnalazioneActivity;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.projectiello.teampiattaforme.iello.R;
import com.projectiello.teampiattaforme.iello.utilities.HelperRete;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by riccardomaldini on 08/10/17.
 * Sposta la mappa a seconda dell'indirizzo selezionato dall'utente in SegnalazioneActivity.
 */
class AsyncRicercaIndirizzo extends AsyncTask<Void, JSONObject, JSONObject> {
    private String mQuery;
    private SegnalazioneActivity mSegnalazioneActivity;

    AsyncRicercaIndirizzo(String query, SegnalazioneActivity segnalazioneActivity) {
        mQuery = query;
        mSegnalazioneActivity = segnalazioneActivity;
    }

    @Override
    protected void onPreExecute() {
        mSegnalazioneActivity.showProgressBar();
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        // viene interrogata l'Api di Google per il Geocoding: viene passato all'Api un indirizzo,
        // e se questo viene interpretato da Google, vengono fornite in risposta le coordinate di
        // tale indirizzo
        String queryFormattata = mQuery.replaceAll(" ", "+");
        String url = "https://maps.google.com/maps/api/geocode/json" +
                "?address=" + queryFormattata + "&key=" + mSegnalazioneActivity.getString(R.string.google_geoc_key);

        return HelperRete.volleySyncRequest(mSegnalazioneActivity, url);
    }


    @Override
    protected void onPostExecute(JSONObject response) {
        // l'oggetto in risposta è la risposta dell'Api. Se corrisponde a delle coordinate, queste
        // vengono utilizzate per centrare la mappa
        try {
            Log.i("jsonresp", response.toString());

            double lng = response.optJSONArray("results").optJSONObject(0)
                    .optJSONObject("geometry").optJSONObject("location")
                    .optDouble("lng");

            double lat = response.optJSONArray("results").optJSONObject(0)
                    .optJSONObject("geometry").optJSONObject("location")
                    .optDouble("lat");

            LatLng coordRicerca = new LatLng(lat, lng);

            mSegnalazioneActivity.getMappa().muoviCamera(coordRicerca);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mSegnalazioneActivity,
                    R.string.indirizzo_non_riconosciuto, Toast.LENGTH_SHORT).show();
        }

        mSegnalazioneActivity.hideProgressBar();
    }
}
