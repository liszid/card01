package com.example.card01;

import android.app.Application;

import com.example.card01.Configuration.PreferencesManager;

public class GlobalApp extends Application {
    private PreferencesManager preferencesManager;

    @Override
    public void onCreate() {
        super.onCreate();
        preferencesManager = new PreferencesManager(this);
    }

    public PreferencesManager getPreferencesManager() {
        return preferencesManager;
    }
}
