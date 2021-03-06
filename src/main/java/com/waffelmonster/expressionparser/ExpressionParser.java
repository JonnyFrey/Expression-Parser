package com.waffelmonster.expressionparser;

import com.waffelmonster.expressionparser.binarytree.BinaryTreeUtils;
import com.waffelmonster.expressionparser.binarytree.Node;
import com.waffelmonster.expressionparser.conversion.Expression;
import com.waffelmonster.expressionparser.conversion.Operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by jonnyfrey on 3/1/16.
 */
public class ExpressionParser {


    public static void main(String[] args) {
        String[] argument = args;
        if (args.length == 0) {
            argument = applyScanner();
        }

        runWithArguments(normalize(argument));
        System.out.println("===========Random=========");
        runWithRandom();
    }

    private static void runWithArguments(String[] argument) {
        Expression expression = new Expression();
        String[] parsed = new String[]{"Unable", "to", "parse"};
        String result = "Unable to parse";
        Node root = null;
        try {
            parsed = expression.parse(argument);
            result = "" + expression.evaluate(parsed);
            root = BinaryTreeUtils.convertPostfixToTree(parsed);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Original = " + concat(argument));
            System.out.println("Postfix  = " + concat(parsed));
            System.out.println("Result   = " + result);
            if (root != null) {
                System.out.println("Tree: ");
                BinaryTreeUtils.printNode(root);
                System.out.println("Original " + concat(BinaryTreeUtils.convertTreeToInfix(root)));
            }
        }
    }

    private static void runWithRandom() {
        Expression expression = new Expression();
        Node random = BinaryTreeUtils.generateRandomExpression(1, 9, 6);
        String[] infix = BinaryTreeUtils.convertTreeToInfix(random);

        String[] parsed = new String[]{"Unable", "to", "parse"};
        String result = "Unable to parse";
        Node reparsedRandom = null;
        try {
            parsed = expression.parse(infix);
            result = expression.simplify(random).getData();
            reparsedRandom = BinaryTreeUtils.convertPostfixToTree(parsed);
        } catch (ArithmeticException e) {
            System.out.println("Random generator produced an illegal statement");
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Random Expression = " + concat(infix));
            System.out.println("Postfix           = " + concat(parsed));
            System.out.println("Result            = " + result);
            System.out.println("Tree: ");
            BinaryTreeUtils.printNode(random);
            if (reparsedRandom != null) {
                System.out.println("Tree Reconversion: ");
                BinaryTreeUtils.printNode(reparsedRandom);
            }
        }
    }

    private static String generatePrint(String original, String postfix, String result) {
        StringBuffer sb = new StringBuffer();

        sb.append("Original = " + original).append("\n");
        sb.append("Postfix  = " + postfix).append("\n");
        sb.append("Result   = " + result).append("\n");
        sb.append("Tree: ");

        return sb.toString();
    }

    private static String generatePrint(Node original, String postfix, String result) {
        StringBuffer sb = new StringBuffer();

        sb.append("Original = " + BinaryTreeUtils.convertTreeToInfix(original)).append("\n");
        sb.append("Postfix  = " + postfix).append("\n");
        sb.append("Result   = " + result).append("\n");
        sb.append("Tree: ");

        return sb.toString();
    }

    private static String[] applyScanner() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input expression");
        return scanner.nextLine().split(" ");
    }


    private static String concat(String[] rpn) {
        String result = "";
        if (rpn == null) {
            return result;
        }
        for (String element : rpn) {
            result += element + " ";
        }
        return result.trim();
    }

    //Example 32(25 - 5) => "32", "*", "(", "25", "-", "5", ")"
    // Type out 32(25 - 5) => "32(25", "-", "5)"
    //If number not next to an operator, insert a *
    private static String[] normalize(String[] args) {
        if (args.length == 0) {
            return new String[0];
        }
        List<String> normalized = new ArrayList();
        //Correctly break up tokens
        for (String token : args) {
            if (token == null || token.isEmpty()) {
                continue;
            }
            if (Expression.isNumeric(token) || Operations.isAnOperator(token)) {
                normalized.add(token);
            } else {
                normalized.addAll(breakUpToken(token));
            }
        }
        //Inserts multipy in correct spots
        normalized = insertMultiply(normalized);
        String[] result = new String[normalized.size()];
        return normalized.toArray(result);
    }

    //Break it up into characters and keep apending the first character until parsed
    private static List<String> breakUpToken(String token) {
        char[] pieces = token.toCharArray();
        boolean firstCharNumeric = Character.isDigit(pieces[0]);
        String transformToken = "" + pieces[0];
        //Loop through the whole token
        for (int i = 1; i < pieces.length; i++) {
            if (firstCharNumeric == Character.isDigit(pieces[i])) {
                transformToken += "" + pieces[i];
            } else {
                firstCharNumeric = !firstCharNumeric;
                transformToken += " " + pieces[i];
            }
        }
        return Arrays.asList(transformToken.split(" "));
    }

    private static List<String> insertMultiply(List<String> separatedToken) {
        List<String> result = new ArrayList<>();
        result.add(separatedToken.get(0));
        for (int i = 1; i < separatedToken.size(); i++) {
            String prevToken = separatedToken.get(i - 1);
            String token = separatedToken.get(i);
            if (Expression.isNumeric(prevToken)) {
                if ("(".equals(token) || !Operations.isAnOperator(token) && !Expression.isNumeric(token)) {
                    result.add("*");
                }
            }
            result.add(token);
        }
        return result;
    }


}
