package com.example.worldcityweathers;


import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class WeatherApiDownloader extends Thread {

    String stringUrl;
    String result;
    private Context context;

    public WeatherApiDownloader(Context context, String url){
        this.context = context;
        this.stringUrl = url;
        result = "";
    }

    @Override
    public void run(){

        //Reads api that obtained from constructor.
        try {
            URL url = new URL(stringUrl);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            InputStream in = httpsURLConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();

            while(data != -1){
                char currentResult = (char) data;
                result += currentResult;
                data = reader.read();
            }

            Message message = MainActivity.getHandler().obtainMessage();
            message.what = 0;
            message.obj = result;

            message.sendToTarget();



        }catch (Exception e){
            e.printStackTrace();
            Looper.prepare();
            Toast.makeText(context, "Could not find weather", Toast.LENGTH_SHORT).show();

        }
    }
}
