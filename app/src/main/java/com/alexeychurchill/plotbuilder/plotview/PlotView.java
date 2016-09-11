package com.alexeychurchill.plotbuilder.plotview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
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
    //Scaling
    private static final float SCALE_MIN = 1.0F;
    private static final float SCALE_MAX = 5.0F;
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
    //Plot points
    private List<DoublePoint> mPlotPoints;
    //onPlotBuild listener
    private OnPlotBuildListener mListener;
    //Scaling handling
    private float mScaleFactor = 1.0F;
    private float mScaleFocusX = 0.0F;
    private float mScaleFocusY = 0.0F;
    private boolean mScaleScalingDraw = false;
    private ScaleGestureDetector mScaleDetector;

    public PlotView(Context context) {
        super(context);
        init(context);
    }

    public PlotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        //mPaint initialization
        mPaint.setStyle(Paint.Style.STROKE);
        setAntialiasing(true);
        //mConveter initialization
        setMinX(DEFAULT_MIN_X);
        setMaxX(DEFAULT_MAX_X);
        setMinY(DEFAULT_MIN_Y);
        setMaxY(DEFAULT_MAX_Y);
        //Creating scale gesture detector
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener(this));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        return true;
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
    * Plot points getter/setter
    * */
    public List<DoublePoint> getPlotPoints() {
        return mPlotPoints;
    }

    public void setPlotPoints(List<DoublePoint> plotPoints) {
        this.mPlotPoints = plotPoints;
    }

    /*
    * Allows to set listener
    * */
    public void setListener(OnPlotBuildListener listener) {
        this.mListener = listener;
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
        //Calling listener
        //Scaling flag being reset
        if (mListener != null) {
            double minX = mConverter.getScaledShiftedMinX();
            double maxX = mConverter.getScaledShiftedMaxX();
            mListener.onPlotBuild(this, minX, maxX, canvas.getWidth(), mScaleScalingDraw);
            mScaleScalingDraw = false;
        }
        //Drawing bg
        drawBackground(canvas);
        //Drawing axes
        drawAxes(canvas);
        //Drawing plot
        drawPlot(canvas);
    }

    /*
    * This setups screen converter.
    * */
    private void setupScreenConverter(Canvas canvas) {
        mConverter.setWidth(canvas.getWidth());
        mConverter.setHeight(canvas.getHeight());
        mConverter.setScale(mScaleFactor);
        double scaleFocusXRel = (mScaleFocusX / canvas.getWidth()) * 2.0 - 1.0;
        double scaleFocusYRel = 1.0 - (mScaleFocusY / canvas.getHeight()) * 2.0;
        mConverter.setShiftX(scaleFocusXRel);
        mConverter.setShiftY(scaleFocusYRel);
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
        int top = mConverter.toScreenY(0.0);
        if (top < 0 || top >= canvas.getHeight()) {
            return;
        }
        mPaint.setColor(mAxesColor);
        mPaint.setStrokeWidth(mAxesWidth);
        //Drawing axis
        canvas.drawLine(0.0F, (float) top, canvas.getWidth(), (float) top, mPaint);
        //Drawing arrow
        canvas.drawLine(canvas.getWidth(), (float) top,
                canvas.getWidth() - mArrowSize / 2.0F, (float) top - mArrowSize / 2.0F,
                mPaint); //Top
        canvas.drawLine(canvas.getWidth(), (float) top,
                canvas.getWidth() - mArrowSize / 2.0F, (float) top + mArrowSize / 2.0F,
                mPaint); //Bottom
        //Drawing ticks
        if (mTicksNeeded) {
            int ticksStart = (int) Math.floor(mConverter.toWorldByX(0));
            int ticksStop = (int) Math.ceil(mConverter.toWorldByX(canvas.getWidth()));
            for (int tick = ticksStart; tick < ticksStop; tick++) {
                double tickLeft = mConverter.toScreenX(tick);
                canvas.drawLine((float) tickLeft, (float) top - mTickSize / 2.0F,
                        (float) tickLeft, (float) top + mTickSize / 2.0F,
                        mPaint);
            }
        }
    }

    private void drawYAxis(Canvas canvas) {
        int left = mConverter.toScreenX(0.0);
        if (left < 0 || left >= canvas.getWidth()) {
            return;
        }
        mPaint.setColor(mAxesColor);
        mPaint.setStrokeWidth(mAxesWidth);
        //Drawing axis
        canvas.drawLine((float) left, 0.0F, (float) left, canvas.getHeight(), mPaint);
        //Drawing arrow
        canvas.drawLine((float) left, 0.0F, (float) left - mArrowSize / 2.0F, mArrowSize / 2.0F, mPaint); //Left
        canvas.drawLine((float) left, 0.0F, (float) left + mArrowSize / 2.0F, mArrowSize / 2.0F, mPaint); //Right
        //Drawing ticks
        if (mTicksNeeded) { //As we need ticks in integer coords, we can represent world Ys as ints
            int ticksStart = (int) Math.floor(mConverter.toWorldByY(canvas.getHeight()));
            int ticksStop = (int) Math.ceil(mConverter.toWorldByY(0));
            for (int tick = ticksStart; tick <= ticksStop; tick++) {
                double tickTop = mConverter.toScreenY(tick);
                canvas.drawLine((float) left - mTickSize / 2.0F, (float) tickTop,
                        (float) left + mTickSize / 2.0F, (float) tickTop,
                        mPaint);
            }
        }
    }

    /*
    * This method draws plot line from the points list
    * */
    private void drawPlot(Canvas canvas) {
        if (mPlotPoints == null) {
            return;
        }
        if (mPlotPoints.size() == 0) {
            return;
        }
        //Plot path
        Path plotPath = new Path();
        plotPath.moveTo(mConverter.toScreenX(mPlotPoints.get(0).getX()), mConverter.toScreenY(mPlotPoints.get(0).getY()));
        for (DoublePoint point : mPlotPoints) {
            int x = mConverter.toScreenX(point.getX());
            int y = mConverter.toScreenY(point.getY());
            plotPath.lineTo(x, y);
        }
        //Draw
        mPaint.setColor(mLineColor);
        mPaint.setStrokeWidth(mLineWidth);
        canvas.drawPath(plotPath, mPaint);
    }

    /*
    * onPlotBuild() called when plot redrawing
    * */
    public interface OnPlotBuildListener {
        void onPlotBuild(PlotView view, double minX, double maxX, int width, boolean scaling);
    }

    private static class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private PlotView mPlotView;

        public ScaleListener(PlotView plotView) {
            this.mPlotView = plotView;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mPlotView.mScaleFactor *= detector.getScaleFactor();
            mPlotView.mScaleFactor = Math.max(PlotView.SCALE_MIN, Math.min(PlotView.SCALE_MAX, mPlotView.mScaleFactor));
            mPlotView.mScaleScalingDraw = true;
            mPlotView.mScaleFocusX = detector.getFocusX();
            mPlotView.mScaleFocusY = detector.getFocusY();
            mPlotView.invalidate();
            return true;
        }
    }
}
