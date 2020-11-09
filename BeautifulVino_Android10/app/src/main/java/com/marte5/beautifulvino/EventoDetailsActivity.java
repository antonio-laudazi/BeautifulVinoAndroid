
package com.marte5.beautifulvino;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.transition.Fade;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.marte5.beautifulvino.Model.Azienda;
import com.marte5.beautifulvino.Model.Esito;
import com.marte5.beautifulvino.Model.Evento;
import com.marte5.beautifulvino.Model.HttpHandler;
import com.marte5.beautifulvino.Model.SharedPreferencesManager;
import com.marte5.beautifulvino.Model.Utente;
import com.marte5.beautifulvino.Model.Vino;
import com.marte5.beautifulvino.Utility.Animations;
import com.marte5.beautifulvino.Utility.ExtendedScrollView;
import com.marte5.beautifulvino.Utility.Utility;
import com.marte5.beautifulvino.dummy.ListItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventoDetailsActivity extends AppCompatActivity implements OnMapReadyCallback,
        MyUtenteRecyclerViewAdapter.OnItemClickListener, MyAziendaRecyclerViewAdapter.OnItemClickListener,
        ViniFragment.OnListFragmentInteractionListener, ViniActivity.OnListFragmentInteractionListener {

    public MyVinoRecyclerViewAdapter adapterVini;
    public MyUtenteRecyclerViewAdapter adapterUtenti;
    public MyAziendaRecyclerViewAdapter adapterAzienda;

    private ImageView imageViewEvento;
    private TextView textViewCitta;
    private ImageView imagePin;
    private ImageButton buttonPreferito;
    private TextView textViewTitolo;
    private TextView textViewData;
    private TextView textViewPrezzo;
    private TextView textViewTema;

    private String TAG = EventoDetailsActivity.class.getSimpleName();
    private ExtendedScrollView scrollViewEvento;
    private FrameLayout frameLayoutWithTitoloInAlto;
    private TextView textViewTitleView;
    private RelativeLayout relativeLayoutWithTitolo;
    private Button buttonAcquista;

    private TextView textViewTesto;

    private RelativeLayout relLayoutPB;

    private LinearLayout linearLayoutLocation;
    private RecyclerView recyclerViewAziende;

    private LinearLayout linearLayoutVini;
    private RecyclerView recyclerViewVini;

    private LinearLayout linearLayoutDove;
    private TextView textViewDoveCitta;
    private TextView textViewDoveInd;
    private TextView textViewDoveTelefono;
    private TextView textViewDoveEmail;

    private LinearLayout linearLayoutPartecipanti;
    private TextView textViewMaxPartecipanti;
    private TextView textViewDisponibili;

    private LinearLayout linearLayoutBadge;

    private TextView textViewTitoloBadge;
    private ImageView imageViewBadgeEvento;
    private LinearLayout linearLayoutIscritti;

    private ProgressDialog waitDialog;

    private MyUtenteRecyclerViewAdapter.OnItemClickListener mListenerUtenti;
    private ViniFragment.OnListFragmentInteractionListener mListenerVino;
    private MyAziendaRecyclerViewAdapter.OnItemClickListener mListenerAzienda;

    private GoogleMap mMap;
    private Marker markerEv;

    private List<ListItem> listVini;
    private List<Azienda> aziendeOspitante;
    private String idUtente;
    private Evento evento;

    private int initYButtonAcquista;
    private int initYTitleViewInAlto;
    private boolean initPosition;
    private int heightScreen;
    private int altezzaButtonAcquistaMargine;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_evento_details);
        supportPostponeEnterTransition();
        idUtente = SharedPreferencesManager.getIdUser(EventoDetailsActivity.this);
        Bundle data = getIntent().getExtras();
        evento = data.getParcelable("evento");
        createListVini();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        heightScreen = Utility.getHeightScreen(this);
        scrollViewEvento = findViewById(R.id.scrollViewEvento);
        frameLayoutWithTitoloInAlto = findViewById(R.id.frameLayoutTitleView);
        relativeLayoutWithTitolo = findViewById(R.id.relativeLayoutTitoloEvento);
        textViewTitleView = findViewById(R.id.textViewTitleView);

        buttonAcquista = findViewById(R.id.buttonAcquistaEvento);
        buttonAcquista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (evento.getNumPostiDisponibiliEvento() <= 0 && evento.getNumMaxPartecipantiEvento() > 0) {
                    String message = "Che peccato, sei arrivato tardi! I posti disponibili per questo evento sono terminati. Ma non tutto è perduto: salva l'evento tra i preferiti e ti avvertiremo nel caso in cui ci fosse una maggiore disponibilità. Grazie!";
                    AlertDialog.Builder builder = new AlertDialog.Builder(EventoDetailsActivity.this);
                    builder.setTitle("")
                            .setMessage(message)
                            .setCancelable(true);
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    createAlertDialogWithPicker();
                }
            }
        });

        buttonPreferito = findViewById(R.id.buttonPrefEvento);
        buttonPreferito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (evento.statoEventoModificabile()) {
                    if (evento.getStatoPreferitoEvento().equals(Evento.VALUES_STATO_PREFERITO)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EventoDetailsActivity.this);
                        builder.setTitle("")
                                .setMessage("Sicuro di voler rimuovere questo evento dai preferiti?")
                                .setCancelable(true)
                                .setPositiveButton("Sì", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        changeStatoPreferitoEvento();
                                    }
                                })
                                .setNegativeButton("Annulla", null);
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        changeStatoPreferitoEvento();
                    }
                } else {
                    String prenotati = "prenotati";
                    if (evento.getAcquistabileEvento() == Evento.VALUES_STATO_ACQUISTABILE_SI) {
                        prenotati = "acquistati";
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(EventoDetailsActivity.this);
                    builder.setTitle("Attenzione!")
                            .setMessage("Non puoi cancellare gli eventi " + prenotati + " fino alle ore 12:00 del giorno successivo all'evento.")
                            .setCancelable(true);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        final Animation animationUp = AnimationUtils.loadAnimation(this, R.anim.up_animation);
        final Animation animationDown = AnimationUtils.loadAnimation(this, R.anim.down_animation);

        initPosition = true;
        initYTitleViewInAlto = setInitPositionOfTitleView();
        initYButtonAcquista = setInitPositionOfButtonAcquista();
        altezzaButtonAcquistaMargine = (int) (92 * buttonAcquista.getContext().getResources().getDisplayMetrics().density);//60=altezza button+12=margine+20

        imageViewEvento = findViewById(R.id.imageViewEvento);
        textViewCitta = findViewById(R.id.textViewCitta);
        textViewTitolo = findViewById(R.id.textViewTitolo);
        textViewData = findViewById(R.id.textViewData);
        textViewPrezzo = findViewById(R.id.textViewPrezzo);
        imagePin = findViewById(R.id.imageViewPin);
        textViewTema = findViewById(R.id.textViewTema);

        textViewTesto = findViewById(R.id.textViewTesto);

        linearLayoutLocation = findViewById(R.id.linearLayoutLocation);
        mListenerAzienda = this;
        aziendeOspitante = new ArrayList<>();
        aziendeOspitante.add(evento.getAziendaOspitanteEvento());
        adapterAzienda = new MyAziendaRecyclerViewAdapter(aziendeOspitante, mListenerAzienda, this);
        recyclerViewAziende = findViewById(R.id.listAziendaEvento);
        recyclerViewAziende.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAziende.setAdapter(adapterAzienda);

        linearLayoutVini = findViewById(R.id.linearLayoutVini);
        mListenerVino = this;
        adapterVini = new MyVinoRecyclerViewAdapter(listVini, mListenerVino, false);
        recyclerViewVini = findViewById(R.id.listVino);
        recyclerViewVini.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVini.setAdapter(adapterVini);

        linearLayoutDove = findViewById(R.id.linearLayoutDove);
        ImageButton buttonMapEvento = findViewById(R.id.buttonMapEvento);
        buttonMapEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%f,%f (%s)\"", evento.getLatitudineEvento(), evento.getLongitudineEvento(), evento.getLatitudineEvento(), evento.getLongitudineEvento(), evento.getTitoloEvento());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });
        textViewDoveCitta = findViewById(R.id.textViewDoveCittaEvento);
        textViewDoveInd = findViewById(R.id.textViewDoveIndirizzoEvento);
        textViewDoveTelefono = findViewById(R.id.textViewDoveTelefonoEvento);
        textViewDoveEmail = findViewById(R.id.textViewDoveEmailEvento);

        linearLayoutPartecipanti = findViewById(R.id.linearLayoutPartecipanti);
        textViewMaxPartecipanti = findViewById(R.id.textViewMaxEvento);
        textViewDisponibili = findViewById(R.id.textViewDisponibiliEvento);

        linearLayoutIscritti = findViewById(R.id.linearLayoutIscritti);
        mListenerUtenti = this;
        adapterUtenti = new MyUtenteRecyclerViewAdapter(evento.getIscrittiEvento(), mListenerUtenti, this);
        RecyclerView recyclerViewIscritti = findViewById(R.id.listIscrittiEvento);
        recyclerViewIscritti.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewIscritti.setAdapter(adapterUtenti);

        linearLayoutBadge = findViewById(R.id.linearLayoutBadge);
        textViewTitoloBadge = findViewById(R.id.textViewTitoloBadge);
        imageViewBadgeEvento = findViewById(R.id.imageViewBadgeEvento);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapEvento);
        mapFragment.getMapAsync(this);

        // inner = imageViewEvento;
        scrollViewEvento.setImageView(imageViewEvento);

        scrollViewEvento.setOnDetectScrollListener(new ExtendedScrollView.OnDetectScrollListener() {
            @Override
            public void onUpScrolling() {
                int scrollY = scrollViewEvento.getScrollY();

                if (scrollY > relativeLayoutWithTitolo.getY() && initPosition) {
                    Animations.moveViewToFinalPosition(frameLayoutWithTitoloInAlto, -initYTitleViewInAlto);
                    Animations.moveViewToFinalPosition(buttonAcquista, -initYButtonAcquista);
                    initPosition = false;
                }

                if (scrollY + heightScreen >= textViewTesto.getY() + altezzaButtonAcquistaMargine && textViewTesto.getVisibility() == View.INVISIBLE) {
                    textViewTesto.startAnimation(animationUp);
                    textViewTesto.setVisibility(View.VISIBLE);
                }

                if (scrollY + heightScreen >= linearLayoutLocation.getY() + altezzaButtonAcquistaMargine && linearLayoutLocation.getVisibility() == View.INVISIBLE) {
                    linearLayoutLocation.startAnimation(animationUp);
                    linearLayoutLocation.setVisibility(View.VISIBLE);
                }

                if (scrollY + heightScreen >= linearLayoutVini.getY() + altezzaButtonAcquistaMargine && linearLayoutVini.getVisibility() == View.INVISIBLE) {
                    linearLayoutVini.startAnimation(animationUp);
                    linearLayoutVini.setVisibility(View.VISIBLE);
                }

                if (scrollY + heightScreen >= linearLayoutDove.getY() + altezzaButtonAcquistaMargine && linearLayoutDove.getVisibility() == View.INVISIBLE) {
                    linearLayoutDove.startAnimation(animationUp);
                    linearLayoutDove.setVisibility(View.VISIBLE);
                }

                if (scrollY + heightScreen >= linearLayoutPartecipanti.getY() + altezzaButtonAcquistaMargine && linearLayoutPartecipanti.getVisibility() == View.INVISIBLE) {
                    linearLayoutPartecipanti.startAnimation(animationUp);
                    linearLayoutPartecipanti.setVisibility(View.VISIBLE);
                }

                if (scrollY + heightScreen >= linearLayoutIscritti.getY() + altezzaButtonAcquistaMargine && linearLayoutIscritti.getVisibility() == View.INVISIBLE) {
                    linearLayoutIscritti.startAnimation(animationUp);
                    linearLayoutIscritti.setVisibility(View.VISIBLE);
                }

                if (scrollY + heightScreen >= linearLayoutBadge.getY() + altezzaButtonAcquistaMargine && linearLayoutBadge.getVisibility() == View.INVISIBLE) {
                    linearLayoutBadge.startAnimation(animationUp);
                    linearLayoutBadge.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onDownScrolling() {
                int scrollY = scrollViewEvento.getScrollY();
                if (scrollY <= relativeLayoutWithTitolo.getY() && !initPosition) {
                    Animations.moveViewToInitPosition(frameLayoutWithTitoloInAlto, initYTitleViewInAlto);
                    Animations.moveViewToInitPosition(buttonAcquista, initYButtonAcquista);
                    initPosition = true;
                }

                if (scrollY + heightScreen < textViewTesto.getY() + altezzaButtonAcquistaMargine + 40 && textViewTesto.getVisibility() == View.VISIBLE) {
                    textViewTesto.startAnimation(animationDown);
                    textViewTesto.setVisibility(View.INVISIBLE);

                }

                if (scrollY + heightScreen < linearLayoutLocation.getY() + altezzaButtonAcquistaMargine + 40 && linearLayoutLocation.getVisibility() == View.VISIBLE) {
                    linearLayoutLocation.startAnimation(animationDown);
                    linearLayoutLocation.setVisibility(View.INVISIBLE);
                }

                if (scrollY + heightScreen < linearLayoutVini.getY() + altezzaButtonAcquistaMargine + 40 && linearLayoutVini.getVisibility() == View.VISIBLE) {
                    linearLayoutVini.startAnimation(animationDown);
                    linearLayoutVini.setVisibility(View.INVISIBLE);
                }

                if (scrollY + heightScreen < linearLayoutDove.getY() + altezzaButtonAcquistaMargine + 40 && linearLayoutDove.getVisibility() == View.VISIBLE) {
                    linearLayoutDove.startAnimation(animationDown);
                    linearLayoutDove.setVisibility(View.INVISIBLE);
                }

                if (scrollY + heightScreen < linearLayoutPartecipanti.getY() + altezzaButtonAcquistaMargine + 40 && linearLayoutPartecipanti.getVisibility() == View.VISIBLE) {
                    linearLayoutPartecipanti.startAnimation(animationDown);
                    linearLayoutPartecipanti.setVisibility(View.INVISIBLE);
                }

                if (scrollY + heightScreen < linearLayoutIscritti.getY() + altezzaButtonAcquistaMargine + 40 && linearLayoutIscritti.getVisibility() == View.VISIBLE) {
                    linearLayoutIscritti.startAnimation(animationDown);
                    linearLayoutIscritti.setVisibility(View.INVISIBLE);
                }

                if (scrollY + heightScreen < linearLayoutBadge.getY() + altezzaButtonAcquistaMargine + 40 && linearLayoutBadge.getVisibility() == View.VISIBLE) {
                    linearLayoutBadge.startAnimation(animationDown);
                    linearLayoutBadge.setVisibility(View.INVISIBLE);
                }
            }
        });

        setupWindowAnimations();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = data.getString(MainActivity.TRANSITION_IM);
            String cittaTransitionName = data.getString(MainActivity.TRANSITION_CITTA);
            String titoloTransitionName = data.getString(MainActivity.TRANSITION_TITOLO);
            String dataTransitionName = data.getString(MainActivity.TRANSITION_DATA);

            String pinTransitionName = data.getString(MainActivity.TRANSITION_PIN);
            String prezzoTransitionName = data.getString(MainActivity.TRANSITION_PREZZO);
            String whiteTransitionName = data.getString(MainActivity.TRANSITION_WHITE_V);
            String temaTransitionName = data.getString(MainActivity.TRANSITION_TEMA);

            imageViewEvento.setTransitionName(imageTransitionName);
            textViewCitta.setTransitionName(cittaTransitionName);
            textViewTitolo.setTransitionName(titoloTransitionName);
            textViewData.setTransitionName(dataTransitionName);
            textViewPrezzo.setTransitionName(prezzoTransitionName);
            relativeLayoutWithTitolo.setTransitionName(whiteTransitionName);
            imagePin.setTransitionName(pinTransitionName);
            textViewTema.setTransitionName(temaTransitionName);
        }

        relLayoutPB = findViewById(R.id.relativeLayoutPB);
        relLayoutPB.setVisibility(View.INVISIBLE);

        relLayoutPB.setTransitionGroup(false);
        setViewToEvento();
        new GetEvento().execute();

    }

    private void changeStatoPreferitoEvento() {
        ChangeStatoEvento cse = new ChangeStatoEvento(evento.getStatoEvento(), evento.getStatoPreferitoEvento(), 0, false);//evento.getStatoEvento=oldStato
        if (evento.getStatoPreferitoEvento().equals(Evento.VALUES_STATO_OTHER)) {
            evento.setStatoPreferitoEvento(Evento.VALUES_STATO_PREFERITO);
        } else {
            evento.setStatoPreferitoEvento(Evento.VALUES_STATO_OTHER);
        }
        cse.execute();
        setButtonsAcquistaPreferito();
    }

    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(500);
        getWindow().setEnterTransition(fade);
    }

    private void createListVini() {
        List<Azienda> aziendeFornitrici = evento.getAziendeFornitriceEvento();
        if (listVini == null) {
            listVini = new ArrayList<>();
        } else {
            listVini.clear();
        }
        for (int i = 0; i < aziendeFornitrici.size(); i++) {
            listVini.add(aziendeFornitrici.get(i));
            List<ListItem> viniEvento = aziendeFornitrici.get(i).getViniAzienda();
            int size = viniEvento.size() > 3 ? 3 : viniEvento.size();
            for (int j = 0; j < size; j++) {
                listVini.add(viniEvento.get(j));
            }
        }
        // listVini.add(evento.getAziendaFornitriceEvento());
       /* List<ListItem> viniEvento = evento.getAziendaFornitriceEvento().getViniAzienda();
        int size = viniEvento.size() > 3 ? 3 : viniEvento.size();
        for (int i = 0; i < size; i++) {
            listVini.add(viniEvento.get(i));
        }*/


    }

    private void setViewToEvento() {

        if (evento.eventoAcquistabile()) {
            buttonAcquista.setVisibility(View.VISIBLE);
        } else {
            buttonAcquista.setVisibility(View.INVISIBLE);
        }

        textViewTitleView.setText(evento.getTitoloEvento());
        Picasso.with(this).load(Uri.parse(evento.getUrlFotoEvento())).noFade().error(R.drawable.placeholder).placeholder(R.drawable.placeholder).fit()
                .centerCrop().into(imageViewEvento, new Callback() {
            @Override
            public void onSuccess() {
                supportStartPostponedEnterTransition();
            }

            @Override
            public void onError() {
                supportStartPostponedEnterTransition();
            }
        });

        textViewCitta.setText(evento.getCittaEvento());
        textViewTitolo.setText(evento.getTitoloEvento());
        textViewData.setText(evento.getDataStringEvento());
        textViewPrezzo.setText(evento.getPrezzoStringEvento());

        Spannable str = new SpannableString("Tema: " + evento.getTemaEvento());
        str.setSpan(new StyleSpan(Typeface.BOLD), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewTema.setText(str);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewTesto.setText(Html.fromHtml(evento.getTestoEvento(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            textViewTesto.setText(Html.fromHtml(evento.getTestoEvento()));
        }

        setButtonsAcquistaPreferito();
        setViewDove();
        setViewPartecipanti();
        setViewBadge();
        setViewsToInvisible();
    }

    private void setViewsToInvisible() {
        if (textViewTesto.getY() > heightScreen)
            textViewTesto.setVisibility(View.INVISIBLE);

        if (evento.getAziendaOspitanteEvento().getIdAzienda() == null || evento.getAziendaOspitanteEvento().getIdAzienda() == "") {
            linearLayoutLocation.setVisibility(View.GONE);
        } else {
            if (linearLayoutLocation.getY() > heightScreen)
                linearLayoutLocation.setVisibility(View.INVISIBLE);
            else {
                linearLayoutLocation.setVisibility(View.VISIBLE);
            }
        }

        if (evento.getAziendeFornitriceEvento() != null && evento.getAziendeFornitriceEvento().size() == 0) {
            linearLayoutVini.setVisibility(View.GONE);
        } else {
            if (linearLayoutVini.getY() > heightScreen)
                linearLayoutVini.setVisibility(View.INVISIBLE);
            else
                linearLayoutVini.setVisibility(View.VISIBLE);
        }
        if (linearLayoutDove.getY() > heightScreen)
            linearLayoutDove.setVisibility(View.INVISIBLE);
        else
            linearLayoutDove.setVisibility(View.VISIBLE);

        linearLayoutPartecipanti.setVisibility(View.INVISIBLE);
        if (evento.getIscrittiEvento().size() == 0) {
            linearLayoutIscritti.setVisibility(View.GONE);
        } else {
            linearLayoutIscritti.setVisibility(View.INVISIBLE);
        }

        if (evento.getBadgeEvento().getIdBadge() == "") {
            linearLayoutBadge.setVisibility(View.GONE);

        } else {
            linearLayoutBadge.setVisibility(View.INVISIBLE);
        }
    }


    private int setInitPositionOfTitleView() {
        int altezzaFrameLayoutWithTitoloInAlto = (int) getResources().getDimension(R.dimen.title_view_height);
        int dpValue = 20; // margin in dips
        float d = this.getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);
        int initYdiTitleView = -(altezzaFrameLayoutWithTitoloInAlto + margin);
        frameLayoutWithTitoloInAlto.setY(initYdiTitleView);
        return initYdiTitleView;
    }

    private int setInitPositionOfButtonAcquista() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int initYdiButtonAcquista = size.y;
        buttonAcquista.setY(initYdiButtonAcquista);
        return initYdiButtonAcquista;
    }

    private void setViewDove() {
        if (markerEv != null) {
            LatLng evLatLng = new LatLng(evento.getLatitudineEvento(), evento.getLongitudineEvento());
            markerEv.setPosition(evLatLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(evLatLng, 17));
        }
        textViewDoveCitta.setText(evento.getCittaEvento());
        textViewDoveInd.setText(evento.getIndirizzoEvento());
        textViewDoveTelefono.setText(evento.getTelefonoEvento());
        textViewDoveEmail.setText(evento.getEmailEvento());
    }

    private void setViewPartecipanti() {
        if (evento.getNumMaxPartecipantiEvento() == 0) {
            textViewMaxPartecipanti.setText("Max: illimitati");
            textViewDisponibili.setText("Disponibili: illimitati");
        } else {
            textViewMaxPartecipanti.setText("Max " + evento.getNumMaxPartecipantiEvento());
            textViewDisponibili.setText("Disponibili ancora " + evento.getNumPostiDisponibiliEvento() + " posti");
        }
    }

    private void setViewBadge() {
        textViewTitoloBadge.setText(evento.getBadgeEvento().getNomeBadge());
        Picasso.with(this).load(Uri.parse(evento.getBadgeEvento().getUrlLogoBadge())).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).fit()
                .centerCrop().into(imageViewBadgeEvento);

    }

    private void setButtonsAcquistaPreferito() {
        if (evento.getStatoPreferitoEvento().equals(Evento.VALUES_STATO_OTHER) || evento.getStatoPreferitoEvento() == "") {
            buttonPreferito.setImageDrawable(this.getResources().getDrawable(R.drawable.button_pref_off, null));
        } else {
            buttonPreferito.setImageDrawable(this.getResources().getDrawable(R.drawable.button_pref_on, null));
        }

        buttonPreferito.setEnabled(true);//!evento.getStatoEvento().equals(Evento.VALUES_STATO_ACQUISTATO));
        //    buttonAcquista.setEnabled(evento.getStatoEvento().equals(Evento.VALUES_STATO_OTHER));

        String str = "";
        Spannable strSpan;

        if (evento.getAcquistabileEvento() == Evento.VALUES_STATO_ACQUISTABILE_SI) {
            str = "Acquista";
        } else {
            str = "Prenota";
        }
        strSpan = new SpannableString("" + str + " (" + evento.getPrezzoStringEvento() + ")");
        strSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        buttonAcquista.setText(strSpan);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        int height = 84;
        int width = 66;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.pin_big, null);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        LatLng evLatLng = new LatLng(evento.getLatitudineEvento(), evento.getLongitudineEvento());
        MarkerOptions marker = new MarkerOptions().position(evLatLng).title(evento.getTitoloEvento());
        marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        markerEv = mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(evLatLng, 17));
    }

    @Override
    public void onItemClickUtente(Utente utente) {
        Intent intent = new Intent(this, UtenteDetailsActivity.class);
        intent.putExtra("utente", utente);
        startActivity(intent);
    }

    @Override
    public void OnItemClickAzienda(Azienda azienda) {
        Intent intent = new Intent(this, AziendaDetailsActivity.class);
        intent.putExtra("azienda", azienda);
        startActivity(intent);
    }

    private void createAlertDialogWithPicker() {
        if (evento.getNumPostiDisponibiliEvento() > 0) {
            final NumberPicker numberPicker = new NumberPicker(this);
            if (evento.getNumPostiDisponibiliEvento() == 0) {
                numberPicker.setMaxValue(evento.getNumMaxPartecipantiEvento());
            } else {
                numberPicker.setMaxValue(evento.getNumPostiDisponibiliEvento());
            }
            String strPrenotaAcquista = "prenotare";
            String strPagherai = "(pagherai direttamente all'evento)";
            String inCorso = "Prenotazione in corso...";
            String strStato = Evento.VALUES_STATO_PRENOTATO;

            if (evento.getAcquistabileEvento() == Evento.VALUES_STATO_ACQUISTABILE_SI) {
                strPrenotaAcquista = "acquistare";
                strPagherai = "";
                strStato = Evento.VALUES_STATO_ACQUISTATO;
                inCorso = "Acquisto in corso...";
            }

            numberPicker.setMinValue(1);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("");
            builder.setMessage("Quanti biglietti desideri " + strPrenotaAcquista + "?");
            builder.setView(numberPicker);

            final String finalStrPrenotaAcquista = strPrenotaAcquista;
            final String finalStrPagherai = strPagherai;
            final String finalStrStato = strStato;
            final String finalInCorso = inCorso;

            builder.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EventoDetailsActivity.this);
                    builder.setTitle("");
                    builder.setMessage("Confermi di voler " + finalStrPrenotaAcquista + " " + numberPicker.getValue() +
                            " posti per questo evento al prezzo di € " + String.format("%.2f", numberPicker.getValue() * evento.getPrezzoEvento()) +
                            " " + finalStrPagherai + "?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Sì", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ChangeStatoEvento cse = new ChangeStatoEvento(evento.getStatoEvento(), evento.getStatoPreferitoEvento(), numberPicker.getValue(), true);
                            evento.setStatoEvento(finalStrStato);
                            evento.setStatoPreferitoEvento(Evento.VALUES_STATO_PREFERITO);
                            showWaitDialog(finalInCorso);
                            cse.execute();
                            setButtonsAcquistaPreferito();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (evento.getTitoloEvento().equals("")) {
            mFirebaseAnalytics.setCurrentScreen(this, TAG, null);
        } else {
            mFirebaseAnalytics.setCurrentScreen(this, evento.getTitoloEvento(), null);
        }
    }


    @Override
    public void onListFragmentInteraction(Vino v) {
        Intent intent = new Intent(this, VinoDetailsActivity.class);
        intent.putExtra("vino", v);
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(Azienda az) {
        if (az.getIdAzienda() != "") {
            Intent intent = new Intent(this, AziendaDetailsActivity.class);
            intent.putExtra("azienda", az);
            startActivity(intent);
        }
    }

    @Override
    public void onButtonMostraClick() {
        Intent intent = new Intent(this, ViniActivity.class);
        intent.putExtra("evento", evento);
        startActivity(intent);
    }

    private void showWaitDialog(String message) {
        closeWaitDialog();
        waitDialog = new ProgressDialog(this);
        waitDialog.setTitle(message);
        waitDialog.setCancelable(false);
        waitDialog.show();
    }

    private void closeWaitDialog() {
        try {
            waitDialog.dismiss();
        } catch (Exception e) {
            //
        }
    }


    private class GetEvento extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            relLayoutPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCallGetEvento(evento, idUtente);

            //   Log.d(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jsonEsito = jsonObj.getJSONObject("esito");
                    final Esito es = new Esito(jsonEsito);
                    if (es.getCodice() != Esito.ERROR_CODE) {
                        evento = new Evento(jsonObj.getJSONObject("evento"));
                    } else {
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
                String mess = "Errore di comunicazione con il server.";
                if (!Utility.isNetworkAvailable(EventoDetailsActivity.this)) {
                    mess = "Assenza di collegamento, controllare la connessione dati.";
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
            setViewToEvento();
            createListVini();
            adapterVini.updateVini(listVini);
            aziendeOspitante.clear();
            aziendeOspitante.add(evento.getAziendaOspitanteEvento());
            adapterAzienda.updateAziende(aziendeOspitante);
            adapterUtenti.updateUtenti(evento.getIscrittiEvento());
            relLayoutPB.setVisibility(View.INVISIBLE);
        }
    }

    private class ChangeStatoEvento extends AsyncTask<Void, Void, Void> {
        private String oldAcqStato;
        private String oldPrefStato;
        private boolean acquisto;
        private int numPartecipanti;

        public ChangeStatoEvento(String oldAcqStato, String oldPrefStato, int numPartecipanti, boolean acquisto) {
            super();
            this.oldAcqStato = oldAcqStato;
            this.oldPrefStato = oldPrefStato;
            this.acquisto = acquisto;
            this.numPartecipanti = numPartecipanti;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (acquisto) {
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCallChangeStatoEvento(evento, idUtente, numPartecipanti);
            // Log.d(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    final Esito es = new Esito(jsonObj.getJSONObject("esito"));
                    if (es.getCodice() == Esito.ERROR_CODE) {
                        evento.setStatoEvento(oldAcqStato);
                        evento.setStatoPreferitoEvento(oldPrefStato);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setButtonsAcquistaPreferito();
                                Toast.makeText(getApplicationContext(),
                                        es.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

                    } else {
                        if (acquisto) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String prenotazione = "prenotazione";
                                    String prenotato = "prenotato";
                                    if (evento.getAcquistabileEvento() == Evento.VALUES_STATO_ACQUISTABILE_SI) {
                                        prenotazione = "acquisto";
                                        prenotato = "acquistato";
                                    }
                                    AlertDialog.Builder builder = new AlertDialog.Builder(EventoDetailsActivity.this);
                                    builder.setTitle("Conferma " + prenotazione)
                                            .setMessage("Hai " + prenotato + " " + numPartecipanti + " posti per questo evento. Grazie!")
                                            .setCancelable(true);
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            });
                        }
                    }
                } catch (final JSONException e) {
                    evento.setStatoEvento(oldAcqStato);
                    evento.setStatoPreferitoEvento(oldPrefStato);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setButtonsAcquistaPreferito();
                            Toast.makeText(getApplicationContext(),
                                    "Errore di comunicazione con il server: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                evento.setStatoEvento(oldAcqStato);
                evento.setStatoPreferitoEvento(oldPrefStato);
                String mess = "Errore di comunicazione con il server.";
                if (!Utility.isNetworkAvailable(EventoDetailsActivity.this)) {
                    mess = "Assenza di collegamento, controllare la connessione dati.";
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
            if (acquisto) {
                closeWaitDialog();
            }
        }
    }


}
