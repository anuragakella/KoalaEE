package com.koala.koala;

import com.koala.helpers.TokenType;


// Token class forms the backbone of the parser and the lexer.
// Each token has info about itself: it's position and value, and an object if the token is a literal.
public class Token {

    // TokenType enum
    private TokenType type; //stores token type
    private String token; // stores the token in string from source
    private Object literal; // stores the object version fo the token if available
    private int line; // line number for the token
    private int col; // col number for the token

    // Getter methods to access the vars
    public TokenType geType(){
        return type;
    }
    public int getCol(){
        return col;
    }

    public Object getLiteral() {
        return literal;
    }

    public String getToken() {
        return token;
    }

    // Constructor to crate a token while lexing
    public Token(TokenType type, String token, Object literal, int line, int col){
        this.type = type;
        this.token = token;
        this.literal = literal;
        this.line = line;
        this.col = col;
    }

    // for debugging, to display the token after lexing
    public String showToken(){
        if(type == TokenType.END_OF_FILE)
            return "EOF";
        return type + ": " + token;
    }
}
