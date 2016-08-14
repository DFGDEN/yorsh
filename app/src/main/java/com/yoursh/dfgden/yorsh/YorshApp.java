package com.yoursh.dfgden.yorsh;

import android.app.Application;

import com.yoursh.dfgden.yorsh.database.DataProvider;

/**
 * Created by dfgden on 8/1/16.
 */
public class YorshApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DataProvider.initInstance(getApplicationContext());
    }
}
