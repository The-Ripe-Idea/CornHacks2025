package bananalang;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 * This class is responsible for interpreting the Banana language.
 */
public class BananaInterpreter {

    private final Stack<Double> stack = new Stack<>();
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Executes the given list of Banana language commands.
     * 
     * @param commands the list of commands to execute
     */

    public void run(List<String> commands) {
        Iterator<String> iterator = commands.iterator();
        while (iterator.hasNext()) {
            String cmd = iterator.next();
            
            if (cmd.equals("PUSH_ONE")) {
                // Get the next command which should be a number
                if (iterator.hasNext()) {
                    String numberStr = iterator.next();
                    try {
                        double number = Integer.parseInt(numberStr);
                        this.stack.push(number);
                    } catch (NumberFormatException e) {
                        this.error("Invalid number after PUSH_ONE: " + numberStr);
                    }
                } else {
                    this.error("PUSH_ONE requires a number but none was provided");
                }
                continue;
            }
            
            if (cmd.equals("PUSH_INPUT")) {
                // Read input from console and validate it only contains 🍌
                String input;
                boolean validInput = false;
                
                while (!validInput) {
                    input = this.scanner.nextLine();
                    validInput = true;
                    
                    // Check if input only contains 🍌 emojis (nothing else, not even whitespace)
                    for (int i = 0; i < input.length(); ) {
                        int codePoint = input.codePointAt(i);
                        String emoji = new String(Character.toChars(codePoint));
                        
                        if (!emoji.equals("🍌")) {
                            validInput = false;
                            break;
                        }
                        
                        i += Character.charCount(codePoint);
                    }
                    
                    if (validInput) {
                        // Count the number of 🍌 emojis (input is already validated to only contain 🍌)
                        double bananaCount = 0;
                        for (int i = 0; i < input.length(); ) {
                            bananaCount++;
                            i += Character.charCount(input.codePointAt(i));
                        }
                        
                        this.stack.push(bananaCount);
                    }
                }
                continue;
            }
            
            switch (cmd) {

                case "ADD": {
                    if (this.stack.size() < 2) {
                        this.error("ADD needs 2 values!");
                        break;
                    }
                    double b = this.stack.pop();
                    double a = this.stack.pop();
                    this.stack.push(a + b);
                    break;
                }

                case "MULTIPLY": {
                    if (this.stack.size() < 2) {
                        this.error("MULTIPLY needs 2 values!");
                        break;
                    }
                    double b = this.stack.pop();
                    double a = this.stack.pop();
                    this.stack.push(a * b);
                    break;
                }

                case "PRINT": {
                    if (this.stack.isEmpty()) {
                        this.error("PRINT needs 1 value!");
                        break;
                    }
                    System.out.print(this.stack.pop());

                    break;
                }

                case "PRINTC":
                    if (this.stack.isEmpty()) {
                        this.error("PRINT needs 1 value!");
                        break;
                    }
                    System.out.print((char) this.stack.pop().intValue());

                    break;

                case "CLEAR": {
                    this.stack.clear();
                    System.out.println("💥 Stack cleared!");
                    break;
                }
 
                case "EQUALS": {
                    double b = this.stack.pop();
                    double a = this.stack.pop();
                    if (a == b) {
                        this.stack.push(1.00);
                    } else {
                        this.stack.push(0.0);
                    }
                    break;
                }


                default:
                    this.error("Unknown command: " + cmd);
                    break;
            }
        }
    }

    /**
     * Prints an error message.
     * 
     * @param msg the error message to print
     */
    private void error(String msg) {
        System.out.println("🚫 Error: " + msg);
    }

}
