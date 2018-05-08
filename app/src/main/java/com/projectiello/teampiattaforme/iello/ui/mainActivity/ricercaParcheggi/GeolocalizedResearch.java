package com.projectiello.teampiattaforme.iello.ui.mainActivity.ricercaParcheggi;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.projectiello.teampiattaforme.iello.R;
import com.projectiello.teampiattaforme.iello.ui.mainActivity.MainActivity;
import com.projectiello.teampiattaforme.iello.dataLogic.ElencoParcheggi;

import static android.content.ContentValues.TAG;

/**
 * Created by riccardomaldini on 25/09/17.
 * Classe utilizzabile all'interno di un'activity. Fornisce tutti i metodi necessari per gestire
 * la geolocalizzazione dell'utente: dall'ottenimento dei permessi alla ricerca vera e propria.
 * Una volta ottenuti i permessi, GeolocalizedResearch avvia la ricerca dei parcheggi situati attorno
 * alla posizione trovata.
 */

public class GeolocalizedResearch {

    // attributo per sfruttare il servizio di geolocalizzazione Google
    private FusedLocationProviderClient mFusedLocationClient;

    // activity utilizzata sia come contesto che per l'avvio del download parcheggi
    private MainActivity mMainActivity;


    /**
     * Costruttore del geolocalizer; inizializza l'attributo activity e il provider per
     * l'individuazione della posizione
     */
    public GeolocalizedResearch(MainActivity a) {
        mMainActivity = a;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mMainActivity);
    }


    /**
     * Avvia la ricerca della posizione dell'utente tramite il provider. Prima della ricerca,
     * verifica se sono disponibili i permessi; quindi al termine della ricerca avvia il download
     * dei parcheggi nelle vicinanze.
     */
    public void avviaRicercaPosizione() {

        // verifica permessi di geolocalizzazione
        if (ActivityCompat.checkSelfPermission(mMainActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mMainActivity,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            || Build.VERSION.SDK_INT < 23) {

            Log.v(TAG,"Permission is granted");

            // se si dispone del permesso, inizia la ricerca
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(mMainActivity, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {

                                mMainActivity.showProgressBar();
                                // Logic to handle location object
                                LatLng coordRicerca
                                        = new LatLng(location.getLatitude(), location.getLongitude());

                                ElencoParcheggi.getInstance().setCoordAttuali(coordRicerca);
                                mMainActivity.setTitle(mMainActivity.getString(R.string.tua_posizione));

                                AsyncDownloadParcheggi asyncDownload
                                        = new AsyncDownloadParcheggi(mMainActivity, coordRicerca);
                                asyncDownload.execute();

                            } else {
                                Toast.makeText(mMainActivity, R.string.attiva_gps,
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        } else {

            // se non si dispone del permesso, richiedi il permesso all'utente
            Toast.makeText(mMainActivity, R.string.autorizza_permesso, Toast.LENGTH_LONG).show();
            Log.v(TAG,"Permission is revoked");
            ActivityCompat.requestPermissions(mMainActivity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

            // una volta ottenuto il permesso viene riavviata la ricerca posizione (onRequestPermissionResult)
        }
    }
}
