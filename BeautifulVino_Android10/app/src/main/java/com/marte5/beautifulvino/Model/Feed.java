package com.marte5.beautifulvino.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Marte5, Maria Tourbanova on 14/02/18.
 */

public class Feed implements Parcelable, Comparable {
  /*  private String  idFeed;
    private Long  dataFeed;
    private String  headerFeed;
    private String  sottoHeaderFeed;
    private String  titoloFeed;
    private String  urlImmagineHeaderFeed;
    private String  urlImmagineFeed;
    private String  testoLabelFeed;
    private String  testoFeed;
    private Vino  vinoFeed;
    private Evento  eventoFeed;
    private Azienda  aziendaFeed;
    private int  tipoFeed; //determina la dettaglioView che si apre cliccando sulla cella es. dettEventoView, determina il tipo di cella
    private String  idEntitaFeed;
    private String  tipoEntitaHeader;//determina la dettaglioView che si apre cliccando su header es. dettEventoView
    private String  idEntitaHeader;
    private String  urlVideoFeed;*/

    private JSONObject jsonObjFeed;

    //determina il pulsante nella dettaglioView ToGo, nel caso del post ToGo è hidden
    public static final int VALUES_TIPO_FEED_PUBBLICITA = 1;
    public static final int VALUES_TIPO_FEED_AZIENDA = 2;
    public static final int VALUES_TIPO_FEED_VINO = 3;
    public static final int VALUES_TIPO_FEED_EVENTO = 4;
    public static final int VALUES_TIPO_FEED_POST = 5;

    //determina la dettaglioView che si apre cliccando su header es. dettEventoView
    public static final String VALUES_TIPO_ENTITA_HEADER_AZIENDA = "AZ";
    public static final String VALUES_TIPO_ENTITA_HEADER_PROFILO = "UT";
    public static final String VALUES_TIPO_ENTITA_HEADER_EVENTO = "EV";
    public static final String VALUES_TIPO_ENTITA_HEADER_VINO = "VI";

    public static final String KEY_ID_FEED = "idFeed";
    public static final String KEY_DATA_FEED = "dataFeed";
    public static final String KEY_HEADER_FEED = "headerFeed";
    public static final String KEY_SOTTO_HEADER_FEED = "sottoHeaderFeed";
    public static final String KEY_TITOLO_FEED = "titoloFeed";
    public static final String KEY_URL_IMG_HEADER_FEED = "urlImmagineHeaderFeed";
    public static final String KEY_URL_IMG_FEED = "urlImmagineFeed";
    public static final String KEY_TESTO_LABEL_FEED = "testoLabelFeed";
    public static final String KEY_TESTO_FEED = "testoFeed";
    public static final String KEY_VINO_FEED = "vinoFeedInt";
    public static final String KEY_EVENTO_FEED = "eventoFeedInt";
    public static final String KEY_AZIENDA_FEED = "aziendaFeed";
    public static final String KEY_TIPO_FEED = "tipoFeed";
    public static final String KEY_ID_ENTITA_FEED = "idEntitaFeed";
    public static final String KEY_TIPO_ENTITA_HEADER = "tipoEntitaHeaderFeed";
    public static final String KEY_ID_ENTITA_HEADER = "idEntitaHeaderFeed";
    public static final String KEY_URL_VIDEO_FEED = "urlVideoFeed";

    public Feed(JSONObject jsonObjFeed) {
        if (jsonObjFeed == null) {
            this.jsonObjFeed = new JSONObject();
        } else
            this.jsonObjFeed = jsonObjFeed;
    }

    public String getIdFeed() {
        return JsonParser.getStringValue(KEY_ID_FEED,this.jsonObjFeed);
    }

    public Long getDataFeed() {
        return JsonParser.getLongValue(KEY_DATA_FEED,this.jsonObjFeed);
    }

    public String getHeaderFeed() {
        return JsonParser.getStringValue(KEY_HEADER_FEED,this.jsonObjFeed);
    }

    public String getSottoHeaderFeed() {
        return JsonParser.getStringValue(KEY_SOTTO_HEADER_FEED,this.jsonObjFeed);
    }

    public String getTitoloFeed() {
        return JsonParser.getStringValue(KEY_TITOLO_FEED,this.jsonObjFeed);
    }

