package com.alexeychurchill.plotbuilder.graphics;

/**
 * ScreenConverter converts coordinates from physical to screen
 * and vice versa.
 */

public class ScreenConverter {
    //World coordinates data
    private double minX; //By X
    private double maxX;
    private double minY; //By Y
    private double maxY;
    //Screen data
    private int width;
    private int height;
    //Scaling
    private double scale = 1.0;
    //Shifting
    private double shiftX = 0.0;
    private double shiftY = 0.0;

    //From world to screen by X
    public int worldToScreenX(double x) {
        double xAxisLength = (maxX - minX) / scale;
        double xFromMin = x - minX;
        return (int) (width * xFromMin / xAxisLength);
    }

    //From world to screen by Y
    public int worldToScreenY(double y) {
        double yAxisLength = (maxY - minY) / scale;
        double yFromMin = y - minY;
        return (int) (height * (1.0 - yFromMin / yAxisLength));
    }

    //From screen to world by X
    public double screenToWorldByX(int x) {
        double xAxisLength = (maxX - minX) / scale;
        return (1.0 * x) / width * xAxisLength + minX;
    }

    //From screen to world by Y
    public double screenToWorldByY(int y) {
        double yAxisLength = (maxY - minY) / scale;
        return (1.0 - (1.0 * y) / height) * yAxisLength + minY;
    }

    /*
    * Scaling
    * */
    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public void resetScale() {
        setScale(1.0);
    }

    /*
    * Shifting. In percents.
    * */

    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
