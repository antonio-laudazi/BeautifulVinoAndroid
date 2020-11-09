package com.marte5.beautifulvino.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.marte5.beautifulvino.dummy.ListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marte5, Maria Tourbanova on 12/02/18.
 */

public class Azienda extends ListItem implements Parcelable {
  /*  private String idAzienda;
    private String nomeAzienda;
    private String infoAzienda;//nella preview testo breve
    private String descrizioneAzienda;//testo lungo
    private String regioneAzienda;
    private String cittaAzienda;
    private String indirizzoAzienda;
    private String sitoAzienda;
    private String emailAzienda;
    private String telefonoAzienda;
    private Double latitudineAzienda;
    private Double longitudineAzienda;
    private String urlLogoAzienda;
    private String urlImmagineAzienda;
    private Evento[] eventiAzienda;
    private Vino[] viniAzienda;*/

    private JSONObject jsonObjAzienda;

    public static final String KEY_ID_AZIENDA = "idAzienda";
    public static final String KEY_NOME_AZIENDA = "nomeAzienda";
    public static final String KEY_INFO_AZIENDA = "infoAzienda";
    //public static final String KEY_DESCR_AZIENDA = "descrizioneAzienda";
    public static final String KEY_REGIONE_AZIENDA = "regioneAzienda";
    public static final String KEY_CITTA_AZIENDA = "cittaAzienda";
    public static final String KEY_IND_AZIENDA = "indirizzoAzienda";
    public static final String KEY_SITO_AZIENDA = "sitoAzienda";
    public static final String KEY_EMAIL_AZIENDA = "emailAzienda";
    public static final String KEY_TELEFONO_AZIENDA = "telefonoAzienda";
    public static final String KEY_LAT_AZIENDA = "latitudineAzienda";
    public static final String KEY_LONG_AZIENDA = "longitudineAzienda";
    public static final String KEY_URL_LOGO_AZIENDA = "urlLogoAzienda";
    public static final String KEY_URL_IMG_AZIENDA = "urlImmagineAzienda";
    public static final String KEY_EVENTI_AZIENDA = "eventiAzienda";
    public static final String KEY_VINI_AZIENDA = "viniAzienda";


    public Azienda(JSONObject jsonObjAzienda) {
        if (jsonObjAzienda == null) {
            this.jsonObjAzienda = new JSONObject();
        } else
            this.jsonObjAzienda = jsonObjAzienda;
    }

    public String getIdAzienda() {
        return JsonParser.getStringValue(KEY_ID_AZIENDA, this.jsonObjAzienda);
    }

    public void setIdAzienda(String idAzienda) {
        try {
            jsonObjAzienda.put(KEY_ID_AZIENDA, idAzienda);
            } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getNomeAzienda() {
        return JsonParser.getStringValue(KEY_NOME_AZIENDA, this.jsonObjAzienda);
    }


    public void setNomeAzienda(String nomeAz) {
        try {
            jsonObjAzienda.put(KEY_NOME_AZIENDA, nomeAz);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getInfoAzienda() {
        return JsonParser.getStringValue(KEY_INFO_AZIENDA, this.jsonObjAzienda);
    }


   /* public String getDescrizioneAzienda() {
        return JsonParser.getStringValue(KEY_DESCR_AZIENDA, this.jsonObjAzienda);
    }*/

    public String getRegioneAzienda() {
        return JsonParser.getStringValue(KEY_REGIONE_AZIENDA, this.jsonObjAzienda);
    }

    public String getCittaAzienda() {
        return JsonParser.getStringValue(KEY_CITTA_AZIENDA, this.jsonObjAzienda);
    }

    public String getIndirizzoAzienda() {
        return JsonParser.getStringValue(KEY_IND_AZIENDA, this.jsonObjAzienda);
    }

    public String getSitoAzienda() {
        return JsonParser.getStringValue(KEY_SITO_AZIENDA, this.jsonObjAzienda);
    }

    public String getEmailAzienda() {
        return JsonParser.getStringValue(KEY_EMAIL_AZIENDA, this.jsonObjAzienda);
    }

    public String getTelefonoAzienda() {
        return JsonParser.getStringValue(KEY_TELEFONO_AZIENDA, this.jsonObjAzienda);
    }

    public Double getLatitudineAzienda() {
        return JsonParser.getDoubleValue(KEY_LAT_AZIENDA, this.jsonObjAzienda);
    }

    public Double getLongitudineAzienda() {
        return JsonParser.getDoubleValue(KEY_LONG_AZIENDA, this.jsonObjAzienda);
    }

    public String getUrlLogoAzienda() {
        return JsonParser.getStringValue(KEY_URL_LOGO_AZIENDA, this.jsonObjAzienda);
    }

    public String getUrlImmagineAzienda() {
        return JsonParser.getStringValue(KEY_URL_IMG_AZIENDA, this.jsonObjAzienda);
    }

    public List<Evento> getEventiAzienda() {
        JSONArray jsonArray = JsonParser.getJSONArrayValue(KEY_EVENTI_AZIENDA, this.jsonObjAzienda);
        List<Evento> eventiAz = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonEv = jsonArray.getJSONObject(i);
                    eventiAz.add(new Evento(jsonEv));
                } catch (JSONException e) {
                    return eventiAz;
                }
            }
        }
        return eventiAz;
    }

   /* public List<Vino> getViniAzienda() {
        JSONArray jsonArray = JsonParser.getJSONArrayValue(KEY_VINI_AZIENDA, this.jsonObjAzienda);
        List<Vino> viniAz = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonVino = jsonArray.getJSONObject(i);
                    viniAz.add(new Vino(jsonVino));
                } catch (JSONException e) {
                    return viniAz;
                }
            }
        }
        return viniAz;
    }*/

    public List<ListItem> getViniAzienda() {
        JSONArray jsonArray = JsonParser.getJSONArrayValue(KEY_VINI_AZIENDA, this.jsonObjAzienda);
        List<ListItem> viniAz = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonVino = jsonArray.getJSONObject(i);
                    viniAz.add(new Vino(jsonVino));
                } catch (JSONException e) {
                    return viniAz;
                }
            }
        }
        return viniAz;
    }


    protected Azienda(Parcel in) {
        try {
            jsonObjAzienda = in.readByte() == 0x00 ? null : new JSONObject(in.readString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getType() {
        return TYPE_AZ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (jsonObjAzienda == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeString(jsonObjAzienda.toString());
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Azienda> CREATOR = new Parcelable.Creator<Azienda>() {
        @Override
        public Azienda createFromParcel(Parcel in) {
            return new Azienda(in);
        }

        @Override
        public Azienda[] newArray(int size) {
            return new Azienda[size];
        }
    };
}