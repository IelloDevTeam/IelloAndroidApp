package com.projectiello.teampiattaforme.iello.dataLogic;

/**
 * Created by riccardomaldini on 25/09/17.
 * Singleton che memorizza a tempo di esecuzione i parcheggi scaricati dall'API
 */

public class ElencoParcheggi {
    private static final ElencoParcheggi ourInstance = new ElencoParcheggi();

    public static ElencoParcheggi getInstance() {
        return ourInstance;
    }

    private ElencoParcheggi() {
    }
}
