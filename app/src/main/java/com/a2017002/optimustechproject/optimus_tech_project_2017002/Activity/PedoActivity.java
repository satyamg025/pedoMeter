package com.a2017002.optimustechproject.optimus_tech_project_2017002.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a2017002.optimustechproject.optimus_tech_project_2017002.R;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.LoginDataumPOJO;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.util.DbHandler;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.util.SimpleStepDetector;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.util.StepListener;
import com.google.gson.Gson;

@SuppressLint("HandlerLeak")
public class PedoActivity extends AppCompatActivity implements SensorEventListener,StepListener {

    TextView  distance_tv,time_tv,speed_tv;
    Handler handler;
    Button pause,stop;
    ImageView music;
   // MediaPlayer player;

    private SimpleStepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private int numSteps;

    LoginDataumPOJO data;
    Gson gson=new Gson();

    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;

    int Seconds, Minutes, MilliSeconds,hours ;
    Boolean pause_boo=false,stop_boo=false;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedo);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Activity");
        setSupportActionBar(toolbar);
        handler=new Handler();

        //player = MediaPlayer.create(this, R.);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new SimpleStepDetector();
        simpleStepDetector.registerListener(this);

        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);

        pause=(Button)findViewById(R.id.pause);
        stop=(Button)findViewById(R.id.stop);
        distance_tv=(TextView)findViewById(R.id.distance);
        time_tv=(TextView)findViewById(R.id.time);
        speed_tv=(TextView)findViewById(R.id.speed);
        music=(ImageView)findViewById(R.id.music);

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
                startActivity(intent);
            }
        });

        data=gson.fromJson(DbHandler.getString(this,"login_data","{}"),LoginDataumPOJO.class);

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pause.getText().equals("PAUSE")) {
                    TimeBuff += MillisecondTime;

                    handler.removeCallbacks(runnable);

                    stop.setEnabled(true);

                    pause.setText("RESUME");
                    pause_boo=true;
                }
                else{
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);

                    pause.setText("PAUSE");

                    pause_boo=false;
                }


            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(stop.getText().toString().equals("STOP")) {

                    handler.removeCallbacks(runnable);

                    MillisecondTime = 0L;
                    StartTime = 0L;
                    TimeBuff = 0L;
                    UpdateTime = 0L;
                    Seconds = 0;
                    Minutes = 0;
                    MilliSeconds = 0;

                    stop_boo = true;

                    pause.setText("PAUSE");
                    //stop.setEnabled(false);
                    pause.setEnabled(false);

                    stop.setText("START");
                }
                else{
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);

                    stop_boo=false;
                    pause_boo=false;
                    pause.setEnabled(true);
                }

            }
        });


    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            hours=Minutes/60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            time_tv.setText(""+String.format("%02d",hours)+":" +String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));


            float distance = (Float.valueOf("0.415")*Float.valueOf(data.getHeight())*numSteps)/100;
            //Toast.makeText(PedoActivity.this,String.valueOf(Float.valueOf("0.415")*Float.valueOf(data.getHeight())),Toast.LENGTH_LONG).show();

            distance_tv.setText(String.format("%02f",distance)+" m");

            speed_tv.setText(String.format("%02f",Float.valueOf(distance)/(Float.valueOf(Seconds)))+" m/s");
            //Toast.makeText(PedoActivity.this,String.valueOf(Float.valueOf("0.415")*Float.valueOf(data.getHeight())),Toast.LENGTH_LONG).show();

            handler.postDelayed(this, 0);
        }

    };


    @Override
    public void onResume() {
        super.onResume();
        numSteps = 0;
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        if(!stop_boo) {
            if (!pause_boo) {
                numSteps++;
                //Toast.makeText(PedoActivity.this,String.valueOf(numSteps),Toast.LENGTH_SHORT).show();
            }
        }else{
            numSteps=0;
        }
    }
}
