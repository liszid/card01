package com.example.card01.Modules;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.card01.Configuration.PreferencesManager;
import com.example.card01.GlobalApp;
import com.example.card01.R;

public class MentalCalc extends AppCompatActivity {
    private TextView resultView;
    private final StringBuilder currentInput = new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        resultView = findViewById(R.id.resultView);

        int[] buttonIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5,
                R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDot, R.id.btnAdd,
                R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide, R.id.btnEquals,
                R.id.btnAC, R.id.btnSign, R.id.btnPercent
        };

        for (int id : buttonIds) {
            Button button = findViewById(id);
            button.setTag(getResources().getResourceEntryName(id));
            button.setOnClickListener(view -> onButtonClick((String) view.getTag()));
        }
    }
    @SuppressLint({"NonConstantResourceId", "DiscouragedApi"})
    private void onButtonClick(String id) {
        PreferencesManager preferencesManager = ((GlobalApp) getApplicationContext()).getPreferencesManager();
        String predefinedResult = preferencesManager.getString("predefinedValue", "42");

        if ("btn0".equals(id) || "btn1".equals(id) || "btn2".equals(id) || "btn3".equals(id) ||
                "btn4".equals(id) || "btn5".equals(id) || "btn6".equals(id) || "btn7".equals(id) ||
                "btn8".equals(id) || "btn9".equals(id) || "btnDot".equals(id)) {
            currentInput.append(((Button) findViewById(getResources().getIdentifier(id, "id", getPackageName()))).getText());
            resultView.setText(currentInput.toString());
        } else if ("btnAdd".equals(id) || "btnSubtract".equals(id) || "btnMultiply".equals(id) ||
                "btnDivide".equals(id) || "btnSign".equals(id) || "btnPercent".equals(id)) {
            currentInput.append(" ").append(((Button) findViewById(getResources().getIdentifier(id, "id", getPackageName()))).getText()).append(" ");
            resultView.setText(currentInput.toString());
        } else if ("btnAC".equals(id)) {
            currentInput.setLength(0);
            resultView.setText("0");
        } else if ("btnEquals".equals(id)) {
            resultView.setText(predefinedResult);
        } else {
            resultView.setText(predefinedResult);
        }
    }
}