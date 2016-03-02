package com.waffelmonster.expressionparser.conversion.operators;

/**
 * Created by jonnyfrey on 3/1/16.
 */
public class Exponent extends Operator {
    public static final String SIGN = "^";
    public static final int PRIORITY = 3;

    public Exponent() {
        super(SIGN, PRIORITY);
        setApplyRight(true);
    }

    @Override
    public double apply(double val1, double val2) {
        return Math.pow(val1, val2);
    }
}
