package com.marte5.beautifulvino;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.marte5.beautifulvino.Model.Esito;
import com.marte5.beautifulvino.Model.HttpHandler;
import com.marte5.beautifulvino.Model.Provincia;
import com.marte5.beautifulvino.Model.SharedPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class ProvinceActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private String TAG = MainActivity.class.getSimpleName();

    ListView list;
    ListViewProvinceAdapter provinceAdapter;
    SearchView editsearch;
    ArrayList<Provincia> provinceArrayList;
    private ProgressDialog pDialog;
    private String idUtente;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        idUtente = SharedPreferencesManager.getIdUser(ProvinceActivity.this);
        // Locate the ListView in listview_main.xml
        list = findViewById(R.id.listViewProvince);
        provinceArrayList = new ArrayList<>();
        new GetProvince().execute();
        // Pass results to ListViewAdapter Class
        provinceAdapter = new ListViewProvinceAdapter(this, provinceArrayList);

        // Binds the Adapter to the ListView
        list.setAdapter(provinceAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, provinceArrayList.get(position).getNomeProvincia());
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, bundle);
                SharedPreferencesManager.setProvincia(ProvinceActivity.this,provinceArrayList.get(position));
                Intent intent = new Intent();
                intent.putExtra("provincia", provinceArrayList.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        // Locate the EditText in listview_main.xml
        editsearch = findViewById(R.id.searchViewProvince);
        editsearch.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        provinceAdapter.filter(text);
        return false;
    }

    public class GetProvince extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProvinceActivity.this);
            pDialog.setTitle("Caricamento...");
            pDialog.setMessage("");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCallGetProvince(idUtente);

           // Log.d(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jsonEsito = jsonObj.getJSONObject("esito");
                    final Esito es = new Esito(jsonEsito);
                    if (es.getCodice() != Esito.ERROR_CODE) {
                        JSONArray provinceJson = jsonObj.getJSONArray("province");
                        provinceArrayList.clear();
                        for (int i = 0; i < provinceJson.length(); i++) {
                            JSONObject c = provinceJson.getJSONObject(i);
                            Provincia p = new Provincia(c);
                            provinceArrayList.add(p);
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Errore di comunicazione con il server.",
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
            if (pDialog.isShowing())
                pDialog.dismiss();
            Collections.sort(provinceArrayList);
            provinceAdapter.refreshProvince(provinceArrayList);
        }


    }

}
