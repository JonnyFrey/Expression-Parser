package com.waffelmonster.expressionparser.binarytree;

/**
 * Created by jonnyfrey on 3/2/16.
 */
public class Node {
    private String data;
    private Node left;
    private Node right;

    public Node() {
        this.data = "";
    }

    public Node(String data) {
        this.data = data;
    }

    public Node(double data) {
        this.data = "" + (int) data;
    }

    public Node withData(String data) {
        this.data = data;
        return this;
    }

    public Node withLeft(Node left) {
        this.left = left;
        return this;
    }

    public Node withRight(Node right) {
        this.right = right;
        return this;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "Node : " + data;
    }
}
