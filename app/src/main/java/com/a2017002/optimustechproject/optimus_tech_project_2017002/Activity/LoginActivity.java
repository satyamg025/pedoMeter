package com.a2017002.optimustechproject.optimus_tech_project_2017002.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.a2017002.optimustechproject.optimus_tech_project_2017002.Interface.LoginRequest;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.R;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.LoginDataPOJO;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.LoginDataumPOJO;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.networking.ServiceGenerator;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.util.ColoredSnackbar;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.util.DbHandler;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.util.NetworkCheck;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class LoginActivity extends AppCompatActivity {

    Button register,login;
    Toolbar toolbar;
    EditText username;
    EditText password;
    Gson gson=new Gson();
    ProgressDialog progressDialog;
    private boolean doubleBackToExitPressedOnce = false;
    private ColoredSnackbar coloredSnackBar;
    private View.OnClickListener snackBarListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            login();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Login");

        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);

        login=(Button)findViewById(R.id.login);

        register=(Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("")){
                    username.setError("Username required");
                }
                if(password.getText().toString().equals("")){
                    password.setError("Password required");
                }

                if(!username.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    login();
                }


            }
        });
    }

    public void login(){

        if(NetworkCheck.isNetworkAvailable(LoginActivity.this)){

            progressDialog=new ProgressDialog(LoginActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Authenticating");
            progressDialog.show();

            LoginRequest loginRequest= ServiceGenerator.createService(LoginRequest.class,username.getText().toString(),password.getText().toString());
            Observable<LoginDataPOJO> observable=loginRequest.requestResponse(FirebaseInstanceId.getInstance().getToken());
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<LoginDataPOJO>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    progressDialog.dismiss();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error connecting to server", Snackbar.LENGTH_SHORT);
                    coloredSnackBar.warning(snackbar).show();

                }

                @Override
                public void onNext(LoginDataPOJO loginDataPOJO) {
                    progressDialog.dismiss();
                    LoginDataumPOJO data=loginDataPOJO.getData();
                    if(!loginDataPOJO.getErrror()){
                        Toast.makeText(LoginActivity.this,loginDataPOJO.getMessage(),Toast.LENGTH_LONG).show();
                        DbHandler.setSession(LoginActivity.this,gson.toJson(data),data.getKey());
                    }
                    else{
                        new AlertDialog.Builder(LoginActivity.this)
                                .setMessage(loginDataPOJO.getMessage())
                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // onBackPressed();
                                    }
                                });

                    }

                }

            });

        }

        else{
            Snackbar snackbar=Snackbar.make(findViewById(android.R.id.content),"No internet connection",Snackbar.LENGTH_LONG).setAction("Retry", snackBarListener);
            coloredSnackBar.alert(snackbar).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Press back again to exit", Snackbar.LENGTH_SHORT);
        coloredSnackBar.warning(snackbar).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


}
