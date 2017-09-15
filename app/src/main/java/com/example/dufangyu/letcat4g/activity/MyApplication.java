package com.example.dufangyu.letcat4g.activity;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.dufangyu.letcat4g.service.TraceServiceImpl;
import com.xdandroid.hellodaemon.DaemonEnv;

/**
 * Created by dufangyu on 2017/6/12.
 */

public class MyApplication extends Application{

    private static MyApplication instance;
    private SharedPreferences preferences;
    private SharedPreferences.Editor meditor;


    @Override
    public void onCreate() {
        super.onCreate();
        instance= this;
        DaemonEnv.initialize(this, TraceServiceImpl.class, DaemonEnv.DEFAULT_WAKE_UP_INTERVAL);
        preferences =instance.getSharedPreferences("settingfile", MODE_PRIVATE);
        meditor = preferences.edit();

    }




    public static MyApplication getInstance(){
        return instance;
    }


    public String getStringPerference(String key)
    {
        return preferences.getString(key,"");
    }

    public void setStringPerference(String key,String value)
    {
        meditor.putString(key,value).commit();
    }

    public void setlongPreference(String key,long value)
    {
        meditor.putLong(key, value).commit();
    }

    public  long getLongPerference(String key)
    {
        return preferences.getLong(key,0L);
    }
    public void setBooleanPerference(String key,boolean value)
    {
        meditor.putBoolean(key,value).commit();
    }

    public  boolean getBooleanPerference(String key)
    {
        return preferences.getBoolean(key,false);
    }
}
