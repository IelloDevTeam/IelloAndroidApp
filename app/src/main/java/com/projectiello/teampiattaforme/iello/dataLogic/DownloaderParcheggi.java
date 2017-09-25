package com.projectiello.teampiattaforme.iello.dataLogic;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by riccardomaldini on 25/09/17.
 * AsyncTask che gestisce il download dei dati dall'API in modo asincrono, popolando il Singleton
 * ElencoParcheggi.
 */

public class DownloaderParcheggi {

    // utile per determinare se internet è attivo
    private static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
