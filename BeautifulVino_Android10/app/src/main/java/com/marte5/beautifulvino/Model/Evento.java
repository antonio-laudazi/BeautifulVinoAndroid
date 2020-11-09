package com.marte5.beautifulvino.Model;

import android.nfc.Tag;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Marte5, Maria Tourbanova on 12/02/18.
 */

public class Evento implements Parcelable, Comparable {
    private JSONObject jsonObjEvento;

    public static final String VALUES_STATO_PREFERITO = "P";
    public static final String VALUES_STATO_ACQUISTATO = "A";
    public static final String VALUES_STATO_PRENOTATO = "P";
    public static final String VALUES_STATO_OTHER = "N";
    public static final int VALUES_STATO_ACQUISTABILE_SI = 1;
    public static final int VALUES_STATO_ACQUISTABILE_NO = 0;

    public static final String KEY_ID_EVENTO = "idEvento";
    public static final String KEY_DATA_EVENTO = "dataEvento";
    public static final String KEY_CITTA_EVENTO = "cittaEvento";
    public static final String KEY_TITOLO_EVENTO = "titoloEvento";
    public static final String KEY_PREZZO_EVENTO = "prezzoEvento";
    public static final String KEY_URL_FOTO_EVENTO = "urlFotoEvento";
    public static final String KEY_STATO_EVENTO = "statoEvento";
    public static final String KEY_STATO_PREFERITO_EVENTO = "statoPreferitoEvento";
    public static final String KEY_ACQUISTABILE_EVENTO = "acquistabileEvento";
    public static final String KEY_TEMA_EVENTO = "temaEvento";
    public static final String KEY_TESTO_EVENTO = "testoEvento";
    public static final String KEY_LAT_EVENTO = "latitudineEvento";
    public static final String KEY_LON_EVENTO = "longitudineEvento";
    public static final String KEY_IND_EVENTO = "indirizzoEvento";
    public static final String KEY_TELEFONO_EVENTO = "telefonoEvento";
    public static final String KEY_EMAIL_EVENTO = "emailEvento";
    public static final String KEY_NUM_MAX_PART_EVENTO = "numMaxPartecipantiEvento";
    public static final String KEY_NUM_POSTI_DISPONIBILI_EVENTO = "numPostiDisponibiliEvento";
    public static final String KEY_AZIENDA_OSPITANTE_EVENTO = "aziendaOspitanteEvento";
    public static final String KEY_AZIENDE_VINI_EVENTO = "aziendeViniEvento";
    public static final String KEY_BADGE_EVENTO = "badgeEvento";
    public static final String KEY_ISCRITTI_EVENTO = "iscrittiEvento";


    public Evento(JSONObject jsonObjEvento) {
        if (jsonObjEvento == null) {
            this.jsonObjEvento = new JSONObject();
        } else
            this.jsonObjEvento = jsonObjEvento;
    }

    public String getIdEvento() {
        return JsonParser.getStringValue(KEY_ID_EVENTO, this.jsonObjEvento);
    }

    public long getDataEvento() {
        return JsonParser.getLongValue(KEY_DATA_EVENTO, this.jsonObjEvento);
    }

    public String getDataStringEvento() {
        Date date = new Date(getDataEvento());
        Format formatter = new SimpleDateFormat("dd MMMM', ore' HH:mm", Locale.ITALY);
        return formatter.format(date);
    }

    public String getCittaEvento() {
        return JsonParser.getStringValue(KEY_CITTA_EVENTO, this.jsonObjEvento);
    }

    public String getTitoloEvento() {
        return JsonParser.getStringValue(KEY_TITOLO_EVENTO, this.jsonObjEvento);
    }

    public Double getPrezzoEvento() {
        return JsonParser.getDoubleValue(KEY_PREZZO_EVENTO, this.jsonObjEvento);
    }

    public String getPrezzoStringEvento() {
        if (getPrezzoEvento() == 0) {
            return "Gratuito";
        } else {
            return String.format("€ %.2f", getPrezzoEvento());
        }
    }

    public String getUrlFotoEvento() {
        return JsonParser.getStringValue(KEY_URL_FOTO_EVENTO, this.jsonObjEvento);
    }

