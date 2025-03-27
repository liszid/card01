package com.example.card01.Modules;

import com.example.card01.Configuration.PreferencesManager;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.card01.GlobalApp;
import com.example.card01.MainActivity;
import com.example.card01.R;
import java.util.Random;

public class ObjectThroughPhone extends AppCompatActivity {
    private float dX, dY;
    private int parentWidth, parentHeight;
    private boolean gravityEnabled, colorChangeEnabled, flipEnabled;
    private int coinAmount;
    private int coinSizeMm;
    private String coinFaceType;
    private GestureDetector gestureDetector;
    private ImageView coin;
    private ViewGroup parentLayout;

    @SuppressLint({"ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);
        PreferencesManager preferencesManager = ((GlobalApp) getApplicationContext()).getPreferencesManager();
        coin = findViewById(R.id.coinThroughPhone);
        coinSizeMm = preferencesManager.getInt("coinSize", 38);
        if (coinSizeMm == 0) {
            String otherCoinSize = preferencesManager.getString("otherCoinSize", "38");
            coinSizeMm = Integer.parseInt(otherCoinSize);
        }
        coinFaceType = preferencesManager.getString("coinFaceType", "morgan");
        if (coinFaceType.equals("other")) {
            Bitmap coinFaceImage = preferencesManager.getBitmap("coinFaceImage", null);
            if (coinFaceImage != null) {
                coin.setImageBitmap(coinFaceImage);
            } else {
                coin.setImageResource(R.drawable.coin_face_morgan);
            }
        } else {
            @SuppressLint("DiscouragedApi") int drawableId = getResources().getIdentifier(coinFaceType, "drawable", getPackageName());
            if (drawableId != 0) {
                coin.setImageResource(drawableId);
            } else {
                coin.setImageResource(R.drawable.coin_face_morgan);
            }
        }
        ViewGroup.LayoutParams layoutParams = coin.getLayoutParams();
        float mmToPx = coinSizeMm * getResources().getDisplayMetrics().xdpi / 25.4f;
        layoutParams.width = (int) mmToPx;
        layoutParams.height = (int) mmToPx;
        coin.setLayoutParams(layoutParams);
        parentLayout = (ViewGroup) coin.getParent();
        parentLayout.post(() -> {
            parentWidth = parentLayout.getWidth();
            parentHeight = parentLayout.getHeight();
        });
        // Betöltjük a funkciók állapotát
        gravityEnabled = preferencesManager.getBoolean("gravityEnabled", false);
        colorChangeEnabled = preferencesManager.getBoolean("colorChangeEnabled", false);
        flipEnabled = preferencesManager.getBoolean("flipEnabled", false);
        coinAmount = preferencesManager.getInt("coinAmount", 1);
        gestureDetector = new GestureDetector(this, new GestureListener());
        coin.setOnTouchListener((view, event) -> {
            gestureDetector.onTouchEvent(event);
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    dX = view.getX() - event.getRawX();
                    dY = view.getY() - event.getRawY();
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    if (event.getPointerCount() == 2) {
                        if (gravityEnabled) {
                            //showDebugToast("Gesture detected: two-finger tap (gravity)");
                            dropCoin(view);
                        }
                        if (colorChangeEnabled) {
                            //showDebugToast("Gesture detected: two-finger tap (color change)");
                            changeCoinColor(coin);
                        }
                        if (flipEnabled) {
                            //showDebugToast("Gesture detected: two-finger tap (flip)");
                            flipCoins(parentLayout);
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    float newX = event.getRawX() + dX;
                    float newY = event.getRawY() + dY;

                    view.animate()
                            .x(newX)
                            .y(newY)
                            .setDuration(0)
                            .start();
                    break;
                case MotionEvent.ACTION_UP:
                    float endX = view.getX();
                    float endY = view.getY();

                    if (endX < 0 || endX > parentWidth - view.getWidth() ||
                            endY < 0 || endY > parentHeight - view.getHeight()) {
                        float finalX = endX < 0 ? -view.getWidth() : endX > parentWidth - view.getWidth() ? parentWidth : endX;
                        float finalY = endY < 0 ? -view.getHeight() : endY > parentHeight - view.getHeight() ? parentHeight : endY;

                        view.animate()
                                .x(finalX)
                                .y(finalY)
                                .setDuration(300)
                                .withEndAction(() -> view.setVisibility(View.GONE))
                                .start();
                    }
                    break;
                default:
                    return false;
            }
            return true;
        });
        parentLayout.setOnTouchListener((view, event) -> {
            gestureDetector.onTouchEvent(event);
            if (event.getAction() == MotionEvent.ACTION_DOWN && coin.getVisibility() == View.GONE) {
                coin.setVisibility(View.VISIBLE);
                coin.setX(event.getRawX() + dX);
                coin.setY(event.getRawY() + dY);
            }
            return false;
        });
    }
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(@NonNull MotionEvent event) {
            return true;
        }
        @Override
        public boolean onDoubleTap(MotionEvent event) {
            if (event.getPointerCount() == 2) {
                showDebugToast("Gesture detected: double tap with two fingers");
                if (colorChangeEnabled) {
                    changeCoinColor(coin);
                }
                return true;
            }
            return false;
        }
        @Override
        public boolean onFling(MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            if (e1 != null && e1.getPointerCount() == 2 && e2.getPointerCount() == 2) {
                if (Math.abs(velocityY) > Math.abs(velocityX)) {
                    showDebugToast("Gesture detected: two-finger vertical swipe (gravity)");
                    if (gravityEnabled) {
                        dropCoin(coin);
                    }
                } else if (Math.abs(velocityX) > Math.abs(velocityY)) {
                    showDebugToast("Gesture detected: two-finger horizontal swipe (flip)");
                    if (flipEnabled) {
                        flipCoins(parentLayout);
                    }
                }
                return true;
            }
            return false;
        }
    }
    private void showToast(String message) {
        Toast.makeText(ObjectThroughPhone.this, message, Toast.LENGTH_SHORT).show();
    }
    private void changeCoinColor(ImageView coin) {
        @SuppressLint("DiscouragedApi") int newDrawableId = getResources().getIdentifier("other_coin_face", "drawable", getPackageName());
        @SuppressLint("UseCompatLoadingForDrawables") Drawable newDrawable = getResources().getDrawable(newDrawableId, null);
        coin.setImageDrawable(newDrawable);
    }
    private void dropCoin(View coin) {
        coin.animate()
                .translationY(parentHeight - coin.getHeight())
                .setDuration(1000)
                .start();
    }
    @SuppressLint("ClickableViewAccessibility")
    private void flipCoins(ViewGroup parentLayout) {
        float mmToPx = coinSizeMm * getResources().getDisplayMetrics().xdpi / 25.4f;

        for (int i = 0; i < coinAmount; i++) {
            ImageView newCoin = new ImageView(this);
            @SuppressLint("DiscouragedApi") int drawableId = getResources().getIdentifier(coinFaceType, "drawable", getPackageName());
            if (drawableId != 0) {
                newCoin.setImageResource(drawableId);
            } else {
                newCoin.setImageResource(R.drawable.coin_face_morgan);
            }
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams((int) mmToPx, (int) mmToPx);
            newCoin.setLayoutParams(layoutParams);
            Random random = new Random();
            newCoin.setX(random.nextFloat() * (parentWidth - newCoin.getWidth()));
            newCoin.setY(random.nextFloat() * (parentHeight - newCoin.getHeight()));
            parentLayout.addView(newCoin);
            // Ha a gravitáció is be van kapcsolva, az új érmék is essen
            if (gravityEnabled) {
                dropCoin(newCoin);
            }
            // Húzás figyelése az új érméken
            newCoin.setOnTouchListener((view, event) -> {
                gestureDetector.onTouchEvent(event);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float newX = event.getRawX() + dX;
                        float newY = event.getRawY() + dY;

                        view.animate()
                                .x(newX)
                                .y(newY)
                                .setDuration(0)
                                .start();
                        break;
                    default:
                        return false;
                }
                return true;
            });
        }
    }
    private void showDebugToast(String message) {
        runOnUiThread(() -> Toast.makeText(ObjectThroughPhone.this, message, Toast.LENGTH_SHORT).show());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(ObjectThroughPhone.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
