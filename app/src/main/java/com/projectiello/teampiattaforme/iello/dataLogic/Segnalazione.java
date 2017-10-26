package com.projectiello.teampiattaforme.iello.dataLogic;

/**
 * Created by andrea on 30/09/17.
 * Classe che rappresenta una segnalazione utente.
 */

public class Segnalazione {

    private double mLatitude;
    private double mLongitude;

    public Segnalazione(double mLatitude, double mLongitude) {
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }
}

