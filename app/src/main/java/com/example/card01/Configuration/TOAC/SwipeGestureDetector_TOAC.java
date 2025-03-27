package com.example.card01.Configuration.TOAC;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class SwipeGestureDetector_TOAC extends GestureDetector.SimpleOnGestureListener {
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        assert e1 != null;
        float diffY = e2.getY() - e1.getY();
        float diffX = e2.getX() - e1.getX();
        if (Math.abs(diffY) > Math.abs(diffX)) {
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (e2.getX() > e1.getX()) {
                    onSwipeDown();
                } else {
                    onSwipeUp();
                }
                return true;
            }
        }
        return false;
    }

    public void onSwipeUp() {
        //handleVolumeChange(1);
    }

    public void onSwipeDown() {
        //handleVolumeChange(-1);
    }
}
