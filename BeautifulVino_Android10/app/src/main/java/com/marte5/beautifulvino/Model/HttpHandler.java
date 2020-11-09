package com.marte5.beautifulvino.Model;

import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;

import com.marte5.beautifulvino.Utility.ImageUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Marte5, Maria Tourbanova on 13/02/18.
 */

public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();
    private static final String stringUrlGet = "https://gmnh1plxq7.execute-api.eu-central-1.amazonaws.com/BeautifulVinoGet";
    private static final String stringUrlPut = "https://4aqjw0dwx0.execute-api.eu-central-1.amazonaws.com/BeautifulVinoPut";
    private static final String stringUrlConnect = "https://ivbkaplee3.execute-api.eu-central-1.amazonaws.com/BeautifulVinoConnect";

    public String makeServiceCallGetEventi(Evento ultimoEv, String idProvincia, String idUtente) {

        String response = null;
        try {

            HttpURLConnection conn = createConnection(stringUrlGet);
            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("functionName", "getEventiGen");
            jsonParam.put("idProvincia", idProvincia);
            jsonParam.put("idUtente", idUtente);
            if (ultimoEv != null) {
                jsonParam.put("idUltimoEvento", ultimoEv.getIdEvento());
                jsonParam.put("dataUltimoEvento", ultimoEv.getDataEvento());
            }

            String jsonString = jsonParam.toString();
           // Log.d(TAG,jsonString);
            DataOutputStream printout = new DataOutputStream(conn.getOutputStream());

            printout.writeBytes(jsonString);
            printout.flush();
            printout.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    public String makeServiceCallGetFeed(Feed ultimoF, String idUtente) {
        String response = null;

        try {
            HttpURLConnection conn = createConnection(stringUrlGet);

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("functionName", "getFeedGen");
            jsonParam.put("idUtente", idUtente);
            if (ultimoF != null) {
                jsonParam.put("idUltimoFeed", ultimoF.getIdFeed());
                jsonParam.put("dataUltimoFeed", ultimoF.getDataFeed());
            }

            String jsonString = jsonParam.toString();
            DataOutputStream printout = new DataOutputStream(conn.getOutputStream());

            printout.writeBytes(jsonString);
            printout.flush();
            printout.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    public String makeServiceCallGetProvince(String idUtente) {
        String response = null;
        try {
            HttpURLConnection conn = createConnection(stringUrlGet);

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("functionName", "getProvinceGen");
            jsonParam.put("idUtente", idUtente);

            String jsonString = jsonParam.toString();
         //   Log.d(TAG,jsonString);
            DataOutputStream printout = new DataOutputStream(conn.getOutputStream());

            printout.writeBytes(jsonString);
            printout.flush();
            printout.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }


    public String makeServiceCallGetEvento(Evento ev, String idUtente) {
        String response = null;
        try {
            HttpURLConnection conn = createConnection(stringUrlGet);

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("functionName", "getEventoGen");
            jsonParam.put("dataEvento", ev.getDataEvento());
            jsonParam.put("idEvento", ev.getIdEvento());
            jsonParam.put("idUtente", idUtente);

            String jsonString = jsonParam.toString();
         //   Log.d(TAG, jsonString);

            DataOutputStream printout = new DataOutputStream(conn.getOutputStream());

            printout.writeBytes(jsonString);
            printout.flush();
            printout.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    public String makeServiceCallChangeStatoEvento(Evento ev, String idUtente, int numPartecipanti) {
        String response = null;
        try {
            HttpURLConnection conn = createConnection(stringUrlConnect);
            int pref=0;
            int acq=0;
            if (ev.getStatoEvento().equals(Evento.VALUES_STATO_ACQUISTATO) || ev.getStatoEvento().equals(Evento.VALUES_STATO_PRENOTATO)){
                acq=1;
            }
            if (ev.getStatoPreferitoEvento().equals(Evento.VALUES_STATO_PREFERITO)) {
                pref=1;
            }
            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("functionName", "connectEventoAUtenteGen1");
            jsonParam.put("statoPreferitoEvento", pref);
            jsonParam.put("statoAcquistatoEvento", acq);
            jsonParam.put("idEvento", ev.getIdEvento());
            jsonParam.put("dataEvento", ev.getDataEvento());
            jsonParam.put("idUtente", idUtente);
            jsonParam.put("numeroPartecipanti", numPartecipanti);

            String jsonString = jsonParam.toString();

            DataOutputStream printout = new DataOutputStream(conn.getOutputStream());

            printout.writeBytes(jsonString);
            printout.flush();
            printout.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    public String makeServiceCallChangeStatoUtente(String stato, String idUtenteToChange, String idUtente) {
        String response = null;
        try {
            HttpURLConnection conn = createConnection(stringUrlConnect);
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonUtente = new JSONObject();
            jsonUtente.put("idUtente", idUtenteToChange);
            jsonArray.put(jsonUtente);
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("functionName", "connectUtentiAUtenteGen");
            jsonParam.put("statoUtente", stato);
            jsonParam.put("utenti", jsonArray);
            jsonParam.put("idUtente", idUtente);

            String jsonString = jsonParam.toString();

            DataOutputStream printout = new DataOutputStream(conn.getOutputStream());

            printout.writeBytes(jsonString);
            printout.flush();
            printout.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    public String makeServiceCallChangeStatoVino(String idVino, String newStato, String idUtente) {
        String response = null;
        try {
            HttpURLConnection conn = createConnection(stringUrlConnect);

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("functionName", "connectViniAUtenteGen");
            jsonParam.put("statoVino", newStato);
            jsonParam.put("idVino", idVino);
            jsonParam.put("idUtente", idUtente);

            String jsonString = jsonParam.toString();

            DataOutputStream printout = new DataOutputStream(conn.getOutputStream());

            printout.writeBytes(jsonString);
            printout.flush();
            printout.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    public String makeServiceCallGuadagnaPunti(String idFeed, String idUtente) {
        String response = null;
        try {
            HttpURLConnection conn = createConnection(stringUrlPut);

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("functionName", "putPuntiEsperienza");
            jsonParam.put("idFeed", idFeed);
            jsonParam.put("idUtente", idUtente);

            String jsonString = jsonParam.toString();
            // Log.d(TAG,jsonString);

            DataOutputStream printout = new DataOutputStream(conn.getOutputStream());

            printout.writeBytes(jsonString);
            printout.flush();
            printout.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }


    public String makeServiceCallGetAzienda(String idAzienda) {
        String response = null;
        try {
            HttpURLConnection conn = createConnection(stringUrlGet);

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("functionName", "getAziendaGen");
            jsonParam.put("idAzienda", idAzienda);
            String jsonString = jsonParam.toString();

            DataOutputStream printout = new DataOutputStream(conn.getOutputStream());

            printout.writeBytes(jsonString);
            printout.flush();
            printout.close();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    public String makeServiceCallGetVino(String idVino, String idUtente) {
        String response = null;
        try {
            HttpURLConnection conn = createConnection(stringUrlGet);

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("functionName", "getVinoGen");
            jsonParam.put("idVino", idVino);
            jsonParam.put("idUtente", idUtente);
            String jsonString = jsonParam.toString();

            DataOutputStream printout = new DataOutputStream(conn.getOutputStream());

            printout.writeBytes(jsonString);
            printout.flush();
            printout.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }


    public String makeServiceCallGetViniEvento(Evento ev) {
        String response = null;
        try {
            HttpURLConnection conn = createConnection(stringUrlGet);

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("functionName", "getViniEventoGen");
            jsonParam.put("idEvento", ev.getIdEvento());
            jsonParam.put("dataEvento", ev.getDataEvento());

            String jsonString = jsonParam.toString();

            DataOutputStream printout = new DataOutputStream(conn.getOutputStream());

            printout.writeBytes(jsonString);
            printout.flush();
            printout.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    public String makeServiceCallGetUtente(String idUtente, String idUtentePadre) {
        String response = null;
        try {
            HttpURLConnection conn = createConnection(stringUrlGet);
            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("functionName", "getUtenteGen");
            jsonParam.put("idUtente", idUtente);
            jsonParam.put("idUtentePadre", idUtentePadre);

            String jsonString = jsonParam.toString();
           // Log.d(TAG,jsonString);
            DataOutputStream printout = new DataOutputStream(conn.getOutputStream());

            printout.writeBytes(jsonString);
            printout.flush();
            printout.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    public String makeServiceCallSendUtente(String idUtente, Bitmap fotoBitmap, String citta, String biografia, String username, String email, String professione) {
        String response = null;
        try {
            HttpURLConnection conn = createConnection(stringUrlPut);

            //Create JSONObject here
            JSONObject jsonUtente = new JSONObject();
            jsonUtente.put("idUtente", idUtente);
            jsonUtente.put("biografiaUtente", biografia);
            jsonUtente.put("usernameUtente", username);
            jsonUtente.put("emailUtente", email);
            jsonUtente.put("cittaUtente", citta);
            jsonUtente.put("professioneUtente", professione);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("functionName", "putUserProfileImageWithUserGen");
            jsonParam.put("utente", jsonUtente);
            String stringRepresentation=ImageUtility.fromBitmapToString(fotoBitmap);
            jsonParam.put("base64Image", stringRepresentation);
            String jsonString = jsonParam.toString();

           // String s = URLEncoder.encode(jsonString, "UTF-8");

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));

            bw.write(jsonString);
            bw.flush();
            bw.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }


    private static HttpURLConnection createConnection(String urlString) throws IOException {
        HttpURLConnection conn = null;
        //SystemClock.sleep(4000);
        URL url = new URL(urlString);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json;  charset=utf-8");
        return conn;
    }


    private String convertStreamToString(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
