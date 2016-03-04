package com.waffelmonster.expressionparser;

import com.waffelmonster.expressionparser.binarytree.BinaryTreeUtils;
import com.waffelmonster.expressionparser.binarytree.Node;
import com.waffelmonster.expressionparser.conversion.Expression;

import java.util.Scanner;

/**
 * Created by jonnyfrey on 3/1/16.
 */
public class ExpressionParser {


    public static void main(String[] args) {
        String argument = "";
        if (args.length == 0) {
            argument = applyScanner();
        } else {
            argument = concat(args);
        }

        runWithArguments(argument);
        System.out.println("===========Random=========");
        runWithRandom();
    }

    private static void runWithArguments(String argument) {
        Expression expression = new Expression();
        String[] parsed = new String[]{"Unable", "to", "parse"};
        String result = "Unable to parse";
        Node root = null;
        try {
            parsed = expression.parse(argument.split(" "));
            root = BinaryTreeUtils.convertPostfixToTree(parsed);
            result = "" + expression.simplify(root).getData();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Original = " + argument);
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

    private static String applyScanner() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input expression");
        return scanner.nextLine();
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

}
