package com.example.ultimate;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
  private static final String TAG = "MainActivity";
    TextView textresponse;

    private Sensor SA;
    private Sensor SG;
    private SensorManager SM;
    JSONObject acc;
    JSONObject gyr;

    DB a;
    Cursor r1;
    Cursor r2;
    String logged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SM =(SensorManager)getSystemService(SENSOR_SERVICE);
        SA = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SG = SM.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        acc= new JSONObject();
        gyr= new JSONObject();
        textresponse = findViewById(R.id.textresponse);

        a = new DB(this);

        r1 = a.getLogs();
        r2 = a.getInfo();
        Integer m = new Integer(r2.getCount());
        String n = m.toString();

        Toast.makeText(this, "User count: " + n, Toast.LENGTH_SHORT).show();
    }

    public void Login(View view){

        EditText start1 = findViewById(R.id.uname);
        String user = start1.getText().toString();
        EditText start2 = findViewById(R.id.upass);
        String pass = start2.getText().toString();

        if (user.length() == 0 || pass.length() == 0) {
            Toast.makeText(this, "Some Error", Toast.LENGTH_SHORT).show();
            return;
        }

        int p = 1;

        r2.moveToFirst();
        r2.moveToPrevious();
        while(r2.moveToNext()) {
            String uname = r2.getString(1);
            String upass = r2.getString(2);
            if (user.equals(uname) && pass.equals(upass)) {
                Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show();
                logged = uname;
                p = 2;
            }
        }
        r2.moveToFirst();
        r2.moveToPrevious();

        if (p == 1) {
            Toast.makeText(this, "No user", Toast.LENGTH_SHORT).show();
            return;
        }

        start1.setVisibility(View.GONE);

        View start = findViewById(R.id.start);
        start.setVisibility(View.VISIBLE);
        View stop = findViewById(R.id.stop);
        stop.setVisibility(View.VISIBLE);
        View login = findViewById(R.id.signin);
        login.setVisibility(View.GONE);
        View signup = findViewById(R.id.signup);
        signup.setVisibility(View.GONE);

        start2.setVisibility(View.GONE);

    }

    public void SignUp(View view){

        EditText start1 = findViewById(R.id.uname);
        String user = start1.getText().toString();
        EditText start2 = findViewById(R.id.upass);
        String pass = start2.getText().toString();

        if (user.length() == 0 || pass.length() == 0) {
            Toast.makeText(this, "Some Error", Toast.LENGTH_SHORT).show();
            return;
        }

        r2.moveToFirst();
        r2.moveToPrevious();
        while(r2.moveToNext()) {
            String uname = r2.getString(1);
            if (user.equals(uname)) {
                Toast.makeText(this, "User Exists", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        r2.moveToFirst();
        r2.moveToPrevious();

        a = new DB(this);
        ContentValues c1 = new ContentValues();
        c1.put("uname",user);
        c1.put("upass",pass);
        boolean rec1 = a.infoInsert(c1,"info");
        if (rec1) {
            Toast.makeText(this, "New User", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "New User not created", Toast.LENGTH_SHORT).show();
        }

        start1.setVisibility(View.GONE);

        View start = findViewById(R.id.start);
        start.setVisibility(View.VISIBLE);
        View stop = findViewById(R.id.stop);
        stop.setVisibility(View.VISIBLE);
        View login = findViewById(R.id.signin);
        login.setVisibility(View.GONE);
        View signup = findViewById(R.id.signup);
        signup.setVisibility(View.GONE);

        start2.setVisibility(View.GONE);

    }


    public void StartSensor(View view){

        Toast.makeText(this, "Started", Toast.LENGTH_SHORT).show();
        SM.registerListener((SensorEventListener) this, SA, SensorManager.SENSOR_DELAY_GAME);
        SM.registerListener((SensorEventListener) this,SG,SensorManager.SENSOR_DELAY_GAME);
        textresponse.setText("Collecting Sensor Data");


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
        Log.i("STRING MSG" ,jsonArray.toString() );
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

                        Log.i("STRING MSG" ,response.toString() );

                        textresponse.setText("Activity detected: " + response.toString());

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
