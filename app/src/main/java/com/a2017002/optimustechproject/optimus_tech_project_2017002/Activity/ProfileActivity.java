package com.a2017002.optimustechproject.optimus_tech_project_2017002.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.a2017002.optimustechproject.optimus_tech_project_2017002.Interface.ProfileUpdateRequest;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.Interface.RegistrationRequest;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.R;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.LoginDataumPOJO;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.ProfileUpdatePOJO;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.RegDataPOJO;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.RegDataumPOJO;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.networking.ServiceGenerator;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.util.ColoredSnackbar;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.util.DbHandler;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.util.NetworkCheck;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText first_name,last_name,dob,mobile,weight;
    ImageView img,male,female;
    String gender="J";
    AppCompatButton submit;
    SeekBar height;
    TextView height_tv;
    LoginDataumPOJO data;
    Calendar myCalendar = Calendar.getInstance();
    ProgressDialog progressDialog;
    private ColoredSnackbar coloredSnackbar;
    Gson gson=new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        first_name=(EditText)findViewById(R.id.first_name);
        last_name=(EditText)findViewById(R.id.last_name);
        dob=(EditText)findViewById(R.id.dob);
        mobile=(EditText)findViewById(R.id.mobile);
        weight=(EditText) findViewById(R.id.weight);
        height=(SeekBar) findViewById(R.id.height);
        height_tv=(TextView)findViewById(R.id.height_tv);

        submit=(AppCompatButton)findViewById(R.id.register);
        submit.setText("Update");

        data=gson.fromJson(DbHandler.getString(this,"login_data","{}"),LoginDataumPOJO.class);

        img=(ImageView)findViewById(R.id.img);
        male=(ImageView)findViewById(R.id.male);
        female=(ImageView)findViewById(R.id.female);

        height.setMax(220);

        first_name.setText(data.getFirstName());
        last_name.setText(data.getLastName());
        dob.setText(data.getDob());
        mobile.setText(data.getMobile());

        if(data.getGender().equals("M")){
            male.setColorFilter(getResources().getColor(R.color.colorAccent));
            gender="M";
            female.setColorFilter(null);
            img.setImageDrawable(getResources().getDrawable(R.drawable.male_account));
        }
        else if(data.getGender().equals("F")){
            female.setColorFilter(getResources().getColor(R.color.colorAccent));
            gender="F";
            male.setColorFilter(null);
            img.setImageDrawable(getResources().getDrawable(R.drawable.female_account));
        }

        weight.setText(data.getWeight()+" kg");
        height.setProgress(Integer.valueOf(data.getHeight()));
        height_tv.setText(data.getHeight()+" cm");

        height.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                height_tv.setText(String.valueOf(progress)+" cm");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy";

                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                dob.setText(sdf.format(myCalendar.getTime()));

            }

        };


        dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setColorFilter(getResources().getColor(R.color.colorAccent));
                gender="M";
                female.setColorFilter(null);
                img.setImageDrawable(getResources().getDrawable(R.drawable.male_account));
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female.setColorFilter(getResources().getColor(R.color.colorAccent));
                gender="F";
                male.setColorFilter(null);
                img.setImageDrawable(getResources().getDrawable(R.drawable.female_account));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(first_name.getText().toString().equals("")){
                    first_name.setError("First name required");
                }
                if(last_name.getText().toString().equals("")){
                    last_name.setError("Last name required");
                }

                if(dob.getText().toString().equals("")){
                    dob.setError("DOB required");
                }
                if(mobile.getText().toString().equals("")){
                    mobile.setError("Mobile required");
                }
                if(weight.getText().toString().equals("")){
                    weight.setError("Weight required");
                }
                if(height_tv.getText().toString().equals("0 cm")){
                    Toast.makeText(ProfileActivity.this,"Height requires",Toast.LENGTH_LONG).show();
                }
                if(gender.equals("J")){
                    Toast.makeText(ProfileActivity.this,"Select your gender",Toast.LENGTH_LONG).show();
                }
                if(!first_name.getText().toString().equals("") && !last_name.getText().toString().equals("")  && !dob.getText().toString().equals("") && !mobile.getText().toString().equals("") && !gender.equals("J") && !weight.getText().toString().equals("") && !height_tv.getText().toString().equals("0 cm")){
                    profile_update();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void profile_update(){
        if(NetworkCheck.isNetworkAvailable(ProfileActivity.this)){
            progressDialog=new ProgressDialog(ProfileActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.setCancelable(false);
            progressDialog.show();

            final ProfileUpdateRequest profileUpdateRequest= ServiceGenerator.createService(ProfileUpdateRequest.class,DbHandler.getString(ProfileActivity.this,"bearer",""));
            Call<ProfileUpdatePOJO> call=profileUpdateRequest.requestResponse(first_name.getText().toString(),last_name.getText().toString(),dob.getText().toString(),gender,mobile.getText().toString(), String.valueOf(height.getProgress()),weight.getText().toString());
            call.enqueue(new Callback<ProfileUpdatePOJO>() {
                @Override
                public void onResponse(Call<ProfileUpdatePOJO> call, Response<ProfileUpdatePOJO> response) {
                    progressDialog.dismiss();
                    if(response.code()==200){
                        if(!response.body().getErrror()){
                            Toast.makeText(ProfileActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                            DbHandler.setSession(ProfileActivity.this,gson.toJson(response.body().getData()),response.body().getData().getKey());
                            Intent intent = new Intent(ProfileActivity.this, NavigationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            DbHandler.unsetSession(ProfileActivity.this,"isForcedLoggedOut");

                        }
                    }
                    else{
                        Toast.makeText(ProfileActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                        DbHandler.setSession(ProfileActivity.this,gson.toJson(response.body().getData()),response.body().getData().getKey());
                        Intent intent = new Intent(ProfileActivity.this, NavigationActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ProfileUpdatePOJO> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("error",String.valueOf(t));
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error connecting to server", Snackbar.LENGTH_SHORT);
                    coloredSnackbar.warning(snackbar).show();
                }
            });
        }
        else{
            Snackbar snackbar=Snackbar.make(findViewById(android.R.id.content),"No internet connection",Snackbar.LENGTH_LONG);
            coloredSnackbar.alert(snackbar).show();
        }

    }
}
