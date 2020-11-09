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

public class Vino extends ListItem implements Parcelable {

    private JSONObject jsonObjVino;


    public static final String VALUES_STATO_AGGIUNTO = "P";
    public static final String VALUES_STATO_OTHER = "N";
    public static final String VALUES_STATO_ACQUISTATO = "A";
    public static final int VALUES_STATO_ACQUISTABILE_SI = 1;
    public static final int VALUES_STATO_ACQUISTABILE_NO = 0;

    public static final String KEY_ID_VINO = "idVino";
    public static final String KEY_NOME_VINO = "nomeVino";
    public static final String KEY_UVAGGIO_VINO = "uvaggioVino";
    public static final String KEY_REGIONE_VINO = "regioneVino";
    public static final String KEY_PROFUMO_VINO = "profumoVino";
    public static final String KEY_IN_BREVE_VINO = "inBreveVino";
    public static final String KEY_PREZZO_VINO = "prezzoVino";
    public static final String KEY_INFO_VINO = "infoVino";
    public static final String KEY_STATO_VINO = "statoVino";
    public static final String KEY_ACQUISTABILE_VINO = "acquistabileVino";
    public static final String KEY_URL_LOGO_VINO = "urlLogoVino";
    public static final String KEY_URL_IMMAGINE_VINO = "urlImmagineVino";
    public static final String KEY_UTENTI_VINO = "utentiVino";
    public static final String KEY_AZIENDA_VINO = "aziendaVino";
    public static final String KEY_EVENTI_VINO = "eventiVino";

    public Vino(JSONObject jsonObjVino) {
        if (jsonObjVino == null) {
            this.jsonObjVino = new JSONObject();
        } else
            this.jsonObjVino = jsonObjVino;
    }

    public String getIdVino() {
        return JsonParser.getStringValue(KEY_ID_VINO,this.jsonObjVino);
    }


    public String getNomeVino() {
        return JsonParser.getStringValue(KEY_NOME_VINO,this.jsonObjVino);

    }

    public String getUvaggioVino() {
        return JsonParser.getStringValue(KEY_UVAGGIO_VINO,this.jsonObjVino);

    }

    public String getRegioneVino() {
        return JsonParser.getStringValue(KEY_REGIONE_VINO,this.jsonObjVino);

    }

    public String getProfumoVino() {
        return JsonParser.getStringValue(KEY_PROFUMO_VINO,this.jsonObjVino);

    }

    public String getInBreveVino() {
        return JsonParser.getStringValue(KEY_IN_BREVE_VINO,this.jsonObjVino);

    }

    public String getInfoVino() {
        return JsonParser.getStringValue(KEY_INFO_VINO,this.jsonObjVino);

    }

    public Double getPrezzoVino() {
        return JsonParser.getDoubleValue(KEY_PREZZO_VINO,this.jsonObjVino);
    }

    public String getPrezzoStringVino() {
        return String.format("€ %.2f", getPrezzoVino());
    }


    public String getStatoVino() {
        return JsonParser.getStringValue(KEY_STATO_VINO,this.jsonObjVino);
    }

    public void setStatoVino(String stato) {
        try {
            jsonObjVino.put(KEY_STATO_VINO, stato);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getAcquistabileVino() {
        return JsonParser.getIntValue(KEY_ACQUISTABILE_VINO, this.jsonObjVino);
    }

 /*  public void setAcquistabileVino() {
        try {
            jsonObjVino.put(KEY_ACQUISTABILE_VINO, VALUES_STATO_ACQUISTABILE_SI);
            Log.d("vino", String.valueOf(jsonObjVino));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/


    public String getUrlLogoVino() {
        return JsonParser.getStringValue(KEY_URL_LOGO_VINO,this.jsonObjVino);
    }

    public String getUrlImmagineVino() {
        return JsonParser.getStringValue(KEY_URL_IMMAGINE_VINO,this.jsonObjVino);

    }

    public List<Utente> getUtentiVino() {
        JSONArray jsonArray = JsonParser.getJSONArrayValue(KEY_UTENTI_VINO, this.jsonObjVino);
        List<Utente> utentiVino = new ArrayList<>();
        if (jsonArray != null) {
            for (int i=0; i<jsonArray.length(); i++){
                try {
                    JSONObject jsonUtente=jsonArray.getJSONObject(i);
                    utentiVino.add(new Utente(jsonUtente));
                } catch (JSONException e) {
                    return utentiVino;
                }
            }
        }
        return utentiVino;

    }

    public Azienda getAziendaVino() {
        return new Azienda(JsonParser.getJSONObjectValue(KEY_AZIENDA_VINO, this.jsonObjVino));

    }

    public List<Evento> getEventiVino() {
        JSONArray jsonArray = JsonParser.getJSONArrayValue(KEY_EVENTI_VINO, this.jsonObjVino);
        List<Evento> eventiVino = new ArrayList<>();
        if (jsonArray != null) {
            for (int i=0; i<jsonArray.length(); i++){
                try {
                    JSONObject jsonEvento=jsonArray.getJSONObject(i);
                    eventiVino.add(new Evento(jsonEvento));
                } catch (JSONException e) {
                    return eventiVino;
                }
            }
        }
        return eventiVino;
    }

 /*   protected Vino(Parcel in) {
        idVino = in.readString();
        nomeVino = in.readString();
        uvaggioVino = in.readString();
        regioneVino = in.readString();
        profumoVino = in.readString();
        inBreveVino = in.readString();
        infoVino = in.readString();
        statoVino = in.readString();
        urlLogoVino = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idVino);
        dest.writeString(nomeVino);
        dest.writeString(uvaggioVino);
        dest.writeString(regioneVino);
        dest.writeString(profumoVino);
        dest.writeString(inBreveVino);
        dest.writeString(infoVino);
        dest.writeString(statoVino);
        dest.writeString(urlLogoVino);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Vino> CREATOR = new Parcelable.Creator<Vino>() {
        @Override
        public Vino createFromParcel(Parcel in) {
            return new Vino(in);
        }

        @Override
        public Vino[] newArray(int size) {
            return new Vino[size];
        }
    };
}*/
 protected Vino(Parcel in) {
     try {
         jsonObjVino = in.readByte() == 0x00 ? null : new JSONObject(in.readString());
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
        if (jsonObjVino == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeString(jsonObjVino.toString());
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Vino> CREATOR = new Parcelable.Creator<Vino>() {
        @Override
        public Vino createFromParcel(Parcel in) {
            return new Vino(in);
        }

        @Override
        public Vino[] newArray(int size) {
            return new Vino[size];
        }
    };

    @Override
    public int getType() {
        return TYPE_VINO;
    }
}