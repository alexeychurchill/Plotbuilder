package com.alexeychurchill.plotbuilder.math;

/**
 * ParsedDoubleFunction provides function parsing from interface.
 */
public class ParsedDoubleFunction implements DoubleFunction {
    private MathParser parser = new MathParser();

    public ParsedDoubleFunction(String function) {
        parser.parse(function);
    }

    @Override
    public double f(double x) {
        parser.setVariable("x", x);
        return parser.calculate();
    }
}
