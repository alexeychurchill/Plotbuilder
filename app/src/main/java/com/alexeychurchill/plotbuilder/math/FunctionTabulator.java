package com.alexeychurchill.plotbuilder.math;

import android.support.annotation.NonNull;

import com.alexeychurchill.plotbuilder.graphics.DoublePoint;
import com.alexeychurchill.plotbuilder.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * FunctionTabulator is a mathematical function tabulator.
 */

public class FunctionTabulator {
    private DoubleFunction function;
    private int stepCount;
    private double from;
    private double to;

    public FunctionTabulator() {
    }

    public FunctionTabulator(@NonNull DoubleFunction function) {
        this.function = function;
    }

    public DoubleFunction getFunction() {
        return function;
    }

    public void setFunction(DoubleFunction function) {
        this.function = function;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public double getFrom() {
        return from;
    }

    public void setFrom(double from) {
        this.from = from;
    }

    public double getTo() {
        return to;
    }

    public void setTo(double to) {
        this.to = to;
    }

    public List<DoublePoint> tabulate() {
        if (function == null) {
            return null;
        }
        List<DoublePoint> pointList = new ArrayList<>();
        double delta = (to - from) / stepCount;
        for (int step = 0; step <= stepCount; step++) {
            double x = from + delta * step;
            pointList.add(new DoublePoint(x, function.f(x)));
        }
        return pointList;
    }
}
