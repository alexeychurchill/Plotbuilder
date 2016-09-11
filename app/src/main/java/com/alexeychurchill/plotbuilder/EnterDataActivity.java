package com.alexeychurchill.plotbuilder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alexeychurchill.plotbuilder.math.DoubleFunction;
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
        Intent showPlotIntent = new Intent(this, ShowPlotActivity.class);
        String function = getFunction(); //Function
        if (function == null) {
            return;
        }
        showPlotIntent.putExtra(ShowPlotActivity.EXTRA_FUNCTION, function);
        Double from = getFrom(); //From
        if (from == null) {
            return;
        }
        showPlotIntent.putExtra(ShowPlotActivity.EXTRA_FROM, from);
        Double to = getTo(); //To
        if (to == null) {
            return;
        }
        showPlotIntent.putExtra(ShowPlotActivity.EXTRA_TO, to);
        startActivity(showPlotIntent);
    }

    private String getFunction() {
        EditText etFunction = ((EditText) findViewById(R.id.etFunction));
        if (etFunction == null) {
            return null;
        }
        if (etFunction.getText().length() == 0) {
            showError(R.string.err_not_enough_data);
            etFunction.requestFocus();
            return null;
        }
        return etFunction.getText().toString();
    }

    private Double getFrom() {
        EditText etFrom = ((EditText) findViewById(R.id.etFrom));
        if (etFrom == null) {
            return null;
        }
        return getDoubleFromEditText(etFrom);
    }

    private Double getTo() {
        EditText etTo = ((EditText) findViewById(R.id.etTo));
        if (etTo == null) {
            return null;
        }
        return getDoubleFromEditText(etTo);
    }

    private Double getDoubleFromEditText(EditText et) {
        if (et.getText().length() == 0) {
            showError(R.string.err_not_enough_data);
            et.requestFocus();
            return null;
        }
        Double from;
        try {
            from = Double.parseDouble(et.getText().toString());
        } catch (NumberFormatException e) {
            showError(R.string.err_wrong_number_format);
            et.requestFocus();
            return null;
        }
        return from;
    }

    private void showError(int resId) {
        String errorText = getString(resId);
        showError(errorText);
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