    public String getUrlImmagineHeaderFeed() {
        return JsonParser.getStringValue(KEY_URL_IMG_HEADER_FEED,this.jsonObjFeed);
    }

    public String getUrlImmagineFeed() {
        return JsonParser.getStringValue(KEY_URL_IMG_FEED,this.jsonObjFeed);
    }

    public String getTestoLabelFeed() {
        return JsonParser.getStringValue(KEY_TESTO_LABEL_FEED,this.jsonObjFeed);
    }

    public String getTestoFeed() {
        return JsonParser.getStringValue(KEY_TESTO_FEED,this.jsonObjFeed);
    }

    public Vino getVinoFeed() {
        return new Vino(JsonParser.getJSONObjectValue(KEY_VINO_FEED, this.jsonObjFeed));
    }

    public Evento getEventoFeed() {
        return new Evento(JsonParser.getJSONObjectValue(KEY_EVENTO_FEED, this.jsonObjFeed));
    }

    public Azienda getAziendaFeed() {
        return new Azienda(JsonParser.getJSONObjectValue(KEY_AZIENDA_FEED, this.jsonObjFeed));
    }

    public int getTipoFeed() {
        return JsonParser.getIntValue(KEY_TIPO_FEED,this.jsonObjFeed);
    }

    public String getIdEntitaFeed() {
        return JsonParser.getStringValue(KEY_ID_ENTITA_FEED,this.jsonObjFeed);
    }

    public String getTipoEntitaHeader() {
        return JsonParser.getStringValue(KEY_TIPO_ENTITA_HEADER,this.jsonObjFeed);
    }

    public String getIdEntitaHeader() {
        return JsonParser.getStringValue(KEY_ID_ENTITA_HEADER,this.jsonObjFeed);
    }

    public String getUrlVideoFeed() {
        //  return "https://www.youtube.com/embed/Nr2g5R-v874";
         return JsonParser.getStringValue(KEY_URL_VIDEO_FEED,this.jsonObjFeed);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Feed f= (Feed) o;
        if (getDataFeed() < f.getDataFeed()) {
            return 1;
        }
        else if (getDataFeed() > f.getDataFeed()) {
            return -1;
        }
        else {
            return 0;
        }
    }


    /* protected Feed(Parcel in) {
        idFeed = in.readString();
        dataFeed = in.readByte() == 0x00 ? null : in.readLong();
        headerFeed = in.readString();
        sottoHeaderFeed = in.readString();
        titoloFeed = in.readString();
        urlImmagineHeaderFeed = in.readString();
        urlImmagineFeed = in.readString();
        testoLabelFeed = in.readString();
        testoFeed = in.readString();
        vinoFeed = (Vino) in.readValue(Vino.class.getClassLoader());
        eventoFeed = (Evento) in.readValue(Evento.class.getClassLoader());
        aziendaFeed = (Azienda) in.readValue(Azienda.class.getClassLoader());
        tipoFeed = in.readInt();
        idEntitaFeed = in.readString();
        tipoEntitaHeader = in.readString();
        idEntitaHeader = in.readString();
        urlVideoFeed = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idFeed);
        if (dataFeed == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(dataFeed);
        }
        dest.writeString(headerFeed);
        dest.writeString(sottoHeaderFeed);
        dest.writeString(titoloFeed);
        dest.writeString(urlImmagineHeaderFeed);
        dest.writeString(urlImmagineFeed);
        dest.writeString(testoLabelFeed);
        dest.writeString(testoFeed);
        dest.writeValue(vinoFeed);
        dest.writeValue(eventoFeed);
        dest.writeValue(aziendaFeed);
        dest.writeInt(tipoFeed);
        dest.writeString(idEntitaFeed);
        dest.writeString(tipoEntitaHeader);
        dest.writeString(idEntitaHeader);
        dest.writeString(urlVideoFeed);
    }

    public static final Parcelable.Creator<Feed> CREATOR = new Parcelable.Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel in) {
            return new Feed(in);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };*/
   protected Feed(Parcel in) {
       try {
           jsonObjFeed = in.readByte() == 0x00 ? null : new JSONObject(in.readString());
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
        if (jsonObjFeed == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeString(jsonObjFeed.toString());
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Feed> CREATOR = new Parcelable.Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel in) {
            return new Feed(in);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };
}
