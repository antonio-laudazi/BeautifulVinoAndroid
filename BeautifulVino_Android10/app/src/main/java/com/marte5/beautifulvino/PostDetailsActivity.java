package com.marte5.beautifulvino;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.marte5.beautifulvino.Model.Feed;
import com.marte5.beautifulvino.Model.HttpHandler;
import com.marte5.beautifulvino.Model.SharedPreferencesManager;
import com.marte5.beautifulvino.Model.Vino;
import com.marte5.beautifulvino.Utility.Animations;
import com.marte5.beautifulvino.Utility.ExtendedScrollView;
import com.marte5.beautifulvino.Utility.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class PostDetailsActivity extends AppCompatActivity {

    private String TAG = PostDetailsActivity.class.getSimpleName();
    private Feed feed;
    private ExtendedScrollView scrollViewPost;

    private FrameLayout frameLayoutWithTitoloInAlto;
    private TextView textViewTitleView;

    private LinearLayout linearLayoutWithTitolo;
    private ImageView imageViewPost;
    private TextView textViewNome;
    private TextView textViewInfo;
    private TextView textViewHeader;
    private TextView textViewSottoHeader;
    private ImageView imageViewHeader;

    private int initYTitleViewInAlto;
    private boolean initPosition;
    private WebView mContentWebView;
    private Button buttonFullscreen;
    private Button buttonConferma;
    private String idUtente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        Bundle data = getIntent().getExtras();
        feed = data.getParcelable("feed");
        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageViewPost = findViewById(R.id.imageViewPost);
        scrollViewPost = findViewById(R.id.scrollViewPost);
        scrollViewPost.setImageView(imageViewPost);

        frameLayoutWithTitoloInAlto = findViewById(R.id.frameLayoutTitleView);
        textViewTitleView = findViewById(R.id.textViewTitleView);
        linearLayoutWithTitolo = findViewById(R.id.linearLayoutNomePost);
        initYTitleViewInAlto = setInitPositionOfTitleView();

        initPosition = true;
        buttonConferma = findViewById(R.id.buttonConfermaPost);
        buttonConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetPuntiEsperienza().execute();
                buttonConferma.setVisibility(View.INVISIBLE);

            }
        });
        scrollViewPost.setOnDetectScrollListener(new ExtendedScrollView.OnDetectScrollListener() {
            @Override
            public void onUpScrolling() {
                int scrollY = scrollViewPost.getScrollY();
                if (scrollY > linearLayoutWithTitolo.getY() && initPosition) {
                    Animations.moveViewToFinalPosition(frameLayoutWithTitoloInAlto, -initYTitleViewInAlto);
                    initPosition = false;
                }
            }

            @Override
            public void onDownScrolling() {
                int scrollY = scrollViewPost.getScrollY();
                if (scrollY <= linearLayoutWithTitolo.getY() && !initPosition) {
                    Animations.moveViewToInitPosition(frameLayoutWithTitoloInAlto, initYTitleViewInAlto);
                    initPosition = true;
                }
            }
        });

        textViewNome = findViewById(R.id.textViewTitoloPost);
        textViewInfo = findViewById(R.id.textViewTestoPost);

        imageViewHeader = findViewById(R.id.imageViewHeader);
        textViewHeader = findViewById(R.id.textViewHeader);
        textViewSottoHeader = findViewById(R.id.textViewSottoHeader);
        RelativeLayout relativeLayoutHeader = findViewById(R.id.relativeLayoutHeader);
        relativeLayoutHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (feed.getTipoEntitaHeader().equals(Feed.VALUES_TIPO_ENTITA_HEADER_VINO)) {
                    Intent intent = new Intent(PostDetailsActivity.this, VinoDetailsActivity.class);
                    Vino vino = new Vino(null);
                    intent.putExtra("vino", vino);
                    startActivity(intent);

                } else if (feed.getTipoEntitaHeader().equals(Feed.VALUES_TIPO_ENTITA_HEADER_EVENTO)) {
                    Intent intent = new Intent(PostDetailsActivity.this, EventoDetailsActivity.class);
                    Evento evento = new Evento(null);
                    intent.putExtra("evento", evento);
                    startActivity(intent);

                } else if (feed.getTipoEntitaHeader().equals(Feed.VALUES_TIPO_ENTITA_HEADER_PROFILO)) {
                    Intent intent = new Intent(PostDetailsActivity.this, UtenteDetailsActivity.class);
                    Evento evento = new Evento(null);
                    intent.putExtra("evento", evento);
                    startActivity(intent);
                } else if (feed.getTipoEntitaHeader().equals(Feed.VALUES_TIPO_ENTITA_HEADER_AZIENDA)) {
                    Intent intent = new Intent(PostDetailsActivity.this, AziendaDetailsActivity.class);
                    Azienda az = new Azienda(null);
                    az.setIdAzienda(feed.getIdEntitaHeader());
                    intent.putExtra("azienda", az);
                    startActivity(intent);
                }
            }
        });
        mContentWebView = findViewById(R.id.webViewVideo);

        buttonFullscreen = findViewById(R.id.buttonfullscreen);
        buttonFullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(feed.getUrlVideoFeed()));
                intent.putExtra("force_fullscreen", true);
                startActivity(intent);
            }
        });
        idUtente = SharedPreferencesManager.getIdUser(PostDetailsActivity.this);

        setViewToPost();
    }

    private void setViewToPost() {

        if (feed.getUrlVideoFeed() != "") {
            mContentWebView.setVisibility(View.VISIBLE);
            buttonFullscreen.setVisibility(View.VISIBLE);
            mContentWebView.setWebViewClient(new WebViewClient());
            mContentWebView.getSettings().setJavaScriptEnabled(true);
            mContentWebView.setHorizontalScrollBarEnabled(false);
            mContentWebView.setVerticalScrollBarEnabled(false);
            mContentWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            mContentWebView.getSettings().setAppCacheEnabled(true);

            mContentWebView.loadDataWithBaseURL(null, String.format("<html> <head> <style type=\"text/css\">@font-face" +
                            "body </style> </head> <body style=\"margin: 0; padding: 0\">" +
                            "<div style=\" width: 100%%; min-height: 315px; height: auto; \">" +
                            "<iframe width=\"100%%\" height=\"100%%\" src= \"%s\" frameborder=\"0\" allow=\"autoplay; encrypted-media\" ></iframe></div></body>" +
                            "</html>",
                    feed.getUrlVideoFeed()), "text/html", "UTF-8", null);
        } else {
            mContentWebView.setVisibility(View.GONE);
            buttonFullscreen.setVisibility(View.GONE);
        }
        textViewTitleView.setText(feed.getTitoloFeed());
        Picasso.with(this).load(Uri.parse(feed.getUrlImmagineFeed())).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).fit()
                .centerCrop().into(imageViewPost);

        textViewNome.setText(feed.getTitoloFeed());
        //    textViewInfo.setText(feed.getTestoFeed());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewInfo.setText(Html.fromHtml(feed.getTestoFeed(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            textViewInfo.setText(Html.fromHtml(feed.getTestoFeed()));
        }
        setHeaderView();
    }


    private void setHeaderView() {

        textViewHeader.setText(feed.getHeaderFeed());
        textViewSottoHeader.setText(feed.getSottoHeaderFeed());

        Picasso.with(this).load(Uri.parse(feed.getUrlImmagineHeaderFeed())).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).fit()
                .centerCrop().into(imageViewHeader);

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

    /*
        private void OnItemClickHeaderView(Vino vino) {
            Intent intent = new Intent(this, VinoDetailsActivity.class);
            intent.putExtra("vino", vino);
            startActivity(intent);
        }*/
    private class GetPuntiEsperienza extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCallGuadagnaPunti(feed.getIdFeed(), idUtente);
            //    Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    final Esito es = new Esito(jsonObj.getJSONObject("esito"));
                    if (es.getCodice() == Esito.ERROR_CODE) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                buttonConferma.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(),
                                        es.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

                    } else if (es.getCodice() == Esito.ERROR_CODE_FEED_LETTO) {
                        if (!PostDetailsActivity.this.isFinishing()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    buttonConferma.setVisibility(View.INVISIBLE);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PostDetailsActivity.this);
                                    builder.setTitle("Attenzione!")
                                            .setMessage(es.getMessage())
                                            .setCancelable(true);
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            });
                        }
                    } else {
                        if (!PostDetailsActivity.this.isFinishing()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    buttonConferma.setVisibility(View.INVISIBLE);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PostDetailsActivity.this);
                                    builder.setTitle("Articolo Letto!")
                                            .setMessage("Bene! La lettura di questo articolo ti fa guadagnare 10 punti esperienza")
                                            .setCancelable(true);
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            });
                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            buttonConferma.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),
                                    "Errore di comunicazione con il server: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                String mess="Errore di comunicazione con il server.";
                if(!Utility.isNetworkAvailable(PostDetailsActivity.this)){
                    mess="Assenza di collegamento, controllare la connessione dati.";
                }
                final String finalMess = mess;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonConferma.setVisibility(View.VISIBLE);
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
