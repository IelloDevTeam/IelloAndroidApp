package com.projectiello.teampiattaforme.iello.dataLogic;

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

    public void setIndirizzo(String mIndirizzo) {
        this.mIndirizzo = mIndirizzo;
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
