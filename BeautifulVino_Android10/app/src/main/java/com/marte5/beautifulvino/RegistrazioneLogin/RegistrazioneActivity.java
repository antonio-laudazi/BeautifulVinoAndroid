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
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.marte5.beautifulvino.MainActivity;
import com.marte5.beautifulvino.Model.Esito;
import com.marte5.beautifulvino.Model.HttpHandler;
import com.marte5.beautifulvino.Model.JsonParser;
import com.marte5.beautifulvino.Model.SharedPreferencesManager;
import com.marte5.beautifulvino.OnBoarding.BoardingActivity;
import com.marte5.beautifulvino.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class RegistrazioneActivity extends AppCompatActivity {
    private final String TAG = "RegistrazioneActivity";

    private EditText editTextNome;
    private EditText editTextPassword;
    private EditText editTextEmail;

    private Button buttonRegistrati;
    private ImageView imageFreccia;
    private AlertDialog userDialog;
    private ProgressDialog waitDialog;
    private boolean socialLogin;
    private String userPasswd;

    private String username = "";
    private String about = "";
    private String email = "";
    private String citta = "";
    private String urlImage = "";
    private Bitmap bmUtente = null;
    private String idUtente = "";


    private CallbackManager callbackManager;
    private int RC_SIGN_IN = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);
        callbackManager = CallbackManager.Factory.create();

       /* Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("TODO");
            if (value.equals("exit")) {
                onBackPressed();
            }
        }*/

        init();
    }

    private void init() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                if (editTextNome.getText().length() > 0 && editTextEmail.getText().length() > 0 && isEmailValid(String.valueOf(editTextEmail.getText())) && editTextPassword.length() > 0) {
                    imageFreccia.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.freccia_brown));
                    buttonRegistrati.setEnabled(true);
                } else {
                    imageFreccia.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.freccia_yellow));
                    buttonRegistrati.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        editTextEmail = findViewById(R.id.emailRegistrazione);
        editTextEmail.addTextChangedListener(textWatcher);
        //
        editTextPassword = findViewById(R.id.passwordRegistrazione);
        editTextPassword.addTextChangedListener(textWatcher);
        //
        editTextNome = findViewById(R.id.nomeRegistrazione);
        editTextNome.addTextChangedListener(textWatcher);

        imageFreccia = findViewById(R.id.image_freccia_registrati);
        imageFreccia.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.freccia_yellow));

        Button buttonPrivacy = findViewById(R.id.buttonPrivacy);
        Spannable str = new SpannableString("cliccando \"Registrati\" indicherai di accettare i Termini d'uso");

        str.setSpan(new StyleSpan(Typeface.BOLD), str.length()-13, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        buttonPrivacy.setText(str);

        buttonPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.beautifulvino.com/privacy-policy/"));
                startActivity(browserIntent);
            }
        });

        buttonRegistrati = findViewById(R.id.buttonRegistrati);
        buttonRegistrati.setEnabled(false);
        buttonRegistrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socialLogin = false;
                CognitoUserAttributes userAttributes = new CognitoUserAttributes();

                username = editTextNome.getText().toString();
                email = editTextEmail.getText().toString();
                userPasswd = editTextPassword.getText().toString();
                ;
                userAttributes.addAttribute("nickname", username);

                showWaitDialog("Registrazione...");
                CognitoUserPool pool = AppHelper.getPool();

                pool.signUpInBackground(email, userPasswd, userAttributes, null, signUpHandler);
            }
        });

        LoginButton loginButton = findViewById(R.id.button_registrati_fb);
        loginButton.setReadPermissions("email", "public_profile");//, "user_hometown", "user_location" richiedono l'analisi dell'app//, "user_about_me" obsoleta

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        socialLogin = true;
                        setFacebookSession(loginResult.getAccessToken());
                        new GetFbName(loginResult).execute();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "facebook error " + exception);
                    }
                });

        Button buttonGoogle = findViewById(R.id.button_registrati_gl_custom);
        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });
    }


    private void signInGoogle() {
        socialLogin = true;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.server_client_id))
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            setGoogleSession(account.getIdToken());
            username = account.getDisplayName();
            email = account.getEmail();
            urlImage = account.getPhotoUrl().toString();
            new GetImage().execute();
        } catch (ApiException e) {
            Log.d(TAG, "signInResult:failed code=" + e.getMessage());
        }
    }


    private void setFacebookSession(AccessToken accessToken) {
        CognitoSyncClientManager.addLogins("graph.facebook.com",
                accessToken.getToken());
    }


    private void setGoogleSession(String token) {
        CognitoSyncClientManager.addLogins("accounts.google.com",
                token);
    }


    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    SignUpHandler signUpHandler = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, boolean signUpConfirmationState,
                              CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            //   closeWaitDialog();
            AppHelper.setUser(username);
            user.getSessionInBackground(authenticationHandler);

            // exit(usernameInput, userPasswd);
        }

        @Override
        public void onFailure(Exception exception) {
            closeWaitDialog();
            showDialogMessage("Registrazione fallita!", AppHelper.formatException(exception));
        }
    };

    private void getUserAuthentication(AuthenticationContinuation continuation, String username) {
        if (username != null) {
            AppHelper.setUser(username);
        }
        AuthenticationDetails authenticationDetails = new AuthenticationDetails(username, userPasswd, null);
        continuation.setAuthenticationDetails(authenticationDetails);
        continuation.continueTask();

    }

    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession cognitoUserSession, CognitoDevice device) {
            AppHelper.setCurrSession(cognitoUserSession);
            CognitoUser user = AppHelper.getPool().getCurrentUser();
            user.getDetailsInBackground(new GetDetailsHandler() {
                @Override
                public void onSuccess(CognitoUserDetails cognitoUserDetails) {
                    AppHelper.setUserDetails(cognitoUserDetails);
                    idUtente = cognitoUserDetails.getAttributes().getAttributes().get("sub");
                    new SalvaUtenteTask().execute();
                }

                @Override
                public void onFailure(Exception exception) {
                }
            });
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String username) {
            closeWaitDialog();
            Locale.setDefault(Locale.ITALIAN);
            getUserAuthentication(authenticationContinuation, username);
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
        }

        @Override
        public void onFailure(Exception e) {
            closeWaitDialog();
            showDialogMessage("Registrazione fallita", AppHelper.formatException(e));
        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
        }
    };

    private void launchOptReg() {
        Intent regOptActivity = new Intent(RegistrazioneActivity.this, RegistrazioneOptActivity.class);
        regOptActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(regOptActivity);
    }

    private void launchUser() {
        Intent userActivity = new Intent(this, MainActivity.class);
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
        if (socialLogin) {
            if (SharedPreferencesManager.getFirstLaunch(this))
                launchBoarding();
            else
                launchUser();
        } else {
            launchOptReg();
        }
    }

    private void showDialogMessage(String title, String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                } catch (Exception e) {
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
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

    public class SalvaUtenteTask extends AsyncTask<Void, Void, Void> {

        private Esito esito;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showWaitDialog("Registrazione...");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            if (socialLogin) {
                String identity = CognitoSyncClientManager.getIdentity();
                SharedPreferencesManager.setIdUser(RegistrazioneActivity.this, identity);
                idUtente = identity;
            }
            String jsonStr = sh.makeServiceCallSendUtente(idUtente, bmUtente, citta, about, username, email, "");

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jsonEsito = jsonObj.getJSONObject("esito");
                    esito = new Esito(jsonEsito);
                    if (esito.getCodice() == Esito.ERROR_CODE) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        esito.getMessage(),
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
            closeWaitDialog();
            if (esito.getCodice() != Esito.ERROR_CODE) {
                SharedPreferencesManager.setIdUser(RegistrazioneActivity.this, idUtente);
                launchNextActivity();
            }
        }
    }

    private class GetFbName extends AsyncTask<Void, Void, String> {
        private final LoginResult loginResult;

        public GetFbName(LoginResult loginResult) {
            this.loginResult = loginResult;
        }

        @Override
        protected void onPreExecute() {
            showWaitDialog("Login...");
        }

        protected String doInBackground(Void... params) {

            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            // Application code
                            try {
                                email = JsonParser.getStringValue("email", object);
                                about = JsonParser.getStringValue("about", object);
                                username = JsonParser.getStringValue("name", object);
                                /* JSONObject location = JsonParser.getJSONObjectValue("location", object);
                               if (location != null)
                                    citta = location.getString("name");*/
                                urlImage = object.getJSONObject("picture").getJSONObject("data").getString("url");// JsonParser.getStringValue("id", object);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, name, email, about, picture.type(large)");//hometown, location
            request.setParameters(parameters);
            GraphResponse graphResponse = request.executeAndWait();
            try {
                return graphResponse.getJSONObject().getString("name");
            } catch (JSONException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            closeWaitDialog();
            new GetImage().execute();
        }
    }

    public class GetImage extends AsyncTask<String, Void, Bitmap> {
        private Esito esito;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showWaitDialog("Login...");
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(urlImage);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                HttpURLConnection.setFollowRedirects(true);
                connection.setInstanceFollowRedirects(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                // Log exception
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap b) {
            closeWaitDialog();
            new SalvaUtenteTask().execute();
        }
    }

}
