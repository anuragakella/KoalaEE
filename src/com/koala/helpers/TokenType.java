package com.koala.helpers;


// Token types, characters and operators
public enum TokenType {
    //pseudo-characters
    BAD_TOKEN, END_OF_FILE, NEWLINE,

    //brackets, parentheses
    LEFT_PARENTHESES, RIGHT_PARENTHESES, LEFT_BRACE, RIGHT_BRACE,
    LEFT_SQUARE, RIGHT_SQUARE, SEMICOLON, COMMA, DOT,

    //operators
    SLASH, STAR, PLUS, MINUS, MODULO, CARAT, BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL, GREATER, GREATER_EQUAL, LESS, LESS_EQUAL,

    // literals
    IDENTIFIER, STRING, NUMBER,

    //keywords
    AND, OR, IF, ELSE, FUNCTION, CLASS, FOR, WHILE, RILE,
    BLANK, PRINT, RETURN, THIS, TRUE, FALSE, DEF
}
