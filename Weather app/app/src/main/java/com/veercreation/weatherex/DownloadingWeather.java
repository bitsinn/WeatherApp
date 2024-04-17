package com.veercreation.weatherex;

import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DownloadingWeather {
    private final TextView temperatureTextView;
    private final TextView atmosphereText;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public DownloadingWeather(TextView temperatureTextView, TextView atmosphereText) {
        this.temperatureTextView = temperatureTextView;
        this.atmosphereText = atmosphereText;
    }

    public void execute(String... urls) {
        executor.execute(() -> {
            String result = doInBackground(urls);
            onPostExecute(result);
        });
    }

    private String doInBackground(String... urls) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            StringBuilder result = new StringBuilder();
            int data;
            while ((data = reader.read()) != -1) {
                char current = (char) data;
                result.append(current);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    public void onPostExecute(String s) {
        try {
            JSONObject mainJsonObject = new JSONObject(s);
            JSONObject currentJsonObject = mainJsonObject.getJSONObject("current");
            String temperatureString = currentJsonObject.getString("temp_c") + " C";
            temperatureTextView.post(() -> temperatureTextView.setText(temperatureString));

            JSONObject conditionObject = currentJsonObject.getJSONObject("condition");
            String skyCondition = "It's " + conditionObject.getString("text") + " Sky";
            atmosphereText.post(() -> atmosphereText.setText(skyCondition));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
