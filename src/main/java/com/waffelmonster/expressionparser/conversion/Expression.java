package com.waffelmonster.expressionparser.conversion;

import com.waffelmonster.expressionparser.conversion.operators.Operator;

import java.util.LinkedList;
import java.util.Stack;

import static com.waffelmonster.expressionparser.conversion.Operations.*;

/**
 * Created by jonnyfrey on 3/1/16.
 */
public class Expression {
    private LinkedList<String> output;
    private Stack<String> shuntYard;

    public Expression() {
        output = new LinkedList<>();
        shuntYard = new Stack<>();
    }

    public String[] parse(String[] args) {
        checkSize(args);
        for (String token : args) {
            if (isNumeric(token)) {
                output.add(token);
                continue;
            }
            if (isAnOperator(token)) {
                output.addAll(pushOperatorToStack(token));
                continue;
            }
        }

        //Flush shuntYard remove strangling operators
        output.addAll(flushStack());

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
            if (isAnOperator(token)) {
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

    private LinkedList<String> pushOperatorToStack(String sign) {
        LinkedList<String> replacement = new LinkedList<>();
        //Rules applied here
        //If Stack is empty or the '(' should just push and return
        if (!shuntYard.empty() && !"(".equals(sign)) {

            //If ')' go through the entire shuntYard until the '(' pops up or the shuntYard is empty which means mismatch
            if (")".equals(sign)) {
                while (!"(".equals(shuntYard.peek())) {
                    replacement.add(shuntYard.pop());
                    if (shuntYard.empty()) {
                        throw new IllegalArgumentException("Found a mismatch amount of parenthesis");
                    }
                }
                //Remove ')'
                shuntYard.pop();
                return replacement;
            }

            //Can't have an operator with a smaller priority on top of something with a bigger priority so
            //pop off the ops in the shuntYard till you can push the sign
            while (!shuntYard.empty() && smallerPrecedence(sign, shuntYard.peek())) {
                replacement.add(shuntYard.pop());
            }
        }
        shuntYard.push(sign);
        return replacement;
    }

    private LinkedList<String> flushStack() {
        LinkedList<String> result = new LinkedList<>();

        while (!shuntYard.empty()) {
            String operator = shuntYard.pop();
            if (isParenthesis(operator)) {
                throw new IllegalArgumentException("Found a mismatch amount of parenthesis");
            }
            result.add(operator);
        }
        return result;
    }

    private boolean smallerPrecedence(String op1, String op2) {
        Operator first = getOperator(op1);
        Operator second = getOperator(op2);
        int value = first.compareTo(second);
        if (first.isApplyRight()) {
            return value < 0;
        } else {
            return value <= 0;
        }
    }

}
