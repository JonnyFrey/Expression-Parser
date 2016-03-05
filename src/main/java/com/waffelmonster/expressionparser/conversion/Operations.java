package com.waffelmonster.expressionparser.conversion;

import com.waffelmonster.expressionparser.binarytree.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jonnyfrey on 3/1/16.
 */
public class Operations {
    private static final Map<String, Operators> listOfOperators;
    private static final String isDigitRegex = "[-+]?\\d*\\.?\\d+";
    public static final String leftParenthesis = "(";
    public static final String rightParenthesis = ")";

    static {
        listOfOperators = new HashMap<>();
        listOfOperators.put(Operators.EXPONENT.getSign(), Operators.EXPONENT);
        listOfOperators.put(Operators.MULTIPLY.getSign(), Operators.MULTIPLY);
        listOfOperators.put(Operators.DIVIDE.getSign(), Operators.DIVIDE);
        listOfOperators.put(Operators.ADD.getSign(), Operators.ADD);
        listOfOperators.put(Operators.MINUS.getSign(), Operators.MINUS);
    }

    private Operations() {
    }

    public static String[] getListOfOperators() {
        String[] list = new String[listOfOperators.keySet().size()];
        return listOfOperators.keySet().toArray(list);
    }

    public static Operators getOperator(String sign) {
        return listOfOperators.get(sign);
    }

    public static boolean isAnOperator(String sign) {
        return getOperator(sign) != null || isParenthesis(sign);
    }

    public static boolean isParenthesis(String sign) {
        return leftParenthesis.equals(sign) || rightParenthesis.equals(sign);
    }

    public static double applyOperator(String sign, double val1, double val2) {
        Operators operator = getOperator(sign);
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
