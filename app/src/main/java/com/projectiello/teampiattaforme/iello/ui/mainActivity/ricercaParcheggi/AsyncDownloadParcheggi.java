package com.projectiello.teampiattaforme.iello.ui.mainActivity.ricercaParcheggi;


import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.projectiello.teampiattaforme.iello.R;
import com.projectiello.teampiattaforme.iello.ui.mainActivity.MainActivity;
import com.projectiello.teampiattaforme.iello.ui.mainActivity.ParcheggiFragment;
import com.projectiello.teampiattaforme.iello.dataLogic.ElencoParcheggi;
import com.projectiello.teampiattaforme.iello.dataLogic.Parcheggio;
import com.projectiello.teampiattaforme.iello.utilities.HelperPreferences;
import com.projectiello.teampiattaforme.iello.utilities.HelperRete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by riccardomaldini on 25/09/17.
 * AsyncTask che gestisce il download dei dati da IelloApi in modo asincrono, popolando il Singleton
 * ElencoParcheggi.
 */

public class AsyncDownloadParcheggi extends AsyncTask<Void, Void, String> {

    private static final String BASE_URL = "http://iello.webhop.me:4000/iello/v1/parking";

    // riferimento alla MainActivity che consente di intervenire sull'interfaccia
    private MainActivity mMainActivity;

    // costanti di return
    private static final String RICERCA_COMPLETATA = "RICERCA_COMPLETATA";
    private static final String COMPLETATA_NO_RIS = "COMPLETATA_NO_RIS";
    private static final String NO_INTERNET = "NO_INTERNET";

    // dati sulla posizione intorno cui cercare
    private LatLng mCoordRicerca;
    private int mRange;


    /**
     * Costruttore con popolamento degli attributi. AtStart distingue se il download viene
     * effettuato all'avvio dell'app; il tal caso bisogna eseguire in maniera particolare.
     */
    public AsyncDownloadParcheggi(MainActivity activity, LatLng coordinateRicerca) {
        mMainActivity = activity;
        mCoordRicerca = coordinateRicerca;
        mRange = HelperPreferences.getRange(activity);
    }


    /**
     * Riposiziona la mappa prima di eseguire la ricerca
     */
    @Override
    protected void onPreExecute() {
        mMainActivity.showProgressBar();
        if(HelperRete.isNetworkAvailable(mMainActivity))
            mMainActivity.getMappa().muoviCamera(mCoordRicerca);
        ParcheggiFragment.clearFragment(mMainActivity);
    }


    /**
     * Se possibile, si cercano i parcheggi nelle vicinanze tramite IelloApi.
     */
    @Override
    protected String doInBackground(Void... params) {

        if(HelperRete.isNetworkAvailable(mMainActivity)) {
            // scarica i parcheggi tramite l'Api
            popolaElencoParcheggi();

            // restituisce la corrispondente stringa risultato
            if(ElencoParcheggi.getInstance().getListParcheggi().size() == 0)
                return COMPLETATA_NO_RIS;

            // riordina la lista dei parcheggi in base alla distanza dall'origine
            ElencoParcheggi.getInstance().ordinaParcheggiPerDistanza();

            return RICERCA_COMPLETATA;


        } else {
            return NO_INTERNET;
        }
    }


    /**
     * crea, popola e mostra il fragment parcheggi.
     */
    @Override
    protected void onPostExecute(String result) {
        if(MainActivity.isInForeground()) {
            switch (result) {
                case RICERCA_COMPLETATA:
                    mMainActivity.getMappa().settaMarkers();
                    ParcheggiFragment.newInstance(mMainActivity);
                    break;

                case COMPLETATA_NO_RIS:
                    ParcheggiFragment.clearFragment(mMainActivity);
                    mMainActivity.getMappa().settaMarkers();
                    Toast.makeText(mMainActivity, R.string.no_parcheggi, Toast.LENGTH_SHORT).show();
                    break;

                case NO_INTERNET:
                    ParcheggiFragment.clearFragment(mMainActivity);
                    Toast.makeText(mMainActivity, R.string.no_connection, Toast.LENGTH_SHORT).show();
                    break;
            }
            mMainActivity.hideProgressBar();
        }
    }


    /**
     * Il metodo interroga l'API con dati relativi alle coordinate e al raggio di ricerca, quindi
     * restituisce il risultato in modo sincrono.
     */
    private void popolaElencoParcheggi() {

        ElencoParcheggi.getInstance().getListParcheggi().clear();

        // creazione URL
        String url = BASE_URL +
                "?latitude="    + mCoordRicerca.latitude +
                "&longitude="   + mCoordRicerca.longitude +
                "&radius=" + mRange;

        // interrogazione dell'Api
        JSONObject response = HelperRete.volleySyncRequest(mMainActivity, url);

        System.out.println(response);

        if (response == null)
            return;

        // conversione dell'oggetto JSON in oggetto Java
        try {
            String status = response.getString("status");

            if(status.equals("Success")) {
                JSONArray jArrayParcheggi = response.getJSONObject("message").getJSONArray("parking");

                ElencoParcheggi.getInstance().getListParcheggi().clear();

                for(int i = 0; i < jArrayParcheggi.length(); i++) {
                    Parcheggio newPark = new Parcheggio(jArrayParcheggi.getJSONObject(i));
                    ElencoParcheggi.getInstance().getListParcheggi().add(newPark);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
