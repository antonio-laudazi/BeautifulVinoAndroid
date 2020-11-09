package com.marte5.beautifulvino.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Marte5, Maria Tourbanova on 23/02/18.
 */

public class JsonParser {

    public static String getStringValue(String key, JSONObject jsonObj) {
        if (jsonObj == null) {
            return "";
        }
        try {
            return jsonObj.getString(key);
        } catch (JSONException e) {
            return "";
        }
    }

    public static int getIntValue(String key, JSONObject jsonObj) {
        try {
            return jsonObj.getInt(key);
        } catch (JSONException e) {
            return 0;
        }
    }

    public static Double getDoubleValue(String key, JSONObject jsonObj) {
        try {
            return jsonObj.getDouble(key);
        } catch (JSONException e) {
            return 0.0;
        }
    }

    public static JSONObject getJSONObjectValue(String key, JSONObject jsonObj) {
        try {
            return jsonObj.getJSONObject(key);
        } catch (JSONException e) {
            return null;
        }
    }

    public static JSONArray getJSONArrayValue(String key, JSONObject jsonObj) {
        if (jsonObj == null) {
            return new JSONArray();
        }
        try {
            return jsonObj.getJSONArray(key);
        } catch (JSONException e) {
            return new JSONArray();
        }
    }


    public static long getLongValue(String key, JSONObject jsonObj) {
        try {
            return jsonObj.getLong(key);
        } catch (JSONException e) {
            return 0;
        }
    }
}
