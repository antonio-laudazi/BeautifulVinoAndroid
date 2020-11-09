package com.marte5.beautifulvino.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marte5, Maria Tourbanova on 12/02/18.
 */

public class Utente implements Parcelable {
   /* private String idUtente;
    private String nomeUtente;
    private String cognomeUtente;
    private int creditiUtente;
    private int esperienzaUtente;
    private String livelloUtente;
    private String biografiaUtente;
    private String urlFotoUtente;
    private int numTotEventi;
    private int numTotBadge;
    private int numTotAziende;
    private Evento[] eventiUtente;
    private Azienda[] aziendeUtente;
    private Badge[] badgeUtente;
    private String statoUtente;//S=seguito, N=other
*/
   private JSONObject jsonObjUtente;

    public static final String VALUES_STATO_SEGUITO = "A";
    public static final String VALUES_STATO_OTHER = "D";

    public static final String KEY_ID_UTENTE = "idUtente";
    public static final String KEY_USERNAME_UTENTE = "usernameUtente";
    public static final String KEY_EMAIL_UTENTE = "emailUtente";
    public static final String KEY_CITTA_UTENTE = "cittaUtente";
    public static final String KEY_PROFESSIONE_UTENTE = "professioneUtente";
  //  public static final String KEY_CREDITI_UTENTE = "creditiUtente";
  //  public static final String KEY_ESPERIENZA_UTENTE = "esperienzaUtente";
    public static final String KEY_LIVELLO_UTENTE = "livelloUtente";
    public static final String KEY_PUNTI_MANCANTI_UTENTE = "puntiMancantiProssimoLivelloUtente";
    public static final String KEY_BIOGRAFIA_UTENTE = "biografiaUtente";
    public static final String KEY_URL_FOTO_UTENTE = "urlFotoUtente";
    public static final String KEY_NUM_TOT_EVENTI_UTENTE = "numTotEventi";
    public static final String KEY_NUM_TOT_BADGE_UTENTE = "numTotBadge";
    public static final String KEY_NUM_TOT_AZIENDE_UTENTE = "numTotAziende";
    public static final String KEY_EVENTI_UTENTE = "eventiUtente";
    public static final String KEY_AZIENDE_UTENTE = "aziendeUtente";
    public static final String KEY_BADGE_UTENTE = "badgeUtente";
    public static final String KEY_STATO_UTENTE = "statoUtente";

    public Utente(JSONObject jsonObjUtente) {
        if (jsonObjUtente == null) {
            this.jsonObjUtente = new JSONObject();
        } else
            this.jsonObjUtente = jsonObjUtente;
    }

    public String getIdUtente(){
        return JsonParser.getStringValue(KEY_ID_UTENTE, this.jsonObjUtente);
    }

    public String getUsernameUtente() {
        return JsonParser.getStringValue(KEY_USERNAME_UTENTE, this.jsonObjUtente);
    }

    public String getEmailUtente() {
        return JsonParser.getStringValue(KEY_EMAIL_UTENTE, this.jsonObjUtente);
    }

    public String getCittaUtente() {
        return JsonParser.getStringValue(KEY_CITTA_UTENTE, this.jsonObjUtente);
    }

    public String getProfessioneUtente() {
        return JsonParser.getStringValue(KEY_PROFESSIONE_UTENTE, this.jsonObjUtente);
    }

   /* public int getCreditiUtente() {
        return JsonParser.getIntValue(KEY_CREDITI_UTENTE, this.jsonObjUtente);
    }

    public int getEsperienzaUtente() {
        return JsonParser.getIntValue(KEY_ESPERIENZA_UTENTE, this.jsonObjUtente);
    }*/

    public String getLivelloUtente() {
        return JsonParser.getStringValue(KEY_LIVELLO_UTENTE, this.jsonObjUtente);
    }

    public String getPuntiMancantiUtente() {
        return JsonParser.getStringValue(KEY_PUNTI_MANCANTI_UTENTE, this.jsonObjUtente);
    }

    public String getBiografiaUtente() {
        return JsonParser.getStringValue(KEY_BIOGRAFIA_UTENTE, this.jsonObjUtente);
    }

    public String getUrlFotoUtente() {
        return JsonParser.getStringValue(KEY_URL_FOTO_UTENTE, this.jsonObjUtente);
    }

    public int getNumTotEventi() {
        return JsonParser.getIntValue(KEY_NUM_TOT_EVENTI_UTENTE, this.jsonObjUtente);
    }

    public int getNumTotBadge() {
        return JsonParser.getIntValue(KEY_NUM_TOT_BADGE_UTENTE, this.jsonObjUtente);
    }

