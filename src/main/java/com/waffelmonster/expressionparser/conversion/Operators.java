package com.waffelmonster.expressionparser.conversion;

/**
 * Created by jonnyfrey on 3/4/16.
 */
public enum Operators {

    ADD {
        @Override
        public String getSign() {
            return "+";
        }

        @Override
        public int getPriority() {
            return 1;
        }

        @Override
        public double apply(double val1, double val2) {
            return val1 + val2;
        }
    },

    MINUS {
        @Override
        public String getSign() {
            return "-";
        }

        @Override
        public int getPriority() {
            return 1;
        }

        @Override
        public double apply(double val1, double val2) {
            return val1 - val2;
        }
    },

    MULTIPLY {
        @Override
        public String getSign() {
            return "*";
        }

        @Override
        public int getPriority() {
            return 2;
        }

        @Override
        public double apply(double val1, double val2) {
            return val1 * val2;
        }
    },

    DIVIDE {
        @Override
        public String getSign() {
            return "/";
        }

        @Override
        public int getPriority() {
            return 2;
        }

        @Override
        public double apply(double val1, double val2) {
            return val1 / val2;
        }
    },

    EXPONENT {
        @Override
        public String getSign() {
            return "^";
        }

        @Override
        public int getPriority() {
            return 3;
        }

        @Override
        public double apply(double val1, double val2) {
            return Math.pow(val1, val2);
        }
    };

    public abstract String getSign();

    public abstract int getPriority();

    public abstract double apply(double val1, double val2);

    public int compare(Operators o) {
        if (o == null) {
            return this.getPriority();
        }
        return this.getPriority() - o.getPriority();
    }

}