    public String getStatoEvento() {
        return JsonParser.getStringValue(KEY_STATO_EVENTO, this.jsonObjEvento);
    }

    public void setStatoEvento(String stato) {
        try {
            jsonObjEvento.put(KEY_STATO_EVENTO, stato);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getStatoPreferitoEvento() {
        return JsonParser.getStringValue(KEY_STATO_PREFERITO_EVENTO, this.jsonObjEvento);
    }

    public void setStatoPreferitoEvento(String stato) {
        try {
            jsonObjEvento.put(KEY_STATO_PREFERITO_EVENTO, stato);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getAcquistabileEvento() {
        return JsonParser.getIntValue(KEY_ACQUISTABILE_EVENTO, this.jsonObjEvento);
    }

    public Boolean statoEventoModificabile(){
        if (this.getStatoEvento().equals(Evento.VALUES_STATO_OTHER)){
            return true;
        } else {
            Calendar c = afterDateEvento();
            if(c.compareTo(Calendar.getInstance()) < 1){
                return true;
            } else{
                return false;
            }
        }
    }


    private Calendar afterDateEvento(){
        Date date = new Date(this.getDataEvento());
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE,1);
        c.set(Calendar.HOUR_OF_DAY,12);
        c.set(Calendar.MINUTE,0);

        Date tomorrow = c.getTime();

        Format formatter = new SimpleDateFormat("dd MMMM', ore' HH:mm", Locale.ITALY);

      /*  Log.d("TOMORROW", formatter.format(tomorrow));
        Log.d("getDataEvento", formatter.format(date));
        Log.d("NOW", formatter.format(Calendar.getInstance().getTime()));*/
        return c;
    }


    public Boolean eventoAcquistabile() {
        Calendar c = afterDateEvento();
        if (c.compareTo(Calendar.getInstance()) < 1) {
            return false;
        } else {
            return true;
        }
    }


    public String getTemaEvento() {
        return JsonParser.getStringValue(KEY_TEMA_EVENTO, this.jsonObjEvento);
    }

    public String getTestoEvento() {
        return JsonParser.getStringValue(KEY_TESTO_EVENTO, this.jsonObjEvento);
    }

    public Double getLatitudineEvento() {
        return JsonParser.getDoubleValue(KEY_LAT_EVENTO, this.jsonObjEvento);
    }

    public Double getLongitudineEvento() {
        return JsonParser.getDoubleValue(KEY_LON_EVENTO, this.jsonObjEvento);
    }

    public String getIndirizzoEvento() {
        return JsonParser.getStringValue(KEY_IND_EVENTO, this.jsonObjEvento);
    }

    public String getTelefonoEvento() {
        return JsonParser.getStringValue(KEY_TELEFONO_EVENTO, this.jsonObjEvento);
    }

    public String getEmailEvento() {
        return JsonParser.getStringValue(KEY_EMAIL_EVENTO, this.jsonObjEvento);
    }

    public int getNumMaxPartecipantiEvento() {
        if (JsonParser.getIntValue(KEY_NUM_MAX_PART_EVENTO, this.jsonObjEvento)<0){
            return 0;
        }
        return JsonParser.getIntValue(KEY_NUM_MAX_PART_EVENTO, this.jsonObjEvento);
    }

    public int getNumPostiDisponibiliEvento() {
        if (getNumMaxPartecipantiEvento() <= 0){
            return 10;
        }
        if (JsonParser.getIntValue(KEY_NUM_POSTI_DISPONIBILI_EVENTO, this.jsonObjEvento)<0){
            return 0;
        }
        return JsonParser.getIntValue(KEY_NUM_POSTI_DISPONIBILI_EVENTO, this.jsonObjEvento);
    }

   public Azienda getAziendaFornitriceEvento() {
        JSONArray jsonArray = JsonParser.getJSONArrayValue(KEY_AZIENDE_VINI_EVENTO, this.jsonObjEvento);
        JSONObject jsonAz = null;
        try {
            jsonAz = jsonArray.getJSONObject(0);
        } catch (JSONException e) {
            return new Azienda(jsonAz);
        }
        return new Azienda(jsonAz);
    }

    public List<Azienda> getAziendeFornitriceEvento() {
        JSONArray jsonArray = JsonParser.getJSONArrayValue(KEY_AZIENDE_VINI_EVENTO, this.jsonObjEvento);
        List<Azienda> aziendeEvento = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonAz = jsonArray.getJSONObject(i);
                    aziendeEvento.add(new Azienda(jsonAz));
                } catch (JSONException e) {
                    return aziendeEvento;
                }
            }
        }
        return aziendeEvento;
    }

