package com.waffelmonster.expressionparser.conversion;

import com.waffelmonster.expressionparser.conversion.operators.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

/**
 * Created by jonnyfrey on 3/1/16.
 */
public class Operations {
    private static Map<String, Operator> listOfOperators = new HashMap<>();
    private Stack<String> stack;

    public Operations() {
        setUpList();
        stack = new Stack<>();
    }

    public static Map<String, Operator> getListOfOperators() {
        setUpList();
        return listOfOperators;
    }

    private static void setUpList() {
        if (!listOfOperators.isEmpty()) {
            return;
        }
        listOfOperators = new HashMap<>();
        listOfOperators.put(Exponent.SIGN, new Exponent());
        listOfOperators.put(Multiply.SIGN, new Multiply());
        listOfOperators.put(Divide.SIGN, new Divide());
        listOfOperators.put(Add.SIGN, new Add());
        listOfOperators.put(Minus.SIGN, new Minus());
    }

    public static Operator getOperator(String sign) {
        setUpList();
        return listOfOperators.get(sign);
    }

    public static boolean isAnOperator(String sign) {
        return getOperator(sign) != null || isParenthesis(sign);
    }

    private static boolean isParenthesis(String sign) {
        return "(".equals(sign) || ")".equals(sign);
    }

    public static double applyOperator(String sign, double val1, double val2) {
        Operator operator = getOperator(sign);
        if (operator == null) {
            throw new IllegalArgumentException("Unable to find: " + sign);
        }
        return operator.apply(val1, val2);
    }

    public LinkedList<String> pushOperatorToStack(String sign) {
        LinkedList<String> replacement = new LinkedList<>();
        //Rules applied here
        //If Stack is empty or the '(' should just push and return
        if (!stack.empty() && !"(".equals(sign)) {

            //If ')' go through the entire stack until the '(' pops up or the stack is empty which means mismatch
            if (")".equals(sign)) {
                while (!"(".equals(stack.peek())) {
                    replacement.add(stack.pop());
                    if (stack.empty()) {
                        throw new IllegalArgumentException("Found a mismatch amount of parenthesis");
                    }
                }
                //Remove ')'
                stack.pop();
                return replacement;
            }

            //Can't have an operator with a smaller priority on top of something with a bigger priority so
            //pop off the ops in the stack till you can push the sign
            while (!stack.empty() && smallerPrecedence(sign, stack.peek())) {
                replacement.add(stack.pop());
            }
        }
        stack.push(sign);
        return replacement;
    }

    public LinkedList<String> flushStack() {
        LinkedList<String> result = new LinkedList<>();

        while (!stack.empty()) {
            String operator = stack.pop();
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
