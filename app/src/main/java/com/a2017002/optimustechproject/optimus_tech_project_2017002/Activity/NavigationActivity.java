package com.a2017002.optimustechproject.optimus_tech_project_2017002.Activity;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a2017002.optimustechproject.optimus_tech_project_2017002.R;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.LoginDataumPOJO;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.util.DbHandler;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.util.GPSTracker;
import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView image;
    TextView name,distance_tv,time_tv,speed_tv;
    LoginDataumPOJO data;
    Gson gson=new Gson();
    Button start,stop,pause;
    GPSTracker gpsTracker;
    Geocoder geocoder;

    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;

    Handler handler;
    Location locationA = new Location("point A");
    Location locationB = new Location("point B");

    int Seconds, Minutes, MilliSeconds,hours ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gpsTracker=new GPSTracker(this);

        start=(Button)findViewById(R.id.start);
        stop=(Button)findViewById(R.id.stop);
        pause=(Button)findViewById(R.id.pause);

        stop.setEnabled(false);
        pause.setEnabled(false);

        handler=new Handler();

        distance_tv=(TextView)findViewById(R.id.distance);
        time_tv=(TextView)findViewById(R.id.time);
        speed_tv=(TextView)findViewById(R.id.speed);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        image=(ImageView)hView.findViewById(R.id.image);
        name=(TextView)hView.findViewById(R.id.name);

        data=gson.fromJson(DbHandler.getString(NavigationActivity.this,"login_data",""),LoginDataumPOJO.class);
        name.setText(data.getFirstName()+" "+data.getLastName());
        if(data.getGender().equals("M")){
            image.setImageDrawable(getResources().getDrawable(R.drawable.male_account));
        }
        else if(data.getGender().equals("F")){
            image.setImageDrawable(getResources().getDrawable(R.drawable.female_account));
        }

        geocoder=new Geocoder(NavigationActivity.this, Locale.getDefault());
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(gpsTracker.canGetLocation()){
                    List<Address> addresses;
                    try {
                        addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
                        if(addresses.size()!=0) {
                            Toast.makeText(NavigationActivity.this,String.valueOf(gpsTracker.getLatitude())+" "+String.valueOf(gpsTracker.getLongitude()),Toast.LENGTH_LONG).show();
                            locationA.setLatitude(gpsTracker.getLatitude());
                            locationA.setLongitude(gpsTracker.getLongitude());
                            locationB.setLatitude(gpsTracker.getLatitude());
                            locationB.setLongitude(gpsTracker.getLongitude());
                        }
                        else{
                            Toast.makeText(NavigationActivity.this,"Unable to get location.. Try again later ",Toast.LENGTH_LONG).show();
                        }

                    }
                    catch (Exception e) {
                        Toast.makeText(NavigationActivity.this,"Unable to get location.. Try again later ",Toast.LENGTH_LONG).show();

                        e.printStackTrace();

                    }
                }
                else
                {
                    gpsTracker.showSettingsAlert();
                }

                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);

                stop.setEnabled(true);
                pause.setEnabled(true);
                start.setEnabled(false);

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pause.getText().equals("PAUSE")) {
                    TimeBuff += MillisecondTime;

                    handler.removeCallbacks(runnable);

                    stop.setEnabled(true);

                    pause.setText("RESUME");
                }
                else{
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);

                    pause.setText("PAUSE");

                }


            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                handler.removeCallbacks(runnable);

                MillisecondTime = 0L ;
                StartTime = 0L ;
                TimeBuff = 0L ;
                UpdateTime = 0L ;
                Seconds = 0 ;
                Minutes = 0 ;
                MilliSeconds = 0 ;

                pause.setText("PAUSE");
                stop.setEnabled(false);
                pause.setEnabled(false);
                start.setEnabled(true);

            }
        });

    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            if(Seconds%5==0){
                getLocation();
            }

            Minutes = Seconds / 60;

            hours=Minutes/60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            time_tv.setText(""+String.format("%02d",hours)+":" +String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));


            float distance = locationA.distanceTo(locationB);
            //Toast.makeText(NavigationActivity.this,String.valueOf(distance/1000),Toast.LENGTH_LONG).show();

            distance_tv.setText(String.valueOf(distance/1000)+" km");


            speed_tv.setText(String.valueOf(distance/Seconds)+" m/s");

            handler.postDelayed(this, 0);
        }

    };

    public void getLocation(){
        if(gpsTracker.canGetLocation()){
            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
                if(addresses.size()!=0) {
                    Toast.makeText(NavigationActivity.this,String.valueOf(gpsTracker.getLatitude())+" "+String.valueOf(gpsTracker.getLongitude()),Toast.LENGTH_LONG).show();
                    locationB.setLatitude(gpsTracker.getLatitude());
                    locationB.setLongitude(gpsTracker.getLongitude());
                }
                else{
                    Toast.makeText(NavigationActivity.this,"Unable to get location.. Try again later ",Toast.LENGTH_LONG).show();
                }

            }
            catch (Exception e) {
                Toast.makeText(NavigationActivity.this,"Unable to get location.. Try again later ",Toast.LENGTH_LONG).show();

                e.printStackTrace();

            }
        }
        else
        {
            gpsTracker.showSettingsAlert();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
