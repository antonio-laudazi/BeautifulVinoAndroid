package com.marte5.beautifulvino.Model;

import org.json.JSONObject;

/**
 * Created by Marte5, Maria Tourbanova on 12/02/18.
 */

public class Esito {

    private JSONObject jsonObjEsito;

    private final String KEY_MESSAGIO = "message";
    private final String KEY_CODICE = "codice";
    public static int ERROR_CODE = 500;
    public static int ERROR_CODE_FEED_LETTO = 600;

    public Esito(JSONObject jsonObjEsito) {
        if (jsonObjEsito == null) {
            this.jsonObjEsito = new JSONObject();
        } else
            this.jsonObjEsito = jsonObjEsito;
    }

    public String getMessage() {
        return JsonParser.getStringValue(KEY_MESSAGIO,this.jsonObjEsito);
    }

    public int getCodice() {
        return JsonParser.getIntValue(KEY_CODICE,this.jsonObjEsito);
    }

}
