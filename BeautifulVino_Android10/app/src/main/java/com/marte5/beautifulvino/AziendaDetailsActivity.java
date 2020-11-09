package com.marte5.beautifulvino;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.marte5.beautifulvino.Model.Azienda;
import com.marte5.beautifulvino.Model.Esito;
import com.marte5.beautifulvino.Model.Evento;
import com.marte5.beautifulvino.Model.HttpHandler;
import com.marte5.beautifulvino.Model.Vino;
import com.marte5.beautifulvino.Utility.Animations;
import com.marte5.beautifulvino.Utility.ExtendedScrollView;
import com.marte5.beautifulvino.Utility.Utility;
import com.marte5.beautifulvino.dummy.ListItem;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AziendaDetailsActivity extends AppCompatActivity implements OnMapReadyCallback, EventiFragment.OnListFragmentInteractionListener, ViniFragment.OnListFragmentInteractionListener {

    private String TAG = AziendaDetailsActivity.class.getSimpleName();
    private Azienda azienda;
    private List<ListItem> listVini;

    private ExtendedScrollView scrollViewAzienda;

    private FrameLayout frameLayoutWithTitoloInAlto;
    private TextView textViewTitleView;

    private LinearLayout linearLayoutWithTitolo;
    private ImageView imageViewAzienda;
    private TextView textViewCittaRegione;
    private TextView textViewNome;
    private TextView textViewInfo;

    private LinearLayout linearLayoutDove;
    private TextView textViewDoveCitta;
    private TextView textViewDoveInd;
    private TextView textViewDoveTelefono;
    private TextView textViewDoveEmail;
    private TextView textViewDoveSito;

    private RelativeLayout relLayoutPB;


    RecyclerView recyclerViewVini;
    private LinearLayout linearLayoutEventi;

    private int initYTitleViewInAlto;
    private boolean initPosition;
    private int heightScreen;
    private int altezzaButtonAcquistaMargine;

    public MyVinoRecyclerViewAdapter adapterVini;
    public MyEventoRecyclerViewAdapter adapterEventi;
    private ViniFragment.OnListFragmentInteractionListener mListenerVino;
    private EventiFragment.OnListFragmentInteractionListener mListenerEvento;

    private GoogleMap mMap;
    private Marker markerAz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azienda_details);
        Bundle data = getIntent().getExtras();
        azienda = data.getParcelable("azienda");
      //  this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        createListVini();

        scrollViewAzienda = findViewById(R.id.scrollViewAzienda);

        frameLayoutWithTitoloInAlto = findViewById(R.id.frameLayoutTitleView);
        textViewTitleView = findViewById(R.id.textViewTitleView);
        linearLayoutWithTitolo = findViewById(R.id.linearLayoutNomeAzienda);

        imageViewAzienda = findViewById(R.id.imageViewAzienda);
        textViewCittaRegione = findViewById(R.id.textViewLuogoAzienda);
        textViewNome = findViewById(R.id.textViewTitoloAzienda);
        textViewInfo = findViewById(R.id.textViewTestoAzienda);

        linearLayoutDove = findViewById(R.id.linearLayoutDoveAzienda);
        textViewDoveCitta = findViewById(R.id.textViewDoveCittaAzienda);
        textViewDoveInd = findViewById(R.id.textViewDoveIndirizzoAzienda);
        textViewDoveTelefono = findViewById(R.id.textViewDoveTelefonoAzienda);
        textViewDoveEmail = findViewById(R.id.textViewDoveEmailAzienda);
        textViewDoveSito = findViewById(R.id.textViewDoveSitoAzienda);

        mListenerVino = this;
        adapterVini = new MyVinoRecyclerViewAdapter(listVini, mListenerVino, true);
        recyclerViewVini = findViewById(R.id.listViniAzienda);
        recyclerViewVini.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVini.setAdapter(adapterVini);

        linearLayoutEventi=findViewById(R.id.linearLayoutEventiAzienda);
        mListenerEvento=this;
        RecyclerView recyclerViewEventi = findViewById(R.id.listProssimiEventi);
        recyclerViewEventi.setLayoutManager(new LinearLayoutManager(this));
        adapterEventi = new MyEventoRecyclerViewAdapter(recyclerViewEventi, azienda.getEventiAzienda(),null, mListenerEvento,true);
        recyclerViewEventi.setAdapter(adapterEventi);

        altezzaButtonAcquistaMargine = (int) (92 * linearLayoutEventi.getContext().getResources().getDisplayMetrics().density);//60=altezza button+12=margine+20
        ImageButton buttonMapEvento = findViewById(R.id.buttonMapAzienda);
        buttonMapEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%f,%f (%s)\"", azienda.getLatitudineAzienda(), azienda.getLongitudineAzienda(), azienda.getLatitudineAzienda(), azienda.getLongitudineAzienda(), azienda.getNomeAzienda());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapAzienda);
        mapFragment.getMapAsync(this);

        final Animation animationUp = AnimationUtils.loadAnimation(this, R.anim.up_animation);
        final Animation animationDown = AnimationUtils.loadAnimation(this, R.anim.down_animation);

        initYTitleViewInAlto = setInitPositionOfTitleView();
        initPosition = true;
        heightScreen = Utility.getHeightScreen(this);
        scrollViewAzienda.setImageView(imageViewAzienda);

        scrollViewAzienda.setOnDetectScrollListener(new ExtendedScrollView.OnDetectScrollListener() {
            @Override
            public void onUpScrolling() {
                int scrollY = scrollViewAzienda.getScrollY();
                if (scrollY > linearLayoutWithTitolo.getY() && initPosition) {
                    Animations.moveViewToFinalPosition(frameLayoutWithTitoloInAlto, -initYTitleViewInAlto);
                    initPosition = false;
                }
                if (scrollY + heightScreen >= linearLayoutDove.getY() + altezzaButtonAcquistaMargine && linearLayoutDove.getVisibility() == View.INVISIBLE) {
                    linearLayoutDove.startAnimation(animationUp);
                    linearLayoutDove.setVisibility(View.VISIBLE);
                }

                if (scrollY + heightScreen >= recyclerViewVini.getY() + altezzaButtonAcquistaMargine && recyclerViewVini.getVisibility() == View.INVISIBLE) {
                    recyclerViewVini.startAnimation(animationUp);
                    recyclerViewVini.setVisibility(View.VISIBLE);
                }

                if (scrollY + heightScreen >= linearLayoutEventi.getY() + altezzaButtonAcquistaMargine && linearLayoutEventi.getVisibility() == View.INVISIBLE) {
                    linearLayoutEventi.startAnimation(animationUp);
                    linearLayoutEventi.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onDownScrolling() {
                int scrollY = scrollViewAzienda.getScrollY();
                if (scrollY <= linearLayoutWithTitolo.getY() && !initPosition) {
                    Animations.moveViewToInitPosition(frameLayoutWithTitoloInAlto, initYTitleViewInAlto);
                    initPosition = true;
                }
                if (scrollY + heightScreen < linearLayoutDove.getY() + altezzaButtonAcquistaMargine+40 && linearLayoutDove.getVisibility() == View.VISIBLE) {
                    linearLayoutDove.startAnimation(animationDown);
                    linearLayoutDove.setVisibility(View.INVISIBLE);
                }

                if (scrollY + heightScreen < recyclerViewVini.getY() + altezzaButtonAcquistaMargine+40 && recyclerViewVini.getVisibility() == View.VISIBLE) {
                    recyclerViewVini.startAnimation(animationDown);
                    recyclerViewVini.setVisibility(View.INVISIBLE);
                }

                if (scrollY + heightScreen < linearLayoutEventi.getY() + altezzaButtonAcquistaMargine+40 && linearLayoutEventi.getVisibility() == View.VISIBLE) {
                    linearLayoutEventi.startAnimation(animationDown);
                    linearLayoutEventi.setVisibility(View.INVISIBLE);
                }
            }
        });
        relLayoutPB=findViewById(R.id.relativeLayoutPB);
        relLayoutPB.setVisibility(View.INVISIBLE);

        setViewToAzienda();
        new GetAzienda().execute();
    }

    private void createListVini() {
        if (listVini == null) {
            listVini = new ArrayList<>();
        } else {
            listVini.clear();
        }
        Azienda aziendaHeader=new Azienda(null);
        aziendaHeader.setNomeAzienda("Lista dei Vini");
        listVini.add(aziendaHeader);
        List<ListItem> viniEvento = azienda.getViniAzienda();
        for (int i=0; i<viniEvento.size(); i++) {
            listVini.add(viniEvento.get(i));
        }
    }

    private void setViewToAzienda(){
        textViewTitleView.setText(azienda.getNomeAzienda());
        Picasso.with(this).load(Uri.parse(azienda.getUrlImmagineAzienda())).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).fit()
                .centerCrop().into(imageViewAzienda);
        if (azienda.getCittaAzienda() != "" && azienda.getRegioneAzienda() != "") {
            textViewCittaRegione.setText(azienda.getCittaAzienda() + ", " + azienda.getRegioneAzienda());
        }
        textViewNome.setText(azienda.getNomeAzienda());
        textViewInfo.setText(azienda.getInfoAzienda());
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewInfo.setText(Html.fromHtml(azienda.getInfoAzienda(), Html.FROM_HTML_MODE_COMPACT));
        }else{
            textViewInfo.setText(Html.fromHtml(azienda.getInfoAzienda()));
        }*/
        setViewDove();
        setViewsToInvisible();
    }

    private void setViewsToInvisible() {
        if (linearLayoutDove.getY()>heightScreen)
            linearLayoutDove.setVisibility(View.INVISIBLE);
        recyclerViewVini.setVisibility(View.INVISIBLE);
        if (azienda.getEventiAzienda().size() == 0) {
            linearLayoutEventi.setVisibility(View.GONE);
        } else {
            linearLayoutEventi.setVisibility(View.INVISIBLE);
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

    private void setViewDove() {

        if (markerAz!=null){
            LatLng azLatLng = new LatLng(azienda.getLatitudineAzienda(), azienda.getLongitudineAzienda());
            markerAz.setPosition(azLatLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(azLatLng, 17));
        }
        textViewDoveCitta.setText(azienda.getCittaAzienda());
        textViewDoveInd.setText(azienda.getIndirizzoAzienda());
        textViewDoveTelefono.setText(azienda.getTelefonoAzienda());
        textViewDoveEmail.setText(azienda.getEmailAzienda());
        textViewDoveSito.setText(azienda.getSitoAzienda());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        int height = 84;
        int width = 66;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.pin_big,null);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        LatLng azLatLng = new LatLng(azienda.getLatitudineAzienda(), azienda.getLongitudineAzienda());
        MarkerOptions marker = new MarkerOptions().position(azLatLng).title(azienda.getNomeAzienda());
        marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        markerAz = mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(azLatLng, 17));
    }

    @Override
    public void onListFragmentInteraction(Vino v) {
        Intent intent = new Intent(this, VinoDetailsActivity.class);
        intent.putExtra("vino", v);
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(Azienda az) {
    }

    @Override
    public void onButtonMostraClick() {

    }
    @Override
    public void onListFragmentInteraction(Evento ev, ImageView sharedImageView, TextView citta, TextView titolo, ImageView pinImage, TextView prezzo, TextView data, View whiteView, TextView tema) {
        Intent intent = new Intent(this, EventoDetailsActivity.class);
        intent.putExtra("evento", ev);
        startActivity(intent);
    }

    private class GetAzienda extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            relLayoutPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCallGetAzienda(azienda.getIdAzienda());

           // Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jsonEsito = jsonObj.getJSONObject("esito");
                    final Esito es = new Esito(jsonEsito);
                    if (es.getCodice() != Esito.ERROR_CODE) {
                        azienda=new Azienda(jsonObj.getJSONObject("azienda"));
                    }else{
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
                String mess="Errore di comunicazione con il server.";
                if(!Utility.isNetworkAvailable(AziendaDetailsActivity.this)){
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
            setViewToAzienda();
            createListVini();
            adapterVini.updateVini(listVini);
            adapterEventi.updateEventi(azienda.getEventiAzienda());
            relLayoutPB.setVisibility(View.INVISIBLE);
        }

    }


}
