package com.projectiello.teampiattaforme.iello.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.projectiello.teampiattaforme.iello.UI.mainActivity.MainActivity;
import com.projectiello.teampiattaforme.iello.utilities.HelperPreferences;

/**
 * Created by Riccardo Maldini on 25/09/2017.
 * Questa classe implementa una splash screen nel modo corretto, senza mostrare una view.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // procedi in modo diverso a seconda della tipologia dell'accesso
        boolean isFirstLaunch = HelperPreferences.isFirstTimeLaunch(this);

        if (isFirstLaunch) {
            // primo accesso assoluto: vai a introScreen
            HelperPreferences.setFirstTimeLaunchDone(this);

            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
        } else {
            // altrimenti vai a MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }
}

