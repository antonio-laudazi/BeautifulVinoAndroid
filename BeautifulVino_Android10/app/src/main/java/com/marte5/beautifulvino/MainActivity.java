package com.marte5.beautifulvino;

import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.marte5.beautifulvino.Model.Azienda;
import com.marte5.beautifulvino.Model.Badge;
import com.marte5.beautifulvino.Model.Esito;
import com.marte5.beautifulvino.Model.Evento;
import com.marte5.beautifulvino.Model.Feed;
import com.marte5.beautifulvino.Model.HttpHandler;
import com.marte5.beautifulvino.Model.Provincia;
import com.marte5.beautifulvino.Model.SharedPreferencesManager;
import com.marte5.beautifulvino.Model.Vino;
import com.marte5.beautifulvino.OnBoarding.MyBounceInterpolator;
import com.marte5.beautifulvino.RegistrazioneLogin.CognitoSyncClientManager;
import com.marte5.beautifulvino.Utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EventiFragment.OnLoadMoreEventiListener, FeedFragment.OnLoadMoreFeedListener, EventiFragment.OnListFragmentInteractionListener, BadgeFragment.OnListFragmentInteractionListener, ViniFragment.OnListFragmentInteractionListener, FeedFragment.OnListFragmentInteractionListener {

    private String TAG = MainActivity.class.getSimpleName();

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private static final int REQ_CODE_PROVINCE = 1;
    private ArrayList<Evento> eventi;
    private ArrayList<Feed> feedArrayList;
    private Provincia provinciaSelezionata;
    private Button buttonProvincia;
    private RelativeLayout layoutButtons;
    private String idUtente;
    private int numTotEventi;
    private int numTotFeed;
    private LinearLayout linearLayoutEventiVuota;
    private ImageView cerchioGiallo;
    private ImageView cerchioRosa;
    private boolean loadedEventiError;
    private boolean loadedFeedError;
    private Evento lastEv;
    private Feed lastF;
    private boolean feedRefreshing;

    private ProgressBar progressBarEventi;
    private ProgressBar progressBarFeed;
    private Point p;

    public static String TRANSITION_IM = "transition_name_im";
    public static String TRANSITION_CITTA = "transition_name_c";
    public static String TRANSITION_TITOLO = "transition_name_titolo";
    public static String TRANSITION_PIN = "transition_name_pin";
    public static String TRANSITION_PREZZO = "transition_name_prezzo";
    public static String TRANSITION_DATA = "transition_name_data";
    public static String TRANSITION_WHITE_V = "transition_name_white_view";
    public static String TRANSITION_TEMA = "transition_name_tema";
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(MainActivity.this);
        setContentView(R.layout.activity_main);
        linearLayoutEventiVuota = findViewById(R.id.linearLayoutListEventiVuota);
        TextView textViewStringVuota=findViewById(R.id.textViewListEventiVuota3);
        textViewStringVuota.setText(Html.fromHtml("Nel frattempo puoi dare un’occhiata alla sezione <b><font color='#462b35'>Scopri</font></b> e " +
                        "imparare un sacco di cose direttamente dagli addetti ai lavori!"));
      //  textViewStringVuota.setText(Html.fromHtml(getResources().getString(R.string.text_view_eventi_vuota3)));
     //   mytextview.setText(Html.fromHtml(sourceString));

        progressBarEventi = findViewById(R.id.progress_bar_eventi);
        progressBarEventi.setVisibility(View.GONE);
        progressBarFeed = findViewById(R.id.progress_bar_feed);
        progressBarFeed.setVisibility(View.GONE);
        feedRefreshing=false;

        idUtente = SharedPreferencesManager.getIdUser(MainActivity.this);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (SharedPreferencesManager.getProvincia(this) == null) {
            provinciaSelezionata = new Provincia(true);
            SharedPreferencesManager.setProvincia(this, provinciaSelezionata);
        } else {
            provinciaSelezionata = SharedPreferencesManager.getProvincia(this);
        }
        fragmentManager = getSupportFragmentManager();
        eventi = new ArrayList();
        feedArrayList = new ArrayList();
        numTotEventi = 0;
        numTotFeed = 0;
        fragment = new EventiFragment();
        loadFragment(fragment, "EventiFragment");
        new RefreshEventi().execute();
        new GetFeedArrayList(true).execute();

        layoutButtons = findViewById(R.id.relativeLayoutButtonsEventi);

        buttonProvincia = findViewById(R.id.buttonProvince);
        buttonProvincia.setText(SharedPreferencesManager.getProvincia(this).getNomeProvincia());
        buttonProvincia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProvinceActivity.class);
                startActivityForResult(intent, REQ_CODE_PROVINCE);
            }
        });

        Button buttonInfo = findViewById(R.id.buttonInfo);
        buttonInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });


        p = Utility.getScreenSize(this);

        cerchioGiallo = findViewById(R.id.imageViewYellowMain);
        cerchioRosa = findViewById(R.id.imageViewRosaMain);
