package com.projectiello.teampiattaforme.iello.UI.segnalazioneActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.projectiello.teampiattaforme.iello.R;
import com.projectiello.teampiattaforme.iello.utilities.HelperRete;

import org.json.JSONObject;

/**
 * Created by riccardomaldini on 29/09/17.
 * Activity dalla quale si può inviare una segnalazione di un posteggio individuato da un utente.
 */

public class SegnalazioneActivity extends AppCompatActivity {

    // riferimento a vari elementi dell'interfaccia
    private FrameLayout mProgressBar;
    private EditText mEditIndirizzo;
    private FloatingActionButton mFabInvia;

    // riferimento alla classe per la gestione della mappa
    private MappaSegnalazione mMappa;

    // riferimento alla classe per la gestione delle segnalazioni
    private HelperSegnalazione mHelperSegnalazione;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inizializza vari elementi base dell'interfaccia utente
        setContentView(R.layout.activity_segnalazione);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.segnala_posteggio);
        }

        // inizializza il collegamento a Firebase
        mHelperSegnalazione = new HelperSegnalazione(this);

        // inizializza la mappa
        mMappa = new MappaSegnalazione(this);

        // la progressBar. Viene resa visibile durante il collegamento al database remoto e durante
        // la ricerca dell'indirizzo sulla mappa
        mProgressBar = findViewById(R.id.clippedProgressBar);
        hideProgressBar();

        // l'EditText consente all'utente di digitare un indirizzo sulla barra di ricerca; ...
        mEditIndirizzo = findViewById(R.id.editIndirizzo);
        mEditIndirizzo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(HelperRete.isNetworkAvailable(SegnalazioneActivity.this)) {
                        String query = v.getText().toString();
                        AsyncRicercaIndirizzo rri
                                = new AsyncRicercaIndirizzo(query, SegnalazioneActivity.this);
                        rri.execute();
                    } else {
                        Toast.makeText(SegnalazioneActivity.this, R.string.no_connection,
                                Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

        // ... al click su fabSearch vengono cercate le coordinate dell'indirizzo e riposizionata
        // la mappa di conseguenza
        FloatingActionButton fabSearch = findViewById(R.id.fabSearch);
        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(HelperRete.isNetworkAvailable(SegnalazioneActivity.this)) {
                    String query = mEditIndirizzo.getText().toString();
                    AsyncRicercaIndirizzo rri
                            = new AsyncRicercaIndirizzo(query, SegnalazioneActivity.this);
                    rri.execute();
                } else {
                    Toast.makeText(SegnalazioneActivity.this, R.string.no_connection,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // il fabInvia è un tanto che consente di inviare la posizione selezionata dall'utente al
        // database remoto. L'invio viene gestito dall'apposita classe
        mFabInvia = findViewById(R.id.fabInvia);
        mFabInvia.hide();
        mFabInvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mHelperSegnalazione.sendLocation(mMappa.getMarkerPosition(), new HelperSegnalazione.APICallback() {
                @Override
                public void OnResult(boolean isError, JSONObject jsonObject) {
                    if(!isError)
                        Toast.makeText(SegnalazioneActivity.this, R.string.segnalazione_inviata, Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(SegnalazioneActivity.this, R.string.errore_invio, Toast.LENGTH_SHORT).show();

                    getMappa().resettaInterfacciaMappa();
                }

                @Override
                public void OnAuthError() {
                    Toast.makeText(SegnalazioneActivity.this, R.string.auth_error, Toast.LENGTH_SHORT).show();
                }
            });
            }
        });
    }


    /*
     * Metodi per mostrare - nascondere la progressBar dall'esterno dall'activity
     */
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }


    /*
     * Metodi per mostrare - nascondere il fabInvia dall'esterno dell'activity
     */
    public void hideFabInvia() {
        mFabInvia.hide();
    }

    public void showFabInvia() {
        mFabInvia.show();
    }


    /**
     * metodo per accedere alla mappa dell'activity dall'esterno
     */
    public MappaSegnalazione getMappa() {
        return mMappa;
    }
}
