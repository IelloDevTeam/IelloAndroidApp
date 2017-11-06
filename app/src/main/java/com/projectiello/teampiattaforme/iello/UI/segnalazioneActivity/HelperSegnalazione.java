package com.projectiello.teampiattaforme.iello.UI.segnalazioneActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.projectiello.teampiattaforme.iello.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by riccardomaldini on 08/10/17.
 * Classe per la gestione dell'invio delle segnalazioni tramite IelloAPI. L'API utilizza infatti un
 * meccanismo di autorizzazioni, che viene gestito qua dentro.
 */
public class HelperSegnalazione extends ContextWrapper {

    private static final String TAG = "HelperSegnalazione";
    private static final String BASE_URL = "http://cloudpi.webhop.me:4000/iello/v1/parking/";

    public interface APICallback
    {
        void OnResult(boolean isError, JSONObject jsonObject);
        void OnAuthError();
    }

    public HelperSegnalazione(Context context) {
        super(context);
    }

    /**
     * Metodo per l'invio di una singola posizione al DB remoto
     */
    void sendLocation(final LatLng location, @NonNull final APICallback apiCallback) {
        if (location != null) {
            JSONObject body = new JSONObject();
            try {
                body.put("longitude", location.longitude);
                body.put("latitude", location.latitude);

                JsonObjectRequest volleyRequest
                        = new JsonObjectRequest(Request.Method.POST, BASE_URL + "report",
                                                body, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String status = response.optString("status","error");
                        if(status.equals("Success"))
                            apiCallback.OnResult(false, response);
                        else
                            apiCallback.OnResult(true, response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && (networkResponse.statusCode == 500 || networkResponse.statusCode == 400)) {
                            // HTTP Status Code: 500 errore server di caricamento parcheggio
                            // 400 richesta malformata
                            apiCallback.OnResult(true, null);
                        }
                        Log.d(TAG, error.toString());
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("Accept", "application/json");
                        map.put("Authorization", getString(R.string.iello_api_key));
                        return map;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(volleyRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "Errore inaspettato nell'invio della posizione");
        }
    }
}
