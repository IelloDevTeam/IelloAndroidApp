package com.projectiello.teampiattaforme.iello.dataLogic;

import com.google.firebase.database.PropertyName;

/**
 * Created by andrea on 30/09/17.
 */

public class Segnalazione {

    @PropertyName("latitude")
    private double mLatitude;

    @PropertyName("longitude")
    private double mLongitude;

    // uuid dell'utente che ha fatto la segnalazione
    @PropertyName("userUUID")
    private String mUserUUID;

    public Segnalazione(double mLatitude, double mLongitude, String mUserUUID) {
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mUserUUID = mUserUUID;
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

    public String getUserUUID() {
        return mUserUUID;
    }

    public void setUserUUID(String mUserUUID) {
        this.mUserUUID = mUserUUID;
    }
}
