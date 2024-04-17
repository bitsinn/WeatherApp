package com.veercreation.weatherex;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
    TextView temperatureTextView , atmosphereText;
    EditText cityInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperatureTextView = findViewById(R.id.temperatureTextView);
        cityInput = findViewById(R.id.inputCityText);
        atmosphereText = findViewById(R.id.atmosphereTextView);

    }

    public void getData(View view){
        DownloadingWeather weatherTask = new DownloadingWeather(temperatureTextView, atmosphereText);
        String city = String.valueOf(cityInput.getText());
        try {
            city = URLEncoder.encode(city , "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //TODO
            e.printStackTrace();
        }
        String url = "https://api.weatherapi.com/v1/current.json?key=9c0f3203757049a1bdf61654212411&q="+city;
        String result = null;
        try {
            weatherTask.execute(url);
            InputMethodManager imr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imr.hideSoftInputFromWindow(cityInput.getWindowToken(),0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}