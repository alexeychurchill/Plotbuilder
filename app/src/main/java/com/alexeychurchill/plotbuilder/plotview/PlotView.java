package com.alexeychurchill.plotbuilder.plotview;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.alexeychurchill.plotbuilder.graphics.DoublePoint;
import com.alexeychurchill.plotbuilder.graphics.ScreenConverter;

import java.util.List;

public class PlotView extends View {
    //Default mins and maxes
    private static final double DEFAULT_MIN_X = -5.0;
    private static final double DEFAULT_MAX_X = 5.0;
    private static final double DEFAULT_MIN_Y = -5.0;
    private static final double DEFAULT_MAX_Y = 5.0;
    //Colors
    private int mBackgroundColor = Color.WHITE;
    private int mLineColor = Color.BLUE;
    private int mAxesColor = Color.BLACK;
    //Lines' width
    private float mAxesWidth = 2.0F;
    private float mLineWidth = 1.0F;
    //Paint
    private Paint mPaint = new Paint();
    //Axes data
    private boolean mTicksNeeded = true;
    private float mTickSize = 10.0F;
    private float mArrowSize = 10.0F;
    //Screen converter
    private ScreenConverter mConverter = new ScreenConverter();

    public PlotView(Context context) {
        super(context);
        init();
    }

    public PlotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setAntialiasing(true);
        setMinX(DEFAULT_MIN_X);
        setMaxX(DEFAULT_MAX_X);
        setMinY(DEFAULT_MIN_Y);
        setMaxY(DEFAULT_MAX_Y);
    }

    /*
    * AntiAlias getter/setter
    * */

    public void setAntialiasing(boolean antialiasing) {
        mPaint.setAntiAlias(antialiasing);
    }

    public boolean getAntialiasing() {
        return mPaint.isAntiAlias();
    }

    /*
    * BG color getter/setter
    * */

    public int getBGColor() {
        return mBackgroundColor;
    }

    public void setBGColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
    }

    /*
    * Line color getter/setter
    * */

    public int getLineColor() {
        return mLineColor;
    }

    public void setLineColor(int lineColor) {
        this.mLineColor = lineColor;
    }

    /*
    * Line width getter/setter
    * */

    public float getLineWidth() {
        return mLineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.mLineWidth = lineWidth;
    }

    /*
    * Axes color getter/setter
    * */

    public int getAxesColor() {
        return mAxesColor;
    }

    public void setAxesColor(int axesColor) {
        this.mAxesColor = axesColor;
    }

    /*
    * Axes width getter/setter
    * */

    public float getAxesWidth() {
        return mAxesWidth;
    }

    public void setAxesWidth(float axesWidth) {
        this.mAxesWidth = axesWidth;
    }

    /*
    * Real world bounds getters
    * */

    public double getMinX() {
        return mConverter.getMinX();
    }

    public double getMaxX() {
        return mConverter.getMaxX();
    }

    public double getMinY() {
        return mConverter.getMinY();
    }

    public double getMaxY() {
        return mConverter.getMaxY();
    }

    /*
    * Real world bounds setters
    * */

    public void setMinX(double minX) {
        mConverter.setMinX(minX);
    }

    public void setMaxX(double maxX) {
        mConverter.setMaxX(maxX);
    }

    public void setMinY(double minY) {
        mConverter.setMinY(minY);
    }

    public void setMaxY(double maxY) {
        mConverter.setMaxY(maxY);
    }

    /*
    * Calls invalidate(). This causes onDraw() call, which contains
    * call of the listener onBuildPlot method.
    * */

    public void update() {
        invalidate();
    }

    /*
    * This draws plot and calls listener's method.
    * */

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Setting up screen converter
        setupScreenConverter(canvas);
        //Drawing bg
        drawBackground(canvas);
        //Drawing axes
        drawAxes(canvas);
    }

    /*
    * This setups screen converter.
    * */
    private void setupScreenConverter(Canvas canvas) {
        mConverter.setWidth(canvas.getWidth());
        mConverter.setHeight(canvas.getHeight());
    }

    /*
    * This draws background.
    * */
    private void drawBackground(Canvas canvas) {
        canvas.drawColor(mBackgroundColor);
    }

    /*
    * This draws axes.
    * */
    private void drawAxes(Canvas canvas) {
        drawXAxis(canvas);
        drawYAxis(canvas);
    }

    private void drawXAxis(Canvas canvas) {
        //...
    }

    private void drawYAxis(Canvas canvas) {
        double yAxisLeft = mConverter.worldToScreenX(0.0);
        mPaint.setColor(mAxesColor);
        mPaint.setStrokeWidth(mAxesWidth);
        //Drawing axis
        canvas.drawLine((float) yAxisLeft, 0.0F, (float) yAxisLeft, canvas.getHeight(), mPaint);
        //Drawing arrow
        canvas.drawLine((float) yAxisLeft, 0.0F, (float) yAxisLeft - mArrowSize / 2.0F, mArrowSize / 2.0F, mPaint); //Left
        canvas.drawLine((float) yAxisLeft, 0.0F, (float) yAxisLeft + mArrowSize / 2.0F, mArrowSize / 2.0F, mPaint); //Right
    }
}
