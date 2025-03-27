package com.example.card01.Configuration.TOAC;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.example.card01.Modules.ThinkOfACard;

import java.util.Objects;

// Bluetooth hangerő szabláyozás lekezelése
public class VolumeReceiver_TOAC extends BroadcastReceiver {
    private final ThinkOfACard thinkOfACard;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isPending = false;

    public VolumeReceiver_TOAC(ThinkOfACard activity) {
        this.thinkOfACard = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), "android.media.VOLUME_CHANGED_ACTION")) {
            if (!isPending) {
                isPending = true;
                handler.postDelayed(() -> {
                    int volume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", -1);
                    int prevVolume = intent.getIntExtra("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE", -1);
                    if (volume > prevVolume) {
                        thinkOfACard.handleVolumeChange(1);
                    } else if (volume < prevVolume) {
                        thinkOfACard.handleVolumeChange(-1);
                    }
                    isPending = false;
                }, 200); // 200ms késleltetés
            }
        }
    }
}
