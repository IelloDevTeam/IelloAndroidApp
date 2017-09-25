package com.projectiello.teampiattaforme.iello.utilities;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.projectiello.teampiattaforme.iello.UI.MainActivity;
import com.projectiello.teampiattaforme.iello.apiConnection.AsyncDownloadParcheggi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by riccardomaldini on 25/09/17.
 * AsynkTask che prende in ingresso un indirizzo e in base a questo trova i posteggi nelle vicinanze.
 */

public class AsyncTrovaParcheggiByIndirizzo extends AsyncTask<String, Void, String[]> {
    private MainActivity mMainActivity;
    private String mIndirizzo;

    public AsyncTrovaParcheggiByIndirizzo(MainActivity m, String ind) {
        mMainActivity = m;
        mIndirizzo = ind.replaceAll(" ", "+" +
                "");
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(mMainActivity, "Please wait..", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String[] doInBackground(String... params) {
        String response;
        try {
            response = getLatLongByURL("http://maps.google.com/maps/api/geocode/json?address=" + mIndirizzo + "&sensor=false");
            Log.d("response", "" + response);
            return new String[]{response};
        } catch (Exception e) {
            return new String[]{"error"};
        }
    }

    @Override
    protected void onPostExecute(String... result) {
        try {
            JSONObject jsonObject = new JSONObject(result[0]);

            double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            Log.d("latitude", "" + lat);
            Log.d("longitude", "" + lng);


            AsyncDownloadParcheggi adp = new AsyncDownloadParcheggi(mMainActivity, lat, lng);
            adp.execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public String getLatLongByURL(String requestURL) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}