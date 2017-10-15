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


    public Parcheggio findParcheggioByCoordinate(LatLng coordinate) {
        Parcheggio risultato = null;

        for(Parcheggio p: mListParcheggi) {
            if(p.getCoordinate().latitude == coordinate.latitude
                    && p.getCoordinate().longitude == coordinate.longitude)
                risultato = p;
        }

        return risultato;
    }

    public LatLng getCoordAttuali() {
        return mCoordAttuali;
    }

    public void setCoordAttuali(LatLng mCoordAttuali) {
        this.mCoordAttuali = mCoordAttuali;
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
