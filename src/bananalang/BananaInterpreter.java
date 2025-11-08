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
                        stack.push(number);
                    } catch (NumberFormatException e) {
                        error("Invalid number after PUSH_ONE: " + numberStr);
                    }
                } else {
                    error("PUSH_ONE requires a number but none was provided");
                }
                continue;
            }
            
            switch (cmd) {

                case "ADD": {
                    if (stack.size() < 2) {
                        error("ADD needs 2 values!");
                        break;
                    }
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a + b);
                    break;
                }

                case "MULTIPLY": {
                    if (stack.size() < 2) {
                        error("MULTIPLY needs 2 values!");
                        break;
                    }
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a * b);
                    break;
                }

                case "PRINT": {
                    if (stack.isEmpty()) {
                        error("PRINT needs 1 value!");
                        break;
                    }
                    System.out.println("🍌 Output: " + stack.pop());
                    break;
                }

                case "PRINTC":
                    if (stack.isEmpty()) {
                        error("PRINT needs 1 value!");
                        break;
                    }
                    System.out.println("🍌 Output: " + (char) stack.pop().intValue());
                    break;

                case "CLEAR": {
                    stack.clear();
                    System.out.println("💥 Stack cleared!");
                    break;
                }

                default:
                    error("Unknown command: " + cmd);
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
