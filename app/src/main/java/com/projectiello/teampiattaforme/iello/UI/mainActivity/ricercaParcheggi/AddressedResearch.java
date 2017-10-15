package com.projectiello.teampiattaforme.iello.UI.mainActivity.ricercaParcheggi;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.projectiello.teampiattaforme.iello.R;
import com.projectiello.teampiattaforme.iello.UI.mainActivity.MainActivity;
import com.projectiello.teampiattaforme.iello.dataLogic.ElencoParcheggi;
import com.projectiello.teampiattaforme.iello.utilities.HelperRete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by riccardomaldini on 25/09/17.
 * AsyncTask che prende in ingresso un indirizzo sotto forma di stringa, lo converte in coordinate
 * geografiche tramite l'Api di geocoding di Google, e infine trova i parcheggi nelle vicinanze di
 * tale coordinata.
 */

public class AddressedResearch extends AsyncTask<Void, String, String> {

    // utilizzato sia come contesto che come activityMain all'avvio di AsyncDownloadParcheggi
    private MainActivity mMainActivity;

    // query inserita dall'utente nella barra di ricerca, convertita in formato per url
    private String mQueryTitolo;

    private LatLng mCoordinateCercate;

    // query non formattata
    private String mQueryGrezza;

    // costanti di return
    private static final String RICERCA_COMPLETATA = "RICERCA_COMPLETATA";
    private static final String NO_INTERNET = "NO_INTERNET";



    /**
     * Costruttore dell'asyncTask, nel quale vengono adattati e memorizzati i parametri necessari.
     */
    public AddressedResearch(MainActivity mainActivity, String queryGrezza) {
        mMainActivity = mainActivity;
        mQueryGrezza = queryGrezza;
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
    protected String doInBackground(Void... params) {
        if (HelperRete.isNetworkAvailable(mMainActivity)) {

            String queryFormattata = mQueryGrezza.replaceAll(" ", "+" + "");
            mQueryTitolo = mQueryGrezza.substring(0,1).toUpperCase() + mQueryGrezza.substring(1);

            // creazione url
            String url = "https://maps.google.com/maps/api/geocode/json" +
                    "?address=" + queryFormattata + "&key=" + mMainActivity.getString(R.string.google_geoc_key);

            JSONObject response = HelperRete.volleySyncRequest(mMainActivity, url);

            // ottieni le coordinate dell'indirizzo tramite la risposta di GoogleApi
            try {
                if (response != null) {
                    Log.i("jsonresp", response.toString());

                    double lng = ((JSONArray) response.get("results")).getJSONObject(0)
                            .getJSONObject("geometry").getJSONObject("location")
                            .getDouble("lng");

                    double lat = ((JSONArray) response.get("results")).getJSONObject(0)
                            .getJSONObject("geometry").getJSONObject("location")
                            .getDouble("lat");

                    mCoordinateCercate = new LatLng(lat, lng);

                    return RICERCA_COMPLETATA;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            return NO_INTERNET;
        }

        return null;
    }


    /**
     * Al termine della ricerca dell'indirizzo, viene effettuata la ricerca dei parcheggi in zona.
     */
    @Override
    protected void onPostExecute(String result) {
        switch(result) {
            case RICERCA_COMPLETATA: {
                ElencoParcheggi.getInstance().setCoordAttuali(mCoordinateCercate);
                mMainActivity.setTitle(mQueryTitolo);

                AsyncDownloadParcheggi adp
                        = new AsyncDownloadParcheggi(mMainActivity, mCoordinateCercate);
                adp.execute();
                break;
            }

            case NO_INTERNET: {
                Toast.makeText(mMainActivity, R.string.no_connection, Toast.LENGTH_SHORT).show();
                mMainActivity.hideProgressBar();
                break;
            }

            default: {
                Toast.makeText(mMainActivity, R.string.indirizzo_non_riconosciuto, Toast.LENGTH_SHORT).show();
                mMainActivity.hideProgressBar();
                break;
            }
        }
    }
}