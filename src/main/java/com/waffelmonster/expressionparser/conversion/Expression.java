package com.waffelmonster.expressionparser.conversion;

import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by jonnyfrey on 3/1/16.
 */
public class Expression {
    private LinkedList<String> output;
    private Operations operations;

    public Expression() {
        output = new LinkedList<>();
        operations = new Operations();
    }

    public String[] parse(String[] args) {
        checkSize(args);
        for (String token : args) {
            if (isNumeric(token)) {
                output.add(token);
                continue;
            }
            if (Operations.isAnOperator(token)) {
                output.addAll(operations.pushOperatorToStack(token));
                continue;
            }
        }

        //Flush stack remove strangling operators
        output.addAll(operations.flushStack());

        //Turn the nice list into an array
        String[] array = new String[output.size()];
        return output.toArray(array);
    }

    public double evaluate(String[] args) {
        checkSize(args);
        Stack<Double> stack = new Stack<>();

        for (int pos = 0; pos < args.length; pos++) {
            String token = args[pos];

            if (isNumeric(token)) {
                stack.push(new Double(token));
                continue;
            }
            if (Operations.isAnOperator(token)) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Not enough values for the operation " + token + " pos: " + pos);
                }
                double val2 = stack.pop();
                double val1 = stack.pop();
                stack.push(Operations.applyOperator(token, val1, val2));
                continue;
            }
            throw new IllegalArgumentException("Found an illegal character: " + token + " pos: " + pos);
        }
        double result = stack.pop();
        if (!stack.empty()) {
            throw new IllegalArgumentException("Expression contains extra values");
        }
        return result;
    }

    private void checkSize(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No expression was defined");
        }
    }

    public static boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

}
