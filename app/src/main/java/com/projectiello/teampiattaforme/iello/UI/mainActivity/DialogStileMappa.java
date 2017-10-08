package com.projectiello.teampiattaforme.iello.UI.mainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.projectiello.teampiattaforme.iello.R;
import com.projectiello.teampiattaforme.iello.utilities.HelperPreferences;

/**
 * Created by riccardomaldini on 28/09/17.
 * Dialog che permette di impostare lo stile della mappa.
 */

public class DialogStileMappa extends DialogFragment {

    private RadioGroup mGroup;

    /**
     * metodo statico che permette di inizializzare e avviare il dialog facilmente
     */
    public static void newInstance(Activity a) {
        DialogStileMappa f = new DialogStileMappa();
        f.show(a.getFragmentManager(), "stileMappa");
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // il builder ci servirà a costruire la finestra di dialogo in modo standard
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater  = getActivity().getLayoutInflater();
        final ViewGroup nullParent = null;
        View dialogView = inflater.inflate(R.layout.dialog_stile_mappa, nullParent);

        // inizializza l'interfaccia del dialog
        builder.setIcon(R.drawable.ic_layers_black_24px);
        builder.setTitle(getString(R.string.personalizza_mappa));
        builder.setMessage("Seleziona i colori della mappa nella schermata principale.");

        mGroup = dialogView.findViewById(R.id.rgpStili);

        int stileMappa = HelperPreferences.getStileMappa(getActivity());

        switch (stileMappa) {
            case R.raw.style_standard:
                mGroup.check(R.id.rdb1);
                break;
            case R.raw.style_silver:
                mGroup.check(R.id.rdb2);
                break;
            case R.raw.style_night:
                mGroup.check(R.id.rdb3);
                break;
            case R.raw.style_dark:
                mGroup.check(R.id.rdb4);
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
                        case R.id.rdb1:
                            HelperPreferences.setStileMappa(getActivity(), R.raw.style_standard);
                            break;
                        case R.id.rdb2:
                            HelperPreferences.setStileMappa(getActivity(), R.raw.style_silver);
                            break;
                        case R.id.rdb3:
                            HelperPreferences.setStileMappa(getActivity(), R.raw.style_night);
                            break;
                        case R.id.rdb4:
                            HelperPreferences.setStileMappa(getActivity(), R.raw.style_dark);
                            break;
                    }
                    ((MainActivity) getActivity()).getMappa().aggiornaStile();
                    dismiss();

                } // end if

            });
        }
    }
}
