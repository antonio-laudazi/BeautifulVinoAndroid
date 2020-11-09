package com.marte5.beautifulvino.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Marte5, Maria Tourbanova on 12/02/18.
 */

public class Badge  implements Parcelable {
    /*private String idBadge;
    private String nomeBadge;
    private String infoBadge;
    private String urlLogoBadge;
    private String tuoBadge;*/

    private JSONObject jsonObjBadge;

    public static final String KEY_ID_BADGE = "idBadge";
    public static final String KEY_NOME_BADGE = "nomeBadge";
    public static final String KEY_INFO_BADGE = "infoBadge";
    public static final String KEY_URL_LOGO_BADGE = "urlLogoBadge";
    public static final String KEY_TUO_BADGE = "tuoBadge";
    public static final String KEY_EVENTO_BADGE = "eventoBadge";

    public static final String VALUES_TUO_NO = "N";
    public static final String VALUES_TUO_SI = "S";

    public Badge() {
    }

    public Badge(JSONObject jsonObjBadge) {
        if (jsonObjBadge == null) {
            this.jsonObjBadge = new JSONObject();
        } else
            this.jsonObjBadge = jsonObjBadge;
    }

    public String getIdBadge() {
        return JsonParser.getStringValue(KEY_ID_BADGE,this.jsonObjBadge);
    }

    public String getNomeBadge() {
        return JsonParser.getStringValue(KEY_NOME_BADGE,this.jsonObjBadge);
    }

    public String getInfoBadge() {
        return JsonParser.getStringValue(KEY_INFO_BADGE,this.jsonObjBadge);
    }

    public String getUrlLogoBadge() {
        return JsonParser.getStringValue(KEY_URL_LOGO_BADGE,this.jsonObjBadge);
    }

    public String getTuoBadge() {
        return JsonParser.getStringValue(KEY_TUO_BADGE,this.jsonObjBadge);
    }

    public Evento getEventoBadge() {
        return new Evento(JsonParser.getJSONObjectValue(KEY_EVENTO_BADGE, this.jsonObjBadge));
    }


    protected Badge(Parcel in) {
        try {
            jsonObjBadge = in.readByte() == 0x00 ? null : new JSONObject(in.readString());
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
        if (jsonObjBadge == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeString(jsonObjBadge.toString());
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Badge> CREATOR = new Parcelable.Creator<Badge>() {
        @Override
        public Badge createFromParcel(Parcel in) {
            return new Badge(in);
        }

        @Override
        public Badge[] newArray(int size) {
            return new Badge[size];
        }
    };
}