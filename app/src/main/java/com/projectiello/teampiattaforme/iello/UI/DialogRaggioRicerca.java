package com.projectiello.teampiattaforme.iello.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.projectiello.teampiattaforme.iello.R;
import com.projectiello.teampiattaforme.iello.utilities.HelperPreferences;

/**
 * Created by riccardomaldini on 28/09/17.
 * Dialog che permette di impostare il raggio della ricerca.
 */

public class DialogRaggioRicerca extends DialogFragment {

    private RadioGroup mGroup;

    /**
     * metodo statico che permette di inizializzare e avviare il dialog facilmente
     */
    public static void newInstance(Activity a) {
        DialogRaggioRicerca f = new DialogRaggioRicerca();
        f.show(a.getFragmentManager(), "addPartita");
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // il builder ci servirà a costruire la finestra di dialogo in modo standard
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater  = getActivity().getLayoutInflater();
        final ViewGroup nullParent = null;
        View dialogView = inflater.inflate(R.layout.dialog_raggio_ricerca, nullParent);

        // inizializza l'interfaccia del dialog
        builder.setIcon(R.drawable.ic_straighten_black_24px);
        builder.setTitle(getString(R.string.raggio_ricerca));

        builder.setMessage("Seleziona il raggio entro il quale visualizzare i parcheggi " +
                "nelle vicinanze.");

        mGroup = dialogView.findViewById(R.id.rgpRaggi);

        int raggioDiPartenza = HelperPreferences.getRange(getActivity());

        switch (raggioDiPartenza) {
            case 100:
                mGroup.check(R.id.rdb100m);
                break;
            case 200:
                mGroup.check(R.id.rdb200m);
                break;
            case 500:
                mGroup.check(R.id.rdb500m);
                break;
            case 1000:
                mGroup.check(R.id.rdb1km);
                break;
            case 10000:
                mGroup.check(R.id.rdb10km);
                break;
        }


        // button positivo: gestito onStart()
        builder.setPositiveButton("Imposta", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                /* Do nothing here because we override this button later to change the close behaviour.
                 * However, we still need this because on older versions of Android unless we
                 * pass a handler the button doesn't get instantiated
                 */
            }
        });

        // Button cancella: esci
        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });

        builder.setView(dialogView);
        return builder.create();

    } // end onCreateDialog


    /** override per gestire il Button aggiungi senza far chiudere il dialog */
    @Override
    public void onStart()
    {
        super.onStart();
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null) {
            Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // salva il nuovo risultato
                    switch(mGroup.getCheckedRadioButtonId()) {
                        case R.id.rdb100m:
                            HelperPreferences.setRange(getActivity(), 100);
                            break;
                        case R.id.rdb200m:
                            HelperPreferences.setRange(getActivity(), 200);
                            break;
                        case R.id.rdb500m:
                            HelperPreferences.setRange(getActivity(), 500);
                            break;
                        case R.id.rdb1km:
                            HelperPreferences.setRange(getActivity(), 1000);
                            break;
                        case R.id.rdb10km:
                            HelperPreferences.setRange(getActivity(), 10000);
                            break;
                    }

                    dismiss();

                } // end if

            });
        }
    }
}
