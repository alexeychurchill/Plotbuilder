package com.alexeychurchill.plotbuilder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alexeychurchill.plotbuilder.math.MathParser;

import java.util.Calendar;

public class EnterDataActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = "EnterDataActivity";
    private static final String DEFAULT_FUNCTION = "lg(x^3-1.2)/(x^2+cos(x))";
    private static final String DEFAULT_FROM = "1.2";
    private static final String DEFAULT_TO = "5.0";
    private static final int CLICK_INTERVAL_MS = 1000;
    private static final int CLICK_COUNT = 5;

    private int mClicks = 0;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data);
        Button btnBuild = ((Button) findViewById(R.id.btnBuild));
        if (btnBuild != null) {
            btnBuild.setOnClickListener(this);
        }
        EditText etFunction = ((EditText) findViewById(R.id.etFunction));
        if (etFunction != null) {
            etFunction.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBuild:
                callShowPlotActivity();
                break;
            case R.id.etFunction:
                etFunctionClick();
                break;
        }
    }

    private void etFunctionClick() {
        long thisClickTime = Calendar.getInstance().getTimeInMillis();
        if ((thisClickTime - mLastClickTime) > CLICK_INTERVAL_MS) {
            mClicks = 0;
        }
        mLastClickTime = thisClickTime;
        mClicks++;
        if (mClicks >= CLICK_COUNT) {
            mClicks = 0;
            etFunctionClickAction();
        }
    }

    private void etFunctionClickAction() {
        EditText etFunction = ((EditText) findViewById(R.id.etFunction));
        EditText etFrom = ((EditText) findViewById(R.id.etFrom));
        EditText etTo = ((EditText) findViewById(R.id.etTo));
        if (etFunction != null) {
            etFunction.setText(DEFAULT_FUNCTION);
        }
        if (etFrom != null) {
            etFrom.setText(DEFAULT_FROM);
        }
        if (etTo != null) {
            etTo.setText(DEFAULT_TO);
        }
    }

    private void callShowPlotActivity() {
        hideError();
        EditText etFunction = ((EditText) findViewById(R.id.etFunction));
        EditText etFrom = ((EditText) findViewById(R.id.etFrom));
        EditText etTo = ((EditText) findViewById(R.id.etTo));
        Intent showPlotIntent = new Intent(this, ShowPlotActivity.class);
        if (etFunction == null || etFrom == null || etTo == null) {
            return;
        }
        if (etFunction.getText().length() == 0) { //Function
            showError("Not enough data!");
            etFunction.requestFocus();
            return;
        }
        showPlotIntent.putExtra(ShowPlotActivity.EXTRA_FUNCTION, etFunction.getText().toString());
        if (etFrom.getText().length() == 0) { //From
            showError("Not enough data!");
            etFrom.requestFocus();
            return;
        }
        try {
            double from = Double.parseDouble(etFrom.getText().toString());
            showPlotIntent.putExtra(ShowPlotActivity.EXTRA_FROM, from);
        } catch (NumberFormatException e) {
            showError("Wrong number format!");
            etFrom.requestFocus();
            return;
        }
        if (etTo.getText().length() == 0) { //To
            showError("Not enough data!");
            etTo.requestFocus();
            return;
        }
        try {
            double to = Double.parseDouble(etTo.getText().toString());
            showPlotIntent.putExtra(ShowPlotActivity.EXTRA_TO, to);
            etTo.requestFocus();
        } catch (NumberFormatException e) {
            showError("Wrong number format!");
            return;
        }
        startActivity(showPlotIntent);
    }

    private void showError(String description) {
        TextView tvError = ((TextView) findViewById(R.id.tvError));
        if (tvError == null) {
            return;
        }
        tvError.setText(description);
        tvError.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        TextView tvError = ((TextView) findViewById(R.id.tvError));
        if (tvError == null) {
            return;
        }
        tvError.setVisibility(View.GONE);
    }
}
