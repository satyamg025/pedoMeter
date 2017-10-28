package com.a2017002.optimustechproject.optimus_tech_project_2017002.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a2017002.optimustechproject.optimus_tech_project_2017002.Interface.StoreActivityRequest;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.R;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.LoginDataumPOJO;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.StoreActivityPOJO;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.networking.ServiceGenerator;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.util.DbHandler;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.util.SimpleStepDetector;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.Interface.StepListener;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    ProgressDialog progressDialog;

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

                TimeBuff += MillisecondTime;

                handler.removeCallbacks(runnable);

                stop.setEnabled(true);

                pause.setText("RESUME");
                pause_boo=true;

                new AlertDialog.Builder(PedoActivity.this).setCancelable(false)
                        .setMessage("Are you sure you want to stop this acitvity?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                progressDialog=new ProgressDialog(PedoActivity.this);
                                progressDialog.setIndeterminate(true);
                                progressDialog.setMessage("Loading...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                String dateString =String.valueOf(new Date());
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date convertedDate = new Date();
                                try {
                                    convertedDate=dateFormat.parse(dateString);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                StoreActivityRequest storeActivityRequest= ServiceGenerator.createService(StoreActivityRequest.class,DbHandler.getString(PedoActivity.this,"bearer",""));
                                Call<StoreActivityPOJO> call=storeActivityRequest.requestResponse(distance_tv.getText().toString(),speed_tv.getText().toString(),time_tv.getText().toString(),String.valueOf(convertedDate));
                                call.enqueue(new Callback<StoreActivityPOJO>() {
                                    @Override
                                    public void onResponse(Call<StoreActivityPOJO> call, Response<StoreActivityPOJO> response) {

                                        progressDialog.dismiss();
                                        if(response.code()==200){
                                            if(response.body().getError()){
                                                DbHandler.unsetSession(PedoActivity.this,"isForcedLoggedOut");
                                            }
                                            else{
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
                                                pause.setEnabled(false);

                                                Intent intent=new Intent(PedoActivity.this,ShareActivity.class);

                                                intent.putExtra("distance",distance_tv.getText().toString());
                                                intent.putExtra("speed",speed_tv.getText().toString());
                                                intent.putExtra("time",time_tv.getText().toString());
                                                intent.putExtra("date",String.valueOf(new Date()));

                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                        else{

                                            new AlertDialog.Builder(PedoActivity.this)
                                                    .setCancelable(false)
                                                    .setTitle("Network Error")
                                                    .setMessage("There was some error in connecting to server\nPlease check your internet connection to store your activity\nelse you will lose your activity data")
                                                    .setPositiveButton("Connect to internet", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

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
                                                            pause.setEnabled(false);

                                                            Intent intent=new Intent(PedoActivity.this,ShareActivity.class);
                                                            intent.putExtra("distance",distance_tv.getText().toString());
                                                            intent.putExtra("speed",speed_tv.getText().toString());
                                                            intent.putExtra("time",time_tv.getText().toString());
                                                            intent.putExtra("date",String.valueOf(new Date()));
                                                            startActivity(intent);

                                                            finish();

                                                        }
                                                    }).create().show();

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<StoreActivityPOJO> call, Throwable t) {

                                        progressDialog.dismiss();
                                        new AlertDialog.Builder(PedoActivity.this)
                                                .setCancelable(false)
                                                .setTitle("Network Error")
                                                .setMessage("There was some error in connecting to server\nPlease check your internet connection to store your activity\nelse you will lose your activity data")
                                                .setPositiveButton("Connect to internet", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

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
                                                        pause.setEnabled(false);

                                                        Intent intent=new Intent(PedoActivity.this,ShareActivity.class);
                                                        intent.putExtra("distance",distance_tv.getText().toString());
                                                        intent.putExtra("speed",speed_tv.getText().toString());
                                                        intent.putExtra("time",time_tv.getText().toString());
                                                        intent.putExtra("date",String.valueOf(new Date()));
                                                        startActivity(intent);

                                                        finish();

                                                    }
                                                }).create().show();

                                    }
                                });


                            }
                        }).create().show();

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

            distance_tv.setText(String.format("%.2f",distance)+" m");

            speed_tv.setText(String.format("%.2f",Float.valueOf(distance)/(Float.valueOf(Seconds)))+" m/s");
            //Toast.makeText(PedoActivity.this,String.valueOf(Float.valueOf("0.415")*Float.valueOf(data.getHeight())),Toast.LENGTH_LONG).show();

            handler.postDelayed(this, 0);
        }

    };


    @Override
    public void onResume() {
        super.onResume();
       // numSteps = 0;
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onPause() {
        super.onPause();
       // sensorManager.unregisterListener(this);
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
