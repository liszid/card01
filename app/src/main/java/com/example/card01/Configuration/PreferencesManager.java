package com.example.card01.Configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class PreferencesManager {

    private static final String PREF_NAME = "app_settings";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // String érték mentése
    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    // String érték lekérése
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    // Boolean érték mentése
    public void saveBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    // Boolean érték lekérése
    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    // Integer érték mentése
    public void saveInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    // Integer érték lekérése
    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void saveBitmap(String key, Bitmap bitmap) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] byteArray = bos.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        editor.putString(key, encoded);
        editor.apply();
    }

    public Bitmap getBitmap(String key, Bitmap defaultValue) {
        String encoded = sharedPreferences.getString(key, null);
        if (encoded == null) {
            return defaultValue;
        }
        byte[] byteArray = Base64.decode(encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    // Törlés egy adott kulcs alapján
    public void removeKey(String key) {
        editor.remove(key);
        editor.apply();
    }

    // Minden beállítás törlése
    public void clearAll() {
        editor.clear();
        editor.apply();
    }
}
