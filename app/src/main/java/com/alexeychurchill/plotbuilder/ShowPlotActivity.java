package com.alexeychurchill.plotbuilder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.alexeychurchill.plotbuilder.graphics.DoublePoint;
import com.alexeychurchill.plotbuilder.math.DoubleFunction;
import com.alexeychurchill.plotbuilder.math.FunctionTabulator;
import com.alexeychurchill.plotbuilder.math.ParsedDoubleFunction;
import com.alexeychurchill.plotbuilder.plotview.PlotView;

import java.util.List;

public class ShowPlotActivity extends AppCompatActivity
        implements PlotView.OnPlotBuildListener {
    private static final String LOG_TAG = "ShowPlotActivity";
    public static final String DOMAIN = "com.alexeychurchill.plotbuilder";
    public static final String EXTRA_FUNCTION = DOMAIN.concat(".EXTRA_FUNCTION");
    public static final String EXTRA_FROM = DOMAIN.concat(".EXTRA_FROM");
    public static final String EXTRA_TO = DOMAIN.concat(".EXTRA_TO");

    private String mFunctionSource;
    private DoubleFunction mFunction;
    private double mFrom;
    private double mTo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_plot);
        if (savedInstanceState == null) {
            Intent myCallIntent = getIntent();
            loadWithIntent(myCallIntent);
        } else {
            loadFromBundle(savedInstanceState);
        }
        mFunction = new ParsedDoubleFunction(mFunctionSource);
        PlotView mPVPlot = ((PlotView) findViewById(R.id.pvPlot));
        if (mPVPlot != null) {
            mPVPlot.setMinX(mFrom);
            mPVPlot.setMaxX(mTo);
            mPVPlot.setListener(this);
            mPVPlot.setAxesColor(ContextCompat.getColor(this, R.color.colorPrimary));
            mPVPlot.setLineColor(ContextCompat.getColor(this, R.color.colorAccent));
            mPVPlot.setLineWidth(2.0F);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveToBundle(outState);
    }

    /*
    * Inits with Intent
    * */
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

    /*
    * Saves to bundle
    * */
    private void saveToBundle(Bundle state) {
        if (state == null) {
            return;
        }
        state.putString(EXTRA_FUNCTION, mFunctionSource);
        state.putDouble(EXTRA_FROM, mFrom);
        state.putDouble(EXTRA_TO, mTo);
    }

    /*
    * Loads from bundle
    * */
    private boolean loadFromBundle(Bundle state) {
        if (state == null) {
            return false;
        }
        mFunctionSource = state.getString(EXTRA_FUNCTION);
        mFrom = state.getDouble(EXTRA_FROM, 0.0);
        mTo = state.getDouble(EXTRA_TO, 0.0);
        return true;
    }

    /*
    * Function tabulator
    * */
    private List<DoublePoint> tabulate(double from, double to, int steps) {
        if (mFunction == null) {
            return null;
        }
        FunctionTabulator tabulator = new FunctionTabulator(mFunction);
        tabulator.setFrom(from);
        tabulator.setTo(to);
        tabulator.setStepCount(steps);
        return tabulator.tabulate();
    }

    /*
    * Finds minimum Y
    * */
    private double getMinY(List<DoublePoint> points) {
        if (points == null) {
            return 0.0;
        }
        if (points.isEmpty()) {
            return 0.0;
        }
        double min = points.get(0).getY();
        for (DoublePoint point : points) {
            min = Math.min(min, point.getY());
        }
        return min;
    }

    /*
    * Finds maximum Y
    * */
    private double getMaxY(List<DoublePoint> points) {
        if (points == null) {
            return 0.0;
        }
        if (points.isEmpty()) {
            return 0.0;
        }
        double max = points.get(0).getY();
        for (DoublePoint point : points) {
            max = Math.max(max, point.getY());
        }
        return max;
    }

    /*
    * Callback. Builds plot.
    * */
    @Override
    public void onPlotBuild(PlotView view, double minX, double maxX, int width, boolean scaling) {
        List<DoublePoint> points = tabulate(minX, maxX, width);
        if (points == null) {
            return;
        }
        if (!scaling) {
            view.setMinY(getMinY(points));
            view.setMaxY(getMaxY(points));
        }
        view.setPlotPoints(points);
    }
}
