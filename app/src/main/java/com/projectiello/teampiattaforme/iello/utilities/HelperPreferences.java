package com.projectiello.teampiattaforme.iello.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.projectiello.teampiattaforme.iello.R;

/**
 * Created by riccardomaldini on 25/09/17.
 * Classe per la gestione semplificata delle sharedPreferences.
 */

public class HelperPreferences {

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String RANGE = "Range";
    private static final String STILE_MAPPA = "StileMappa";


    public static void setFirstTimeLaunchDone(Context c) {
        SharedPreferences pref = getSharedPrefs(c);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, false);
        editor.apply();
    }


    public static boolean isFirstTimeLaunch(Context c) {
        SharedPreferences pref = getSharedPrefs(c);
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


    public static void setRange(Context c, int rangeInMetri){
        SharedPreferences pref = getSharedPrefs(c);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(RANGE, rangeInMetri);
        editor.apply();
    }


    public static int getRange(Context c) {
        SharedPreferences pref = getSharedPrefs(c);
        return pref.getInt(RANGE, 1000);
    }


    public static void setStileMappa(Context c, int resStile){
        SharedPreferences pref = getSharedPrefs(c);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(STILE_MAPPA, resStile);
        editor.commit();
    }


    public static int getStileMappa(Context c) {
        SharedPreferences pref = getSharedPrefs(c);
        return pref.getInt(STILE_MAPPA, R.raw.style_standard);
    }

    private static SharedPreferences getSharedPrefs(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
