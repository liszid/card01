package com.example.card01;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.card01.Configuration.Settings;
import com.example.card01.Modules.ObjectThroughPhone;
import com.example.card01.Modules.MentalCalc;
import com.example.card01.Modules.ThinkOfACard;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {
    static {
        if (!OpenCVLoader.initDebug()) {
            Log.e("OpenCV", "Nem sikerült betölteni az OpenCV könyvtárat!");
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Funkció indító gombok
        Button btnThinkOfACard = findViewById(R.id.btn_think_of_a_card);
        Button btnCoinThroughPhone = findViewById(R.id.btn_coin_through_phone);
        Button btnMentalCalc = findViewById(R.id.btn_mental_calc);
        Button btnMindReading = findViewById(R.id.btn_mind_reading);
        Button btnSettings = findViewById(R.id.btn_settings);

        // Infó gombok
        Button btnInfoThinkOfACard = findViewById(R.id.btn_info_think_of_a_card);
        Button btnInfoCoinThroughPhone = findViewById(R.id.btn_info_coin_through_phone);
        Button btnInfoMentalCalc = findViewById(R.id.btn_info_mental_calc);
        Button btnInfoMindReading = findViewById(R.id.btn_info_mind_reading);

        btnThinkOfACard.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ThinkOfACard.class);
            startActivity(intent);
        });

        btnCoinThroughPhone.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ObjectThroughPhone.class);
            startActivity(intent);
            MainActivity.this.finish();
        });

        btnMentalCalc.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MentalCalc.class);
            startActivity(intent);
        });

        btnMindReading.setOnClickListener(view -> {
            // Your mind reading activity or method call here
        });

        btnSettings.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
        });

        btnInfoThinkOfACard.setOnClickListener(view -> showInfoDialog("file:///android_asset/thinkofacard.html"));
        btnInfoCoinThroughPhone.setOnClickListener(view -> showInfoDialog("file:///android_asset/cointhroughphone.html"));
        btnInfoMentalCalc.setOnClickListener(view -> showInfoDialog("file:///android_asset/mentalcalc.html"));
        btnInfoMindReading.setOnClickListener(view -> showInfoDialog("file:///android_asset/mindreading.html"));
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void showInfoDialog(String url) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_info);

        WebView webView = dialog.findViewById(R.id.webview_info);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        ImageButton btnClose = dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }
}
