package com.marte5.beautifulvino.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Marte5, Maria Tourbanova on 14/02/18.
 */

public class Provincia implements Parcelable, Comparable<Provincia> {
    private String idProvincia;
    private String nomeProvincia;

    public static final String KEY_ID_PROVINCIA = "idProvincia";
    public static final String KEY_NOME_PROVINCIA = "nomeProvincia";

    public Provincia() {

    }

    public Provincia(boolean all) {
        if (all) {
            this.idProvincia = "X";
            this.nomeProvincia = "TUTTI";
        }
    }

    public Provincia(JSONObject jsonObj) {
        try {
            this.idProvincia = jsonObj.getString(KEY_ID_PROVINCIA);
            this.nomeProvincia = jsonObj.getString(KEY_NOME_PROVINCIA);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(String idProvincia) {
        this.idProvincia = idProvincia;
    }

    public String getNomeProvincia() {
        return nomeProvincia;
    }

    public void setNomeProvincia(String nomeProvincia) {
        this.nomeProvincia = nomeProvincia;
    }

    public Provincia(Parcel in) {
        idProvincia = in.readString();
        nomeProvincia = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idProvincia);
        dest.writeString(nomeProvincia);
    }

    @Override
    public int compareTo(@NonNull Provincia p) {
        return nomeProvincia.compareToIgnoreCase(p.nomeProvincia);
    }


    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Provincia> CREATOR = new Parcelable.Creator<Provincia>() {
        @Override
        public Provincia createFromParcel(Parcel in) {
            return new Provincia(in);
        }

        @Override
        public Provincia[] newArray(int size) {
            return new Provincia[size];
        }
    };


}
