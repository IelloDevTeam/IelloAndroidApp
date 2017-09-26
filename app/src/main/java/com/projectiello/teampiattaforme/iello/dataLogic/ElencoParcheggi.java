package com.projectiello.teampiattaforme.iello.dataLogic;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
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
    private LatLng mCoordAttuali;


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
}
