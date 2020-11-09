package com.marte5.beautifulvino;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VinoDetailsActivity extends AppCompatActivity implements MyUtenteRecyclerViewAdapter.OnItemClickListener, MyAziendaRecyclerViewAdapter.OnItemClickListener, EventiFragment.OnListFragmentInteractionListener {

    private String TAG = VinoDetailsActivity.class.getSimpleName();

    public MyEventoRecyclerViewAdapter adapterEventi;
    public MyAziendaRecyclerViewAdapter adapterAzienda;
    public MyUtenteRecyclerViewAdapter adapterUtenti;

    private Vino vino;
    private List<Azienda> aziendaList;
    private String idUtente;

    private ExtendedScrollView scrollViewVino;
    private FrameLayout frameLayoutWithTitoloInAlto;
    private TextView textViewTitleView;
    private LinearLayout linearLayoutWithTitolo;

    private ImageView imageViewVino;
    private Button buttonAggiungi;
    private TextView textViewNome;
    private TextView textViewNomeAz;
    private TextView textViewUvaggio;
    private TextView textViewRegione;
    private TextView textViewInBreve;
    private TextView textViewInfo;

    private LinearLayout linearLayoutAzienda;
    private LinearLayout linearLayoutEventi;
    private LinearLayout linearLayoutCarta;

    private Button buttonAcquista;

    private RelativeLayout relLayoutPB;

    private int initYButtonAcquista;
    private int initYTitleViewInAlto;
    private boolean initPosition;
    private int heightScreen;
    private int altezzaButtonAcquistaMargine;

    private MyAziendaRecyclerViewAdapter.OnItemClickListener mListenerAzienda;
    private MyUtenteRecyclerViewAdapter.OnItemClickListener mListenerUtenti;
    private EventiFragment.OnListFragmentInteractionListener mListenerEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vino_details);
        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        idUtente = SharedPreferencesManager.getIdUser(this);
        Bundle data = getIntent().getExtras();
        vino = data.getParcelable("vino");

        heightScreen = Utility.getHeightScreen(this);

        scrollViewVino = findViewById(R.id.scrollViewVino);
        frameLayoutWithTitoloInAlto = findViewById(R.id.frameLayoutTitleView);
        textViewTitleView = findViewById(R.id.textViewTitleView);
        linearLayoutWithTitolo = findViewById(R.id.linearLayoutTitleVino);

        imageViewVino = findViewById(R.id.imageViewVino);

        textViewNome = findViewById(R.id.textViewTitoloVino);
        textViewNomeAz = findViewById(R.id.textViewNomeAzVino);
        textViewUvaggio = findViewById(R.id.textViewUvaggioVino);
        textViewRegione = findViewById(R.id.textViewRegioneVino);
        textViewInBreve = findViewById(R.id.textViewInBreveVino);
        textViewInfo = findViewById(R.id.textViewInfoVino);

        mListenerEvento = this;
        RecyclerView recyclerViewEventi = (RecyclerView) findViewById(R.id.listEventiVino);
        recyclerViewEventi.setLayoutManager(new LinearLayoutManager(this));
        adapterEventi = new MyEventoRecyclerViewAdapter(recyclerViewEventi, vino.getEventiVino(), null, mListenerEvento, true);
        recyclerViewEventi.setAdapter(adapterEventi);

        mListenerUtenti = this;
        adapterUtenti = new MyUtenteRecyclerViewAdapter(vino.getUtentiVino(), mListenerUtenti, this);
        RecyclerView recyclerViewUtenti = (RecyclerView) findViewById(R.id.listCartaVino);
        recyclerViewUtenti.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUtenti.setAdapter(adapterUtenti);

        mListenerAzienda = this;
        aziendaList = new ArrayList<>();
        aziendaList.add(vino.getAziendaVino());
        adapterAzienda = new MyAziendaRecyclerViewAdapter(aziendaList, mListenerAzienda, this);
        RecyclerView recyclerViewAzienda = findViewById(R.id.listAziendaVino);
        recyclerViewAzienda.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAzienda.setAdapter(adapterAzienda);

        linearLayoutAzienda = findViewById(R.id.linearLayoutAziendaVino);
        linearLayoutEventi = findViewById(R.id.linearLayoutEventiVino);
        linearLayoutCarta = findViewById(R.id.linearLayoutNellaCarta);

        buttonAcquista = findViewById(R.id.buttonAcquistaVino);
        buttonAcquista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertDialog("", "Confermi di voler acquistare il vino al prezzo di " + vino.getPrezzoStringVino() + "?");
            }
        });

        buttonAggiungi = findViewById(R.id.buttonAggiungiVino);
        buttonAggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ChangeStatoVino csv = new ChangeStatoVino(vino.getStatoVino());//vino.getStatoVino=oldStato

                if (vino.getStatoVino() == "" || vino.getStatoVino().equals(Vino.VALUES_STATO_OTHER)) {
                    vino.setStatoVino(Vino.VALUES_STATO_AGGIUNTO);
                    csv.execute();
                    setButtonsAggiungiAcquista();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(VinoDetailsActivity.this);
                    builder.setTitle("")
                            .setMessage("Sicuro di voler rimuovere questo vino dalla tua Carta dei Vini?")
                            .setCancelable(true)
                            .setPositiveButton("Sì", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    vino.setStatoVino(Vino.VALUES_STATO_OTHER);
                                    csv.execute();
                                    setButtonsAggiungiAcquista();
                                }
                            })
                            .setNegativeButton("Annulla", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }
        });

        final Animation animationUp = AnimationUtils.loadAnimation(this, R.anim.up_animation);
        final Animation animationDown = AnimationUtils.loadAnimation(this, R.anim.down_animation);

        initYTitleViewInAlto = setInitPositionOfTitleView();
        initYButtonAcquista = setInitPositionOfButtonAcquista();
        initPosition = true;
        altezzaButtonAcquistaMargine = (int) (92 * buttonAcquista.getContext().getResources().getDisplayMetrics().density);//60=altezza button+12=margine+20
        scrollViewVino.setImageView(imageViewVino);

        scrollViewVino.setOnDetectScrollListener(new ExtendedScrollView.OnDetectScrollListener() {
            @Override
            public void onUpScrolling() {
                int scrollY = scrollViewVino.getScrollY();
                if (scrollY > linearLayoutWithTitolo.getY() && initPosition) {
                    Animations.moveViewToFinalPosition(frameLayoutWithTitoloInAlto, -initYTitleViewInAlto);
                    Animations.moveViewToFinalPosition(buttonAcquista, -initYButtonAcquista);
                    initPosition = false;
                }

                if (scrollY + heightScreen >= textViewInfo.getY() + altezzaButtonAcquistaMargine && textViewInfo.getVisibility() == View.INVISIBLE) {
                    textViewInfo.startAnimation(animationUp);
                    textViewInfo.setVisibility(View.VISIBLE);
                }

                if (scrollY + heightScreen >= linearLayoutAzienda.getY() + altezzaButtonAcquistaMargine && linearLayoutAzienda.getVisibility() == View.INVISIBLE) {
                    linearLayoutAzienda.startAnimation(animationUp);
                    linearLayoutAzienda.setVisibility(View.VISIBLE);
                }

                if (scrollY + heightScreen >= linearLayoutEventi.getY() + altezzaButtonAcquistaMargine && linearLayoutEventi.getVisibility() == View.INVISIBLE) {
                    linearLayoutEventi.startAnimation(animationUp);
                    linearLayoutEventi.setVisibility(View.VISIBLE);
                }

                if (scrollY + heightScreen >= linearLayoutCarta.getY() + altezzaButtonAcquistaMargine && linearLayoutCarta.getVisibility() == View.INVISIBLE) {
                    linearLayoutCarta.startAnimation(animationUp);
                    linearLayoutCarta.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onDownScrolling() {
                int scrollY = scrollViewVino.getScrollY();
                if (scrollY <= linearLayoutWithTitolo.getY() && !initPosition) {
                    Animations.moveViewToInitPosition(frameLayoutWithTitoloInAlto, initYTitleViewInAlto);
                    Animations.moveViewToInitPosition(buttonAcquista, initYButtonAcquista);
                    initPosition = true;
                }

                if (scrollY + heightScreen < textViewInfo.getY() + altezzaButtonAcquistaMargine + 40 && textViewInfo.getVisibility() == View.VISIBLE) {
                    textViewInfo.startAnimation(animationDown);
                    textViewInfo.setVisibility(View.INVISIBLE);

                }

                if (scrollY + heightScreen < linearLayoutAzienda.getY() + altezzaButtonAcquistaMargine + 40 && linearLayoutAzienda.getVisibility() == View.VISIBLE) {
                    linearLayoutAzienda.startAnimation(animationDown);
                    linearLayoutAzienda.setVisibility(View.INVISIBLE);
                }

                if (scrollY + heightScreen < linearLayoutEventi.getY() + altezzaButtonAcquistaMargine + 40 && linearLayoutEventi.getVisibility() == View.VISIBLE) {
                    linearLayoutEventi.startAnimation(animationDown);
                    linearLayoutEventi.setVisibility(View.INVISIBLE);
                }

                if (scrollY + heightScreen < linearLayoutCarta.getY() + altezzaButtonAcquistaMargine + 40 && linearLayoutCarta.getVisibility() == View.VISIBLE) {
                    linearLayoutCarta.startAnimation(animationDown);
                    linearLayoutCarta.setVisibility(View.INVISIBLE);
                }
            }
        });

        relLayoutPB = findViewById(R.id.relativeLayoutPB);
        relLayoutPB.setVisibility(View.INVISIBLE);

        setViewToVino();
        new GetVino().execute();
    }

    private void setViewToVino() {
        if (vino.getAcquistabileVino() == Vino.VALUES_STATO_ACQUISTABILE_NO) {
            buttonAcquista.setVisibility(View.INVISIBLE);
        } else {
            buttonAcquista.setVisibility(View.VISIBLE);
        }
        textViewTitleView.setText(vino.getNomeVino());
        Picasso.with(this).load(Uri.parse(vino.getUrlImmagineVino())).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).fit()
                .centerCrop().into(imageViewVino);

        textViewNome.setText(vino.getNomeVino());
        textViewNomeAz.setText(vino.getAziendaVino().getNomeAzienda());

        textViewUvaggio.setText(vino.getUvaggioVino());
        textViewRegione.setText(vino.getRegioneVino());

        Spannable str = new SpannableString("In Breve: " + vino.getInBreveVino());
        str.setSpan(new StyleSpan(Typeface.BOLD), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewInBreve.setText(str);

        // textViewInfo.setText(vino.getInfoVino());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewInfo.setText(Html.fromHtml(vino.getInfoVino(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            textViewInfo.setText(Html.fromHtml(vino.getInfoVino()));
        }
        setButtonsAggiungiAcquista();
        setViewsToInvisible();
    }

    private void setViewsToInvisible() {
        if (textViewInfo.getY() > heightScreen)
            textViewInfo.setVisibility(View.INVISIBLE);
        if (linearLayoutAzienda.getY() > heightScreen)
            linearLayoutAzienda.setVisibility(View.INVISIBLE);
        if (vino.getEventiVino().size() == 0) {
            linearLayoutEventi.setVisibility(View.GONE);
        } else {
            linearLayoutEventi.setVisibility(View.INVISIBLE);
        }
        if (vino.getUtentiVino().size() == 0) {
            linearLayoutCarta.setVisibility(View.GONE);
        } else {
            linearLayoutCarta.setVisibility(View.INVISIBLE);
        }
    }


    private void createAlertDialog(String titolo, String mess) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titolo)
                .setMessage(mess)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ChangeStatoVino cse = new ChangeStatoVino(vino.getStatoVino());
                        vino.setStatoVino(Vino.VALUES_STATO_ACQUISTATO);
                        cse.execute();
                        setButtonsAggiungiAcquista();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

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

    private void setButtonsAggiungiAcquista() {
        if (vino.getStatoVino().equals(Vino.VALUES_STATO_OTHER) || vino.getStatoVino() == "") {
            buttonAggiungi.setBackground(this.getResources().getDrawable(R.drawable.border_yellow, null));
            buttonAggiungi.setText("AGGIUNGI");
        } else {
            buttonAggiungi.setBackground(this.getResources().getDrawable(R.drawable.layer_yellow_corner_text_view, null));
            buttonAggiungi.setText("AGGIUNTO");
        }

        Spannable strSpan;
        String str;
        str = "Acquista";
        strSpan = new SpannableString("" + str + " (" + vino.getPrezzoStringVino() + ")");
        strSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        buttonAcquista.setText(strSpan);
    }

    @Override
    public void onItemClickUtente(Utente utente) {
        Intent intent = new Intent(this, UtenteDetailsActivity.class);
        intent.putExtra("utente", utente);
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(Evento ev, ImageView sharedImageView, TextView c, TextView titolo, ImageView pinImage, TextView prezzo, TextView data, View whiteView, TextView tema) {
        Intent intent = new Intent(this, EventoDetailsActivity.class);
        intent.putExtra("evento", ev);
        startActivity(intent);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.flContainer, fragment);
        transaction.commit();
    }

    @Override
    public void OnItemClickAzienda(Azienda azienda) {
        Intent intent = new Intent(this, AziendaDetailsActivity.class);
        intent.putExtra("azienda", azienda);
        startActivity(intent);
    }

    private class GetVino extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            relLayoutPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCallGetVino(vino.getIdVino(), idUtente);

            // Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jsonEsito = jsonObj.getJSONObject("esito");
                    final Esito es = new Esito(jsonEsito);
                    if (es.getCodice() != Esito.ERROR_CODE) {
                        vino = new Vino(jsonObj.getJSONObject("vino"));
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
                                    "Errore di comunicazione con il server. " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                String mess = "Errore di comunicazione con il server.";
                if (!Utility.isNetworkAvailable(VinoDetailsActivity.this)) {
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
            setViewToVino();
            aziendaList.clear();
            aziendaList.add(vino.getAziendaVino());
            adapterAzienda.updateAziende(aziendaList);
            adapterEventi.updateEventi(vino.getEventiVino());
            adapterUtenti.updateUtenti(vino.getUtentiVino());
            relLayoutPB.setVisibility(View.INVISIBLE);
        }

    }

    private class ChangeStatoVino extends AsyncTask<Void, Void, Void> {
        private String oldStato;

        public ChangeStatoVino(String oldStato) {
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

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCallChangeStatoVino(vino.getIdVino(), vino.getStatoVino(), idUtente);

            // Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jsonEsito = jsonObj.getJSONObject("esito");
                    final Esito es = new Esito(jsonEsito);
                    if (es.getCodice() == Esito.ERROR_CODE) {
                        vino.setStatoVino(oldStato);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setButtonsAggiungiAcquista();
                                Toast.makeText(getApplicationContext(),
                                        es.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                    }
                } catch (final JSONException e) {
                    vino.setStatoVino(oldStato);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setButtonsAggiungiAcquista();
                            Toast.makeText(getApplicationContext(),
                                    "Errore di comunicazione con il server: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                vino.setStatoVino(oldStato);
                String mess = "Errore di comunicazione con il server.";
                if (!Utility.isNetworkAvailable(VinoDetailsActivity.this)) {
                    mess = "Assenza di collegamento, controllare la connessione dati.";
                }
                final String finalMess = mess;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setButtonsAggiungiAcquista();
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


}
