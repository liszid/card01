package com.example.card01.Configuration;

import android.app.Application;
import android.util.Log;

import org.opencv.android.OpenCVLoader;

public class OpenCVInitializer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (!OpenCVLoader.initDebug()) {
            // Hiba az OpenCV inicializálása közben
            Log.e("OpenCV", "Nem sikerült betölteni az OpenCV könyvtárat!");
        }
    }
}
