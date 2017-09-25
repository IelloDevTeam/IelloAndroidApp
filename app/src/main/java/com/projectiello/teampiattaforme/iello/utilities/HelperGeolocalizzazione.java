package com.projectiello.teampiattaforme.iello.utilities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.projectiello.teampiattaforme.iello.Manifest;
import com.projectiello.teampiattaforme.iello.UI.MainActivity;
import com.projectiello.teampiattaforme.iello.apiConnection.AsyncDownloadParcheggi;

import static android.content.ContentValues.TAG;

/**
 * Created by riccardomaldini on 25/09/17.
 * Fornisce tutti i metodi necessari per gestire la geolocalizzazione e restituire la posizione
 * dell'utente.
 */

public class HelperGeolocalizzazione {

    // attributo per l'individuazione della posizione
    private FusedLocationProviderClient mFusedLocationClient;
    private MainActivity mMainActivity;

    public HelperGeolocalizzazione(MainActivity a) {
        mMainActivity = a;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mMainActivity);
    }


    public void avviaRicercaPosizione() {
        if (ActivityCompat.checkSelfPermission(mMainActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mMainActivity,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG,"Permission is granted");

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(mMainActivity, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                AsyncDownloadParcheggi asyncDownload
                                        = new AsyncDownloadParcheggi(mMainActivity, location.getLatitude(),
                                        location.getLongitude());
                                asyncDownload.execute();
                            }
                        }
                    });


        } else {
            Toast.makeText(mMainActivity, "Autorizza il permesso per poter indivudare la tua posizione", Toast.LENGTH_LONG).show();
            Log.v(TAG,"Permission is revoked");
            ActivityCompat.requestPermissions(mMainActivity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

    }
}
