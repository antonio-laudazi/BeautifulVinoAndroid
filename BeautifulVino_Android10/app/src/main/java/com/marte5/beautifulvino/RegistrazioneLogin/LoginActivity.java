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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.services.cognitoidentityprovider.model.AuthFlowType;
import com.amazonaws.services.cognitoidentityprovider.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidentityprovider.model.PasswordResetRequiredException;
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

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LoginActivity";

    private EditText editTextPassword;
    private EditText editTextEmail;
    private ImageView imageFreccia;

    private Button buttonAccedi;
    private Button buttonPswDimenticata;
    private AlertDialog userDialog;
    private ProgressDialog waitDialog;
    private String userPsw;
    private CallbackManager callbackManager;

    private String username="";
    private String about="";
    private String email="";
    private String citta="";
    private String urlImage="";
    private Bitmap bmUtente=null;

    private int RC_SIGN_IN = 3;



    SignUpHandler signupHandler = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser cUser, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            //AppHelper.setCurrSession(cognitoUserSession);
            CognitoUser user = AppHelper.getPool().getCurrentUser();
            //
            user.getDetailsInBackground(new GetDetailsHandler() {
                @Override
                public void onSuccess(CognitoUserDetails cognitoUserDetails) {
                    AppHelper.setUserDetails(cognitoUserDetails);
                    SharedPreferencesManager.setIdUser(LoginActivity.this, cognitoUserDetails.getAttributes().getAttributes().get("sub"));
                    launchNextActivity();
                }

                @Override
                public void onFailure(Exception exception) {
                    String exceptionMessage = exception.getMessage();
                }
            });

            closeWaitDialog();
        }

        @Override
        public void onFailure(Exception e) {
            closeWaitDialog();
            showDialogMessage("Login fallito", AppHelper.formatException(e));
        }
    };

    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession cognitoUserSession, CognitoDevice device) {
            AppHelper.setCurrSession(cognitoUserSession);
            CognitoUser user = AppHelper.getPool().getCurrentUser();
            user.getDetailsInBackground(new GetDetailsHandler() {
                @Override
                public void onSuccess(CognitoUserDetails cognitoUserDetails) {
                    AppHelper.setUserDetails(cognitoUserDetails);
                    SharedPreferencesManager.setIdUser(LoginActivity.this, cognitoUserDetails.getAttributes().getAttributes().get("sub"));
                    launchNextActivity();
                }

                @Override
                public void onFailure(Exception exception) {

                }
            });

            closeWaitDialog();
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String username) {
            //closeWaitDialog();
            //Locale.setDefault(Locale.ITALIAN);
            //getUserAuthentication(authenticationContinuation, username);

            String password = userPsw;
            AuthenticationDetails authenticationDetails = new AuthenticationDetails(username, password, null);
            //authenticationDetails.setAuthenticationType("USER_PASSWORD");
            authenticationContinuation.setAuthenticationDetails(authenticationDetails);
            authenticationContinuation.continueTask();
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
        }

        @Override
        public void onFailure(Exception e) {
            closeWaitDialog();
            if(e instanceof PasswordResetRequiredException) {
                showDialogMessage("Necessario reset password. Controlla la mail che ti è stata inviata", AppHelper.formatException(e));
            } else {
                showDialogMessage("Login fallito", AppHelper.formatException(e));
            }


        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
       /*  Bundle extras = getIntent().getExtras();
       if (extras != null) {
            String value = extras.getString("TODO");
            if (value.equals("exit")) {
                onBackPressed();
            }
        }*/
        CognitoSyncClientManager.init(this);
        init();
    }

    private void init() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                if (isEmailValid(String.valueOf(editTextEmail.getText())) && editTextPassword.length() > 0) {
                    imageFreccia.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.freccia_brown));
                    buttonAccedi.setEnabled(true);
                } else {
                    imageFreccia.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.freccia_yellow));
                    buttonAccedi.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        editTextEmail = findViewById(R.id.emailLogin);
        editTextEmail.addTextChangedListener(textWatcher);
        //
        editTextPassword = findViewById(R.id.passwordLogin);
        editTextPassword.addTextChangedListener(textWatcher);

        imageFreccia = findViewById(R.id.image_freccia_login);
        imageFreccia.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.freccia_yellow));

        buttonAccedi = findViewById(R.id.buttonAccediLogin);
        buttonAccedi.setEnabled(false);
        buttonAccedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWaitDialog("Login...");
                username=String.valueOf(editTextEmail.getText());
                userPsw = String.valueOf(editTextPassword.getText());
                CognitoUserAttributes attributes = new CognitoUserAttributes();
                //AppHelper.getPool().signUpInBackground(username,userPsw,attributes, null, signupHandler);
                AppHelper.getPool().getUser(username).getSessionInBackground(authenticationHandler);
            }
        });
        buttonPswDimenticata = findViewById(R.id.buttonPswDimenticata);

        buttonPswDimenticata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = String.valueOf(editTextEmail.getText());
                if (isEmailValid(emailStr)) {
                    showWaitDialog("Richiesta nuova password...");
                    AppHelper.getPool().getUser(emailStr).forgotPasswordInBackground(handler);
                } else {
                    showDialogMessage("Email non è valida", "Inserisci un indirizzo email valido.");
                }
            }
        });

        LoginButton loginButton = findViewById(R.id.button_accedi_fb);
        loginButton.setReadPermissions("email", "public_profile");//"user_about_me" = obsoleta, "user_hometown", "user_location"

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
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

       /* final SignInButton signInButton = findViewById(R.id.button_accedi_gl);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"signInGoogle");

                signInGoogle();
            }
        });*/

        Button buttonGoogle=findViewById(R.id.button_accedi_gl_custom);
        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });
    }

    private void signInGoogle() {
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
            username=account.getDisplayName();
            email=account.getEmail();
            urlImage=account.getPhotoUrl().toString();
            new GetImage().execute();
        } catch (ApiException e) {
            Log.d(TAG, "signInResult:failed code=" + e.toString());
        }
    }


    private void setFacebookSession(AccessToken accessToken) {
    //    Log.i(TAG, "facebook token: " + accessToken.getToken());
        CognitoSyncClientManager.addLogins("graph.facebook.com",
                accessToken.getToken());
    }


    private void setGoogleSession(String token) {
    //    Log.d(TAG, "google token: " + token);
        CognitoSyncClientManager.addLogins("accounts.google.com",
                token);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private void launchPasswordReset() {
        //nel caso in cui la risposta sia "necessario il reset della password"
    }

    private void launchUser() {
        Intent userActivity = new Intent(this, MainActivity.class);
       // userActivity.putExtra("name", username);
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

    private void getUserAuthentication(AuthenticationContinuation continuation, String username) {
        if (username != null) {
            AppHelper.setUser(username);
        }
        AuthenticationDetails authenticationDetails = new AuthenticationDetails(username, userPsw, null);
        continuation.setAuthenticationDetails(authenticationDetails);
        continuation.continueTask();
    }

    ForgotPasswordHandler handler = new ForgotPasswordHandler() {
        @Override
        public void onSuccess() {
            showDialogMessage("Password dimenticata", "La tua richiesta è stata inviata con successo. Controlla la tua email");
        }

        @Override
        public void getResetCode(ForgotPasswordContinuation continuation) {
            closeWaitDialog();
            showDialogMessage("Password dimenticata", "Ti abbiamo mandato una mail per il ripristino della password. Se non la vedi controlla nella casella di Spam");
        }

        /**
         * This is called for all fatal errors encountered during the password reset process
         * Probe {@exception} for cause of this failure.
         * @param exception
         */
        public void onFailure(Exception exception) {
            closeWaitDialog();
            showDialogMessage("Errore", AppHelper.formatException(exception));
        }
    };



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

        private Esito esito;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showWaitDialog("Login...");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String identity = CognitoSyncClientManager.getIdentity();
            SharedPreferencesManager.setIdUser(LoginActivity.this, identity);
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCallSendUtente(identity, bmUtente, citta, about, username, email, "");

           // Log.d(TAG, "Response from url: " + jsonStr);

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
//            showWaitDialog("Login...");
        }

        protected String doInBackground(Void... params) {
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                       //     Log.v("LoginActivity", response.toString());
                            // Application code
                            try {
                                email = JsonParser.getStringValue("email", object);
                                about = JsonParser.getStringValue("about", object);
                                username = JsonParser.getStringValue("name", object);
                             /*   JSONObject location = JsonParser.getJSONObjectValue("location", object);
                                if (location!=null){
                                citta = location.getString("name");
                                }*/
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


