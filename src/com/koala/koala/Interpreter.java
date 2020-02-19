package com.koala.koala;

import com.koala.diagnostics.Diagnostics;
import com.koala.helpers.ErrorType;
import com.koala.syntax.Expression;

public class Interpreter implements Expression.Visitor<Object> {

    public Diagnostics runTimeErrors = new Diagnostics();

    private Object traverse(Expression expression) throws Exception {
        if(expression instanceof Expression.LiteralExpression)
            return ((Expression.LiteralExpression) expression).value;
        return expression.evaluate(this);
    }

    private Object boolEval(boolean b){
        if(b)
            return "True";
        return "False";
    }

    @Override
    public Object evaluateBinary(Expression.BinaryExpression binaryExpression) throws Exception {
        Object left = traverse(binaryExpression.left);
        Object right = traverse(binaryExpression.right);
        switch (binaryExpression.operator.geType()){

            case PLUS:
                if(left instanceof Double && right instanceof Double)
                    return (double)left + (double)right;
                 else if(left instanceof String && right instanceof String)
                    return  left.toString() + right.toString();
                break;

            case MINUS:
                if(left instanceof Double && right instanceof Double)
                    return (double)left - (double)right;
                else
                    throw new Exception("Operator '-' cannot be applied to '" + left.getClass() + "', '" + right.getClass() + "'");

            case STAR:
                if(left instanceof Double && right instanceof Double){
                    return (double)left * (double)right;
                } else{
                    throw new Exception("Operator '*' cannot be applied to '" + left.getClass() + "', '" + right.getClass() + "'");
                }
            case SLASH:
                if(left instanceof Double && right instanceof Double)
                    return (double)left / (double)right;
                else
                    throw new Exception("Operator '*' cannot be applied to '" + left.getClass() + "', '" + right.getClass() + "'");

            case MODULO:
                if(left instanceof Double && right instanceof Double)
                    return (double)left % (double)right;
                else
                    throw new Exception("Operator '%' cannot be applied to '" + left.getClass() + "', '" + right.getClass() + "'");

            case LESS:
                if(left instanceof Double && right instanceof Double)
                    return boolEval((double)left < (double)right);
                else
                    throw new Exception("Operator '<' cannot be applied to '" + left.getClass() + "', '" + right.getClass() + "'");

            case GREATER:
                if(left instanceof Double && right instanceof Double)
                    return boolEval((double)left > (double)right);
                else
                    throw new Exception("Operator '>' cannot be applied to '" + left.getClass() + "', '" + right.getClass() + "'");

            case LESS_EQUAL:
                if(left instanceof Double && right instanceof Double)
                    return boolEval((double)left <= (double)right);

                else
                    throw new Exception("Operator '<=' cannot be applied to '" + left.getClass() + "', '" + right.getClass() + "'");

            case GREATER_EQUAL:
                if(left instanceof Double && right instanceof Double)
                    return (double)left >= (double)right;
                else
                    throw new Exception("Operator '>=' cannot be applied to '" + left.getClass() + "', '" + right.getClass() + "'");

            case BANG_EQUAL:
                if(left instanceof Double && right instanceof Double)
                    return boolEval((double)left != (double)right);
                else
                    throw new Exception("Operator '!=' cannot be applied to '" + left.getClass() + "', '" + right.getClass() + "'");

            case EQUAL_EQUAL:
                if(left instanceof Double && right instanceof Double)
                    return boolEval((double)left == (double)right);
                else if((left instanceof Boolean && right instanceof Boolean))
                    return boolEval((boolean)left == (boolean)right);
                else
                    throw new Exception("Operator '==' cannot be applied to '" + left.getClass() + "', '" + right.getClass() + "'");

            default:
                runTimeErrors.pushError("Unidentified Token ", 0, 0, ErrorType.PARSE_ERROR);
                throw new Exception("Unidentified Token ");
        }
        return null;
    }

    @Override
    public Object evaluateGroup(Expression.CompoundExpression compoundExpression) throws Exception {
        return traverse(compoundExpression.expression);
    }

    @Override
    public Object evaluateUnary(Expression.UnaryExpression unaryExpression) throws Exception {
        Object expression = traverse(unaryExpression.expression);
        switch (unaryExpression.operator.geType()){
            case PLUS:
                return expression;
            case MINUS:
                return -1 * (double)expression;
            case BANG:
                if(expression instanceof Boolean)
                    return !(boolean)expression;
                else if(expression == null)
                    return false;
                    return true;
            default:
                runTimeErrors.pushError("Unidentified Token ", 0, 0, ErrorType.PARSE_ERROR);
                throw new Exception("Unidentified Token ");
        }
    }

    @Override
    public Object evaluateLiteral(Expression.LiteralExpression literalExpression) {
        return literalExpression.value;
    }
}
