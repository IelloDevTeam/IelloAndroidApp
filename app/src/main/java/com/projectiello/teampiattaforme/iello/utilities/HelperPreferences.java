package com.projectiello.teampiattaforme.iello.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by riccardomaldini on 25/09/17.
 * Classe per la gestione facilitata delle sharedPreferences.
 */

public class HelperPreferences {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    // opzione primoAvvio
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    // opzione range di ricerca
    private static final String RANGE = "Range";


    // costruttore
    public HelperPreferences(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();
    }


    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


    public void setRange(int rangeInMetri){
        editor.putInt(RANGE, rangeInMetri);
        editor.commit();
    }


    public int getRange() {
        return pref.getInt(RANGE, 100);
    }
}
