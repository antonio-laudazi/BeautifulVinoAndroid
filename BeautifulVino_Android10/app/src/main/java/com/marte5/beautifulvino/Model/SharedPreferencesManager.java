package com.marte5.beautifulvino.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Marte5, Maria Tourbanova on 16/02/18.
 */

public class SharedPreferencesManager {

    private SharedPreferences sharedPref;
    private static final String TAG = SharedPreferencesManager.class.getSimpleName();
    private static final String KEY_AUTENTICATO = "autenticato";
    private static final String KEY_ID_USER = "idUser";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_PROVINCIA_ID = "provinciaId";
    private static final String KEY_PROVINCIA_NOME = "provinciaNome";
    private static final String KEY_IDENTITY = "Identity";
    private static final String KEY_FIRST_TIME = "firstTime";

    public SharedPreferencesManager(Context mContext) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public static void setAutenticato(Context mContext, Boolean autenticato) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putBoolean(KEY_AUTENTICATO, autenticato).apply();
    }

    public static Boolean getAutenticato(Context mContext) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(KEY_AUTENTICATO, false);
    }


    public static void setIdUser(Context mContext, String idUser) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString(KEY_ID_USER, idUser).apply();
    }

    public static String getIdUser(Context mContext) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getString(KEY_ID_USER, "");
    }

    public static void setFirstLaunch(Context mContext, boolean first) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putBoolean(KEY_FIRST_TIME, first).apply();
    }

    public static boolean getFirstLaunch(Context mContext) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(KEY_FIRST_TIME, true);
    }


    public static void setIdentity(Context mContext, String identity) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString(KEY_IDENTITY, identity).apply();
    }

    public static String getIdentity(Context mContext) {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getString(KEY_IDENTITY, "");
    }


    public static void setProvincia(Context mContext, Provincia pr) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString(KEY_PROVINCIA_ID, pr.getIdProvincia()).apply();
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString(KEY_PROVINCIA_NOME, pr.getNomeProvincia()).apply();
    }

    public static Provincia getProvincia(Context mContext) {

        String nomeProvincia = PreferenceManager.getDefaultSharedPreferences(mContext).getString(KEY_PROVINCIA_NOME, "");
        String idProvincia = PreferenceManager.getDefaultSharedPreferences(mContext).getString(KEY_PROVINCIA_ID, "");
        if (nomeProvincia.equals("") || idProvincia.equals("")) {
            return null;
        } else {
            Provincia pr = new Provincia();
            pr.setNomeProvincia(nomeProvincia);
            pr.setIdProvincia(idProvincia);
            return pr;
        }
    }


}