//(int left, int top, int right, int bottom)
        ViewGroup.MarginLayoutParams marginParamsRosa = (ViewGroup.MarginLayoutParams) cerchioRosa.getLayoutParams();
        marginParamsRosa.setMargins(p.x, (int) -(marginParamsRosa.height / 1.90), 0, 0);

        ViewGroup.MarginLayoutParams marginParamsYellow = (ViewGroup.MarginLayoutParams) cerchioGiallo.getLayoutParams();
        marginParamsYellow.setMargins(p.x, (int) -(marginParamsYellow.height / 1.82), 0, 0);
        animateCerchi();

    }

    private void animateCerchi() {

        ViewGroup.MarginLayoutParams marginParamsRosa = (ViewGroup.MarginLayoutParams) cerchioRosa.getLayoutParams();
        marginParamsRosa.setMargins(90, (int) -(marginParamsRosa.height / 1.82), 0, 0);
        ViewGroup.MarginLayoutParams marginParamsG = (ViewGroup.MarginLayoutParams) cerchioGiallo.getLayoutParams();
        marginParamsG.setMargins(-60, (int) -(marginParamsG.height / 1.90), 0, 0);

        final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.15, 20);
        myAnim.setInterpolator(interpolator);
        myAnim.setStartOffset(400);
        cerchioRosa.startAnimation(myAnim);

        final Animation myAnimG = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
        MyBounceInterpolator interpolatorG = new MyBounceInterpolator(0.15, 20);
        myAnimG.setInterpolator(interpolatorG);
        cerchioGiallo.startAnimation(myAnimG);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_PROVINCE) {
            if (resultCode == RESULT_OK) {
                Provincia pr = data.getParcelableExtra("provincia");
                buttonProvincia.setText(pr.getNomeProvincia());
                provinciaChanged(pr);
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Bundle bundle = new Bundle();
            ViewGroup.MarginLayoutParams marginParamsRosa = (ViewGroup.MarginLayoutParams) cerchioRosa.getLayoutParams();
            ViewGroup.MarginLayoutParams marginParamsG = (ViewGroup.MarginLayoutParams) cerchioGiallo.getLayoutParams();
            switch (item.getItemId()) {
                case R.id.navigation_eventi:
                    cerchioGiallo.setScaleX(1);
                    cerchioRosa.setScaleX(1f);
                    marginParamsRosa.setMargins(90, (int) -(marginParamsRosa.height / 1.82), 0, 0);
                    marginParamsG.setMargins(-60, (int) -(marginParamsG.height / 1.90), 0, 0);
                    layoutButtons.setVisibility(View.VISIBLE);
                    cerchioGiallo.setVisibility(View.VISIBLE);
                    cerchioRosa.setVisibility(View.VISIBLE);
                    fragment = new EventiFragment();
                    bundle.putParcelableArrayList("eventi", eventi);
                    fragment.setArguments(bundle);
                    if (eventi.size() == 0) {
                        linearLayoutEventiVuota.setVisibility(View.VISIBLE);
                    } else {
                        linearLayoutEventiVuota.setVisibility(View.GONE);
                    }
                    loadFragment(fragment, "A_ListaEventi");
                    progressBarFeed.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_feed:

                    if (feedRefreshing){
                        progressBarFeed.setVisibility(View.VISIBLE);
                    }else{
                        progressBarFeed.setVisibility(View.INVISIBLE);
                    }
                    layoutButtons.setVisibility(View.GONE);
                    cerchioGiallo.setVisibility(View.GONE);
                    cerchioRosa.setVisibility(View.GONE);
                    fragment = new FeedFragment();
                    bundle.putParcelableArrayList("feed", feedArrayList);
                    fragment.setArguments(bundle);
                    loadFragment(fragment, "A_ListaFeed");
                    linearLayoutEventiVuota.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_profilo:
                    progressBarFeed.setVisibility(View.INVISIBLE);
                    cerchioGiallo.setVisibility(View.GONE);
                    cerchioRosa.setVisibility(View.GONE);
                    layoutButtons.setVisibility(View.GONE);
                    fragment = new UtenteFragment();
                    loadFragment(fragment, "");
                    linearLayoutEventiVuota.setVisibility(View.GONE);
                    return true;
            }
            return false;
        }
    };


    private void loadFragment(Fragment fragment, String nameFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.flContainer, fragment);
        transaction.commit();
        mFirebaseAnalytics.setCurrentScreen(MainActivity.this, nameFragment, null);
    }

    @Override
    public void onListFragmentInteraction(Evento ev, ImageView sharedImageView, TextView citta, TextView titolo, ImageView pinImage, TextView prezzo, TextView data, View whiteView, TextView tema) {
        Intent intent = new Intent(this, EventoDetailsActivity.class);
        intent.putExtra("evento", ev);
        intent.putExtra(TRANSITION_IM, ViewCompat.getTransitionName(sharedImageView));
        intent.putExtra(TRANSITION_CITTA, ViewCompat.getTransitionName(citta));
        intent.putExtra(TRANSITION_TITOLO, ViewCompat.getTransitionName(titolo));
        intent.putExtra(TRANSITION_PIN, ViewCompat.getTransitionName(pinImage));
        intent.putExtra(TRANSITION_PREZZO, ViewCompat.getTransitionName(prezzo));
        intent.putExtra(TRANSITION_DATA, ViewCompat.getTransitionName(data));
        intent.putExtra(TRANSITION_WHITE_V, ViewCompat.getTransitionName(whiteView));
        intent.putExtra(TRANSITION_TEMA, ViewCompat.getTransitionName(tema));

        Pair<View, String> p1 = Pair.create((View) sharedImageView, ViewCompat.getTransitionName(sharedImageView));
        Pair<View, String> p2 = Pair.create((View) citta, ViewCompat.getTransitionName(citta));
        Pair<View, String> p3 = Pair.create((View) titolo, ViewCompat.getTransitionName(titolo));
        Pair<View, String> p4 = Pair.create((View) pinImage, ViewCompat.getTransitionName(pinImage));
        Pair<View, String> p5 = Pair.create((View) prezzo, ViewCompat.getTransitionName(prezzo));
        Pair<View, String> p6 = Pair.create((View) data, ViewCompat.getTransitionName(data));
        Pair<View, String> p7 = Pair.create(whiteView, ViewCompat.getTransitionName(whiteView));
        Pair<View, String> p8 = Pair.create((View) tema, ViewCompat.getTransitionName(tema));

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p7, p2, p3, p4, p5, p6, p8);
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onListFragmentInteraction(Vino v) {
        Intent intent = new Intent(this, VinoDetailsActivity.class);
        intent.putExtra("vino", v);
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(Azienda az) {
        Intent intent = new Intent(this, AziendaDetailsActivity.class);
        intent.putExtra("azienda", az);
        startActivity(intent);
    }

    @Override
    public void onButtonMostraClick() {

    }

    @Override
    public void onListFragmentInteraction(Feed f) {
        if (f.getTipoFeed() == Feed.VALUES_TIPO_FEED_PUBBLICITA || f.getTipoFeed() == Feed.VALUES_TIPO_FEED_VINO) {
            Intent intent = new Intent(this, VinoDetailsActivity.class);
            intent.putExtra("vino", f.getVinoFeed());
            startActivity(intent);
        } else if (f.getTipoFeed() == Feed.VALUES_TIPO_FEED_POST || f.getTipoFeed() == Feed.VALUES_TIPO_FEED_AZIENDA) {
            Intent intent = new Intent(this, PostDetailsActivity.class);
            intent.putExtra("feed", f);
            startActivity(intent);
        } else if (f.getTipoFeed() == Feed.VALUES_TIPO_FEED_EVENTO) {
            Intent intent = new Intent(this, EventoDetailsActivity.class);
            intent.putExtra("evento", f.getEventoFeed());
            startActivity(intent);
        }
    }

    @Override
    public void onListFragmentInteraction(Badge b) {
        if (b.getEventoBadge().getIdEvento() != "") {
            Intent intent = new Intent(this, EventoDetailsActivity.class);
            intent.putExtra("evento", b.getEventoBadge());
            startActivity(intent);
        }
    }


    @Override
    public void onListHeaderFragmentInteraction(Feed feed) {
        if (feed.getTipoEntitaHeader().equals(Feed.VALUES_TIPO_ENTITA_HEADER_VINO)) {
            Intent intent = new Intent(this, VinoDetailsActivity.class);
            Vino vino = new Vino(null);
            intent.putExtra("vino", vino);
            startActivity(intent);

        } else if (feed.getTipoEntitaHeader().equals(Feed.VALUES_TIPO_ENTITA_HEADER_EVENTO)) {
            Intent intent = new Intent(this, EventoDetailsActivity.class);
            Evento evento = new Evento(null);
            intent.putExtra("evento", evento);
            startActivity(intent);

        } else if (feed.getTipoEntitaHeader().equals(Feed.VALUES_TIPO_ENTITA_HEADER_PROFILO)) {
            Intent intent = new Intent(this, UtenteDetailsActivity.class);
            Evento evento = new Evento(null);
            intent.putExtra("evento", evento);
            startActivity(intent);
        } else if (feed.getTipoEntitaHeader().equals(Feed.VALUES_TIPO_ENTITA_HEADER_AZIENDA)) {
            Intent intent = new Intent(this, AziendaDetailsActivity.class);
            Azienda az = new Azienda(null);
            az.setIdAzienda(feed.getIdEntitaHeader());
            intent.putExtra("azienda", az);
            startActivity(intent);
        }
    }

    @Override
    public void onLoadMoreEventi() {
        if (fragment instanceof EventiFragment) {
            if (numTotEventi > eventi.size() && !loadedEventiError) {
                lastEv = eventi.get(eventi.size() - 1);
                new GetMoreEventi().execute();
            } else {
                EventiFragment evF = (EventiFragment) fragment;
                evF.stopLoading(null);
            }
        }
    }

    @Override
    public void onLoadMoreFeed() {
        if (fragment instanceof FeedFragment) {
            if (numTotFeed > feedArrayList.size() && !loadedFeedError) {
                lastF = feedArrayList.get(feedArrayList.size() - 1);
                new GetFeedArrayList(false).execute();
            } else {
                FeedFragment fF = (FeedFragment) fragment;
                fF.stopLoading(null);
            }
        }
    }

    public void refreshEventi() {
        new RefreshEventi().execute();
    }


    public void getFeed(boolean refresh) {
        new GetFeedArrayList(refresh).execute();
    }


    public void provinciaChanged(Provincia pr) {
        if (provinciaSelezionata.getIdProvincia() != pr.getIdProvincia()) {
            provinciaSelezionata = pr;
            new RefreshEventi().execute();
        }
    }


    public class RefreshEventi extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarEventi.setVisibility(View.VISIBLE);
            progressBarEventi.setIndeterminate(true);
            eventi.clear();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCallGetEventi(null, provinciaSelezionata.getIdProvincia(), idUtente);
            //   Log.d(TAG, "RefreshEventi " + provinciaSelezionata.getIdProvincia());

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jsonEsito = jsonObj.getJSONObject("esito");
                    final Esito es = new Esito(jsonEsito);
                    if (es.getCodice() != Esito.ERROR_CODE) {
                        loadedEventiError = false;
                        JSONArray eventiJson = jsonObj.getJSONArray("eventi");
                        //    Log.d(TAG, "num eventi ricevuti " + eventiJson.length());
                        for (int i = 0; i < eventiJson.length(); i++) {
                            JSONObject c = eventiJson.getJSONObject(i);
                            Evento ev = new Evento(c);
                            eventi.add(ev);
                            // Log.d("PIPPO---->>", "RefreshEventi evento titiolo " + ev.getTitoloEvento() + " " + ev.getIdEvento());
                        }
                        numTotEventi = jsonObj.getInt("numTotEventi");
                    } else {
                        loadedEventiError = true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        es.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                    }
                } catch (final JSONException e) {
                    loadedEventiError = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Errore di comunicazione con il server. " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                loadedEventiError = true;
                String mess="Errore di comunicazione con il server.";
                if(!Utility.isNetworkAvailable(MainActivity.this)){
                    mess="Assenza di collegamento, controllare la connessione dati.";
                }
                final String finalMess = mess;
                runOnUiThread(new Runnable() {
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
            progressBarEventi.setVisibility(View.GONE);
            if (fragment instanceof EventiFragment) {
                if (eventi.size() == 0 && !loadedEventiError) {
                    linearLayoutEventiVuota.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutEventiVuota.setVisibility(View.GONE);
                }
                ((EventiFragment) fragment).updateEventiFragment(eventi);
            }
        }

    }

    public class GetMoreEventi extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((EventiFragment) fragment).addLoading();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCallGetEventi(lastEv, provinciaSelezionata.getIdProvincia(), idUtente);

           // Log.e(TAG, "Response from url: " + jsonStr);
            ((EventiFragment) fragment).removeLoading();

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jsonEsito = jsonObj.getJSONObject("esito");
                    final Esito es = new Esito(jsonEsito);
                    if (es.getCodice() != Esito.ERROR_CODE) {
                        JSONArray eventiJson = jsonObj.getJSONArray("eventi");
                       // Log.d(TAG, "num eventi ricevuti " + eventiJson.length());
                        loadedEventiError = false;
                        for (int i = 0; i < eventiJson.length(); i++) {
                            JSONObject c = eventiJson.getJSONObject(i);
                            Evento ev = new Evento(c);
                            eventi.add(ev);
                            // Log.d("PIPPO---->>", "GetMoreEventi evento titiolo " + ev.getTitoloEvento() + " " + ev.getIdEvento());
                        }
                        numTotEventi = jsonObj.getInt("numTotEventi");
                    } else {
                        loadedEventiError = true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        es.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                    }
                } catch (final JSONException e) {
                    loadedEventiError = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Errore di comunicazione con il server: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                loadedEventiError = true;
                String mess="Errore di comunicazione con il server.";
                if(!Utility.isNetworkAvailable(MainActivity.this)){
                    mess="Assenza di collegamento, controllare la connessione dati.";
                }
                final String finalMess = mess;
                runOnUiThread(new Runnable() {
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
            // Dismiss the progress dialog
            if (fragment instanceof EventiFragment) {
                ((EventiFragment) fragment).stopLoading(eventi);
            }
        }

    }

    private class GetFeedArrayList extends AsyncTask<Void, Void, Void> {
        private boolean refresh;

        public GetFeedArrayList(boolean refresh) {
            super();
            this.refresh = refresh;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (refresh) {
                feedRefreshing=true;
                if (fragment instanceof FeedFragment) {
                    progressBarFeed.setVisibility(View.VISIBLE);
                }
                feedArrayList.clear();
            } else {
                ((FeedFragment) fragment).addLoading();
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr;
            if (refresh) {
                jsonStr = sh.makeServiceCallGetFeed(null, idUtente);
            } else {
                ((FeedFragment) fragment).removeLoading();
                jsonStr = sh.makeServiceCallGetFeed(lastF, idUtente);
            }
           // Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jsonEsito = jsonObj.getJSONObject("esito");
                    final Esito es = new Esito(jsonEsito);
                    if (es.getCodice() != Esito.ERROR_CODE) {
                        loadedFeedError = false;
                        JSONArray feedArrayJson = jsonObj.getJSONArray("feed");
                        // Log.d(TAG, "num feed ricevuti " + feedArrayJson.length());
                        for (int i = 0; i < feedArrayJson.length(); i++) {
                            JSONObject c = feedArrayJson.getJSONObject(i);
                            Feed f = new Feed(c);
                            feedArrayList.add(f);
                            //  Log.d(TAG, c.toString());
                        }
                        numTotFeed = jsonObj.getInt("numTotFeed");
                    } else {
                        loadedFeedError = true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        es.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                    }
                } catch (final JSONException e) {
                    loadedFeedError = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Errore di comunicazione con il server. " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                loadedFeedError = true;
                String mess="Errore di comunicazione con il server.";
                if(!Utility.isNetworkAvailable(MainActivity.this)){
                    mess="Assenza di collegamento, controllare la connessione dati.";
                }
                final String finalMess = mess;
                runOnUiThread(new Runnable() {
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
            feedRefreshing=false;
            if (fragment instanceof FeedFragment) {
                if (refresh) {
                    progressBarFeed.setVisibility(View.GONE);
                    ((FeedFragment) fragment).updateFeedFragment(feedArrayList);
                } else {
                    ((FeedFragment) fragment).stopLoading(feedArrayList);

                }
            }
        }
    }
}
