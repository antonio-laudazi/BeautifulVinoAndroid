package com.marte5.beautifulvino;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.marte5.beautifulvino.Model.Badge;
import com.marte5.beautifulvino.Model.Esito;
import com.marte5.beautifulvino.Model.Evento;
import com.marte5.beautifulvino.Model.HttpHandler;
import com.marte5.beautifulvino.Model.SharedPreferencesManager;
import com.marte5.beautifulvino.Model.Utente;
import com.marte5.beautifulvino.Utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class UtenteFragment extends Fragment implements EventiFragment.OnListFragmentInteractionListener, UtentePagerAdapter.OnUtenteButtonsListener {

    private EventiFragment.OnListFragmentInteractionListener mListenerEventi;
    private String TAG = UtenteFragment.class.getSimpleName();
    private static final int REQ_CODE_UTENTE = 2;
    private UtentePagerAdapter adapter;
    private Utente utente;
    private String idUtentePadre;
    private TabLayout tabLayout;

    private LinearLayout linearLayoutListVuota;
    private TextView textViewListVuotaTitolo;
    private TextView textViewListVuotaTesto;

    private ViewPager viewPager;
    private Pager adapterP;

    private int page;
    private ArrayList<ImageView> indicators;
    private FirebaseAnalytics mFirebaseAnalytics;

    public UtenteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "A_Profilo", null);
        idUtentePadre = SharedPreferencesManager.getIdUser(getActivity());
        if (getArguments() != null) {
            utente = (Utente) getArguments().get("utente");
        } else {
            utente = new Utente(null);
        }

        adapterP = new Pager(getChildFragmentManager());

        Bundle bundle = new Bundle();
        EventiFragment evF = new EventiFragment();
        bundle.putParcelableArrayList("eventi", (ArrayList<? extends Parcelable>) utente.getEventiUtente());
        bundle.putBoolean("disableSwape", true);
        if (utente.getIdUtente().equals(SharedPreferencesManager.getIdUser(getContext())) || utente.getIdUtente() == ""){//mio profilo
            bundle.putBoolean("prezzoHidden", true);
        }else{
            bundle.putBoolean("prezzoHidden", false);
        }
        evF.setArguments(bundle);

        ViniFragment vF = new ViniFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putParcelableArrayList("vini", (ArrayList<? extends Parcelable>) utente.getAziendeUtente());
        vF.setArguments(bundle);

        BadgeFragment bF = new BadgeFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putParcelableArrayList("badge", (ArrayList<? extends Parcelable>) utente.getBadgeUtente());
        bF.setArguments(bundle);

        adapterP.addFragment(evF, "EVENTI");
        adapterP.addFragment(vF, "CARTA DEI VINI");
        adapterP.addFragment(bF, "BADGE");

   //   new GetUtente().execute(); //spostato nel onResume reload utenti ogni volta che visualizza il fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_utente_list, container, false);


        tabLayout = view.findViewById(R.id.tabLayout);

        linearLayoutListVuota =  view.findViewById(R.id.linearLayoutListVuota);
        textViewListVuotaTitolo =  view.findViewById(R.id.textViewListVuota1);
        textViewListVuotaTesto =  view.findViewById(R.id.textViewListVuota2);
        linearLayoutListVuota.setVisibility(View.INVISIBLE);

        tabLayout.addTab(tabLayout.newTab().setText("EVENTI"));
        tabLayout.addTab(tabLayout.newTab().setText("CARTA DEI VINI"));
        tabLayout.addTab(tabLayout.newTab().setText("BADGE"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = view.findViewById(R.id.pager);

        viewPager.setAdapter(adapterP);
        viewPager.setOffscreenPageLimit(2);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setFragment(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setupWithViewPager(viewPager);

        adapter = new UtentePagerAdapter(utente, view.getContext(), this);
        ViewPager myPager = view.findViewById(R.id.viewPagerUtente);
        myPager.setAdapter(adapter);
        myPager.setCurrentItem(0);
        myPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                page = position;
                updateIndicators(page);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        indicators = new ArrayList<>();
        indicators.add((ImageView) view.findViewById(R.id.utente_indicator_0));
        indicators.add((ImageView) view.findViewById(R.id.utente_indicator_1));
        updateIndicators(myPager.getCurrentItem());
        return view;
    }

    private void updateIndicators(int position) {
        for (int i = 0; i < indicators.size(); i++) {
            indicators.get(i).setBackgroundResource(
                    i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EventiFragment.OnListFragmentInteractionListener) {
            mListenerEventi = (EventiFragment.OnListFragmentInteractionListener) getActivity();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListenerEventi = null;
    }

    @Override
    public void onListFragmentInteraction(Evento ev, ImageView sharedImageView, TextView c, TextView titolo, ImageView pinImage, TextView prezzo, TextView data, View whiteView, TextView tema) {

    }

    private void setFragment(int position) {
        //   int position = tab.getPosition();
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id-Profilo");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Profilo");
        switch (position) {
            case 0: {
                if (utente.getEventiUtente().size()==0 && utente.getIdUtente().equals(SharedPreferencesManager.getIdUser(getContext()))) {//mio Profilo
                    textViewListVuotaTitolo.setText(R.string.text_view_eventi_profilo_vuota_titolo);
                    textViewListVuotaTesto.setText(R.string.text_view_eventi_profilo_vuota_testo);
                    linearLayoutListVuota.setVisibility(View.VISIBLE);
                }else{
                    linearLayoutListVuota.setVisibility(View.GONE);
                }
                EventiFragment evF = (EventiFragment) adapterP.getItem(position);
                evF.updateEventiFragment(utente.getEventiUtente());

                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "EventiProfilo");
            }
            break;
            case 1: {
                if (utente.getAziendeUtente().size()==0 && utente.getIdUtente().equals(SharedPreferencesManager.getIdUser(getContext()))){
                    textViewListVuotaTitolo.setText(R.string.text_view_vini_profilo_vuota_titolo);
                    textViewListVuotaTesto.setText(R.string.text_view_vini_profilo_vuota_testo);
                    linearLayoutListVuota.setVisibility(View.VISIBLE);
                }else{
                    linearLayoutListVuota.setVisibility(View.GONE);
                }
                ViniFragment vF = (ViniFragment) adapterP.getItem(position);
                vF.updateViniFragment(utente.getAziendeUtente());
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "ViniProfilo");
            }
            break;
            case 2: {
                if (utente.getBadgeUtente().size()==0 && utente.getIdUtente().equals(SharedPreferencesManager.getIdUser(getContext()))){
                    textViewListVuotaTitolo.setText(R.string.text_view_badge_profilo_vuota_titolo);
                    textViewListVuotaTesto.setText(R.string.text_view_badge_profilo_vuota_testo);
                    linearLayoutListVuota.setVisibility(View.VISIBLE);
                }else{
                    linearLayoutListVuota.setVisibility(View.GONE);
                }
                BadgeFragment bF = (BadgeFragment) adapterP.getItem(position);
                bF.adapterBadge.updateBadge(utente.getBadgeUtente(), utente.getIdUtente().equals(SharedPreferencesManager.getIdUser(getContext())));
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "BadgeProfilo");
            }
            break;
        }
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onImpostazioniButtonPressed() {
        Intent intent = new Intent(this.getContext(), ImpostazioniActivity.class);
        intent.putExtra("utente", utente);
        startActivityForResult(intent, REQ_CODE_UTENTE);
    }

    @Override
    public void onSeguiButtonPressed() {
        ChangeStatoUtente csu = new ChangeStatoUtente(utente.getStatoUtente());//utente.getStatoUtente=oldStato

        if (utente.getStatoUtente() == "" || utente.getStatoUtente().equals(Utente.VALUES_STATO_OTHER)) {
            utente.setStatoUtente(Utente.VALUES_STATO_SEGUITO);
        } else {
            utente.setStatoUtente(Utente.VALUES_STATO_OTHER);
        }
        csu.execute();
        adapter.setButtonSegui();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_UTENTE) {
            if (resultCode == MainActivity.RESULT_OK) {
           //    new GetUtente().execute(); //spostato nel onResume reload utenti ogni volta che visualizza il fragment
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        new GetUtente().execute();
    }

    public class GetUtente extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String idUtente;
            if (utente.getIdUtente() == "") {//mio profilo
                idUtente = idUtentePadre;
            } else {
                idUtente = utente.getIdUtente();
            }
            String jsonStr = sh.makeServiceCallGetUtente(idUtente, idUtentePadre);
           //  Log.d(TAG, jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jsonEsito = jsonObj.getJSONObject("esito");
                    final Esito es = new Esito(jsonEsito);
                    if (es.getCodice() != Esito.ERROR_CODE) {
                        utente = new Utente(jsonObj.getJSONObject("utente"));
                  //      Log.d(TAG, String.valueOf(utente.getBadgeUtente()));
                    } else {
                        if (getActivity() != null)
                            getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity().getApplicationContext(),
                                        es.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                    }
                } catch (final JSONException e) {
                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Errore nel messaggio del server: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                    String mess="Errore di comunicazione con il server.";
                    if(!Utility.isNetworkAvailable(getApplicationContext())){
                        mess="Assenza di collegamento, controllare la connessione dati.";
                    }
                    final String finalMess = mess;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    finalMess,
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            adapter.refreshViews(utente);
            int position = tabLayout.getSelectedTabPosition();
            setFragment(position);
        }
    }

    private class ChangeStatoUtente extends AsyncTask<Void, Void, Void> {
        private String oldStato;

        public ChangeStatoUtente(String oldStato) {
            super();
            this.oldStato = oldStato;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String myIdUtente = SharedPreferencesManager.getIdUser(getActivity());
            String utenteToChange = utente.getIdUtente();

            String jsonStr = sh.makeServiceCallChangeStatoUtente(utente.getStatoUtente(), utenteToChange, myIdUtente);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jsonEsito = jsonObj.getJSONObject("esito");
                    final Esito es = new Esito(jsonEsito);
                    if (es.getCodice() == Esito.ERROR_CODE) {
                        utente.setStatoUtente(oldStato);
                        if (getActivity() != null)
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.setButtonSegui();
                                    Toast.makeText(getApplicationContext(),
                                            es.getMessage(),
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            });
                    }
                } catch (final JSONException e) {
                    utente.setStatoUtente(oldStato);
                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setButtonSegui();
                                Toast.makeText(getApplicationContext(),
                                        "Errore nel messaggio del server: " + e.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

                }
            } else {
                utente.setStatoUtente(oldStato);
                String mess="Errore di comunicazione con il server.";
                if(!Utility.isNetworkAvailable(getApplicationContext())){
                    mess="Assenza di collegamento, controllare la connessione dati.";
                }
                final String finalMess = mess;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                finalMess,
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }


    class Pager extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Pager(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}


