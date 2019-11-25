package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
//import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
//import android.view.View;

import android.widget.TextView;

//extra
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import android.view.View;

//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.OutputStream;

//import static java.lang.System.out;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private static final String TAG = "MainActivity";
    private TextView Xtext, Ytext, Ztext, ntext, textX, textY, textZ;
   // private Sensor mySensor;
    private Sensor SA;
    private Sensor SG;

    private SensorManager SM;

    //Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG,"onCreate: Initializing Sensor Services");

        SM =(SensorManager)getSystemService(SENSOR_SERVICE);
        SA = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SG = SM.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

       // mySensor= SM.getDefaultSensor(Sensor.TYPE_ALL);
        SM.registerListener(this, SA, SensorManager.SENSOR_DELAY_NORMAL);
        SM.registerListener(this,SG,SensorManager.SENSOR_DELAY_NORMAL);
        Log.i(TAG,"onCreate: Registered sensors Listener");

        Xtext= (TextView)findViewById(R.id.Xtext);
        Ytext= (TextView)findViewById(R.id.Ytext);
        Ztext= (TextView)findViewById(R.id.Ztext);
       // ntext= (TextView)findViewById(R.id.ntext);
        textX= (TextView)findViewById(R.id.textX);
        textY= (TextView)findViewById(R.id.textY);
        textZ= (TextView)findViewById(R.id.textZ);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
       Xtext.setText("X: "+ event.values[0]);
       Ytext.setText("Y: "+ event.values[1]);
       Ztext.setText("Z: "+ event.values[2]);
      // ntext.setText (event.sensor.getName());
        String sensorName = event.sensor.getName();

        Log.d(TAG, sensorName + ": X: " + event.values[0] + "; Y: " + event.values[1] + "; Z: " + event.values[2] + ";");

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        textX.setText("GX : " + (int) x + " rad/s");
        textY.setText("GY : " + (int) y + " rad/s");
        textZ.setText("GZ : " + (int) z + " rad/s");

       // Log.i(TAG, "onSensorChanged: X: "+ event.values[0] + " Y: "+ event.values[1] + " Z: "+ event.values[2]);
        //timer.scheduleAtFixedRate(new TimerTask() {
        //  @Override
        //  public void run() {
      /*  String FILENAME = "log_weight.csv";
        //  String filePath= Environment.getExternalStorageDirectory().getAbsolutePath()+ "/C:/Users/Prerana";
        String entry = Xtext.getText().toString() + "," + Ytext.getText().toString() + "," + Ztext.getText().toString();
        try {

            FileOutputStream out = openFileOutput(FILENAME, Context.MODE_APPEND);
            out.write(entry.getBytes());
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // }
        // }, 20000, 20000);*/
    }
     /*  timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String FILENAME = "log_weight.csv";
                //  String filePath= Environment.getExternalStorageDirectory().getAbsolutePath()+ "/C:/Users/Prerana";
                String entry= Xtext.getText().toString() + "," + Ytext.getText().toString() + "," + Ztext.getText().toString();
                try{
                    // File dir= new File(filePath);
                    //if(!dir.exists()){
                    //  FileOutputStream out = null;

                    FileOutputStream out= openFileOutput(FILENAME, Context.MODE_APPEND);
                    out.write(entry.getBytes());
                    out.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1000, 20000);*/



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    //  public void savelog() {}

    /*public void appendLog(String text)
    {
        File logFile = new File("C:/Users/Prerana/log_weight.csv");
        String entry= Xtext.getText().toString() + "," + Ytext.getText().toString() + "," + Ztext.getText().toString();
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {

                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(entry);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {

            e.printStackTrace();
        }
    } */


}
