package com.marte5.beautifulvino.RegistrazioneLogin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.marte5.beautifulvino.MainActivity;
import com.marte5.beautifulvino.Model.SharedPreferencesManager;
import com.marte5.beautifulvino.OnBoarding.BoardingActivity;
import com.marte5.beautifulvino.R;

import java.util.Locale;

public class FirstActivity extends AppCompatActivity {

    private String TAG = FirstActivity.class.getSimpleName();
    private String username;
    private String password;
    private ProgressDialog waitDialog;
    private AlertDialog userDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_first);
        Button buttonLogin = findViewById(R.id.buttonLoginFirst);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  showWaitDialog("Login...");
                Intent loginActivity = new Intent(FirstActivity.this, LoginActivity.class);
                //     startActivityForResult(loginActivity, 1);
                startActivity(loginActivity);
            }
        });

        Button buttonRegistrati = findViewById(R.id.buttonRegistratiFirst);
        buttonRegistrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerActivity = new Intent(FirstActivity.this, RegistrazioneActivity.class);
                startActivity(registerActivity);
            }
        });
        AppHelper.init(getApplicationContext());
        CognitoSyncClientManager.init(this);
        findCurrent();
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

    private void findCurrent() {
        if (CognitoSyncClientManager.getCachedIdentityId() != null && SharedPreferencesManager.getIdUser(FirstActivity.this) != "") {
            launchNextActivity();
        } else if (SharedPreferencesManager.getIdUser(FirstActivity.this) != "") {
            CognitoUser user = AppHelper.getPool().getCurrentUser();
            username = user.getUserId();
            if (username != null) {
                AppHelper.setUser(username);
                showWaitDialog("");
                user.getSessionInBackground(authenticationHandler);
            }
        }

    }

    private void getUserAuthentication(AuthenticationContinuation continuation, String username) {
        if (username != null) {
            AppHelper.setUser(username);
        }
        AuthenticationDetails authenticationDetails = new AuthenticationDetails(this.username, password, null);
        continuation.setAuthenticationDetails(authenticationDetails);
        continuation.continueTask();
    }

    private void showWaitDialog(String message) {
        closeWaitDialog();
        waitDialog = new ProgressDialog(this);
        waitDialog.setTitle(message);
        waitDialog.show();
    }

    private void closeWaitDialog() {
        try {
            waitDialog.dismiss();
        } catch (Exception e) {
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
                    //
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }


    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession cognitoUserSession, CognitoDevice device) {
            closeWaitDialog();
            AppHelper.setCurrSession(cognitoUserSession);
            launchNextActivity();
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
            showDialogMessage("Sign-in failed", AppHelper.formatException(e));
        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
        }
    };


}
