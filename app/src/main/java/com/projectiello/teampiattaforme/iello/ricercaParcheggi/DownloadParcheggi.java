package com.projectiello.teampiattaforme.iello.ricercaParcheggi;


import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.projectiello.teampiattaforme.iello.R;
import com.projectiello.teampiattaforme.iello.UI.MainActivity;
import com.projectiello.teampiattaforme.iello.UI.ParcheggiFragment;
import com.projectiello.teampiattaforme.iello.dataLogic.ElencoParcheggi;
import com.projectiello.teampiattaforme.iello.dataLogic.Parcheggio;
import com.projectiello.teampiattaforme.iello.utilities.MappaPrincipale;
import com.projectiello.teampiattaforme.iello.utilities.HelperPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by riccardomaldini on 25/09/17.
 * AsyncTask che gestisce il download dei dati da IelloApi in modo asincrono, popolando il Singleton
 * ElencoParcheggi.
 */

public class DownloadParcheggi extends AsyncTask<Void, Void, String> {

    // riferimento alla MainActivity che consente di intervenire sull'interfaccia
    private MainActivity mActivity;

    // costanti di return
    private static final String RICERCA_COMPLETATA = "RICERCA_COMPLETATA";
    private static final String COMPLETATA_NO_RIS = "COMPLETATA_NO_RIS";
    private static final String NO_INTERNET = "NO_INTERNET";

    // dati sulla posizione intorno cui cercare
    private LatLng mCoordRicerca;
    private int mRange;


    /**
     * Costruttore con popolamento degli attributi
     */
    public DownloadParcheggi(MainActivity activity, LatLng coordinateRicerca) {
        mActivity = activity;
        mCoordRicerca = coordinateRicerca;
        mRange = HelperPreferences.getRange(activity);
    }


    /**
     * Riposiziona la mappa prima di eseguire la ricerca
     */
    @Override
    protected void onPreExecute() {
        if(HelperRete.isNetworkAvailable(mActivity))
            MappaPrincipale.getInstance().muoviCamera(mCoordRicerca);
    }


    /**
     * Se possibile, si cercano i parcheggi nelle vicinanze tramite IelloApi.
     */
    @Override
    protected String doInBackground(Void... params) {

        if(HelperRete.isNetworkAvailable(mActivity))  {
            popolaElencoParcheggi();

            if(ElencoParcheggi.getInstance().getListParcheggi().size() == 0)
                return COMPLETATA_NO_RIS;

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
        switch(result) {
            case RICERCA_COMPLETATA:
                ParcheggiFragment.newInstance(mActivity);
                MappaPrincipale.getInstance().settaMarkers();
                break;

            case COMPLETATA_NO_RIS:
                ParcheggiFragment.clearFragment(mActivity);
                MappaPrincipale.getInstance().settaMarkers();
                Toast.makeText(mActivity, R.string.no_parcheggi, Toast.LENGTH_SHORT).show();
                break;

            case NO_INTERNET:
                Toast.makeText(mActivity, R.string.no_connection, Toast.LENGTH_SHORT).show();
                break;
        }
        mActivity.hideProgressBar();
    }


    /**
     * Il metodo interroga l'API con dati relativi alle coordinate e al raggio di ricerca, quindi
     * restituisce il risultato in modo sincrono.
     */
    private void popolaElencoParcheggi() {

        ElencoParcheggi.getInstance().getListParcheggi().clear();

        // creazione URL
        String url = "http://cloudpi.webhop.me:4000/parking" +
                     "?lat="    + mCoordRicerca.latitude +
                     "&lon= "   + mCoordRicerca.longitude /*+
                     "&radius=" + mRange*/; // todo per i test non utilizziamo il range

        // interrogazione dell'Api
        JSONObject response = HelperRete.volleySyncRequest(mActivity, url);

        if (response == null)
            return;

        // conversione dell'oggetto JSON in oggetto Java
        try {
            String status = response.getString("status");

            if(status.equals("OK")) {
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
