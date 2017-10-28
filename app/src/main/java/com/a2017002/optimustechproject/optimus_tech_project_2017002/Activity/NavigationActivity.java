package com.a2017002.optimustechproject.optimus_tech_project_2017002.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.a2017002.optimustechproject.optimus_tech_project_2017002.Interface.ActivityDataRequest;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.R;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.fragments.CalendarFragment;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.fragments.StartActivityFragment;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.fragments.VideoFragment;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.ActivityDataPOJO;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.models.LoginDataumPOJO;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.networking.ServiceGenerator;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.util.ColoredSnackbar;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.util.DbHandler;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.util.ImageTransform;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.util.NetworkCheck;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView image;
    TextView name;
    LoginDataumPOJO data;
    Gson gson=new Gson();
    Boolean doubleBackToExitPressedOnce=false;
    Handler handler;
    NavigationView navigationView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        handler = new Handler();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        image = (ImageView) hView.findViewById(R.id.image);
        name = (TextView) hView.findViewById(R.id.name);

        data = gson.fromJson(DbHandler.getString(NavigationActivity.this, "login_data", ""), LoginDataumPOJO.class);
        name.setText(data.getFirstName() + " " + data.getLastName());
        if (data.getGender().equals("M")) {
            Picasso.with(this).load(R.drawable.male_account).error(R.drawable.ic_account_circle_black_24dp).transform(new ImageTransform()).into(image);
            //image.setImageDrawable(getResources().getDrawable(R.drawable.male_account));
        } else if (data.getGender().equals("F")) {
            Picasso.with(this).load(R.drawable.female_account).error(R.drawable.ic_account_circle_black_24dp).transform(new ImageTransform()).into(image);
            //image.setImageDrawable(getResources().getDrawable(R.drawable.female_account));
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NavigationActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });

        if(NetworkCheck.isNetworkAvailable(NavigationActivity.this)){

            progressDialog=new ProgressDialog(NavigationActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            ActivityDataRequest activityDataRequest= ServiceGenerator.createService(ActivityDataRequest.class,DbHandler.getString(this,"bearer",""));
            Call<ActivityDataPOJO> call=activityDataRequest.requestResponse();
            call.enqueue(new Callback<ActivityDataPOJO>() {
                @Override
                public void onResponse(Call<ActivityDataPOJO> call, Response<ActivityDataPOJO> response) {

                    progressDialog.dismiss();
                    if(response.code()==200){
                        if(response.body().getError()){
                            DbHandler.unsetSession(NavigationActivity.this,"isForcedLoggedOut");
                        }
                        else{

                            DbHandler.putString(NavigationActivity.this,"activity_detail",gson.toJson(response.body().getData()));
                            Log.e("data",String.valueOf(response.body().getData().getDate()));
                        }
                    }
                }

                @Override
                public void onFailure(Call<ActivityDataPOJO> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("error",String.valueOf(t));

                }
            });

        }




    Fragment fragment=null;
    Class fragmentClass=null;
    fragmentClass=StartActivityFragment.class;
     FragmentManager fragmentManager = getSupportFragmentManager();
        try {
        fragment = (Fragment) fragmentClass.newInstance();
    } catch (Exception e) {
        e.printStackTrace();
    }
    replaceFragment(fragment);
    getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

        @Override
        public void onBackStackChanged() {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.flContent);
            if (f != null) {
                updateTitleAndDrawer(f);
            }

        }
    });

}

    private void updateTitleAndDrawer(Fragment fragment) {
        String fragClassName = fragment.getClass().getName();
        if(fragClassName.equals(StartActivityFragment.class.getName())){
            navigationView.getMenu().getItem(0).setChecked(true);
            setTitle("Start Activity");
        }
        if(fragClassName.equals(VideoFragment.class.getName())){
            navigationView.getMenu().getItem(1).setChecked(true);
            setTitle("Yoga Videos");
        }
        if(fragClassName.equals(CalendarFragment.class.getName())){
            navigationView.getMenu().getItem(3).setChecked(true);
            setTitle("Calendar View");
        }

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (!(getSupportFragmentManager().getBackStackEntryCount() == 1)) {
                super.onBackPressed();
            } else {

                if (doubleBackToExitPressedOnce) {
                    this.finishAffinity();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;

                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Press once again to exit", Snackbar.LENGTH_SHORT);
                ColoredSnackbar.warning(snackbar).show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        Fragment fragment = null;
        Class fragmentClass=StartActivityFragment.class;

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch(id)
        {
            case R.id.activity:
                fragmentClass=StartActivityFragment.class;
                break;
            case R.id.video:
                fragmentClass=VideoFragment.class;
                break;
            case R.id.calendar:
                fragmentClass= CalendarFragment.class;
                break;
            case R.id.logout:
                drawer.closeDrawer(GravityCompat.START);
                new AlertDialog.Builder(NavigationActivity.this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to Logout?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DbHandler.unsetSession(NavigationActivity.this, "isLoggedOut");
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            default:fragmentClass=StartActivityFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        replaceFragment(fragment);
        if(item.getItemId()!=R.id.logout) {
            item.setChecked(true);
            setTitle(item.getTitle());
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.flContent, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }



}
