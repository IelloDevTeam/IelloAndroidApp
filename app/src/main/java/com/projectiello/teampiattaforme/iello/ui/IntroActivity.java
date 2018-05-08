package com.projectiello.teampiattaforme.iello.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.projectiello.teampiattaforme.iello.R;
import com.projectiello.teampiattaforme.iello.ui.mainActivity.MainActivity;

/**
 * Created by riccardomaldini on 25/09/17.
 * Activity mostrata al primo avvio dall'app.
 */

public class IntroActivity extends AppIntro2 {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();


        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance(
                getString(R.string.slide_1_title),
                getString(R.string.slide_1_desc),
                R.mipmap.ic_launcher,
                ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null)));


        addSlide(AppIntroFragment.newInstance(
                getString(R.string.slide_2_title),
                getString(R.string.slide_2_desc),
                R.drawable.ic_my_location_white_192dp,
                ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null)));


        addSlide(AppIntroFragment.newInstance(
                getString(R.string.slide_3_title),
                getString(R.string.slide_3_desc),
                R.drawable.ic_search_white_192dp,
                ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null)));


        addSlide(AppIntroFragment.newInstance(
                getString(R.string.slide_4_title),
                getString(R.string.slide_4_desc),
                R.drawable.logo_uniurb_192px,
                ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null)));


        // OPTIONAL METHODS
        setFlowAnimation();
        // Hide Skip/Done button.
        showSkipButton(false);
        setProgressButtonEnabled(true);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        intent.putExtra("justOpen", true);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.

        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        intent.putExtra("justOpen", true);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}