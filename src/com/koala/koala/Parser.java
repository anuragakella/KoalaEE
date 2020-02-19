package com.koala.koala;

        import com.koala.diagnostics.Diagnostics;
        import com.koala.helpers.ErrorType;
        import com.koala.helpers.TokenType;
        import com.koala.syntax.Expression;
        import java.util.ArrayList;
        import java.util.List;

public class Parser {
    private final List<Token> sourceTokens;
    public List<Expression> statements;
    Diagnostics parseErrors = new Diagnostics();
    private int cursor = 0;

    //globalErrorState - flag to stop the parse loop if there's an error
    private boolean globalErrorState;

    // constructor to get the lexed tokens
    public Parser(List<Token> sourceTokens){
        this.sourceTokens = sourceTokens;
    }

    // starts parsing here
    public void parse() {
        statements = new ArrayList<>();
        globalErrorState = false;
        while (!endToken() && !globalErrorState) {
            statements.add(expression());
        }
    }

    // recursive methods to construct an Abstract Syntax tree
    private Expression expression() {
        return andOr();
    }

    private Expression andOr(){
        // checks for and/ or operator..
        Expression left = equality();
        while (peekCheck(TokenType.AND, TokenType.OR)){
            Token op = nextToken();
            Expression right = equality();
            left = new Expression.BinaryExpression(left, op, right);
        }
        return left;
    }

    private Expression equality() {
        Expression left = compare();
        while (peekCheck(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)){
            Token op = nextToken();
            Expression right = compare();
            left = new Expression.BinaryExpression(left, op, right);
        }
        return left;
    }

    private Expression compare() {
        Expression left = addsub();
        while (peekCheck(TokenType.GREATER_EQUAL, TokenType.LESS_EQUAL, TokenType.LESS, TokenType.GREATER)){
            Token op = nextToken();
            Expression right = addsub();
            left = new Expression.BinaryExpression(left, op, right);
        }
        return left;
    }

    private Expression addsub() {
        Expression left = muldiv();
        while (peekCheck(TokenType.PLUS, TokenType.MINUS)){
            Token op = nextToken();
            Expression right = muldiv();
            left = new Expression.BinaryExpression(left, op, right);
        }
        return left;
    }

    private Expression muldiv() {
        Expression left = unary();
        while (peekCheck(TokenType.STAR, TokenType.SLASH, TokenType.MODULO)){
            Token op = nextToken();
            Expression right = unary();
            left = new Expression.BinaryExpression(left, op, right);
        }
        return left;
    }

    private Expression unary() {
        if (peekCheck(TokenType.PLUS, TokenType.MINUS, TokenType.BANG)){
            Token op = nextToken();
            Expression right = unary();
            return new Expression.UnaryExpression(right, op);
        }
        return literal();
    }

    private Expression literal() {
        if(peekCheck(TokenType.FALSE)) {
            nextToken();
            return new Expression.LiteralExpression(false);
        }
        if(peekCheck(TokenType.TRUE)) {
            nextToken();
            return new Expression.LiteralExpression(true);
        }
        if(peekCheck(TokenType.BLANK)) {
            nextToken();
            return new Expression.LiteralExpression(null);
        }
        if(peekCheck(TokenType.NUMBER, TokenType.STRING)) {
            //System.out.println("Found Token " + peek(0).getLiteral());
            return new Expression.LiteralExpression(nextToken().getLiteral());
        }
        if(peekCheck(TokenType.LEFT_PARENTHESES)){
            nextToken();
            Expression paren = expression();
            if(peekCheck(TokenType.RIGHT_PARENTHESES)){
                nextToken();
                return new Expression.CompoundExpression(paren);
            } else {
                parseErrors.pushError("Expected a closing ')'", cursor, currentToken().getCol(), ErrorType.PARSE_ERROR);
            }
        }
        parseErrors.pushError("Unexpected token: '" + currentToken().getToken() + "'", cursor, currentToken().getCol(), ErrorType.PARSE_ERROR);
        globalErrorState = true;
        return new Expression.LiteralExpression("Blank");
    }

    private Token nextToken() {
        cursor++;
        return sourceTokens.get(cursor - 1);
    }
    private Token currentToken() {
        return sourceTokens.get(cursor);
    }
    private boolean peekCheck(TokenType... operators) {
        for(TokenType op : operators){
            //System.out.println(op + " " +  peek(0).geType());
            if(peek(0).geType() == op)
                return true;
        }
        return false;
    }


    // HELPERS
    private boolean endToken(){
        return peek(0).geType() == TokenType.END_OF_FILE;
    }

    private Token peek(int offset) {
        return sourceTokens.get(cursor + offset);
    }
}
