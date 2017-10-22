package com.projectiello.teampiattaforme.iello.UI.mainActivity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.maps.model.LatLng;
import com.projectiello.teampiattaforme.iello.R;
import com.projectiello.teampiattaforme.iello.dataLogic.ElencoParcheggi;
import com.projectiello.teampiattaforme.iello.dataLogic.Parcheggio;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by riccardomaldini on 25/09/17.
 * Fragment che mostra la lista di parcheggi cercati, una volta disponibile.
 */

public class ParcheggiFragment extends Fragment {

    // componenti del fragment
    private Button mBtnEspandi;
    private ExpandableRelativeLayout mExpLayParcheggi, mExpLaySelezioato;

    // componenti del parcheggio selezionato
    private TextView mTxtIndirizzoPS;
    private TextView mTxtDistanzaPS;
    private RelativeLayout mLayoutPS;
    private Button mBtnNavigaPS;

    // riferimento al parcheggio selezionato dall'utente
    private Parcheggio mParkSelezionato;


    /**
     * Metodo statico per instanziare più facilmente il fragment quando necessario
     */
    public static void newInstance(MainActivity activity) {
        // inizializza la FragmentTransaction
        ParcheggiFragment newFragment = new ParcheggiFragment();
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_parcheggi_container, newFragment, "indirizzoParcheggio");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