    public int getNumTotAziende() {
        return JsonParser.getIntValue(KEY_NUM_TOT_AZIENDE_UTENTE, this.jsonObjUtente);
    }

    public List<Evento> getEventiUtente() {
        JSONArray jsonArray = JsonParser.getJSONArrayValue(KEY_EVENTI_UTENTE, this.jsonObjUtente);
        List<Evento> eventiUtente = new ArrayList<>();
        if (jsonArray != null) {
            for (int i=0; i<jsonArray.length(); i++){
                try {
                    JSONObject jsonEvento=jsonArray.getJSONObject(i);
                    eventiUtente.add(new Evento(jsonEvento));
                } catch (JSONException e) {
                    return eventiUtente;
                }
            }
        }
        return eventiUtente;
    }


    public List<Badge> getBadgeUtente() {
        JSONArray jsonArray = JsonParser.getJSONArrayValue(KEY_BADGE_UTENTE, this.jsonObjUtente);
        List<Badge> badgeUtente = new ArrayList<>();
        if (jsonArray != null) {
            for (int i=0; i<jsonArray.length(); i++){
                try {
                    JSONObject jsonBadge=jsonArray.getJSONObject(i);
                    badgeUtente.add(new Badge(jsonBadge));
                } catch (JSONException e) {
                    return badgeUtente;
                }
            }
        }
        return badgeUtente;
    }

  public List<Azienda> getAziendeUtente() {
        JSONArray jsonArray = JsonParser.getJSONArrayValue(KEY_AZIENDE_UTENTE, this.jsonObjUtente);
        List<Azienda> aziendeUtente = new ArrayList<>();
        if (jsonArray != null) {
            for (int i=0; i<jsonArray.length(); i++){
                try {
                    JSONObject jsonAzienda=jsonArray.getJSONObject(i);
                    aziendeUtente.add(new Azienda(jsonAzienda));
                } catch (JSONException e) {
                    return aziendeUtente;
                }
            }
        }
        return aziendeUtente;
    }

     /* public List<ListItem> getAziendeUtente() {
        JSONArray jsonArray = JsonParser.getJSONArrayValue(KEY_AZIENDE_UTENTE, this.jsonObjUtente);
        List<ListItem> aziendeUtente = new ArrayList<>();
        if (jsonArray != null) {
            for (int i=0; i<jsonArray.length(); i++){
                try {
                    JSONObject jsonAzienda=jsonArray.getJSONObject(i);
                    aziendeUtente.add(new Azienda(jsonAzienda));
                } catch (JSONException e) {
                    return aziendeUtente;
                }
            }
        }
        return aziendeUtente;
    }
*/

    public String getStatoUtente() {
        return JsonParser.getStringValue(KEY_STATO_UTENTE, this.jsonObjUtente);
    }

    public void setStatoUtente(String stato) {
        try {
            jsonObjUtente.put(KEY_STATO_UTENTE, stato);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*protected Utente(Parcel in) {
        idUtente = in.readString();
        nomeUtente = in.readString();
        cognomeUtente = in.readString();
        creditiUtente = in.readInt();
        esperienzaUtente = in.readInt();
        livelloUtente = in.readString();
        biografiaUtente = in.readString();
        urlFotoUtente = in.readString();
        numTotEventi = in.readInt();
        numTotBadge = in.readInt();
        numTotAziende = in.readInt();
        statoUtente = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idUtente);
        dest.writeString(nomeUtente);
        dest.writeString(cognomeUtente);
        dest.writeInt(creditiUtente);
        dest.writeInt(esperienzaUtente);
        dest.writeString(livelloUtente);
        dest.writeString(biografiaUtente);
        dest.writeString(urlFotoUtente);
        dest.writeInt(numTotEventi);
        dest.writeInt(numTotBadge);
        dest.writeInt(numTotAziende);
        dest.writeString(statoUtente);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Utente> CREATOR = new Parcelable.Creator<Utente>() {
        @Override
        public Utente createFromParcel(Parcel in) {
            return new Utente(in);
        }

        @Override
        public Utente[] newArray(int size) {
            return new Utente[size];
        }
    };
}*/

    protected Utente(Parcel in) {
        try {
            jsonObjUtente = in.readByte() == 0x00 ? null : new JSONObject(in.readString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (jsonObjUtente == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeString(jsonObjUtente.toString());
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Utente> CREATOR = new Parcelable.Creator<Utente>() {
        @Override
        public Utente createFromParcel(Parcel in) {
            return new Utente(in);
        }

        @Override
        public Utente[] newArray(int size) {
            return new Utente[size];
        }
    };
}
