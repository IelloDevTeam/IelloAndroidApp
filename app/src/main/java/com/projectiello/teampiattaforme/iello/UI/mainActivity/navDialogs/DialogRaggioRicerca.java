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
        f.show(a.getFragmentManager(), "raggioRicerca");
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
        builder.setMessage(R.string.desc_raggio_ricerca);

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


        // button positivo: salva il nuovo risultato
        builder.setPositiveButton(R.string.imposta, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
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