        // imposta un riferimento al fragment in MainActivity
        activity.setFragmentAttivo(newFragment);
    }


    /**
     * Metodo statico per eliminare il fragment, quando non sono disponibili parcheggi
     */
    public static void clearFragment(MainActivity activity) {
        if(activity.getFragmentAttivo() != null) {
            // inizializza la fragmentTransaction per l'eliminazione
            ParcheggiFragment fragAttivo = activity.getFragmentAttivo();
            FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
            ft.remove(fragAttivo);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();

            // elimina il riferimento al fragment in MainActivity
            activity.setFragmentAttivo(null);
        }
    }


    /**
     * Alla creazione del fragment viene popolata la lista dei parcheggi e inizializzati i
     * componenti base d'interfaccia.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // layout inflation
        View view = inflater.inflate(R.layout.fragment_parcheggi, container, false);

        // assegna i componenti del fragment
        mTxtIndirizzoPS = view.findViewById(R.id.txtIndirizzo);
        mTxtDistanzaPS = view.findViewById(R.id.txtDistanza);
        mLayoutPS = view.findViewById(R.id.item_parcheggio);
        mBtnNavigaPS = view.findViewById(R.id.btnNaviga);
        mBtnEspandi = view.findViewById(R.id.btnEspandi);
        mExpLayParcheggi = view.findViewById(R.id.expRecParcheggi);
        mExpLaySelezioato = view.findViewById(R.id.expParcheggioSelezionato);

        // inizializza il RecyclerView (la lista di parcheggi)
        final RecyclerView recParcheggi = view.findViewById(R.id.recParcheggi);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        final ParcheggiAdapter pAdapter = new ParcheggiAdapter();
        recParcheggi.setLayoutManager(mLayoutManager);
        recParcheggi.setItemAnimator(new DefaultItemAnimator());
        recParcheggi.setAdapter(pAdapter);

        // chiude le stats estese all'avvio (a causa di un bug dell'expandable API, non può essere
        // fatto da XML)
        mExpLayParcheggi.collapse();
        mExpLaySelezioato.collapse();

        // inizializza il button di espansione
        mBtnEspandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mExpLayParcheggi.isExpanded()) {
                    mBtnEspandi.setText(getString(R.string.tutti_i_risultati));
                    Drawable img = VectorDrawableCompat.create(getActivity().getResources(),
                                   R.drawable.ic_keyboard_arrow_up_black_24px, null);
                    mBtnEspandi.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                } else {
                    mBtnEspandi.setText(R.string.nascondi_risultati);
                    Drawable img = VectorDrawableCompat.create(getActivity().getResources(),
                                   R.drawable.ic_keyboard_arrow_down_black_24px, null);
                    mBtnEspandi.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                }
                mExpLayParcheggi.toggle();
            }
        });

        return view;
    }


    /**
     * Quando l'utente seleziona un parcheggio, viene invocato questo metodo. Questo mostra i
     * dettagli del parcheggio selezionato dall'utente nella casella 'parcheggio selezionato',
     * mostra la casella stessa e nasconde eventualmente la lista di tutti i parcheggi.
     * @param coordinate coordinate del parcheggio selezionato
     */
    public void setParcheggioSelezionato(LatLng coordinate) {

        // trova il parcheggio selezionato dall'utente a partire dalle coordinate
        mParkSelezionato = ElencoParcheggi .getInstance().findParcheggioByCoordinate(coordinate);

        if (mParkSelezionato != null) {
            // inizializza il primo elemento e mostralo
            mTxtIndirizzoPS.setText(mParkSelezionato.getIndirizzoUI());
            mTxtDistanzaPS.setText(mParkSelezionato.getDistanzaUI());
            mLayoutPS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // sposta la mappa sulla posizione
                    ((MainActivity) getActivity()).getMappa()
                            .muoviCamera(mParkSelezionato.getCoordinate());
                }
            });
            mBtnNavigaPS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // naviga alla posizione
                    double lat = mParkSelezionato.getCoordinate().latitude;
                    double lng = mParkSelezionato.getCoordinate().longitude;

                    String uri = "http://maps.google.com/maps?daddr=" + lat + "," + lng;
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                }
            });
            mExpLaySelezioato.expand();

            // nascondi gli altri risultati
            if (mExpLayParcheggi.isExpanded()) {
                mBtnEspandi.setText(getString(R.string.tutti_i_risultati));
                Drawable img = VectorDrawableCompat.create(getActivity().getResources(),
                        R.drawable.ic_keyboard_arrow_up_black_24px, null);
                mBtnEspandi.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);

                mExpLayParcheggi.collapse();
            }
        }
    }


    /**
     * Quando l'utente clicca un una zona della mappa senza marker, viene invocato questo metodo.
     * Questo nasconde i layout mostrando una porzione più grande di mappa.
     */
    public void nascondiParcheggioSelezionato() {
        mParkSelezionato = null;
        mExpLayParcheggi.collapse();
        mExpLaySelezioato.collapse();
    }



    /** inner class dentro la quale viene definito un custom adapter per la recyclerView */
    private class ParcheggiAdapter extends RecyclerView.Adapter<ParcheggiAdapter.MyViewHolder> {

        private List<Parcheggio> mParcheggiRecycler = new ArrayList<>();

        private ParcheggiAdapter() {
            mParcheggiRecycler = ElencoParcheggi.getInstance().getListParcheggi();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            // widget dell'interfaccia di ogni elemento
            TextView txtIndirizzo, txtDistanza;
            Button btnNaviga;
            RelativeLayout layItem;


            MyViewHolder(View view) {
                super(view);
                txtIndirizzo = view.findViewById(R.id.txtIndirizzo);
                txtDistanza = view.findViewById(R.id.txtDistanza);
                btnNaviga = view.findViewById(R.id.btnNaviga);
                layItem = view.findViewById(R.id.item_parcheggio);
            }
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listitem_parcheggio, parent, false);
            return new MyViewHolder(itemView);
        }


        // mette i dati all'interno dello scheletro
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            // variabile di appoggio che contiene i dati del parcheggio corrente
            final Parcheggio parcheggio = mParcheggiRecycler.get(position);

            // pone i dati all'interno dei widget
            holder.txtIndirizzo.setText(parcheggio.getIndirizzoUI());
            holder.txtDistanza.setText(parcheggio.getDistanzaUI());
            holder.btnNaviga.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // naviga alla posizione
                    double lat = parcheggio.getCoordinate().latitude;
                    double lng = parcheggio.getCoordinate().longitude;

                    String uri = "http://maps.google.com/maps?daddr="+ lat +"," + lng;
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                }
            });
            holder.layItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // sposta la mappa sulla posizione
                    ((MainActivity) getActivity()).getMappa()
                            .muoviCamera(parcheggio.getCoordinate());
                    // seleziona il parcheggio
                    setParcheggioSelezionato(parcheggio.getCoordinate());

                    ((MainActivity) getActivity()).getMappa().deselezionaMarker();
                }
            });
        }


        @Override
        public int getItemCount() {
            return mParcheggiRecycler.size();
        }
    }
}