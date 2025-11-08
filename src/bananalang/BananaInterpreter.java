package bananalang;

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
    public void run(List<BananaCommands> commands) {
        for (BananaCommands cmd : commands) {
            switch (cmd) {
                case PUSH_ONE:
                    stack.push(1);
                    break;

                case ADD: {
                    if (stack.size() < 2) {
                        error("ADD needs 2 values!");
                        break;
                    }
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a + b);
                    break;
                }

                case MULTIPLY: {
                    if (stack.size() < 2) {
                        error("MULTIPLY needs 2 values!");
                        break;
                    }
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a * b);
                    break;
                }

                case PRINT: {
                    if (stack.isEmpty()) {
                        error("PRINT needs 1 value!");
                        break;
                    }
                    System.out.println("🍌 Output: " + stack.pop());
                    break;
                }

                case CLEAR: {
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
