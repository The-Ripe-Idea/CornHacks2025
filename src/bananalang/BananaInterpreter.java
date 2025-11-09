package bananalang;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * This class is responsible for interpreting the Banana language.
 */
public class BananaInterpreter {

    private final Stack<Integer> stack = new Stack<>();

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
                        int number = Integer.parseInt(numberStr);
                        this.stack.push(number);
                    } catch (NumberFormatException e) {
                        this.error("Invalid number after PUSH_ONE: " + numberStr);
                    }
                } else {
                    this.error("PUSH_ONE requires a number but none was provided");
                }
                continue;
            }
            
            switch (cmd) {

                case "ADD": {
                    if (this.stack.size() < 2) {
                        this.error("ADD needs 2 values!");
                        break;
                    }
                    int b = this.stack.pop();
                    int a = this.stack.pop();
                    this.stack.push(a + b);
                    break;
                }

                case "MULTIPLY": {
                    if (this.stack.size() < 2) {
                        this.error("MULTIPLY needs 2 values!");
                        break;
                    }
                    int b = this.stack.pop();
                    int a = this.stack.pop();
                    this.stack.push(a * b);
                    break;
                }

                case "PRINT": {
                    if (this.stack.isEmpty()) {
                        this.error("PRINT needs 1 value!");
                        break;
                    }
                    System.out.println("🍌 Output: " + this.stack.pop());
                    break;
                }

                case "PRINTC":
                    if (this.stack.isEmpty()) {
                        this.error("PRINT needs 1 value!");
                        break;
                    }
                    System.out.println("🍌 Output: " + (char) this.stack.pop().intValue());
                    break;

                case "CLEAR": {
                    this.stack.clear();
                    System.out.println("💥 Stack cleared!");
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
