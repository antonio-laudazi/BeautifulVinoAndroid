/*
 * Copyright 2013-2017 Amazon.com,
 * Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Amazon Software License (the "License").
 * You may not use this file except in compliance with the
 * License. A copy of the License is located at
 *
 *      http://aws.amazon.com/asl/
 *
 * or in the "license" file accompanying this file. This file is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, express or implied. See the License
 * for the specific language governing permissions and
 * limitations under the License.
 */

package com.marte5.beautifulvino.RegistrazioneLogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.marte5.beautifulvino.MainActivity;
import com.marte5.beautifulvino.Model.Esito;
import com.marte5.beautifulvino.Model.HttpHandler;
import com.marte5.beautifulvino.Model.SharedPreferencesManager;
import com.marte5.beautifulvino.OnBoarding.BoardingActivity;
import com.marte5.beautifulvino.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrazioneOptActivity extends AppCompatActivity {

    private String TAG = RegistrazioneOptActivity.class.getSimpleName();

    private EditText editTextCitta;
    private EditText editTextBiografia;

    private Button buttonAvanti;
    private ProgressDialog pDialog;
    private ImageView imageViewUt;
    private ImageView imageFreccia;

    private String username;
    private String email;

    int RESULT_LOAD_IMAGE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_opt);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            extras.getString("username", username);
            extras.getString("email", email);
           /* String value = extras.getString("TODO");
            if (value.equals("exit")) {
                onBackPressed();
            }*/
        }
        init();
    }

    private void init() {
        imageViewUt = findViewById(R.id.imageViewRegOpt);
        imageViewUt.setClipToOutline(true);
        imageViewUt.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.placeholder_user));
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                if (editTextCitta.getText().length()>0 || editTextBiografia.getText().length()>0 ){
                    imageFreccia.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.freccia_brown));
                    buttonAvanti.setEnabled(true);
                }else{
                    imageFreccia.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.freccia_yellow));
                    buttonAvanti.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };

        ImageButton buttonGetFoto = findViewById(R.id.buttonFotoRegOpt);
        buttonGetFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        editTextCitta = findViewById(R.id.cittaRegOpt);
        editTextCitta.addTextChangedListener(textWatcher);
        //
        editTextBiografia = findViewById(R.id.biografiaRegOpt);
        editTextBiografia.addTextChangedListener(textWatcher);

        imageFreccia = findViewById(R.id.image_freccia_reg_opt);
        imageFreccia.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.freccia_yellow));

        buttonAvanti = findViewById(R.id.buttonAvantiRegOpt);
        buttonAvanti.setEnabled(false);
        buttonAvanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             new AddAttributesUtenteTask().execute();
            }
        });

        Button  buttonSkip = findViewById(R.id.buttonPiuTardiRegOpt);
        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNextActivity();
            }
        });

        Button buttonPrivacy = findViewById(R.id.buttonPrivacyOpt);
        Spannable str = new SpannableString("cliccando \"Avanti\" indicherai di accettare i Termini d'uso");

        str.setSpan(new StyleSpan(Typeface.BOLD), str.length()-13, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        buttonPrivacy.setText(str);

        buttonPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.beautifulvino.com/privacy-policy/"));
                startActivity(browserIntent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            imageViewUt.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            buttonAvanti.setEnabled(true);
        }
    }

    private void launchUser() {
        Intent userActivity = new Intent(this, MainActivity.class);
        userActivity.putExtra("name", username);
        userActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(userActivity);
    }


    private void launchBoarding() {
        Intent boardingActivity = new Intent(this, BoardingActivity.class);
        // userActivity.putExtra("name", username);
        boardingActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(boardingActivity);
    }

    private void launchNextActivity() {
        if (SharedPreferencesManager.getFirstLaunch(this))
            launchBoarding();
        else
            launchUser();
    }


    public class AddAttributesUtenteTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RegistrazioneOptActivity.this);
            pDialog.setTitle("Registrazione...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCallSendUtente(SharedPreferencesManager.getIdUser(RegistrazioneOptActivity.this),((BitmapDrawable) imageViewUt.getDrawable()).getBitmap(),
                    editTextCitta.getText().toString(), editTextBiografia.getText().toString(), username, email, "");

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jsonEsito = jsonObj.getJSONObject("esito");
                    final Esito es = new Esito(jsonEsito);
                    if (es.getCodice() == Esito.ERROR_CODE)  {
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
            if (pDialog.isShowing())
                pDialog.dismiss();
            launchNextActivity();
        }

    }

}
