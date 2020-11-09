package com.marte5.beautifulvino;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.marte5.beautifulvino.Model.Esito;
import com.marte5.beautifulvino.Model.HttpHandler;
import com.marte5.beautifulvino.Model.SharedPreferencesManager;
import com.marte5.beautifulvino.Model.Utente;
import com.marte5.beautifulvino.RegistrazioneLogin.AppHelper;
import com.marte5.beautifulvino.RegistrazioneLogin.CognitoSyncClientManager;
import com.marte5.beautifulvino.RegistrazioneLogin.FirstActivity;
import com.marte5.beautifulvino.Utility.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class ImpostazioniActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    private String TAG = ImpostazioniActivity.class.getSimpleName();

    private Utente utente;
    private ImageView imageViewUt;
    private TextView textViewNomeUt;
    private TextView textViewProfessione;
    private TextView textViewEmail;
    private TextView textViewCitta;
    private TextView textViewBiografia;
    private Button buttonSalva;
    private AlertDialog userDialog;

    private boolean buttonSalvaPressed;
    private ProgressDialog pDialog;

    private CognitoUser user;

    int RESULT_LOAD_IMAGE = 0;
     private static final int MY_READ_EXTERNAL_STORAGE=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostazioni);

        utente = getIntent().getExtras().getParcelable("utente");
        imageViewUt = findViewById(R.id.imageViewImpostazioni);
        imageViewUt.setClipToOutline(true);

        textViewNomeUt = findViewById(R.id.nomeImpostazioni);
        textViewNomeUt.setOnFocusChangeListener(this);

        textViewProfessione = findViewById(R.id.professioneImpostazioni);
        textViewProfessione.setOnFocusChangeListener(this);

        textViewEmail = findViewById(R.id.emailImpostazioni);
        textViewEmail.setOnFocusChangeListener(this);

        textViewCitta = findViewById(R.id.cittaImpostazioni);
        textViewCitta.setOnFocusChangeListener(this);

        textViewBiografia = findViewById(R.id.biografiaImpostazioni);
        textViewBiografia.setOnFocusChangeListener(this);
        buttonSalvaPressed = false;
        buttonSalva = findViewById(R.id.buttonSalvaImpostazioni);
        buttonSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSalvaPressed = true;
                new SalvaUtenteTask().execute();
            }
        });
        buttonSalva.setVisibility(View.INVISIBLE);

        ImageButton buttonGetFoto = findViewById(R.id.buttonFotoImpostazioni);
        buttonGetFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(ImpostazioniActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ImpostazioniActivity.this,
                            Manifest.permission.READ_CONTACTS)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(ImpostazioniActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_READ_EXTERNAL_STORAGE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
            }
        });

        Button buttonLogout = findViewById(R.id.buttonLogoutImpostazioni);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
                user.signOut();
                LoginManager.getInstance().logOut();
                signOut();
                CognitoSyncClientManager.getCredentialsProvider().clear();
                SharedPreferencesManager.setIdUser(ImpostazioniActivity.this, "");
                exit();
            }
        });
        setViewToUtente();
    }

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.server_client_id))
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "logout google");
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            imageViewUt.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            buttonSalva.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (buttonSalvaPressed)
            setResult(RESULT_OK);
        else
            setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private void setViewToUtente() {
        textViewNomeUt.setText(utente.getUsernameUtente());
        textViewProfessione.setText(utente.getProfessioneUtente());
        textViewEmail.setText(utente.getEmailUtente());
        textViewCitta.setText(utente.getCittaUtente());
        textViewBiografia.setText(utente.getBiografiaUtente());
        Picasso.with(this).load(Uri.parse(utente.getUrlFotoUtente())).error(R.drawable.placeholder_user).placeholder(R.drawable.placeholder_user).fit()
                .centerCrop().into(imageViewUt);

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            buttonSalva.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        String username = AppHelper.getCurrUser();
        user = AppHelper.getPool().getUser(username);
    }

    private void exit() {
        Intent firstActivity = new Intent(this, FirstActivity.class);
        firstActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(firstActivity);
        finish();
    }

    private void showDialogMessage(String title, String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                } catch (Exception e) {
                    //
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }


    public class SalvaUtenteTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ImpostazioniActivity.this);
            pDialog.setTitle("Salvataggio in corso...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCallSendUtente(SharedPreferencesManager.getIdUser(ImpostazioniActivity.this), ((BitmapDrawable) imageViewUt.getDrawable()).getBitmap(),
                    textViewCitta.getText().toString(), textViewBiografia.getText().toString(), textViewNomeUt.getText().toString(), textViewEmail.getText().toString(),
                    textViewProfessione.getText().toString());

           // Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jsonEsito = jsonObj.getJSONObject("esito");
                    final Esito es = new Esito(jsonEsito);
                    if (es.getCodice() == Esito.ERROR_CODE) {
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
                if(!Utility.isNetworkAvailable(ImpostazioniActivity.this)){
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
            if (pDialog.isShowing())
                pDialog.dismiss();
            showDialogMessage("", "Profilo salvato correttamente");

            buttonSalva.setVisibility(View.INVISIBLE);
        }
    }
}
