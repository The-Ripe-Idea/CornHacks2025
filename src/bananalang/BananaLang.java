package bananalang;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * This class is the main class for the Banana language.
 */
public class BananaLang {
    /**
     * main function for the banana langauge.
     * 
     * @param args - no arguments are needed.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("🍌 Usage: java BananaLang <file.banana>");
            return;
        }

        try {
            String code = new String(Files.readAllBytes(Paths.get(args[0])));
            BananaParser parser = new BananaParser();
            List<String> commands = parser.parse(code);
            BananaInterpreter interpreter = new BananaInterpreter();
            interpreter.run(commands);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Execution error: " + e.getMessage());
        }
    }
}
