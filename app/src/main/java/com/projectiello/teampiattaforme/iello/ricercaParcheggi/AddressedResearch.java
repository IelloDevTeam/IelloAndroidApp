package com.projectiello.teampiattaforme.iello.ricercaParcheggi;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.projectiello.teampiattaforme.iello.R;
import com.projectiello.teampiattaforme.iello.UI.MainActivity;
import com.projectiello.teampiattaforme.iello.dataLogic.ElencoParcheggi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by riccardomaldini on 25/09/17.
 * AsyncTask che prende in ingresso un indirizzo sotto forma di stringa, lo converte in coordinate
 * geografiche tramite l'Api di geocoding di Google, e infine trova i parcheggi nelle vicinanze di
 * tale coordinata.
 */

public class AddressedResearch extends AsyncTask<String, Void, JSONObject> {

    // utilizzato sia come contesto che come activityMain all'avvio di DownloadParcheggi
    private MainActivity mMainActivity;

    // query inserita dall'utente nella barra di ricerca, convertita in formato per url
    private String mQueryFormattata;

    // query non formattata
    private String mQueryGrezza;

    /**
     * Costruttore dell'asyncTask, nel quale vengono adattati e memorizzati i parametri necessari.
     */
    public AddressedResearch(MainActivity mainActivity, String queryGrezza) {
        mMainActivity = mainActivity;
        mQueryGrezza = queryGrezza;
        mQueryFormattata = queryGrezza.replaceAll(" ", "+" + "");
    }


    /**
     * Prima dell'esecuzione viene mostrata una schermata di caricamento
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mMainActivity.showProgressBar();
    }


    /**
     * Viene effettuata la ricerca delle coordinate tramite Google Geocoding API
     */
    @Override
    protected JSONObject doInBackground(String... params) {

        // creazione url
        String url = "https://maps.google.com/maps/api/geocode/json" +
                     "?address=" + mQueryFormattata + "&key=" + mMainActivity.getString(R.string.google_geoc_key);

        Log.i("searchurl", url);
        // interrogazione api
        return HelperRete.volleySyncRequest(mMainActivity, url);
    }


    /**
     * Al termine della ricerca vengono estratte le coordinate della posizione, quindi viene
     * effettuata la ricerca dei parcheggi in zona.
     */
    @Override
    protected void onPostExecute(JSONObject result) {
        try {
            Log.i("jsonresp", result.toString());

            double lng = ((JSONArray) result.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            double lat = ((JSONArray) result.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            LatLng coordRicerca = new LatLng(lat, lng);
            ElencoParcheggi.getInstance().setCoordAttuali(coordRicerca);

            mQueryGrezza = mQueryGrezza.substring(0,1).toUpperCase() + mQueryGrezza.substring(1);
            mMainActivity.setTitle(mQueryGrezza);

            DownloadParcheggi adp = new DownloadParcheggi(mMainActivity, coordRicerca, false);
            adp.execute();

        } catch (JSONException e) {
            e.printStackTrace();
            mMainActivity.hideProgressBar();
        }

    }
}