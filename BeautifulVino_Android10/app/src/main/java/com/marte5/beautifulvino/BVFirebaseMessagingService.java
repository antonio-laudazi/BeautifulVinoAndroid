package com.marte5.beautifulvino;

import android.util.Log;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.retry.PredefinedRetryPolicies;
import com.amazonaws.services.sns.AmazonSNSAsyncClient;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.marte5.beautifulvino.RegistrazioneLogin.CognitoSyncClientManager;

import java.util.concurrent.Executors;

public class BVFirebaseMessagingService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
       //   Log.d("BVFirebaseMessaging", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }


    private void sendRegistrationToServer(String refreshedToken) {
      //  Log.d("refreshed Token", refreshedToken);
        if (refreshedToken != null && refreshedToken != "") {
            CognitoSyncClientManager.init(this);
            AmazonSNSAsyncClient client = new AmazonSNSAsyncClient(CognitoSyncClientManager.getCredentialsProvider(),
                    new ClientConfiguration().withRetryPolicy(PredefinedRetryPolicies.getDefaultRetryPolicy()),
                    Executors.newSingleThreadScheduledExecutor());
            client.setRegion(Region.getRegion(Regions.fromName("eu-central-1")));
            CreatePlatformEndpointRequest c = new CreatePlatformEndpointRequest();
            c.setToken(refreshedToken);
          //Log.d("refreshed Token", refreshedToken);
            c.setPlatformApplicationArn("arn:aws:sns:eu-central-1:801532940274:app/GCM/BeautifulVinoAndroid");
            client.createPlatformEndpoint(c);
        }
    }


}
