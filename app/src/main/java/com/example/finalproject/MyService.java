package com.example.finalproject;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
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

import android.widget.Button;
import android.widget.TextView;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import static android.app.Service.START_STICKY;

public class MyService extends MainActivity implements SensorEventListener {
        private static final String TAG = "MyService";
        private TextView Xtext, Ytext, Ztext, ntext, textX, textY, textZ;
        // private Sensor mySensor;
        private Sensor SA;
        private Sensor SG;

        private SensorManager SM;
        private boolean isRunning  = false;
        private Looper looper;
        private MyServiceHandler myServiceHandler;
        //@Override
        public void onCreate() {
            HandlerThread handlerthread = new HandlerThread("MyThread", Process.THREAD_PRIORITY_BACKGROUND);
            handlerthread.start();
            looper = handlerthread.getLooper();
            myServiceHandler = new MyServiceHandler(looper);

            SM =(SensorManager)getSystemService(SENSOR_SERVICE);
            SA = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            SG = SM.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

            // mySensor= SM.getDefaultSensor(Sensor.TYPE_ALL);
            SM.registerListener(this, SA, SensorManager.SENSOR_DELAY_GAME);
            SM.registerListener(this,SG,SensorManager.SENSOR_DELAY_GAME);
            Log.i(TAG,"onCreate: Registered sensors Listener");
            Xtext= (TextView)findViewById(R.id.Xtext);
            Ytext= (TextView)findViewById(R.id.Ytext);
            Ztext= (TextView)findViewById(R.id.Ztext);
            // ntext= (TextView)findViewById(R.id.ntext);
            textX= (TextView)findViewById(R.id.textX);
            textY= (TextView)findViewById(R.id.textY);
            textZ= (TextView)findViewById(R.id.textZ);
            isRunning = true;
        }
       // @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Message msg = myServiceHandler.obtainMessage();
            msg.arg1 = startId;
            myServiceHandler.sendMessage(msg);
            Toast.makeText(this, "MyService Started.", Toast.LENGTH_SHORT).show();
           // Log.i(TAG, sensorName + ": X: " + event.values[0] + "; Y: " + event.values[1] + "; Z: " + event.values[2] + ";");
            Log.i(TAG,"onCreate: Registered sensors Listener");
            //If service is killed while starting, it restarts.
            return START_STICKY;
        }
       // @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            Xtext.setText("X: " + event.values[0]);
            Ytext.setText("Y: " + event.values[1]);
            Ztext.setText("Z: " + event.values[2]);
            // ntext.setText (event.sensor.getName());
            String sensorName = event.sensor.getName();

            Log.i(TAG, sensorName + ": X: " + event.values[0] + "; Y: " + event.values[1] + "; Z: " + event.values[2] + ";");

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            textX.setText("GX : " + (int) x + " rad/s");
            textY.setText("GY : " + (int) y + " rad/s");
            textZ.setText("GZ : " + (int) z + " rad/s");
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
        @Override
        public void onDestroy() {
            super.onDestroy();
            isRunning = false;
            Toast.makeText(this, "MyService Completed or Stopped.", Toast.LENGTH_SHORT).show();
        }
        private final class MyServiceHandler extends Handler {
            public MyServiceHandler(Looper looper) {
                super(looper);
            }
            @Override
            public void handleMessage(Message msg) {
                synchronized (this) {
                    for (int i = 0; i < 10; i++) {
                        try {
                            Log.i(TAG, "MyService running...");
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            Log.i(TAG, e.getMessage());
                        }
                        if(!isRunning){
                            break;
                        }
                    }
                }
                //stops the service for the start id.
                stopSelfResult(msg.arg1);
            }

            private void stopSelfResult(int arg1) {
            }
        }
    }


