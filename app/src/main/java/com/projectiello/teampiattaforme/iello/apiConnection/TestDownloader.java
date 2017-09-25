package com.projectiello.teampiattaforme.iello.apiConnection;

import android.content.Context;

import com.projectiello.teampiattaforme.iello.dataLogic.ElencoParcheggi;
import com.projectiello.teampiattaforme.iello.dataLogic.Parcheggio;

import java.util.List;
import java.util.Random;

/**
 * Created by riccardomaldini on 25/09/17.
 * Classe di test, da utilizzare in assenza dell'API. Popola la lista di parcheggi con dei parcheggi
 * di urbino.
 */

public class TestDownloader {
    public static void ottieniParcheggi(int rangeInMetri, double lat, double lon, Context c) {
        for (int i = 0; i < 10; i++) {
            Parcheggio p = new Parcheggio();
            p.setIndirizzo("Via del tt eccezionale, 46");
            p.setDistanza(120);


            Random r = new Random();
            int i1 = r.nextInt(50);
            int i2 = r.nextInt(50);

            boolean latsign = r.nextBoolean();
            boolean lonsign = r.nextBoolean();

            double d1 = ((double) i1) * 0.0001;
            double d2 = ((double) i2) * 0.0001;

            if(latsign)
                p.setLat(lat - d1);
            else
                p.setLat(lat + d1);


            if(lonsign)
                p.setLong(lon - d2);
            else
                p.setLong(lon + d2);


            ElencoParcheggi.getInstance().getListParcheggi().add(p);
        }
    }
}
