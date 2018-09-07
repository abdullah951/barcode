package info.androidhive.barcode;

import org.json.JSONException;
import org.json.JSONObject;

interface ServerCallBack {

    void onSuccess(String s);

    void onError(String message);

    void onSuccess(JSONObject response) throws JSONException;
}
