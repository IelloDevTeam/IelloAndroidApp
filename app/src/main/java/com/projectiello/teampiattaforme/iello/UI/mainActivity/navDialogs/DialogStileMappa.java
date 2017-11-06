package com.projectiello.teampiattaforme.iello.UI.mainActivity.navDialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.projectiello.teampiattaforme.iello.R;
import com.projectiello.teampiattaforme.iello.UI.mainActivity.MainActivity;
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
        builder.setMessage(R.string.desc_personalizza_mappa);

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

        // button positivo: salva il nuovo risultato
        builder.setPositiveButton(R.string.imposta, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
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
            }
        });

        // Button cancella: esci
        builder.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });

        builder.setView(dialogView);
        return builder.create();
    }
}