    public Azienda getAziendaOspitanteEvento() {
        return new Azienda(JsonParser.getJSONObjectValue(KEY_AZIENDA_OSPITANTE_EVENTO, this.jsonObjEvento));
    }

    public Badge getBadgeEvento() {
        return new Badge(JsonParser.getJSONObjectValue(KEY_BADGE_EVENTO, this.jsonObjEvento));
    }

    public List<Utente> getIscrittiEvento() {
        JSONArray jsonArray = JsonParser.getJSONArrayValue(KEY_ISCRITTI_EVENTO, this.jsonObjEvento);
        List<Utente> utentiEvento = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonUtente = jsonArray.getJSONObject(i);
                    utentiEvento.add(new Utente(jsonUtente));
                } catch (JSONException e) {
                    return utentiEvento;
                }
            }
        }
        return utentiEvento;
    }

/*
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idEvento);
        dest.writeLong(dataEvento);
        dest.writeString(cittaEvento);
        dest.writeString(titoloEvento);
        if (prezzoEvento == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(prezzoEvento);
        }
        dest.writeString(urlFotoEvento);
        dest.writeString(statoEvento);
        dest.writeString(temaEvento);
        dest.writeString(testoEvento);
        if (latitudineEvento == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(latitudineEvento);
        }
        if (longitudineEvento == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(longitudineEvento);
        }
        dest.writeString(indirizzoEvento);
        dest.writeString(telefonoEvento);
        dest.writeString(emailEvento);
        dest.writeInt(numMaxPartecipantiEvento);
        dest.writeInt(numPostiDisponibiliEvento);
    }


    public Evento(Parcel in) {
        idEvento = in.readString();
        dataEvento = in.readLong();
        cittaEvento = in.readString();
        titoloEvento = in.readString();
        prezzoEvento = in.readByte() == 0x00 ? null : in.readDouble();
        urlFotoEvento = in.readString();
        statoEvento = in.readString();
        temaEvento = in.readString();
        testoEvento = in.readString();
        latitudineEvento = in.readByte() == 0x00 ? null : in.readDouble();
        longitudineEvento = in.readByte() == 0x00 ? null : in.readDouble();
        indirizzoEvento = in.readString();
        telefonoEvento = in.readString();
        emailEvento = in.readString();
        numMaxPartecipantiEvento = in.readInt();
        numPostiDisponibiliEvento = in.readInt();
    }

    public static final Parcelable.Creator<Evento> CREATOR = new Parcelable.Creator<Evento>() {
        @Override
        public Evento createFromParcel(Parcel in) {
            return new Evento(in);
        }

        @Override
        public Evento[] newArray(int size) {
            return new Evento[size];
        }
    };
*/

    @Override
    public int compareTo(@NonNull Object o) {
        Evento ev = (Evento) o;
        if (getDataEvento() < ev.getDataEvento()) {
            return 1;
        } else if (getDataEvento() > ev.getDataEvento()) {
            return -1;
        } else {
            return 0;
        }
    }

    protected Evento(Parcel in) {
        try {
            jsonObjEvento = in.readByte() == 0x00 ? null : new JSONObject(in.readString());
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
        if (jsonObjEvento == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeString(jsonObjEvento.toString());
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Evento> CREATOR = new Parcelable.Creator<Evento>() {
        @Override
        public Evento createFromParcel(Parcel in) {
            return new Evento(in);
        }

        @Override
        public Evento[] newArray(int size) {
            return new Evento[size];
        }
    };


}
