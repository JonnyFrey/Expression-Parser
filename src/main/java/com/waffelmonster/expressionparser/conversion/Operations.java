package com.waffelmonster.expressionparser.conversion;

import com.waffelmonster.expressionparser.binarytree.Node;
import com.waffelmonster.expressionparser.conversion.operators.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jonnyfrey on 3/1/16.
 */
public class Operations {
    private static final Map<String, Operator> listOfOperators;
    private static final String isDigitRegex = "[-+]?\\d*\\.?\\d+";
    public static final String leftParenthesis = "(";
    public static final String rightParenthesis = ")";

    static {
        listOfOperators = new HashMap<>();
        listOfOperators.put(Exponent.SIGN, new Exponent());
        listOfOperators.put(Multiply.SIGN, new Multiply());
        listOfOperators.put(Divide.SIGN, new Divide());
        listOfOperators.put(Add.SIGN, new Add());
        listOfOperators.put(Minus.SIGN, new Minus());
    }

    private Operations() {
    }

    public static String[] getListOfOperators() {
        String[] list = new String[listOfOperators.keySet().size()];
        return listOfOperators.keySet().toArray(list);
    }

    public static Operator getOperator(String sign) {
        return listOfOperators.get(sign);
    }

    public static boolean isAnOperator(String sign) {
        return getOperator(sign) != null || isParenthesis(sign);
    }

    public static boolean isParenthesis(String sign) {
        return leftParenthesis.equals(sign) || rightParenthesis.equals(sign);
    }

    public static double applyOperator(String sign, double val1, double val2) {
        Operator operator = getOperator(sign);
        if (operator == null) {
            throw new IllegalArgumentException("Unable to find: " + sign);
        }
        return operator.apply(val1, val2);
    }

    public static boolean isNumeric(String s) {
        return s.matches(isDigitRegex);
    }

    public static Node simplify(Node root) {
        if (root == null) {
            throw new IllegalStateException("Root can't be null");
        }
        if (isAnOperator(root.getData())) {
            double leftValue = Double.valueOf(simplify(root.getLeft()).getData());
            double rightValue = Double.valueOf(simplify(root.getRight()).getData());
            return new Node("" + Operations.applyOperator(root.getData(), leftValue, rightValue));
        }
        return root;
    }

}
