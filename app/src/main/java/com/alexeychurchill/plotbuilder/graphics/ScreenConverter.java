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
    public int toScreenX(double x) {
        double xAxisLength = getScaledLengthByX();
        double xFromMin = x - getScaledShiftedMinX();
        return (int) (width * xFromMin / xAxisLength);
    }

    //From world to screen by Y
    public int toScreenY(double y) {
        double yAxisLength = getScaledLengthByY();
        double yFromMin = y - getScaledShiftedMinY();
        return (int) (height * (1.0 - yFromMin / yAxisLength));
    }

    //From screen to world by X
    public double toWorldByX(int x) {
        double xAxisLength = getScaledLengthByX();
        return (1.0 * x) / width * xAxisLength + getScaledShiftedMinX();
    }

    //From screen to world by Y
    public double toWorldByY(int y) {
        double yAxisLength = getScaledLengthByY();
        return (1.0 - (1.0 * y) / height) * yAxisLength + getScaledShiftedMinY();
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
    * Shifting.
    * From -1.0 to 1.0;
    * */
    public double getShiftX() {
        return shiftX;
    }

    public void setShiftX(double shiftX) {
        this.shiftX = Math.max(-1.0, Math.min(1.0, shiftX));
    }

    public void resetShiftX() {
        setShiftX(0.0);
    }

    public double getShiftY() {
        return shiftY;
    }

    public void setShiftY(double shiftY) {
        this.shiftY = Math.max(-1.0, Math.min(1.0, shiftY));
    }

    public void resetShiftY() {
        setShiftY(0.0);
    }

    /*
     * Minimal X
     * */
    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    /*
    * Maximal X
    * */
    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    /*
    * Minimal Y
    * */
    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    /*
    * Maximal Y
    * */
    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    /*
    * Axes sizes
    * */
    public double getLengthByX() {
        return maxX - minX;
    }

    public double getLengthByY() {
        return maxY - minY;
    }

    public double getScaledLengthByX() { //Scaled
        return getLengthByX() / scale;
    }

    public double getScaledLengthByY() { //Scaled
        return getLengthByY() / scale;
    }

    /*
    * Scaled mins and maxes
    * */
    public double getScaledMinX() {
        return (minX + maxX - getScaledLengthByX()) / 2.0;
    }

    public double getScaledMaxX() {
        return (minX + maxX + getScaledLengthByX()) / 2.0;
    }

    public double getScaledMinY() {
        return (minY + maxY - getScaledLengthByY()) / 2.0;
    }

    public double getScaledMaxY() {
        return (minY + maxY + getScaledLengthByY()) / 2.0;
    }

    /*
    * Scaled and shifted mins and maxes
    * */
    public double getAvailableShiftByX() { //Available shifts
        return Math.max(0.0, getLengthByX() - getScaledLengthByX());
    }

    public double getAvailableShiftByY() {
        return Math.max(0.0, getLengthByY() - getScaledLengthByY());
    }

    public double getScaledShiftedMinX() {
        return getScaledMinX() + getAvailableShiftByX() / 2.0 * shiftX;
    }

    public double getScaledShiftedMaxX() {
        return getScaledMaxX() + getAvailableShiftByX() / 2.0 * shiftX;
    }

    public double getScaledShiftedMinY() {
        return getScaledMinY() + getAvailableShiftByY() / 2.0 * shiftY;
    }

    public double getScaledShiftedMaxY() {
        return getScaledMaxY() + getAvailableShiftByY() / 2.0 * shiftY;
    }

    /*
    * Real screen width
    * */
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    /*
    * Real screen height
    * */
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
