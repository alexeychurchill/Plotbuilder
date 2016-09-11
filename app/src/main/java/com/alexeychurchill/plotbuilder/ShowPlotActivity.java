package com.alexeychurchill.plotbuilder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import com.alexeychurchill.plotbuilder.graphics.DoublePoint;
import com.alexeychurchill.plotbuilder.graphics.IntPoint;
import com.alexeychurchill.plotbuilder.math.DoubleFunction;
import com.alexeychurchill.plotbuilder.math.FunctionTabulator;
import com.alexeychurchill.plotbuilder.math.ParsedDoubleFunction;
import com.alexeychurchill.plotbuilder.plotview.PlotView;

import java.util.List;

public class ShowPlotActivity extends AppCompatActivity {
    private static final String LOG_TAG = "ShowPlotActivity";
    public static final String DOMAIN = "com.alexeychurchill.plotbuilder";
    public static final String EXTRA_FUNCTION = DOMAIN.concat(".EXTRA_FUNCTION");
    public static final String EXTRA_FROM = DOMAIN.concat(".EXTRA_FROM");
    public static final String EXTRA_TO = DOMAIN.concat(".EXTRA_TO");
    //...
    private String mFunctionSource;
    private DoubleFunction mFunction;
    private double mFrom;
    private double mTo;
    //...
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_plot);
        //...
        //...
        if (savedInstanceState == null) {
            Intent myCallIntent = getIntent();
            loadWithIntent(myCallIntent);
        } else {
            loadFromBundle(savedInstanceState);
        }
        mFunction = new ParsedDoubleFunction(mFunctionSource); //TODO: Parse mFunctionSource
        //TODO: Build plot
        //TODO: Set points
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveToBundle(outState);
    }

    private boolean loadWithIntent(Intent intent) {
        if (intent == null) {
            return false;
        }
        if (intent.hasExtra(EXTRA_FUNCTION)) {
            mFunctionSource = intent.getStringExtra(EXTRA_FUNCTION);
        }
        if (intent.hasExtra(EXTRA_FROM)) {
            mFrom = intent.getDoubleExtra(EXTRA_FROM, 0.0);
        }
        if (intent.hasExtra(EXTRA_TO)) {
            mTo = intent.getDoubleExtra(EXTRA_TO, 0.0);
        }
        return true;
    }

    private void saveToBundle(Bundle state) {
        if (state == null) {
            return;
        }
        state.putString(EXTRA_FUNCTION, mFunctionSource);
        state.putDouble(EXTRA_FROM, mFrom);
        state.putDouble(EXTRA_TO, mTo);
    }

    private boolean loadFromBundle(Bundle state) {
        if (state == null) {
            return false;
        }
        mFunctionSource = state.getString(EXTRA_FUNCTION);
        mFrom = state.getDouble(EXTRA_FROM, 0.0);
        mTo = state.getDouble(EXTRA_TO, 0.0);
        return true;
    }
}
