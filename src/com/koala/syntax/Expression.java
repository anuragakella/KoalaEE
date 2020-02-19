package com.koala.syntax;

import com.koala.koala.Interpreter;
import com.koala.koala.Token;

public abstract class Expression {

    public abstract <T> T evaluate(Visitor<T> visitor) throws Exception;

    public interface Visitor<T>{
        T evaluateBinary(BinaryExpression binaryExpression) throws Exception;
        T evaluateGroup(CompoundExpression compoundExpression) throws Exception;
        T evaluateUnary(UnaryExpression unaryExpression) throws Exception;
        T evaluateLiteral(LiteralExpression literalExpression);
    }

    interface PrintVisitor{
        String printBinary(BinaryExpression binaryExpression);
        String printGroup(CompoundExpression compoundExpression);
        String printUnary(UnaryExpression unaryExpression);
        String  printLiteral(LiteralExpression literalExpression);
    }

    public static class BinaryExpression extends Expression {
        public final Expression left;
        public final Token operator;
        public final Expression right;

        public <T> T evaluate(Visitor<T> visitor) throws Exception {
            return visitor.evaluateBinary(this);
        }

        public String printTree(PrintVisitor printVisitor){
            return printVisitor.printBinary(this);
        }

        public BinaryExpression(Expression left, Token operator, Expression right){
            this.left = left;
            this.operator = operator;
            this.right = right;
        }
    }
   public static class CompoundExpression extends Expression {
        public final Expression expression;

       public String printTree(PrintVisitor printVisitor){
           return printVisitor.printGroup(this);
       }

        public <T> T evaluate(Visitor<T> visitor) throws Exception {
            return visitor.evaluateGroup(this);
        }

        public CompoundExpression(Expression expression){
            this.expression = expression;
        }
    }

    public static class UnaryExpression extends Expression {
        public final Expression expression;
        public final Token operator;

        public String printTree(PrintVisitor printVisitor){
            return printVisitor.printUnary(this);
        }

        public <T> T evaluate(Visitor<T> visitor) throws Exception {
            return visitor.evaluateUnary(this);
        }
        public UnaryExpression(Expression expression, Token operator){
            this.expression = expression;
            this.operator = operator;
        }
    }

   public static class LiteralExpression extends Expression {
        public final Object value;
        public LiteralExpression(Object value){
            this.value = value;
        }
       public String printTree(PrintVisitor printVisitor){
           return printVisitor.printLiteral(this);
       }
        public <T> T evaluate(Visitor<T> visitor){
            return visitor.evaluateLiteral(this);
        }
    }
    public abstract String printTree(PrintVisitor printVisitor);
}
