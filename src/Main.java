import bananalang.BananaInterpreter;
import bananalang.BananaParser;
import bananalang.BananaPreprocessor;
import java.io.File;
import java.io.FileNotFoundException;
//import java.util.ArrayList;
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
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("calc.nana");
        if (!file.exists()) {
            System.err.println("Error: File not found: " + file.getAbsolutePath());
            return;
        }
        String code = BananaPreprocessor.process(file);
        BananaParser parser = new BananaParser();
        List<String> commands = parser.parse(code);
        BananaInterpreter interpreter = new BananaInterpreter();
        interpreter.run(commands);
    }
}
