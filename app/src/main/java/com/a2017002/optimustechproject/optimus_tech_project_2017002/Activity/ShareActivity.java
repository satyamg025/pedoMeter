package com.a2017002.optimustechproject.optimus_tech_project_2017002.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.a2017002.optimustechproject.optimus_tech_project_2017002.R;

public class ShareActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView distance,speed,duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Recent Activity Done");
        setSupportActionBar(toolbar);

        distance=(TextView)findViewById(R.id.distance);
        speed=(TextView)findViewById(R.id.speed);
        duration=(TextView)findViewById(R.id.duration);

        distance.setText(getIntent().getExtras().getString("distance"));
        speed.setText(getIntent().getExtras().getString("speed"));
        duration.setText(getIntent().getExtras().getString("time"));

    }
}
