//package interpreter.lox;
package bin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import bin.Token;
import bin.Scanner;

public class Lox{
    static boolean hadError = false;
    public static void main(String[] args) throws IOException{

        if(args.length > 1){
            System.out.println("Usage: jlox[Script]");
            System.exit(12);
        }
        else if(args.length == 1){
            runFile(args[0]);
        }
        else{
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException{
        byte bytes[] = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        if(hadError){
            System.exit(20);
        }
    }

    private static void runPrompt() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        for(;;){
            System.out.print(">");
            String input = br.readLine();
            if(input == null){
                break;
            }
            run(input);
            hadError = false;
        }
    }

    private static void run(String source){
         Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for(Token token : tokens){
            System.out.println(token);
        }
    }

    static void error(int line, String msg){
        report(line," ",msg);
    }

    private static void report(int line,String where,String msg){
        System.err.println("[line " + line +"] Error"+ where + ": "+msg);
        hadError = true;
    }
}
