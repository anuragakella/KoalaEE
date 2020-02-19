package com.koala.koala;

import com.koala.syntax.Expression;
import com.koala.syntax.TreePrinter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.util.List;

public class Koala {
    private final static String version = "1.0.0";
    public static void main(String args[]) throws Exception {
        if(args.length > 1){
            System.out.println("Use: koala <filename>.kol");
            System.exit(64);
        }
        else if(args.length == 1){
            System.out.println("File");
        } else {
            startRepl();
        }
    }

    private static void startRepl() throws Exception {
        ZonedDateTime z = ZonedDateTime.now();
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        System.out.println("Koala EE " + version +  " on " + System.getProperty("os.name") + " [" + z.getHour() + ":" + z.getMinute() + "/<" + z.getZone() + ">" + "]");
        System.out.println("Type \"help\" or \"credits\" for more info.");
        String inputString;
        while (true){
            System.out.print(">> ");
            inputString = reader.readLine();
            if(inputString.equalsIgnoreCase("help")){
                System.out.println("Koala EE" + version);
                System.out.println("Koala EE (Expression Evaluator) is a basic expression evaluator, that can process and calcuate basic math/ logical expressions.");
                System.out.println("Allowed Operators: ");
                System.out.println("'+' Addition");
                System.out.println("'-' Subtraction");
                System.out.println("'*' Multiplication");
                System.out.println("'/' Division");
                System.out.println("'%' Modulo");
                System.out.println("'<, <=' Less Than, Less Than Equals");
                System.out.println("'>, >=' Greater Than, Greater Than Equals");
                System.out.println("'!, !=, ==' Not, Not Equals, Equality");
                System.out.println("'(, )' Left and Right parentheses to change order of precedence");
                System.out.println("\"True\", \"False\": Booleans");
                System.out.println("--");
                System.out.println("All Numbers are defined as floats by default. ");
            }
            else if(inputString.equalsIgnoreCase("credits")){
                System.out.println("This project was written in Java, by Anurag Akella");
                System.out.println("github: /anuragakella");
                System.out.println("Thank you for using Koala :)");
            }
            else runKoala(inputString);
        }
    }

    private static void runKoala(String src) throws Exception {
        Lexer lexer = new Lexer(src);
        List<Token> toks = lexer.tokenize();
//        for(Token token : toks){
//            System.out.println(token.showToken());
//        }
        for (String error : lexer.diagnostics.errorsList){
            System.out.println(error);
        }
        Parser parser = new Parser(toks);
        parser.parse();
        TreePrinter printer = new TreePrinter();
        Interpreter koalaint = new Interpreter();
//        for(Expression statement : parser.statements){
//            System.out.println(statement.printTree(printer));
//        }
        for (String error : parser.parseErrors.errorsList){
            System.out.println(error);
        }
        for(Expression statement : parser.statements){
            System.out.println(stripOutput(statement.evaluate(koalaint)));
        }
    }

    private static Object stripOutput(Object output) {
//        if(output instanceof Double) {
//            String op = output.toString();
//            if (op.substring(op.indexOf('.'), op.indexOf('.') + 2) == "0")
//                return (output + "").substring(0, );
//        }
        return output;
    }

}
