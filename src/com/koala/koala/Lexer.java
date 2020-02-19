package com.koala.koala;

import java.util.ArrayList;
import java.util.List;

import com.koala.diagnostics.Diagnostics;
import com.koala.helpers.ErrorType;
import com.koala.helpers.TokenType;

public class Lexer {
    private final String source;
    private List<Token> tokenList = new ArrayList<>();
    private int cursor = 0;
    private int tokstart = 0;
    private int line = 1;
    public Diagnostics diagnostics;

    // constructor to accept source string
    public Lexer(String source){
        diagnostics = new Diagnostics();
        this.source = source;
    }

    // starts tokenizing the source
    public List<Token> tokenize(){
        // loop through the string till the end
        while (!sourceEnd()){
            tokstart = cursor;
            tokenizeNext();
        }
        // add an EOF token after the whole file
        tokenList.add(new Token(TokenType.END_OF_FILE, "\0", null, line, cursor));
        return tokenList;
    }

    // scan one token, add it to tokenList and that's it/
    private void tokenizeNext() {
        char ch = moveCursor();
        switch (ch){
            //brackets
            case '(' : pushToken(TokenType.LEFT_PARENTHESES, "(", null, line, cursor); break;
            case ')' : pushToken(TokenType.RIGHT_PARENTHESES, ")", null, line, cursor); break;
            case '{' : pushToken(TokenType.LEFT_BRACE, "{", null, line, cursor); break;
            case '}' : pushToken(TokenType.RIGHT_BRACE, "{", null, line, cursor); break;
            case '[' : pushToken(TokenType.LEFT_SQUARE, "[", null, line, cursor); break;
            case ']' : pushToken(TokenType.RIGHT_SQUARE, "]", null, line, cursor); break;
            case ';' : pushToken(TokenType.SEMICOLON, ";", null, line, cursor); break;
            case ',' : pushToken(TokenType.COMMA, ".", null, line, cursor); break;
            case '.' : pushToken(TokenType.DOT, ".", null, line, cursor); break;

            //operators
            case '/' : pushToken(TokenType.SLASH, "/", null, line, cursor); break;
            case '*' : pushToken(TokenType.STAR, "*", null, line, cursor); break;
            case '+' : pushToken(TokenType.PLUS, "+", null, line, cursor); break;
            case '-' : pushToken(TokenType.MINUS, "-", null, line, cursor); break;
            case '%' : pushToken(TokenType.MODULO, "%", null, line, cursor); break;
            case '^' : pushToken(TokenType.CARAT, "^", null, line, cursor); break;
            case '!' : if(checkNext('=')){
                pushToken(TokenType.BANG_EQUAL, "!=", null, line, cursor);
                moveCursor();
            }
            else pushToken(TokenType.BANG, "!", null, line, cursor);
                break;
            case '<' :
                if(checkNext('=')){
                    pushToken(TokenType.LESS_EQUAL, "<=", null, line, cursor);
                    moveCursor();
                }
                else pushToken(TokenType.LESS, "<", null, line, cursor);
                break;
            case '>' :if(checkNext('=')){
                pushToken(TokenType.GREATER_EQUAL, ">=", null, line, cursor);
                moveCursor();
            }
            else pushToken(TokenType.GREATER, ">", null, line, cursor);
                break;
            case '=' : if(checkNext('=')){
                pushToken(TokenType.EQUAL_EQUAL, "==", null, line, cursor);
                moveCursor();
            }
            else pushToken(TokenType.EQUAL, "=", null, line, cursor);
                break;

            //ignoring these characters
            case ' ' :
            case '\r' :
            case '\t' : break;

            //newline
            case '\n' : pushToken(TokenType.NEWLINE, "\n", null, ++line, cursor); break;


            //string // literals
            case '"' : consumeString(); break;

            default:
                if(isDigit(ch))
                    consumeNumber();
                else if(isAlpha(ch))
                    consumeIdentifier();
                else{
                    diagnostics.pushError("Unexpected Character <" + ch + ">",line, cursor, ErrorType.LEXER_ERROR);
                    pushToken(TokenType.BAD_TOKEN, null, null, line, cursor);
                }
        }


    }


    // HELPER METHODS:

    private boolean checkNext(char match){
        if(peek(0) == match)
            return true;
        return false;
    }

    private void consumeString() {
        while (peek(0) != '"' && !sourceEnd()) moveCursor();
        if(sourceEnd()){
            diagnostics.pushError("Unexpected Character <END_OF_FILE>, expected a '\"'", line, cursor, ErrorType.LEXER_ERROR);
        }
        moveCursor();
        pushToken(TokenType.STRING, source.substring(tokstart + 1, cursor - 1), null, line, cursor);
    }

    //consumes an identifier
    private void consumeIdentifier() {
        while (isAlpha(peek(0)) || isDigit(peek(0))) moveCursor();
        String identifier_text = source.substring(tokstart, cursor);
        if(identifier_text.equals("and"))
            pushToken(TokenType.AND, identifier_text, null, line, tokstart);
        else if(identifier_text.equals("True"))
            pushToken(TokenType.TRUE, identifier_text, null, line, tokstart);
        else if(identifier_text.equals("False"))
            pushToken(TokenType.FALSE, identifier_text, null, line, tokstart);
        else if(identifier_text.equals("or"))
            pushToken(TokenType.OR, identifier_text, null, line, tokstart);
        else pushToken(TokenType.IDENTIFIER, identifier_text, null, line, tokstart);
    }

    //look ahead one characters
    private char peek(int offset) {
        if(sourceEnd()) return '\0';
        return source.charAt(cursor + offset);
    }

    // checks if the current char is an alphabet
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    // Consumes a number
    private void consumeNumber() {
        while (isDigit(peek(0)) && !sourceEnd()) moveCursor();
        if(peek(0) == '.' && isDigit(peek(1)))
            moveCursor();
        while (isDigit(peek(0)) && !sourceEnd()) moveCursor();
        pushToken(TokenType.NUMBER,  source.substring(tokstart, cursor), Double.parseDouble(source.substring(tokstart, cursor)), line, cursor);
    }

    // checks if the current char is a number
    private boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }

    private char moveCursor(){
        cursor++;
        return source.charAt(cursor - 1);
    }

    //push an new token to the tokenList
    private void pushToken(TokenType type, String tok, Object lit, int l, int c){
        tokenList.add(new Token(type, tok, lit, l, c));
    }

    //checks if cursor is at the end
    private boolean sourceEnd() {
        return cursor >= source.length();
    }
}
