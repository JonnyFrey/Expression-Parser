package com.waffelmonster.expressionparser.conversion.operators;

/**
 * Created by jonnyfrey on 3/1/16.
 */
public class Minus extends Operator {
    public static final String SIGN = "-";
    public static final int PRIORITY = 1;

    public Minus() {
        super(SIGN, PRIORITY);
    }

    @Override
    public double apply(double val1, double val2) {
        return val1 - val2;
    }
}
