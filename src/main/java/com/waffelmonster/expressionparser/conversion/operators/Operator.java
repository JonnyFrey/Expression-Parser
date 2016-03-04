package com.waffelmonster.expressionparser.conversion.operators;

/**
 * Created by jonnyfrey on 3/1/16.
 */
public abstract class Operator implements Comparable<Operator> {
    private String sign;
    private int priority;
    private boolean applyRight;

    public Operator(String sign, int priority) {
        this.sign = sign;
        this.priority = priority;
        applyRight = false;
    }

    public String getSign() {
        return sign;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isApplyRight() {
        return applyRight;
    }

    protected void setApplyRight(boolean applyRight) {
        this.applyRight = applyRight;
    }

    public abstract double apply(double val1, double val2);

    @Override
    public int compareTo(Operator o) {
        if (o == null) {
            return this.getPriority();
        }
        return this.getPriority() - o.getPriority();
    }
}
