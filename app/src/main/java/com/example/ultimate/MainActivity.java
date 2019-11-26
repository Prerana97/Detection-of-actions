package com.example.ultimate;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends AppCompatActivity implements SensorEventListener{
  //  private static final String TAG;
  private static final String TAG = "MainActivity";

    private Sensor SA;
    private Sensor SG;
    private SensorManager SM;
    JSONObject acc;
    JSONObject gyr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SM =(SensorManager)getSystemService(SENSOR_SERVICE);
        SA = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SG = SM.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        acc= new JSONObject();
        gyr= new JSONObject();


    }
    public void StartSensor(View view){

        Toast.makeText(this, "Started", Toast.LENGTH_SHORT).show();
        SM.registerListener((SensorEventListener) this, SA, SensorManager.SENSOR_DELAY_GAME);
        SM.registerListener((SensorEventListener) this,SG,SensorManager.SENSOR_DELAY_GAME);


    }
    public void StopSensor(View view) throws JSONException {

        Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
        SM.unregisterListener(this, SA);
        SM.unregisterListener(this, SG);
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(acc);
        jsonArray.put(gyr);
        acc=new JSONObject();
        gyr=new JSONObject();
        sendPost(jsonArray);
           }
    public void sendPost(final JSONArray jsonArray) {
        Thread thread = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                try {
                    URL url = new URL("http://atapadiy.pythonanywhere.com/predict");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);


                    System.out.println(jsonArray);
                    DataOutputStream os = null;
                    os=new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonArray.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());
                    try(BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine = null;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        System.out.println(response.toString());
                        Log.i("STRING MSG" ,response.toString() );
                        String res=response.toString();
                        //Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String sensorName = event.sensor.getName();
        Toast.makeText(this, sensorName, Toast.LENGTH_SHORT).show();

       try{
            if (sensorName== "BMI160_ACCELEROMETER Accelerometer Non-wakeup")
            {
                acc.accumulate("ax",event.values[0]);
                acc.accumulate("ay", event.values[1]);
                acc.accumulate("az", event.values[2]);
            }
            else
            {
                gyr.accumulate("gx", event.values[0]);
                gyr.accumulate("gy", event.values[1]);
                gyr.accumulate("gz",event.values[2]);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
