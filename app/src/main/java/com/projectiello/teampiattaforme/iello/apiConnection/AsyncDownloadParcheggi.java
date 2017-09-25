package com.projectiello.teampiattaforme.iello.apiConnection;

import android.os.AsyncTask;
import android.widget.Toast;

import com.projectiello.teampiattaforme.iello.UI.MainActivity;
import com.projectiello.teampiattaforme.iello.dataLogic.ElencoParcheggi;
import com.projectiello.teampiattaforme.iello.utilities.HelperPreferences;
import com.projectiello.teampiattaforme.iello.utilities.HelperRete;

/**
 * Created by riccardomaldini on 25/09/17.
 * AsyncTask che gestisce il download dei dati dall'API in modo asincrono, popolando il Singleton
 * ElencoParcheggi.
 */

public class AsyncDownloadParcheggi extends AsyncTask<Void, Void, String> {

    private MainActivity mActivity;

    // costanti di return:
    private static final String RICERCA_COMPLETATA = "RICERCA_COMPLETATA"; // pronostici e lega
    private static final String COMPLETATA_NO_RIS = "COMPLETATA_NO_RIS"; // solo lega
    private static final String NO_INTERNET = "NO_INTERNET";             // probabile errore


    // dati sulla posizione intorno cui cercare
    private double mLatitudine, mLongitudine;


    public AsyncDownloadParcheggi(MainActivity activity, double lat, double lon) {
        mActivity = activity;
        mLatitudine = lat;
        mLongitudine = lon;
    }


    /**
     * Riposiziona la camera.
     */
    @Override
    protected void onPreExecute() {
        if(HelperRete.isNetworkAvailable(mActivity))
            mActivity.posizionaCamera(mLatitudine, mLongitudine);
    }

    /**
     * se possibile, si cercano i parcheggi nelle vicinanze tramite l'API.
     */
    @Override
    protected String doInBackground(Void... params) {

        // download partite; qua viene utilizzato il test
        if(HelperRete.isNetworkAvailable(mActivity))  {
            HelperPreferences hp = new HelperPreferences(mActivity);
            int range = hp.getRange();

            ElencoParcheggi.getInstance().getListParcheggi().clear();
            TestDownloader.ottieniParcheggi(range, mLatitudine, mLongitudine, mActivity);

            if(ElencoParcheggi.getInstance().getListParcheggi().size() == 0)
                return COMPLETATA_NO_RIS;

            return RICERCA_COMPLETATA;


        } else {
            return  NO_INTERNET;
        }
    }


    /**
     * crea, popola e mostra il fragment parcheggi.
     */
    @Override
    protected void onPostExecute(String result) {
        switch(result) {
            case RICERCA_COMPLETATA:
                mActivity.popolaParcheggi();
                break;
            case COMPLETATA_NO_RIS:
                Toast.makeText(mActivity, "Non ci sono parcheggi nelle vicinanze.", Toast.LENGTH_SHORT).show();
                break;
            case NO_INTERNET:
                Toast.makeText(mActivity, "Connessione internet non disponibile.", Toast.LENGTH_SHORT).show();
        }
    }
}
