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
	 
	 private static final Interpreter interpreter = new Interpreter();
    static boolean hadError = false;
    static boolean hadRuntimeError = false;

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
        if(hadRuntimeError){
        		System.exit(22);
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

        Parser parser = new Parser(tokens);
        //Expr expression = parser.parse();
        List<Stmt> statements = parser.parse();
			
        //interpreter.interpret(expression);
        if(hadError){
            return;
        }
        interpreter.interpret(statements);

        //System.out.println(new AstPrinter().print(expression));

    }

    static void error(int line, String msg){
        report(line," ",msg);
    }

    private static void report(int line,String where,String msg){
        System.err.println("[line " + line +"] Error"+ where + ": "+msg);
        hadError = true;
    }

	static void error(Token token,String message){
		if(token.type == TokenType.EOF){
			report(token.line," at end",message);
		}
		else{
			report(token.line," at '"+token.lexeme+"'",message);
		}
	}
	
	static void runtimeError(RuntimeError error){
		System.err.println(error.getMessage() + "\n[line " + error.token.line + "]");
		hadRuntimeError = true;
	}
}
