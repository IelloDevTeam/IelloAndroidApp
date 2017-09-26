package com.projectiello.teampiattaforme.iello.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by riccardomaldini on 25/09/17.
 * Classe per la gestione semplificata delle sharedPreferences.
 */

public class HelperPreferences {

    // opzione primoAvvio
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    // opzione range di ricerca
    private static final String RANGE = "Range";


    public static void setFirstTimeLaunch(Context c, boolean isFirstTime) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public static boolean isFirstTimeLaunch(Context c) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


    public static void setRange(Context c, int rangeInMetri){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(RANGE, rangeInMetri);
        editor.apply();
    }


    public static int getRange(Context c) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        return pref.getInt(RANGE, 1000);
    }
}
