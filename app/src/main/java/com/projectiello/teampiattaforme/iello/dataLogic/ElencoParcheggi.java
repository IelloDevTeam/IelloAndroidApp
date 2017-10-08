package com.projectiello.teampiattaforme.iello.dataLogic;

import com.google.android.gms.maps.model.LatLng;
import com.projectiello.teampiattaforme.iello.utilities.MappaGoogle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by riccardomaldini on 25/09/17.
 * Singleton che memorizza a tempo di esecuzione i parcheggi scaricati da IelloApi.
 */

public class ElencoParcheggi {

    private static final ElencoParcheggi ourInstance = new ElencoParcheggi();

    public static ElencoParcheggi getInstance() {
        return ourInstance;
    }

    private ElencoParcheggi() {}

    // lista di memorizzazione dei parcheggi
    private List<Parcheggio> mListParcheggi = new ArrayList<>();

    // memorizza la posizione dalla quale è stata effettuata la ricerca
    private LatLng mCoordAttuali = MappaGoogle.COORD_INIZIALI;


    /**
     * Restituisce la lista dei parcheggi memorizzata nel singleton.
     */
    public List<Parcheggio> getListParcheggi() {
        return mListParcheggi;
    }


    /**
     *  Restituisce la lista utilizzata di parcheggi priva del parcheggio più vicino. Utilizzata nel
     *  recyclerView del fragmentParcheggi.
     */
    public List<Parcheggio> getListParcheggiRecycler() {
        List<Parcheggio> listRecycler = new ArrayList<>();

        for (Parcheggio p : mListParcheggi)
            listRecycler.add(p);

        listRecycler.remove(0);

        return listRecycler;
    }


    /**
     * Restituisce il parcheggio più vicino all'utente. Utilizzato nel fragmentParcheggi.
     */
    public Parcheggio getParkPiuVicino() {
        return mListParcheggi.get(0);
    }


    public LatLng getCoordAttuali() {
        return mCoordAttuali;
    }

    public void setCoordAttuali(LatLng mCoordAttuali) {
        this.mCoordAttuali = mCoordAttuali;
    }


    /**
     * Calcola la distanza in metri di un parcheggio dall'origine
     */
    int calcolaDistanzaDaOrigine(LatLng coordParcheggio) {

        double lat1 = mCoordAttuali.latitude;
        double lon1 = mCoordAttuali.longitude;

        double lat2 = coordParcheggio.latitude;
        double lon2 = coordParcheggio.longitude;

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        double distInM = dist * 1000;
        return (int) distInM;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    /**
     * Ordina i parcheggi in ordine crescente a seconda della distanza dall'origine
     */
    public void ordinaParcheggiPerDistanza() {
        Collections.sort(mListParcheggi, new Comparator<Parcheggio>() {
            @Override
            public int compare(Parcheggio o1, Parcheggio o2) {
                if (o1.getDistanza() > o2.getDistanza()) {
                    return 1;
                }
                else if (o1.getDistanza() < o2.getDistanza()) {
                    return -1;
                }
                else {
                    return 0;
                }
            }
        });
    }
}
