package com.projectiello.teampiattaforme.iello.dataLogic;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by riccardomaldini on 25/09/17.
 * Classe che rappresenta un singolo parcheggio.
 */

public class Parcheggio {
    private  String mIndirizzo;
    private int mDistanza;
    private double mLat, mLong;

    public Parcheggio() {

    }

    public void setIndirizzo(String s) {
        mIndirizzo = s;
    }


    public void setDistanza(int mDistanza) {
        this.mDistanza = mDistanza;
    }

    public void setLat(double mLat) {
        this.mLat = mLat;
    }

    public void setLong(double mLong) {
        this.mLong = mLong;
    }

    public String getDistanzaUI() {
        return mDistanza + "m";
    }

    public String getIndirizzoUI() {
        return mIndirizzo;
    }


    public double getLat() {
        return mLat;
    }

    public double getLong() {
        return mLong;
    }

}
