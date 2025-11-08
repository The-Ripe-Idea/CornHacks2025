import bananalang.BananaCommands;
import bananalang.BananaInterpreter;
import bananalang.BananaParser;
import java.util.List;

/**
 * Main class for testing the Banana language interpreter.
 */
public class Main {
    /**
     * Main method to run the Banana language interpreter.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        String code = "banana banana bananas banananana";
        BananaParser parser = new BananaParser();
        List<BananaCommands> commands = parser.parse(code);
        BananaInterpreter interpreter = new BananaInterpreter();
        interpreter.run(commands);
    }
}
