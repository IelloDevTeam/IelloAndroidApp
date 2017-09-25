package com.projectiello.teampiattaforme.iello.apiConnection;

import com.projectiello.teampiattaforme.iello.dataLogic.ElencoParcheggi;
import com.projectiello.teampiattaforme.iello.dataLogic.Parcheggio;

import java.util.List;

/**
 * Created by riccardomaldini on 25/09/17.
 * Classe di test, da utilizzare in assenza dell'API. Popola la lista di parcheggi con dei parcheggi
 * di urbino.
 */

public class TestDownloader {
    public static void ottieniParcheggi(int rangeInMetri, double lat, double lon) {
        for (int i = 0; i < 10; i++) {
            Parcheggio p = new Parcheggio();
            p.setIndirizzo("via Mazzini, 123");
            p.setDistanza(120);

            double id = ((double) i) * 0.001;

            p.setLat(lat + id);
            p.setLong(lon + id);

            ElencoParcheggi.getInstance().getListParcheggi().add(p);
        }
    }
}
