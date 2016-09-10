package com.alexeychurchill.plotbuilder.plotview;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.alexeychurchill.plotbuilder.graphics.DoublePoint;
import com.alexeychurchill.plotbuilder.graphics.ScreenConverter;

import java.util.List;

public class PlotView extends View {
    private ScreenConverter mConverter = new ScreenConverter();
    private Paint mPaint = new Paint();
    private int mBackgroundColor = Color.WHITE;
    private int mAxesColor = Color.BLACK;
    private int mPlotColor = Color.BLUE;
    private List<DoublePoint> mSeries;

    public PlotView(Context context) {
        super(context);
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
        invalidate();
    }

    public void setSeries(List<DoublePoint> series) {
        this.mSeries = series;
        invalidate();
    }

    public ScreenConverter getScreenConverter() {
        return mConverter;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setupScreenConverter();
        drawBackground(canvas);
        drawAxes(canvas);
        drawPlot(canvas);
    }

    private void drawPlot(Canvas canvas) {
        mPaint.setColor(mPlotColor);
        if (mSeries == null) {
            return;
        }
        if (mSeries.size() == 0) {
            return;
        }
        int lastX = mConverter.worldToScreenX(mSeries.get(0).getX());
        int lastY = mConverter.worldToScreenY(mSeries.get(0).getY());
        for (int i = 1; i < mSeries.size(); i++) {
            int x = mConverter.worldToScreenX(mSeries.get(i).getX());
            int y = mConverter.worldToScreenY(mSeries.get(i).getY());
            canvas.drawLine(lastX, lastY, x, y, mPaint);
            lastX = x;
            lastY = y;
        }
    }

    private void drawAxes(Canvas canvas) {
        mPaint.setColor(mAxesColor);
        //X-Axis
        int yOfAxisX = mConverter.worldToScreenY(0.0);
        canvas.drawLine(0.0F, yOfAxisX, getWidth(), yOfAxisX, mPaint);
        //Y-Axis
        int xOfAxisY = mConverter.worldToScreenX(0.0);
        canvas.drawLine(xOfAxisY, 0.0F, xOfAxisY, getHeight(), mPaint);
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawColor(mBackgroundColor);
    }

    private void setupScreenConverter() {
        mConverter.setWidth(getWidth());
        mConverter.setHeight(getHeight());
    }
}
