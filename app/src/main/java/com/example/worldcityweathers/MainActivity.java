package com.example.worldcityweathers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;


/*
Application provides the api from https://openweathermap.org/.
 */
public class MainActivity extends AppCompatActivity {

    EditText cityEditText;
    TextView resultTextView;
    static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityEditText = findViewById(R.id.cityEditText);
        resultTextView = findViewById(R.id.resultTextView);


        handler = new Handler(Looper.getMainLooper()){
            /*
            what=0: success
            msg.obj carries JSON object that obtained from api.
             */
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == 0){
                    String stringJSON = (String) msg.obj;
                    try{
                        JSONObject json = new JSONObject(stringJSON);
                        JSONArray weatherArray = json.getJSONArray("weather");
                        for(int i=0; i<weatherArray.length(); i++){
                            JSONObject weather = weatherArray.getJSONObject(i);
                            String main = weather.getString("main");
                            String description = weather.getString("description");
                            resultTextView.setText(cityEditText.getText().toString().toUpperCase() + "\n\n" + "Weather: " + main + "\n" + "Description: " + description);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        };
    }

    public static Handler getHandler(){ return handler; }


    //Gets the city's name from plain text and starts weatherApiDownloader
    //To be safe, string value of city is encoded
    public void searchWeather(View view){
        try{
            String cityToSearch = cityEditText.getText().toString();
            String encodedString = URLEncoder.encode(cityToSearch, "UTF-8");
            WeatherApiDownloader weatherApiDownloader = new WeatherApiDownloader(getApplicationContext(),"https://openweathermap.org/data/2.5/weather?q="+ encodedString +"&appid=439d4b804bc8187953eb36d2a8c26a02");
            weatherApiDownloader.start();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_SHORT).show();
        }

    }

}