package com.projectiello.teampiattaforme.iello.dataLogic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by riccardomaldini on 25/09/17.
 * Singleton che memorizza a tempo di esecuzione i parcheggi scaricati dall'API
 */

public class ElencoParcheggi {
    private static final ElencoParcheggi ourInstance = new ElencoParcheggi();

    public static ElencoParcheggi getInstance() {
        return ourInstance;
    }

    private ElencoParcheggi() {}

    private List<Parcheggio> mListParcheggi = new ArrayList<>();

    // restituisce la lista utilizzata nel recyclerView, ovvero senza il primo elemento
    public List<Parcheggio> getListParcheggiRecycler() {
        List<Parcheggio> listRecycler = new ArrayList<>();

        for (Parcheggio p : mListParcheggi) {
            listRecycler.add(p);
        }

        listRecycler.remove(0);

        return listRecycler;
    }


    public List<Parcheggio> getListParcheggi() {
        return mListParcheggi;
    }


    // restituisce la dimensione della lista nel RecyclerView
    public Parcheggio getParkPiuVicino() {
        return mListParcheggi.get(0);
    }
}
