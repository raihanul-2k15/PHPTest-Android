package com.raihanul.phpposttest;

import android.os.AsyncTask;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "AIzaSyAwCDAjRzBQbM2QYwLO9pVR5qMZrjES2oo";

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                String data= "";
                try {
                    URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address="+params[0]+"&key="+API_KEY);
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

                    InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder builder = new StringBuilder();

                    String inputString;
                    while ((inputString = bufferedReader.readLine()) != null) {
                        builder.append(inputString);
                    }

                    urlConnection.disconnect();

                    JSONObject json = new JSONObject(builder.toString());
                    json = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                    String lat = String.valueOf(json.optDouble("lat"));
                    String lng = String.valueOf(json.optDouble("lng"));

                    data = lat + " " + lng;
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return data;
            }

            @Override
            protected void onPostExecute(String s) {
                textView.setText(s);
            }
        }.execute("Dhaka");
    }
}
