package com.koala.syntax;


public class TreePrinter implements Expression.PrintVisitor {
    @Override
    public String printBinary(Expression.BinaryExpression binaryExpression) {
        return ("(" + pTree(binaryExpression.left) + " " + binaryExpression.operator.geType() + " " + pTree(binaryExpression.right) + ")");
    }

    private Object pTree(Expression left) {
        if(left instanceof Expression.LiteralExpression)
            return ((Expression.LiteralExpression) left).value;
        else {
            return left.printTree(this);
        }
    }

    @Override
    public String printGroup(Expression.CompoundExpression compoundExpression) {
        return ("" + pTree(compoundExpression.expression));

    }

    @Override
    public String printUnary(Expression.UnaryExpression unaryExpression) {
        return ("(" + unaryExpression.operator.geType() + " " + pTree(unaryExpression.expression) + ")");

    }

    @Override
    public String printLiteral(Expression.LiteralExpression literalExpression) {
        if(literalExpression.value != null)
        return (literalExpression.value.toString());
        return "";
    }
}
