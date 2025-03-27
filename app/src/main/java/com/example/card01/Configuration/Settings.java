package com.example.card01.Configuration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.card01.R;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Settings extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @SuppressLint("QueryPermissionsNeeded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        PreferencesManager preferencesManager = new PreferencesManager(this);
        Spinner coinFaceTypeSpinner = findViewById(R.id.coinFaceTypeSpinner);
        Spinner coinSizeSpinner = findViewById(R.id.coinSizeSpinner);
        Spinner thinkOfACardSpinner = findViewById(R.id.thinkOfACardSpinner);
        EditText otherCoinSizeEditText = findViewById(R.id.otherCoinSizeEditText);
        EditText mentalCalculatorEditText = findViewById(R.id.mentalCalculatorEditText);
        Button takePhotoButton = findViewById(R.id.takeCoinFacePhotoButton);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch gravitySwitch = findViewById(R.id.gravitySwitch);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch colorChangeSwitch = findViewById(R.id.colorChangeSwitch);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch flipSwitch = findViewById(R.id.flipSwitch);
        EditText coinAmountEditText = findViewById(R.id.coinAmountEditText);
        ArrayAdapter<CharSequence> coinFaceTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.coin_types, android.R.layout.simple_spinner_item);
        coinFaceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coinFaceTypeSpinner.setAdapter(coinFaceTypeAdapter);
        ArrayAdapter<CharSequence> coinSizeAdapter = ArrayAdapter.createFromResource(this,
                R.array.coin_sizes, android.R.layout.simple_spinner_item);
        coinSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coinSizeSpinner.setAdapter(coinSizeAdapter);
        ArrayAdapter<CharSequence> thinkOfACardAdapter = ArrayAdapter.createFromResource(this,
                R.array.toac_switch, android.R.layout.simple_spinner_item);
        thinkOfACardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        thinkOfACardSpinner.setAdapter(thinkOfACardAdapter);
        gravitySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> preferencesManager.saveBoolean("gravityEnabled", isChecked));
        colorChangeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> preferencesManager.saveBoolean("colorChangeEnabled", isChecked));
        loadCurrentSettings(preferencesManager, coinFaceTypeSpinner, coinSizeSpinner, otherCoinSizeEditText, mentalCalculatorEditText, takePhotoButton, gravitySwitch, colorChangeSwitch, flipSwitch, coinAmountEditText, thinkOfACardSpinner);
        coinFaceTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] coinFaceTypeValues = getResources().getStringArray(R.array.coin_type_values);
                String selectedCoinFaceTypeValue = coinFaceTypeValues[position];
                if (selectedCoinFaceTypeValue.equals("other")) {
                    takePhotoButton.setVisibility(View.VISIBLE);
                } else {
                    takePhotoButton.setVisibility(View.GONE);
                    preferencesManager.saveString("coinFaceType", selectedCoinFaceTypeValue);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        flipSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                coinAmountEditText.setVisibility(View.VISIBLE);
            } else {
                coinAmountEditText.setVisibility(View.GONE);
            }
            preferencesManager.saveBoolean("flipEnabled", isChecked);
        });
        coinSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] coinSizeValues = getResources().getStringArray(R.array.coin_size_values);
                String selectedCoinSizeValue = coinSizeValues[position];
                if (selectedCoinSizeValue.equals("0")) {
                    otherCoinSizeEditText.setVisibility(View.VISIBLE);
                    otherCoinSizeEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {}

                        @Override
                        public void afterTextChanged(Editable s) {
                            PreferencesManager preferencesManager = new PreferencesManager(Settings.this);
                            preferencesManager.saveString("otherCoinSize", s.toString());
                        }
                    });
                } else {
                    otherCoinSizeEditText.setVisibility(View.GONE);
                    preferencesManager.saveInt("coinSize", Integer.parseInt(selectedCoinSizeValue));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        thinkOfACardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] thinkOfACardSpinnerValues = getResources().getStringArray(R.array.toac_switch_values);
                String selectedToacValue = thinkOfACardSpinnerValues[position];
                preferencesManager.saveInt("toacType", Integer.parseInt(selectedToacValue));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        coinAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                PreferencesManager preferencesManager = new PreferencesManager(Settings.this);
                preferencesManager.saveInt("coinAmount", Integer.parseInt(s.toString()));
            }
        });
        takePhotoButton.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });
        mentalCalculatorEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String predefinedValue = s.toString();
                preferencesManager.saveString("predefinedValue", predefinedValue);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            assert data != null;
            Bundle extras = data.getExtras();
            assert extras != null;
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            PreferencesManager preferencesManager = new PreferencesManager(this);
            preferencesManager.saveString("coinFaceType", "other");
            assert imageBitmap != null;
            processCapturedImage(imageBitmap);
        }
    }
    private void processCapturedImage(Bitmap imageBitmap) {
        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, width * 4, height * 4, true);
        Mat src = new Mat();
        Utils.bitmapToMat(scaledBitmap, src);
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(gray, gray, new Size(5, 5), 0);
        Mat edges = new Mat();
        Imgproc.Canny(gray, edges, 100, 200);
        Mat circles = new Mat();
        Imgproc.HoughCircles(edges, circles, Imgproc.CV_HOUGH_GRADIENT, 1, (double) edges.rows() / 8, 100, 20, 0, 0);

        if (circles.cols() > 0) {
            double[] circle = circles.get(0, 0);
            Point center = new Point(circle[0], circle[1]);
            int radius = (int) Math.round(circle[2]);
            int x = (int) center.x;
            int y = (int) center.y;
            int left = Math.max(0, x - radius);
            int top = Math.max(0, y - radius);
            int right = Math.min(src.cols(), x + radius);
            int bottom = Math.min(src.rows(), y + radius);

            org.opencv.core.Rect roi = new org.opencv.core.Rect(left, top, right - left, bottom - top);
            if (roi.width > 0 && roi.height > 0) {
                Mat cropped = src.submat(roi);
                Mat mask = Mat.zeros(cropped.size(), CvType.CV_8UC1);
                Imgproc.circle(mask, new Point(radius, radius), radius, new Scalar(255), -1);
                Mat masked = new Mat();
                cropped.copyTo(masked, mask);
                Bitmap croppedBitmap = Bitmap.createBitmap(masked.width(), masked.height(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(masked, croppedBitmap);
                Mat sharpened = new Mat();
                Imgproc.GaussianBlur(masked, sharpened, new Size(0, 0), 10);
                Core.addWeighted(masked, 1.5, sharpened, -0.5, 0, sharpened);
                Bitmap finalBitmap = Bitmap.createBitmap(sharpened.width(), sharpened.height(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(sharpened, finalBitmap);
                ImageView imageView = findViewById(R.id.capturedImageView);
                imageView.setImageBitmap(finalBitmap);
                PreferencesManager preferencesManager = new PreferencesManager(this);
                preferencesManager.saveBitmap("coinFaceImage", finalBitmap);
            }
        }
    }


    private void loadCurrentSettings(PreferencesManager preferencesManager, Spinner coinFaceTypeSpinner, Spinner coinSizeSpinner, EditText otherCoinSizeEditText, EditText mentalCalculatorEditText, Button takePhotoButton, @SuppressLint("UseSwitchCompatOrMaterialCode") Switch gravitySwitch, @SuppressLint("UseSwitchCompatOrMaterialCode") Switch colorChangeSwitch, @SuppressLint("UseSwitchCompatOrMaterialCode") Switch flipSwitch, EditText coinAmountEditText, Spinner thinkOfACardSpinner) {
        String coinFaceType = preferencesManager.getString("coinFaceType", "coin_face_morgan");
        int coinSize = preferencesManager.getInt("coinSize", 38);
        int toacType = preferencesManager.getInt("toacType", 1);
        String predefinedValue = preferencesManager.getString("predefinedValue", "42");
        boolean gravityEnabled = preferencesManager.getBoolean("gravityEnabled", false);
        boolean colorChangeEnabled = preferencesManager.getBoolean("colorChangeEnabled", false);
        boolean flipEnabled = preferencesManager.getBoolean("flipEnabled", false);
        int coinAmount = preferencesManager.getInt("coinAmount", 1);

        selectSpinnerItemByValue(coinFaceTypeSpinner, coinFaceType, R.array.coin_type_values);
        selectSpinnerItemByValue(coinSizeSpinner, String.valueOf(coinSize), R.array.coin_size_values);
        selectSpinnerItemByValue(thinkOfACardSpinner, String.valueOf(toacType), R.array.toac_switch_values);
        if (coinFaceType.equals("other")) {
            takePhotoButton.setVisibility(View.VISIBLE);
        } else {
            takePhotoButton.setVisibility(View.GONE);
        }

        if (String.valueOf(coinSize).equals("Other")) {
            otherCoinSizeEditText.setVisibility(View.VISIBLE);
        } else {
            otherCoinSizeEditText.setVisibility(View.GONE);
        }

        mentalCalculatorEditText.setText(predefinedValue);
        gravitySwitch.setChecked(gravityEnabled);
        colorChangeSwitch.setChecked(colorChangeEnabled);
        flipSwitch.setChecked(flipEnabled);
        if (flipEnabled) {
            coinAmountEditText.setVisibility(View.VISIBLE);
        } else {
            coinAmountEditText.setVisibility(View.GONE);
        }
        coinAmountEditText.setText(String.valueOf(coinAmount));
    }
    private void selectSpinnerItemByValue(Spinner spinner, String value, int valuesArrayResId) {
        String[] values = getResources().getStringArray(valuesArrayResId);
        for (int position = 0; position < values.length; position++) {
            if (values[position].equals(value)) {
                spinner.setSelection(position);
                break;
            }
        }
    }
}
