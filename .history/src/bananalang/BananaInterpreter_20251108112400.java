package src.bananalang;
import java.util.*;

public class BananaInterpreter {

    private final Stack<Integer> stack = new Stack<>();

    public void run(List<BananaCommands> commands) {
        for (BananaCommands cmd : commands) {
            switch (cmd) {
                case PUSH_ONE: stack.push(1);

                case ADD: {
                    if (this.stack.size() < 2) { error("ADD needs 2 values!"); break; }
                    int b = this.stack.pop();
                    int a = this.stack.pop();
                    this.stack.push(a + b);
                    break;
                }

                case MULTIPLY: {
                    if (stack.size() < 2) { error("MULTIPLY needs 2 values!"); break; }
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a * b);
                    break;
                }

                case PRINT: {
                    if (stack.isEmpty()) { error("PRINT needs 1 value!"); break; }
                    System.out.println("🍌 Output: " + stack.pop());
                    break;
                }

                case CLEAR: {
                    stack.clear();
                    System.out.println("💥 Stack cleared!");
                }
            }
        }
    }

    private void error(String msg) {
        System.out.println("🚫 Error: " + msg);
    }
}