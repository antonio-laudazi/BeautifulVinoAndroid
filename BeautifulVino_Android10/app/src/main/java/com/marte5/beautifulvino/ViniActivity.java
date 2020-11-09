package com.marte5.beautifulvino;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.marte5.beautifulvino.Model.Azienda;
import com.marte5.beautifulvino.Model.Esito;
import com.marte5.beautifulvino.Model.Evento;
import com.marte5.beautifulvino.Model.HttpHandler;
import com.marte5.beautifulvino.Model.Vino;
import com.marte5.beautifulvino.Utility.Utility;
import com.marte5.beautifulvino.dummy.ListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViniActivity extends AppCompatActivity implements ViniFragment.OnListFragmentInteractionListener  {

    private String TAG = MainActivity.class.getSimpleName();
    private MyVinoRecyclerViewAdapter adapterVini;
    private List<ListItem> aziende;
    private Evento evento;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_vini);
        Bundle data = getIntent().getExtras();
        evento = data.getParcelable("evento");
        adapterVini = new MyVinoRecyclerViewAdapter(aziende, this, true);
        RecyclerView recyclerView =  findViewById(R.id.listVino);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterVini);
        aziende=new ArrayList<>();
        new GeVini().execute();
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

    private class GeVini extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ViniActivity.this);
            pDialog.setTitle("Caricamento...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Making a request to url and getting response
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCallGetViniEvento(evento);

           // Log.d(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jsonEsito = jsonObj.getJSONObject("esito");
                    final Esito es = new Esito(jsonEsito);
                    if (es.getCodice() != Esito.ERROR_CODE) {
                        JSONArray aziendeJson = jsonObj.getJSONArray("aziende");
                        for (int i = 0; i < aziendeJson.length(); i++) {
                            JSONObject c = aziendeJson.getJSONObject(i);
                            Azienda az = new Azienda(c);
                            aziende.add(az);
                            for (ListItem v : az.getViniAzienda()) {
                                aziende.add(v);
                            }
                        }
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
                String mess="Errore di comunicazione con il server.";
                if(!Utility.isNetworkAvailable(ViniActivity.this)){
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
            adapterVini.updateVini(aziende);
            if (pDialog.isShowing())
                pDialog.dismiss();
        }

    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Vino vino);
    }

}
