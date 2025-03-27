package com.example.card01.Modules;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.card01.Configuration.TOAC.CanvasView_TOAC;
import com.example.card01.Configuration.PreferencesManager;
import com.example.card01.Configuration.TOAC.SwipeGestureDetector_TOAC;
import com.example.card01.Configuration.TOAC.VolumeReceiver_TOAC;
import com.example.card01.GlobalApp;
import com.example.card01.R;

public class ThinkOfACard extends AppCompatActivity {
    private CanvasView_TOAC thinkOfACardCanvasView;
    private int toacType;
    private RelativeLayout cardTextView;
    private int step = 0;
    private String colour = "";
    private String suit = "";
    private String binaryValue = "";
    private VolumeReceiver_TOAC volumeReceiver;
    private GestureDetector gestureDetector;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_card);
        PreferencesManager preferencesManager = ((GlobalApp) getApplicationContext()).getPreferencesManager();
        toacType = preferencesManager.getInt("toacType", 1);

        thinkOfACardCanvasView = findViewById(R.id.thinkOfACardCanvasView);
        cardTextView = findViewById(R.id.cardTextView);

        // Bluetooth hangerő lekezelése
        volumeReceiver = new VolumeReceiver_TOAC(this);
        IntentFilter filter = new IntentFilter("android.media.VOLUME_CHANGED_ACTION");
        if(toacType == 1) {
            registerReceiver(volumeReceiver, filter);
        }

        // onTouch és onSwipe event lekezelés
        View rootView = findViewById(android.R.id.content);
        gestureDetector = new GestureDetector(this, new SwipeGestureDetector_TOAC());
        rootView.setOnTouchListener((v, event) -> {
            if(toacType == 2) {
                handleTouchEvent(event);
            }
            return gestureDetector.onTouchEvent(event);
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toacType == 1) {
            unregisterReceiver(volumeReceiver);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    // Hangerőgombos szabláyozás lekezelése
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (toacType == 0) {
            if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                handleVolumeChange(1);
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                handleVolumeChange(-1);
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }
    public void handleVolumeChange(int volumeChange) {
        if (step == 0) {
            colour = volumeChange > 0 ? "piros" : "fekete";
        } else if (step == 1) {
            suit = getSuit(colour, volumeChange);
        } else if (step >= 2 && step < 6) {
            binaryValue += volumeChange > 0 ? "1" : "0";
        }
        step++;
        if (step == 6) {
            String cardValue = getCardValue(binaryValue);
            displayCard(colour, suit, cardValue);
        }
    }
    private String getSuit(String colour, int direction) {
        if (colour.equals("piros")) {
            return direction > 0 ? "kör" : "káró";
        } else {
            return direction > 0 ? "pikk" : "treff";
        }
    }
    private String getCardValue(String binary) {
        switch (binary) {
            case "0001":
            case "1111":
                return "A";
            case "0010":
                return "2";
            case "0011":
                return "3";
            case "0100":
                return "4";
            case "0101":
                return "5";
            case "0110":
                return "6";
            case "0111":
                return "7";
            case "1000":
                return "8";
            case "1001":
                return "9";
            case "1010":
                return "10";
            case "1011":
                return "J";
            case "1100":
                return "Q";
            case "1101":
                return "K";
            default:
                return "Ismeretlen";
        }
    }
    private void displayCard(String colour, String suit, String value) {
        String suitSymbol;
        int cardColour = Color.BLACK;

        if (colour.equals("piros")) {
            cardColour = Color.RED;
            suitSymbol = suit.equals("kör") ? "♥" : "♦";
        } else {
            suitSymbol = suit.equals("pikk") ? "♠" : "♣";
        }

        String cardText = value + " " + suitSymbol;

        // Bal felső sarok
        TextView topLeft = findViewById(R.id.topLeft);
        topLeft.setText(cardText);
        topLeft.setTextColor(cardColour);

        // Középső szimbólum
        TextView centerSymbol = findViewById(R.id.centerSymbol);
        centerSymbol.setText(suitSymbol);
        centerSymbol.setTextColor(cardColour);

        // Jobb alsó sarok, fejjel lefelé
        TextView bottomRight = findViewById(R.id.bottomRight);
        bottomRight.setText(cardText);
        bottomRight.setTextColor(cardColour);

        // Láthatóvá tenni a kártyát
        cardTextView.setVisibility(View.VISIBLE);
        topLeft.setVisibility(View.VISIBLE);
        centerSymbol.setVisibility(View.VISIBLE);
        bottomRight.setVisibility(View.VISIBLE);

        // Elrejteni a canvasView-t, ha még mindig látszik
        thinkOfACardCanvasView.setVisibility(View.GONE);
    }
    // Bluetooth vezérlős szabláyozás lekezelése
    private void handleTouchEvent(MotionEvent event) {
        String action = "";
        if (event.getX() > event.getY()){
            action = "ACTION_DOWN";
            handleVolumeChange(-1);
        }else {
            action = "ACTION_UP";
            handleVolumeChange(1);
        }

        String message = "Touch event: " + action + " at (" + event.getX() + ", " + event.getY() + ")";
        Log.d("ThinkOfACard", message);
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
