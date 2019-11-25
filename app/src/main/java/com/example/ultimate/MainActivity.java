package com.example.ultimate;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;


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


        // mySensor= SM.getDefaultSensor(Sensor.TYPE_ALL);

    }
    public void StartSensor(View view){
        Toast.makeText(this, "Started", Toast.LENGTH_SHORT).show();



        // mySensor= SM.getDefaultSensor(Sensor.TYPE_ALL);
        SM.registerListener((SensorEventListener) this, SA, SensorManager.SENSOR_DELAY_GAME);
        SM.registerListener((SensorEventListener) this,SG,SensorManager.SENSOR_DELAY_GAME);


    }
    public void StopSensor(View view){
        Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
        SM.unregisterListener(this, SA);
        SM.unregisterListener(this, SG);

        String aString= acc.toString();
        String gString= gyr.toString();
        Log.i(TAG,aString);


    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        String sensorName = event.sensor.getName();
        Toast.makeText(this, sensorName, Toast.LENGTH_SHORT).show();

      //  Log.i(TAG,sensorName + ": X: " + event.values[0] + "; Y: " + event.values[1] + "; Z: " + event.values[2] + ";");
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
