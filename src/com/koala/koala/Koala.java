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
            //start REPL
        }
    }

    private static void startRepl() throws Exception {
        //get TimeZone info for REPL
        ZonedDateTime z = ZonedDateTime.now();
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        // Display Koala basic info
        System.out.println("Koala EE " + version +  " on " + System.getProperty("os.name") + " [" + z.getHour() + ":" + z.getMinute() + "/<" + z.getZone() + ">" + "]");
        System.out.println("Type \"help\" or \"credits\" for more info.");
        String inputString;
        while (true){
            System.out.print(">> ");
            inputString = reader.readLine();
            //help, credits stuff
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
        for (String error : lexer.diagnostics.errorsList){
            System.out.println(error);
        }
        Parser parser = new Parser(toks);
        parser.parse();
        // TreePrinter prints the AST of the current statement.
        // create a new TreePrinter Object and pass it to each of the statements.
        // TreePrinter is a Visitor pattern based class that 'visits' each node and prints the node values out based on how deep it is.
        // TreePrinter printer = new TreePrinter();
        // Interpreter too follows the Visitor Pattern and just 'visits' the tree nodes and executes them one by one.
        // Yes, that isn't very efficient/ optimal
        Interpreter koalaint = new Interpreter();
        for (String error : parser.parseErrors.errorsList){
            // Errors get two lists, one is populated with errors that occur during lexing and this one is populated by the parser
            // Printing all the errors helps.. resolve them?
            System.out.println(error);
        }
        for(Expression statement : parser.statements){
            // stripOutput converts double to int (via strings).. for prettifying the result and nothing else
            System.out.println(stripOutput(statement.evaluate(koalaint)));
        }
    }

    private static Object stripOutput(Object output) {
        if(output instanceof Boolean) {
           if((boolean)output) return "True";
           else return "False";
        }
        return output;
    }

}
