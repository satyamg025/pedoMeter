package com.a2017002.optimustechproject.optimus_tech_project_2017002.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.a2017002.optimustechproject.optimus_tech_project_2017002.Activity.LoginActivity;
import com.a2017002.optimustechproject.optimus_tech_project_2017002.app.Config;
import com.google.firebase.messaging.FirebaseMessaging;


public class DbHandler {

    public static void putInt(Context context, String Key, int value) {
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(Key, value);
            editor.commit();
        }
    }

    public static void putString(Context context, String Key, String value) {
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Key, value);
            editor.commit();
        }
    }

    public static void putBoolean(Context context, String Key, Boolean value) {
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(Key, value);
            editor.commit();
        }
    }

    public static Boolean contains(Context context,String key){
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);
            return prefs.contains(key);
        } else return null;
    }

    public static int getInt(Context context, String Key, int Alternate) {
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);

            return prefs.getInt(Key, Alternate);
        } else return 0;
    }

    public static String getString(Context context, String Key, String Alternate) {
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);
            return prefs.getString(Key, Alternate);
        } else return null;
    }

    public static Boolean getBoolean(Context context, String Key, Boolean Alternate) {
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(Config.DB_NAME, Context.MODE_PRIVATE);
            return prefs.getBoolean(Key, Alternate);
        } else return false;
    }
    public static void remove(Context context,String key){
        if(context!=null){
            SharedPreferences prefs;
            prefs=context.getSharedPreferences(Config.DB_NAME,Context.MODE_PRIVATE);
            if(DbHandler.contains(context,key)) {
                prefs.edit().remove(key).commit();
            }
        }
    }

    public static void clearDb(Context context) {
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences(Config.DB_NAME, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
        }
    }

    public static void setSession(Context context, String login_data, String bearer) {
        if (context != null) {
            FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
            DbHandler.putString(context, "login_data", login_data);
            DbHandler.putString(context, "bearer", bearer);
            DbHandler.putBoolean(context, "isLoggedIn", true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void unsetSession(Context context, String type) {
        if (context != null) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(Config.TOPIC_GLOBAL);
            DbHandler.clearDb(context);
            DbHandler.putBoolean(context, "isLoggedIn", false);
            Bundle b = new Bundle();
            b.putBoolean(type, true);
            Intent i = new Intent(context, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtras(b);
            context.startActivity(i);
            ((Activity) context).finishAffinity();
        }
    }
}
